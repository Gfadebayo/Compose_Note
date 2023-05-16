plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("app.cash.sqldelight")
    id("dagger.hilt.android.plugin")
    id("exzell.compose")
}

android {
    namespace = "com.exzell.composenote"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.exzell.composenote"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
//            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }



    sqldelight {
        databases {
            create("NoteDatabase") {
                packageName.set("com.exzell.composenote.data.database")
                schemaOutputDirectory.set(file("build/outputs/database"))
            }
        }
    }
}

dependencies {
    implementation(libs.bundles.sqldelight)
//    testImplementation(libs.sqlite.jdbc)
    testImplementation(libs.sqldelight.jvm.driver)

    implementation(libs.core)

    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}