plugins {
    id("keel.compose.multiplatform")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.core.designsystem)
    }
}
