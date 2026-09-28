plugins {
    id("keel.room")
    id("keel.koin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}
