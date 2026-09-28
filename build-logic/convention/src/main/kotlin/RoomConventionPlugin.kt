import androidx.room3.gradle.RoomExtension
import com.keel.app.buildlogic.libs
import com.keel.app.buildlogic.library
import com.keel.app.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

/** Room KMP with the bundled SQLite driver. KSP runs the Room compiler for every target. */
class RoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("keel.kmp.library")
        pluginManager.apply(libs.pluginId("ksp"))
        pluginManager.apply(libs.pluginId("room"))

        extensions.configure<RoomExtension> {
            schemaDirectory("$projectDir/schemas")
        }

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain.dependencies {
                api(libs.library("androidx-room-runtime"))
                implementation(libs.library("androidx-sqlite-bundled"))
            }
            targets.matching { it.platformType != KotlinPlatformType.common }.all {
                val kspConfiguration = "ksp" + name.replaceFirstChar(Char::uppercase)
                dependencies.add(kspConfiguration, libs.library("androidx-room-compiler"))
            }
        }
    }
}
