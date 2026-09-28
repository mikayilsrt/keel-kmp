# Keel — plan de restructuration

## Analyse du projet généré

| Élément | État actuel | Cible |
|---|---|---|
| Modules Gradle | `:androidApp`, `:shared` | `:androidApp`, `:shell`, `:core:*`, `:feature:*:api/impl` |
| Plugins | AGP 9.1.1 (`com.android.application`, `com.android.kotlin.multiplatform.library`), Kotlin 2.4.20 (Kotlin intégré à AGP 9 pour `androidApp`), Compose MP 1.12.1, Gradle 9.5.1, toolchain JVM 21 | Versions stables vérifiées, plugins appliqués via `build-logic` |
| Versions à corriger | `material3 = 1.12.0-alpha03` (alpha), `minSdk = 24`, JVM target 11 | Material 3 stable, `minSdk = 26` |
| Framework iOS | Produit par `:shared` (`baseName = "Shared"`, `isStatic = true`, iosArm64 + iosSimulatorArm64) | Produit par `:shell` |
| Projet Xcode | `iosApp/iosApp.xcodeproj`, target `iosApp`, produit `Keel.app`, phase « Compile Kotlin Framework » → `:shared:embedAndSignAppleFrameworkForXcode` | Phase → `:shell:embedAndSignAppleFrameworkForXcode` |
| Scheme Xcode | `iosApp` uniquement dans `xcuserdata` (ignoré par git) | Scheme partagé dans `xcshareddata` pour que `xcodebuild -scheme iosApp` marche sur un clone propre |
| Bundle id / nom | `com.keel.app.Keel$(TEAM_ID)` / `PRODUCT_NAME=Keel` (`Config.xcconfig`) | `com.keel.app` / `Keel` |
| Safe areas | `ContentView` fait déjà `.ignoresSafeArea()` | Insets gérés côté Compose dans le Shell |
| Tests | `commonTest`, `androidHostTest`, `iosTest` | Tests ViewModel en `commonTest`, Koin `verify()` en `androidHostTest` |

Ajustements par rapport au découpage de base :
- La feature `home` est créée **avant** le câblage du Shell (le `NavHost` a besoin de `HomeRoute` comme destination de départ) ; le Shell dépend des `feature:*:api` pour les routes de la BottomBar et de la destination de départ, et des `impl` uniquement pour leur module Koin.
- La BottomBar (Home + Settings) est ajoutée avec la feature `settings`, quand ses deux destinations existent.
- Un plugin de convention supplémentaire `keel.room` (Room + KSP pour chaque cible) est ajouté avec `core:database` pour garder son build court.

## Tâches

| # | Tâche | Modules concernés | Message de commit | Statut |
|---|---|---|---|---|
| 0 | Import du projet généré + plan | racine | `chore: import generated KMP project and add restructuring plan` | ✅ |
| 1 | Suppression du code d'exemple (Greeting, Platform), `.gitignore` Android + Xcode + Kotlin/Native, scheme Xcode partagé, bundle id `com.keel.app` | racine, shared, iosApp | `chore: clean generated sample code and harden gitignore` | ✅ |
| 2 | Version catalog : versions stables vérifiées et compatibles (Kotlin, CMP, AGP, KSP, Koin, Ktor, Room, DataStore…), `minSdk 26` | gradle/libs.versions.toml | `build: set up version catalog with verified stable versions` | ✅ |
| 3 | Included build `build-logic` : KmpLibrary, ComposeMultiplatform, Koin, FeatureApi, FeatureImpl, AndroidApplication ; typesafe project accessors | build-logic, settings, androidApp, shared | `build: add build-logic included build with convention plugins` | ✅ |
| 4 | Renommage `shared` → `shell`, framework iOS produit par `shell`, phase Xcode rebranchée, `androidApp` ne dépend que de `shell` | shell, androidApp, iosApp | `refactor: split shared into shell module and rewire iosApp` | ✅ |
| 5 | `core:model` et `core:common` (dispatchers, Result, extensions) | core:model, core:common | `feat: add core model and common modules` | ✅ |
| 6 | `core:designsystem` : thème M3 clair/sombre, couleurs, typo, composants atomiques, Compose Resources | core:designsystem | `feat: add core designsystem with theme and compose resources` | ✅ |
| 7 | `core:ui` : composants partagés, états Loading / Error | core:ui | `feat: add core ui module with loading and error states` | ✅ |
| 8 | `core:navigation` : `FeatureEntry`, `Navigator` | core:navigation | `feat: add core navigation with FeatureEntry and Navigator` | ✅ |
| 9 | `core:network` : HttpClient Ktor, `networkModule`, engines OkHttp / Darwin | core:network | `feat: add core network module with ktor platform engines` | ✅ |
| 10 | `core:database` : Room KMP + BundledSQLiteDriver, builder par plateforme, plugin `keel.room` | core:database, build-logic | `feat: add core database module with room kmp` | ✅ |
| 11 | `core:datastore` : DataStore Preferences, chemin fourni par plateforme | core:datastore | `feat: add core datastore module with platform file paths` | ✅ |
| 12 | `core:data` : repositories partagés (utilisateurs fake, préférences) | core:data | `feat: add core data module with shared repositories` | ✅ |
| 13 | `core:testing` : fakes, `MainDispatcherRule` équivalent KMP, helpers coroutines | core:testing | `test: add core testing module with fakes and coroutine helpers` | ✅ |
| 14 | `feature:home` api + impl : liste depuis `core:data`, clic → `ProfileRoute(userId)` (crée aussi `feature:profile:api`, cible de la navigation) | feature:home:api, feature:home:impl, feature:profile:api | `feat: add home feature api and impl modules` | ✅ |
| 15 | Shell : `initKoin()`, `App()`, `NavHost` alimenté par `FeatureEntry`, `NavControllerNavigator`, safe areas, init Koin iOS/Android | shell, androidApp, iosApp | `feat: wire shell with koin, navhost and safe area handling` | ✅ |
| 16 | `feature:profile` impl : id lu via `SavedStateHandle.toRoute`, bouton retour | feature:profile:impl, shell | `feat: add profile feature with type-safe userId argument` | ✅ |
| 17 | `feature:settings` api + impl : toggle thème sombre persisté, appliqué par le Shell, BottomBar Home / Settings | feature:settings:api, feature:settings:impl, shell | `feat: add settings feature with persisted dark theme and bottom bar` | ✅ |
| 18 | FeatureImplConventionPlugin : échec du build si un impl dépend d'un autre impl | build-logic | `build: fail build when an impl module depends on another impl` | ✅ |
| 19 | Tests des ViewModels en `commonTest` (Turbine + fakes) ; `allTests` couvre déjà iOS + Android host | feature:*:impl, shell | `test: add viewmodel tests in commonTest using fakes` | ✅ |
| 20 | Test Koin `verify()` des modules en `androidHostTest` | shell | `test: verify koin module graph in android host tests` | ✅ |
| 21 | Script `rename.sh` (testé sur copie temporaire) | racine | `chore: add rename.sh script to bootstrap new projects` | ✅ |
| 22 | README : présentation, architecture, règles de dépendances, lancement Android / simulateur / iPhone, ajout de feature, rename.sh | README.md | `docs: write README for Keel architecture and usage` | ✅ |
| 23 | Deployment target iOS 18.5 (aligné sur les binaires Compose, supprime le warning ld) | iosApp | `build: raise iOS deployment target to 18.5` | ✅ |

## Vérifications par tâche

- Android : `./gradlew :androidApp:assembleDebug`
- iOS framework : `./gradlew :shell:linkDebugFrameworkIosSimulatorArm64` (`:shared:` avant la tâche 4)
- iOS app : `xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -sdk iphonesimulator -destination 'generic/platform=iOS Simulator' build`
- Tests : `./gradlew allTests` (lance `iosSimulatorArm64Test` et `testAndroidHostTest`)
