import com.android.build.api.dsl.ApplicationExtension
import com.keel.app.buildlogic.KeelJvmTarget
import com.keel.app.buildlogic.basePackage
import com.keel.app.buildlogic.libs
import com.keel.app.buildlogic.pluginId
import com.keel.app.buildlogic.version
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

/** Android application module (uses the Kotlin support built into AGP 9). */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("android-application"))
        pluginManager.apply(libs.pluginId("compose-compiler"))

        extensions.configure<ApplicationExtension> {
            namespace = basePackage
            compileSdk = libs.version("android-compileSdk").toInt()
            defaultConfig {
                applicationId = basePackage
                minSdk = libs.version("android-minSdk").toInt()
                targetSdk = libs.version("android-targetSdk").toInt()
                versionCode = 1
                versionName = "1.0"
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
            buildFeatures {
                compose = true
            }
            packaging {
                resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
            buildTypes {
                getByName("release") {
                    isMinifyEnabled = false
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                }
            }
        }

        tasks.withType<KotlinJvmCompile>().configureEach {
            compilerOptions.jvmTarget.set(KeelJvmTarget)
        }
    }
}
