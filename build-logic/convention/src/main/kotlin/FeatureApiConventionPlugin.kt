import com.keel.app.buildlogic.libs
import com.keel.app.buildlogic.library
import com.keel.app.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/** Public contract of a feature: @Serializable routes and interfaces only. No Compose UI, no Koin. */
class FeatureApiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("keel.kmp.library")
        pluginManager.apply(libs.pluginId("kotlin-serialization"))

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain.dependencies {
                implementation(project(":core:navigation"))
                implementation(libs.library("kotlinx-serialization-json"))
            }
        }
    }
}
