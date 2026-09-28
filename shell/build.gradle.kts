plugins {
    id("keel.compose.multiplatform")
    id("keel.koin")
}

kotlin {
    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
            binaryOption("bundleId", "com.keel.app.shell")
        }
    }
    sourceSets {
        commonMain.dependencies {
            api(libs.koin.core)
            implementation(projects.core.common)
            implementation(projects.core.data)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.network)
            implementation(projects.feature.home.api)
            implementation(projects.feature.home.impl)
            implementation(projects.feature.profile.impl)
            implementation(projects.feature.settings.api)
            implementation(projects.feature.settings.impl)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
        }
        androidMain.dependencies {
            api(libs.koin.android)
            implementation(libs.androidx.core.ktx)
        }
    }
}
