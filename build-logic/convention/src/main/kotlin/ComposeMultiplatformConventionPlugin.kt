import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.keel.app.buildlogic.libs
import com.keel.app.buildlogic.library
import com.keel.app.buildlogic.pathNamespace
import com.keel.app.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/** Compose Multiplatform + Material 3 + Compose Resources for a KMP library. */
class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("keel.kmp.library")
        pluginManager.apply(libs.pluginId("compose-multiplatform"))
        pluginManager.apply(libs.pluginId("compose-compiler"))

        extensions.configure<KotlinMultiplatformExtension> {
            (this as ExtensionAware).extensions.configure<KotlinMultiplatformAndroidLibraryTarget> {
                androidResources.enable = true
            }
            sourceSets.commonMain.dependencies {
                implementation(libs.library("compose-runtime"))
                implementation(libs.library("compose-foundation"))
                implementation(libs.library("compose-ui"))
                implementation(libs.library("compose-material3"))
                implementation(libs.library("compose-components-resources"))
                implementation(libs.library("compose-ui-tooling-preview"))
            }
        }

        (extensions.getByType(ComposeExtension::class.java) as ExtensionAware)
            .extensions.configure<ResourcesExtension> {
                packageOfResClass = "$pathNamespace.generated.resources"
            }

        dependencies {
            "androidRuntimeClasspath"(libs.library("compose-ui-tooling"))
        }
    }
}
