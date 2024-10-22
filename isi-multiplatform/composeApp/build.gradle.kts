import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version "2.0.0"
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.kotlinx.datetime)
                api(compose.materialIconsExtended)
                api(libs.precompose)
                api(libs.precompose.viewmodel)

                // Ktor dependencies
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.json)
                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.serialization.kotlinx.json)

                // Kottie => Lottie
                implementation(libs.kottie)

                // Napier Logging
                implementation(libs.napier)

                // Image loader
                implementation(libs.io.coil.kt.coil3)
                implementation(libs.io.coil.kt.coil3.compose)
                implementation(libs.io.coil.kt.coil3.network.ktor)

                // Geocoding
                implementation(libs.compass.geocoder)
                implementation(libs.compass.geolocation)

            }
        }
        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)

                // Ktor Android engine
                implementation(libs.ktor.client.android)

                // Geolocation
                implementation(libs.compass.geocoder.mobile)
                implementation(libs.compass.geolocation.mobile)
                implementation(libs.compass.permissions.mobile)

            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

                // Ktor CIO engine
                implementation(libs.ktor.client.cio)

                // Logging
                implementation(libs.logback.classic)

            }
        }
    }
}

android {
    namespace = "org.guuri11.isimultiplatform"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "org.guuri11.isimultiplatform"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.guuri11.isimultiplatform"
            packageVersion = "1.0.0"
        }
    }
}
