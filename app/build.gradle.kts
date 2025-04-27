

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.project_simplrepair"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.project_simplrepair"
        minSdk = 25
        targetSdk = 34
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
        viewBinding = true
    }
}

dependencies {

    // Permission
    implementation(libs.accompanist.permissions)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.constraintlayout.compose.android)
    implementation(libs.firebase.auth)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.graphics.shapes.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // ROOM
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.appcompat)
    annotationProcessor(libs.androidx.room.room.compiler)
    ksp(libs.androidx.room.room.compiler)

    // moshi
    implementation(libs.moshi.kotlin)
    implementation(libs.converter.moshi)

    //coil compose
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    implementation(libs.androidx.animation) // or latest
    implementation(libs.androidx.navigation.compose) // match your setup
    implementation(libs.material3) // for Material3

    // extended icons
    implementation(libs.androidx.material.icons.extended)

    // 3) CameraX (all at 1.2.3)
    implementation(libs.camera.core)
    implementation(libs.camera.camera2)
    implementation(libs.androidx.camera.lifecycle.v123)
    implementation(libs.androidx.camera.view.v123)

    // 5) Lifecycle & ViewModel
    implementation(libs.androidx.lifecycle.runtime.ktx.v281)
    implementation(libs.lifecycle.viewmodel.ktx)

    val composeBom = platform("androidx.compose:compose-bom:2025.02.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Choose one of the following:
    // Material Design 3
    implementation(libs.androidx.compose.material3.material3)
    // or Material Design 2

    // such as input and measurement/layout
    implementation(libs.ui)

    // Android Studio Preview support
    implementation(libs.ui.tooling.preview)
    debugImplementation(libs.ui.tooling)

    // UI Tests
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.test.manifest)


}

