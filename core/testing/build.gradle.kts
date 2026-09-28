plugins {
    id("keel.kmp.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            api(projects.core.data)
            api(projects.core.model)
            api(libs.kotlin.test)
            api(libs.kotlinx.coroutines.test)
            api(libs.turbine)
        }
        androidMain.dependencies {
            api(libs.kotlin.test.junit)
        }
    }
}
