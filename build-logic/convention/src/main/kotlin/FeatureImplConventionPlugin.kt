import com.keel.app.buildlogic.forbidDependenciesOnOtherFeatureImpls
import com.keel.app.buildlogic.libs
import com.keel.app.buildlogic.library
import com.keel.app.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Implementation of a feature (`:feature:x:impl`). Wires Compose, Koin, the core UI modules
 * and the feature's own api module. Everything in the module should be `internal` except its Koin module.
 *
 * Fails the build if the module depends on another `feature:*:impl` module.
 */
class FeatureImplConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("keel.compose.multiplatform")
        pluginManager.apply("keel.koin")
        pluginManager.apply(libs.pluginId("kotlin-serialization"))
        forbidDependenciesOnOtherFeatureImpls()

        extensions.configure<KotlinMultiplatformExtension> {
            explicitApi()
            sourceSets.commonMain.dependencies {
                implementation(project(path.removeSuffix(":impl") + ":api"))
                implementation(project(":core:designsystem"))
                implementation(project(":core:navigation"))
                implementation(project(":core:ui"))
                implementation(libs.library("androidx-lifecycle-viewmodel-compose"))
                implementation(libs.library("androidx-lifecycle-runtime-compose"))
                implementation(libs.library("androidx-navigation-compose"))
                implementation(libs.library("kotlinx-serialization-json"))
            }
            sourceSets.commonTest.dependencies {
                implementation(project(":core:testing"))
                implementation(libs.library("kotlinx-coroutines-test"))
                implementation(libs.library("turbine"))
            }
        }
    }
}
