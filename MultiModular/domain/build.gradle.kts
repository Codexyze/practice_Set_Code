//plugins {
//    id("java-library")
//    alias(libs.plugins.jetbrains.kotlin.jvm)
//    id("kotlin-kapt")
//  //  id("com.google.dagger.hilt.android")
//    kotlin("plugin.serialization") version "2.1.10"
//    id("com.google.dagger.hilt.android")
//
//}
//plugins {
//    kotlin("jvm")
//}
//
//java {
//    sourceCompatibility = JavaVersion.VERSION_11
//    targetCompatibility = JavaVersion.VERSION_11
//}
//kotlin {
//    compilerOptions {
//        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
//    }
//}
//dependencies{
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
//    implementation("com.google.dagger:hilt-android:2.51.1")
//    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
//    //Most Error making Library
//    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
//
//
//
//}

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "2.1.10"
}

android {
    namespace = "com.nutrino.data" // 👈 change this per module
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {


    // Ktor for network
    implementation("io.ktor:ktor-client-core:2.3.4")
    implementation("io.ktor:ktor-client-cio:2.3.4")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")

    // Hilt for DI
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}
