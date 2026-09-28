import com.keel.app.buildlogic.libs
import com.keel.app.buildlogic.library
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/** Koin (DSL only). Adds the Compose integrations when Compose Multiplatform is applied. */
class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("keel.kmp.library")

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain.dependencies {
                implementation(libs.library("koin-core"))
            }
            sourceSets.commonTest.dependencies {
                implementation(libs.library("koin-test"))
            }
        }

        pluginManager.withPlugin("org.jetbrains.compose") {
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.commonMain.dependencies {
                    implementation(libs.library("koin-compose"))
                    implementation(libs.library("koin-compose-viewmodel"))
                }
            }
        }
    }
}
