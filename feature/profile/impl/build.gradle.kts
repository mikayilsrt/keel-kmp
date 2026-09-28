plugins {
    id("keel.feature.impl")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.core.data)
    }
}
