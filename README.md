# Keel

Keel est un boilerplate **Kotlin Multiplatform + Compose Multiplatform** (Android et iOS, UI partagée)
organisé en modules, sur le modèle de [Now in Android](https://github.com/android/nowinandroid) adapté au KMP.
Il sert de point de départ pour de nouveaux projets : un script (`rename.sh`) le renomme en une commande.

L'app d'exemple contient trois features :

- **Home** : liste d'utilisateurs (source distante factice, mise en cache dans Room), clic → Profile
- **Profile** : reçoit l'`userId` via une route type-safe, bouton retour
- **Settings** : bouton pour activer le thème sombre, enregistré dans DataStore et appliqué par le Shell

## Stack

| Domaine | Librairie |
|---|---|
| UI | Compose Multiplatform, Material 3, Compose Resources |
| Navigation | `org.jetbrains.androidx.navigation:navigation-compose`, routes `@Serializable` |
| ViewModel | `org.jetbrains.androidx.lifecycle` (`lifecycle-viewmodel-compose`) |
| DI | Koin (DSL uniquement) |
| Réseau | Ktor Client (OkHttp sur Android, Darwin sur iOS), kotlinx.serialization |
| Base de données | Room KMP (`androidx.room3`) + `BundledSQLiteDriver`, KSP |
| Préférences | DataStore Preferences |
| Tests | kotlin-test, kotlinx-coroutines-test, Turbine, fakes (pas de MockK) |
| Build | Gradle Kotlin DSL, version catalog, convention plugins (`build-logic`), typesafe project accessors |

Les versions sont centralisées dans [`gradle/libs.versions.toml`](gradle/libs.versions.toml).
Android : minSdk 26, compileSdk/targetSdk 37. iOS : `iosArm64` et `iosSimulatorArm64`.

## Architecture

```
androidApp/               Application Android : Application (initKoin + androidContext) et MainActivity -> App()
iosApp/                   Projet Xcode : iOSApp (initKoin) et ContentView -> MainViewController()
shell/                    App(), NavHost, BottomBar, NavControllerNavigator, initKoin(), framework iOS "Shared"
build-logic/convention/   Convention plugins Gradle
core/
  common/                 AppDispatchers, Result, extensions coroutines
  model/                  Modèles métier (commonMain uniquement)
  designsystem/           Thème, couleurs, typo, icônes, composants atomiques, ressources partagées
  ui/                     États partagés Loading / Error / Empty
  navigation/             Interfaces FeatureEntry et Navigator
  network/                HttpClient Ktor + networkModule, engine par plateforme
  database/               Room KMP, builder par plateforme
  datastore/              DataStore Preferences, chemin du fichier fourni par plateforme
  data/                   Repositories partagés (UserRepository, UserPreferencesRepository)
  testing/                Fakes, runMainTest, données de test
feature/
  home/api, home/impl
  profile/api, profile/impl
  settings/api, settings/impl
```

Les packages suivent les chemins des modules : `:core:network` → `com.keel.app.core.network`,
`:feature:profile:impl` → `com.keel.app.feature.profile.impl`. Le namespace Android de chaque module est
calculé par les convention plugins à partir de `keel.basePackage` (`gradle.properties`) et du chemin du module.

Le code spécifique à une plateforme est placé derrière une interface commonMain fournie par un module Koin
de plateforme (`DataStorePathProvider`, builder Room). `expect`/`actual` n'est utilisé que lorsque c'est plus simple
(engine Ktor, constructeur Room, apparence des barres système).

### Règles de dépendances

1. `shell` dépend de tous les `feature:*:impl`, mais n'en utilise que le module Koin public (`homeModule`, …).
   Il dépend des `feature:*:api` pour la destination de départ et les routes de la BottomBar.
2. `androidApp` et `iosApp` ne dépendent que de `shell` et ne contiennent aucune logique.
3. `feature:x:api` ne contient que des routes `@Serializable` et des interfaces publiques. Il ne dépend que de
   `core:navigation` (et éventuellement `core:model`). Pas de Compose UI, pas de Koin.
4. `feature:x:impl` dépend de son api, des api des features vers lesquelles il navigue et des modules core.
   Tout y est `internal` sauf son module Koin (le mode `explicitApi()` est activé pour y veiller).
5. Un `impl` ne dépend jamais d'un autre `impl`. Les modules `core` ne dépendent jamais des features.

La règle 5 est vérifiée par le build : `keel.feature.impl` fait échouer la configuration si un impl dépend d'un
autre impl, et `keel.kmp.library` fait de même si un module `core` dépend d'une feature.

```
androidApp ─┐
            ├─> shell ─┬─> feature:*:impl ─┬─> feature:*:api ─> core:navigation
iosApp ─────┘          │                   └─> core:{designsystem, ui, navigation, data, …}
                       ├─> feature:*:api
                       └─> core:{common, network, data, designsystem, navigation}
```

### Convention plugins

| Id | Rôle |
|---|---|
| `keel.kmp.library` | KMP (android + iosArm64 + iosSimulatorArm64), config Android commune, host tests |
| `keel.compose.multiplatform` | Compose Multiplatform, Material 3, Compose Resources (`<namespace>.generated.resources`) |
| `keel.koin` | Koin core (+ koin-compose et koin-compose-viewmodel si Compose est appliqué) |
| `keel.room` | Room KMP + KSP pour chaque cible, schémas dans `schemas/` |
| `keel.feature.api` | Module api d'une feature |
| `keel.feature.impl` | Module impl : Compose, Koin, core:ui, core:designsystem, core:navigation, son api, vérification des dépendances |
| `keel.android.application` | Module `androidApp` (Kotlin intégré à AGP 9) |

### Navigation et DI

`core:navigation` déclare :

```kotlin
interface FeatureEntry {
    fun register(builder: NavGraphBuilder, navigator: Navigator)
}

interface Navigator {
    fun navigate(route: Any)
    fun back()
}
```

Chaque impl expose un module Koin :

```kotlin
public val profileModule: Module = module {
    factoryOf(::ProfileEntry) bind FeatureEntry::class
    viewModelOf(::ProfileViewModel)
}
```

`App()` récupère `getKoin().getAll<FeatureEntry>()` et appelle `register` sur chacune dans le `NavHost`.
Les écrans utilisent `koinViewModel()` et les arguments sont lus dans le ViewModel via
`savedStateHandle.toRoute<ProfileRoute>()`.

Chaque écran suit le même modèle : `XxxRoute` (récupère le ViewModel) → `XxxScreen` (stateless, avec `@Preview`),
`XxxUiState` (sealed interface) et `XxxViewModel` qui expose un `StateFlow`.

**Démarrage de Koin.** `shell` expose `initKoin(config: KoinAppDeclaration? = null)`, qui charge les modules core,
feature et shell :

- Android : `KeelApplication.onCreate()` appelle `initKoin { androidContext(this@KeelApplication) }`.
- iOS : `iOSApp.init()` (SwiftUI) appelle `KoinIosKt.doInitKoinIos()` (Kotlin exporte `initKoinIos()` vers Swift en
  ajoutant le préfixe `do`). Ce choix garantit un démarrage unique par processus, avant la création de la première vue
  Compose. `MainViewController()` peut être appelé plusieurs fois par SwiftUI : y démarrer Koin provoquerait une
  `KoinApplicationAlreadyStartedException`.

**Safe areas.** Côté iOS, `ContentView` appelle `.ignoresSafeArea()` : Compose dessine sur tout l'écran et gère lui-même
les insets. Le `Scaffold` du Shell ne consomme aucun inset. Les top bars gèrent la barre d'état et l'encoche, la
BottomBar gère l'indicateur d'accueil et la barre de navigation, et les écrans sans BottomBar (Profile) reçoivent les
insets du bas via leur propre `Scaffold`. La même logique s'applique à Android en edge-to-edge (`enableEdgeToEdge()`).

## Lancer l'application

Prérequis : Android Studio avec le plugin Kotlin Multiplatform, JDK 21 (Gradle le télécharge si besoin, voir `gradle/gradle-daemon-jvm.properties`), Xcode sur un Mac Apple Silicon.

### Android

```bash
./gradlew :androidApp:assembleDebug     # build
./gradlew :androidApp:installDebug      # installation sur un émulateur ou un appareil connecté
```

Ou lancer la configuration `androidApp` depuis Android Studio.

### Simulateur iOS

Ouvrir `iosApp/iosApp.xcodeproj` dans Xcode, choisir le scheme `iosApp` et un simulateur, puis Run.
La phase de build « Compile Kotlin Framework » appelle `./gradlew :shell:embedAndSignAppleFrameworkForXcode`.

En ligne de commande :

```bash
./gradlew :shell:linkDebugFrameworkIosSimulatorArm64
xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -sdk iphonesimulator \
  -destination 'generic/platform=iOS Simulator' build
```

### iPhone physique

1. Dans `iosApp/Configuration/Config.xcconfig`, renseigner `TEAM_ID=` avec l'identifiant de votre équipe Apple
   (Xcode > Settings > Accounts), et vérifier que `PRODUCT_BUNDLE_IDENTIFIER` est unique pour votre compte.
2. Brancher l'iPhone, activer le mode développeur (Réglages > Confidentialité et sécurité > Mode développeur).
3. Dans Xcode, sélectionner l'iPhone comme destination et lancer Run : le framework est compilé pour `iosArm64`.
4. Au premier lancement, approuver le certificat développeur sur l'iPhone (Réglages > Général > VPN et gestion
   de l'appareil).

## Tests

```bash
./gradlew allTests      # iosSimulatorArm64Test + testAndroidHostTest pour chaque module
```

- Les ViewModels sont testés en `commonTest` avec les fakes de `core:testing` (`FakeUserRepository`,
  `FakeUserPreferencesRepository`) et Turbine. `runMainTest { }` remplace `Dispatchers.Main`, comme la
  `MainDispatcherRule` JUnit.
- `shell/src/androidHostTest/.../KoinModulesTest.kt` vérifie tout le graphe Koin avec `verify()`, qui repose sur la
  réflexion JVM et n'est donc pas disponible en `commonTest`.
- `@IgnoreOnAndroidHost` (dans `core:testing`) ignore un test commun sur l'Android host, où `android.os.Bundle` n'est
  qu'un stub. C'est le cas des tests qui dépendent de `SavedStateHandle.toRoute()` : ils s'exécutent quand même sur iOS.

## Ajouter une feature

Exemple avec une feature `search` :

1. Créer `feature/search/api/build.gradle.kts` :
   ```kotlin
   plugins { id("keel.feature.api") }
   ```
   et y déclarer la route dans `com.keel.app.feature.search.api` :
   ```kotlin
   @Serializable data object SearchRoute
   ```
2. Créer `feature/search/impl/build.gradle.kts` :
   ```kotlin
   plugins { id("keel.feature.impl") }

   kotlin {
       sourceSets.commonMain.dependencies {
           implementation(projects.core.data)
           // implementation(projects.feature.profile.api) pour naviguer vers une autre feature
       }
   }
   ```
3. Dans `com.keel.app.feature.search.impl`, écrire `SearchUiState`, `SearchViewModel`, `SearchRoute`/`SearchScreen`
   (tous `internal`), un `SearchEntry : FeatureEntry` qui enregistre `composable<SearchRoute> { … }`, et le module
   public :
   ```kotlin
   public val searchModule: Module = module {
       factoryOf(::SearchEntry) bind FeatureEntry::class
       viewModelOf(::SearchViewModel)
   }
   ```
   Les chaînes vont dans `src/commonMain/composeResources/values/strings.xml` du module.
4. Déclarer les modules dans `settings.gradle.kts` (`include(":feature:search:api")`, `include(":feature:search:impl")`).
5. Dans `shell/build.gradle.kts`, ajouter `implementation(projects.feature.search.impl)` (et l'api si la feature apparaît
   dans la BottomBar), puis ajouter `searchModule` à `featureModules` dans `shell/.../di/KoinInit.kt`.
   Pour une destination de la BottomBar, ajouter une entrée à `TopLevelDestination`.
6. Ajouter les tests du ViewModel en `commonTest` et lancer `./gradlew allTests` : `KoinModulesTest` vérifie que le
   nouveau module est complet.

## Créer un nouveau projet avec rename.sh

```bash
git clone <keel> my-app && cd my-app
./rename.sh MyApp com.example.myapp com.example.myapp
```

Arguments : nom du projet (lettres et chiffres), package racine, bundle identifier iOS. Le script :

- remplace le package racine dans tout le code, les namespaces Gradle (`keel.basePackage`) et l'applicationId ;
- déplace les sources de tous les source sets de tous les modules (et les schémas Room exportés) vers le nouveau package ;
- met à jour `rootProject.name`, le nom affiché sur Android (`app_name`) et sur iOS (`PRODUCT_NAME`), le nom du
  produit dans le projet Xcode et le bundle identifier (`Config.xcconfig`) ;
- vérifie les arguments et s'arrête avec un message clair s'ils sont invalides ;
- supprime les sorties de build obsolètes puis lance les builds de vérification Android, framework iOS et app iOS.

Les valeurs actuelles sont lues dans le projet : le script peut être relancé plus tard. Il fonctionne avec le `sed` BSD
de macOS comme avec GNU sed. Les noms de classes préfixés `Keel` (`KeelTheme`, `KeelDatabase`, …) et les ids des
convention plugins (`keel.*`) ne sont pas renommés : ce sont des noms internes qui n'apparaissent pas dans l'app.
