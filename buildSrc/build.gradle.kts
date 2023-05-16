plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.4.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
    implementation("com.squareup:javapoet:1.13.0")

}

gradlePlugin {
    plugins {
        register("composePlugin") {
            id = "exzell.compose"
            implementationClass = "plugin.ComposePlugin"
        }
    }
}