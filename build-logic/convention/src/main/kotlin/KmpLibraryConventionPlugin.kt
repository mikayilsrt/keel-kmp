import com.keel.app.buildlogic.configureKotlinMultiplatform
import com.keel.app.buildlogic.libs
import com.keel.app.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project

/** Kotlin Multiplatform library targeting Android, iosArm64 and iosSimulatorArm64. */
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("kotlin-multiplatform"))
        pluginManager.apply(libs.pluginId("android-kotlin-multiplatform-library"))
        configureKotlinMultiplatform()
    }
}
