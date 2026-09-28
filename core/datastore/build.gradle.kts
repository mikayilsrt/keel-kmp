plugins {
    id("keel.kmp.library")
    id("keel.koin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.androidx.datastore.preferences)
            implementation(projects.core.model)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
        commonTest.dependencies {
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
        }
    }
}
