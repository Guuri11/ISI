import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("plugin.serialization") version "1.9.0"  // Ensure the version is compatible with your Kotlin version
}

android {
    namespace = "com.guuri11.isi_wear"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.guuri11.isi_wear"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }

        val keystoreFile = project.rootProject.file("secrets.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        val OPEN_AI_API_KEY = properties.getProperty("openai.key") ?: ""
        val WIFI_SSIDs = properties.getProperty("wifi.ssids") ?: ""
        val SERVER = properties.getProperty("server.host") ?: ""

        buildConfigField(
            type = "String",
            name = "OPEN_AI_API_KEY",
            value = OPEN_AI_API_KEY
        )
        buildConfigField(
            type = "String",
            name = "WIFI_SSIDs",
            value = WIFI_SSIDs
        )
        buildConfigField(
            type = "String",
            name = "SERVER",
            value = SERVER
        )
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)  // Or use ktor-client-okhttp for Android
    implementation(libs.ktor.client.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.play.services.wearable)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)
    implementation(libs.compose.material)
    implementation(libs.compose.foundation)
    implementation(libs.activity.compose)
    implementation(libs.core.splashscreen)
    implementation(libs.lottie.compose)
    implementation(libs.openai4j)
    implementation(libs.kotlinx.coroutines.core)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // ISI Library which contains domain & business logic
    implementation(libs.isi.lib)
}