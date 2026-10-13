
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    namespace = "com.inkread.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.inkread.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation("com.google.mlkit:text-recognition:16.0.1")
}

