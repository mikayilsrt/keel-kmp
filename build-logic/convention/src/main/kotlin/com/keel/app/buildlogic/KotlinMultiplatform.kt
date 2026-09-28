package com.keel.app.buildlogic

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal val KeelJvmTarget = JvmTarget.JVM_17

internal fun Project.configureKotlinMultiplatform() {
    extensions.configure<KotlinMultiplatformExtension> {
        (this as ExtensionAware).extensions.configure<KotlinMultiplatformAndroidLibraryTarget> {
            namespace = pathNamespace
            compileSdk = libs.version("android-compileSdk").toInt()
            minSdk = libs.version("android-minSdk").toInt()
            compilerOptions {
                jvmTarget.set(KeelJvmTarget)
            }
            withHostTest {}
        }

        iosArm64()
        iosSimulatorArm64()

        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }

        sourceSets.commonTest.dependencies {
            implementation(libs.library("kotlin-test"))
        }
    }
}
