import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
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
        val WIFI_SSID = properties.getProperty("wifi.ssid") ?: ""
        val SERVER = properties.getProperty("server.host") ?: ""

        buildConfigField(
            type = "String",
            name = "OPEN_AI_API_KEY",
            value = OPEN_AI_API_KEY
        )
        buildConfigField(
            type = "String",
            name = "WIFI_SSID",
            value = WIFI_SSID
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-client-core:2.3.1")
    implementation("io.ktor:ktor-client-cio:2.3.1") // or use ktor-client-okhttp for Android
    implementation("io.ktor:ktor-client-json:2.3.1")
    implementation("io.ktor:ktor-client-logging:2.3.1")
    implementation("io.ktor:ktor-client-serialization:2.3.1")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.1")

    implementation(libs.play.services.wearable)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)
    implementation(libs.compose.material)
    implementation(libs.compose.foundation)
    implementation(libs.activity.compose)
    implementation(libs.core.splashscreen)
    implementation("com.airbnb.android:lottie-compose:4.0.0")
    implementation("dev.ai4j:openai4j:0.17.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

}