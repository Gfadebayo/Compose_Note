package plugin

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import kotlin.jvm.optionals.getOrNull

class ComposePlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            val composeCatalog = extensions.getByType<VersionCatalogsExtension>().named("compose")

            extensions.getByType<BaseAppModuleExtension>().apply {
                buildFeatures.compose = true

                val compilerVersion = composeCatalog.findVersion("compiler").getOrNull()?.requiredVersion
                composeOptions.kotlinCompilerExtensionVersion = compilerVersion
            }

            dependencies {
                implementation(platform(composeCatalog.findLibrary("bom").get()))

                implementation(composeCatalog.findBundle("core").get())

                implementation(composeCatalog.findLibrary("ui-preview").get())
                debugImplementation(composeCatalog.findLibrary("ui-tooling").get())

                androidTestImplementation(composeCatalog.findLibrary("ui-test").get())
                debugImplementation(composeCatalog.findLibrary("ui-test-manifest").get())

                implementation(composeCatalog.findLibrary("appyx").get())            }
        }
    }

    private fun DependencyHandlerScope.implementation(dependencyNotation: Any) {
        add("implementation", dependencyNotation)
    }

    private fun DependencyHandlerScope.debugImplementation(dependencyNotation: Any) {
        add("debugImplementation", dependencyNotation)
    }

    private fun DependencyHandlerScope.androidTestImplementation(dependencyNotation: Any) {
        add("androidTestImplementation", dependencyNotation)
    }
}