package com.keel.app.buildlogic

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency

private val FeatureImplPath = Regex("^:feature:[^:]+:impl$")

/** Rule: a `feature:x:impl` module never depends on another `feature:y:impl` module. */
internal fun Project.forbidDependenciesOnOtherFeatureImpls() =
    forbidProjectDependencies("another feature impl module") { dependencyPath ->
        FeatureImplPath.matches(dependencyPath) && dependencyPath != path
    }

/** Rule: core modules never depend on features. */
internal fun Project.forbidDependenciesOnFeatures() =
    forbidProjectDependencies("a feature module") { dependencyPath -> dependencyPath.startsWith(":feature:") }

private fun Project.forbidProjectDependencies(description: String, isForbidden: (String) -> Boolean) {
    configurations.configureEach {
        val configurationName = name
        dependencies.withType(ProjectDependency::class.java).configureEach {
            if (isForbidden(path)) {
                throw GradleException(
                    "Module '${this@forbidProjectDependencies.path}' must not depend on $description " +
                        "('$path', configuration '$configurationName'). See README > Dependency rules.",
                )
            }
        }
    }
}
