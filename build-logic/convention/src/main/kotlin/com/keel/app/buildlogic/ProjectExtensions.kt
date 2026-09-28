package com.keel.app.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun VersionCatalog.library(alias: String): Provider<MinimalExternalModuleDependency> =
    findLibrary(alias).get()

fun VersionCatalog.version(alias: String): String =
    findVersion(alias).get().requiredVersion

fun VersionCatalog.pluginId(alias: String): String =
    findPlugin(alias).get().get().pluginId

/** Root package of the app, defined once in the root gradle.properties (`keel.basePackage`). */
val Project.basePackage: String
    get() = providers.gradleProperty("keel.basePackage").get()

/** Namespace mirroring the project path, e.g. `:core:network` -> `com.keel.app.core.network`. */
val Project.pathNamespace: String
    get() = (listOf(basePackage) + path.split(":").filter(String::isNotBlank))
        .joinToString(".") { it.replace('-', '_') }
