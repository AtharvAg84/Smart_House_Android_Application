import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.smarthome"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.smarthome"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    viewBinding {
        enabled = true
    }
}

dependencies {

    //Graphs in Android
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")


    // Extra Dependency
    implementation(libs.volley)

    //noinspection UseTomlInstead
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation(libs.material)
    //Layouts Dependecy
    implementation(libs.androidx.constraintlayout)
    implementation(libs.picasso)

    //Location Dependency
    implementation(libs.play.services.location)


    testImplementation(libs.junit)

    // Firebase Dependency
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth) // Add Firebase Auth dependency
    implementation(libs.firebase.core) // Firebase core

    implementation(libs.firebase.database)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.api.common)

    //Gif Handlingg
    implementation(libs.android.gif.drawable)


    //noinspection UseTomlInstead
    implementation("com.google.android.gms:play-services-auth:21.3.0")

    //implementation("com.github.Dimezis:BlurView:version-2.0.6")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}