plugins {
    id("keel.android.application")
}

dependencies {
    implementation(projects.shell)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
}
