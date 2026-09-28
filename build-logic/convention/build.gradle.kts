import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.keel.app.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.composeCompiler.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "keel.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("composeMultiplatform") {
            id = "keel.compose.multiplatform"
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }
        register("koin") {
            id = "keel.koin"
            implementationClass = "KoinConventionPlugin"
        }
        register("featureApi") {
            id = "keel.feature.api"
            implementationClass = "FeatureApiConventionPlugin"
        }
        register("featureImpl") {
            id = "keel.feature.impl"
            implementationClass = "FeatureImplConventionPlugin"
        }
        register("androidApplication") {
            id = "keel.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
    }
}
