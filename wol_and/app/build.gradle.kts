import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.25"
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.gms.google-services")
}

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))

android {
    namespace = "com.glion.wol"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.glion.wol"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "APP_KEY", localProperties.getProperty("appKey"))
        buildConfigField("String", "API_URL", localProperties.getProperty("apiUrl"))
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
    implementation(project(":crypto-module"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.kotlinx.serialization.json)
    // coroutine
    implementation(libs.kotlinx.coroutines.android)
    // splash
    implementation(libs.androidx.core.splashscreen)
    // viewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // navgiation
    implementation(libs.androidx.navigation.compose)
    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.core.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    // DataStore
    implementation(libs.androidx.datastore.preferences)
    // OkHttp3
    implementation(libs.okhttp3.okhttp)
    implementation(libs.okhttp3.logging.interceptor)
    // Retrofit2
    implementation(libs.retrofit)
    // Gson
    implementation(libs.converter.gson)
    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    // FCM
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")

    // test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}