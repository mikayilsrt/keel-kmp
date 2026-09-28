plugins {
    id("keel.feature.impl")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.core.common)
        implementation(projects.core.data)
        implementation(projects.feature.profile.api)
    }
}
