plugins {
    id("keel.kmp.library")
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(libs.androidx.navigation.compose)
    }
}
