plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    //Add this
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "2.1.10"

}

android {
    namespace = "com.example.network"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    //add this
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Dependency from build.gradle.kts(:app)
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

    //Ktor
    //implementation("androidx.core:core-splashscreen:1.0.0")
    implementation("io.ktor:ktor-client-core:2.3.5") // Core Ktor client
    implementation("io.ktor:ktor-client-cio:2.3.5")  // For making network requests
    implementation("io.ktor:ktor-client-content-negotiation:2.3.5") // For automatic JSON serialization
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5") // Kotlinx serialization support
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0") // JSON parser
    //navigation JETPACK COMPOSE
    val nav_version = "2.8.6"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    //Coil Image Loading
    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")
    implementation("com.airbnb.android:lottie-compose:6.4.0")
    implementation ("io.github.shashank02051997:FancyToast:2.0.2")


    //Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    //Most Error making Library
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")



}

kapt {
    correctErrorTypes = true
}
