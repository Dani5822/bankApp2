
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.kotlin.dsl.register
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    alias(libs.plugins.android.application)
    kotlin("android") version "1.8.22"
    id("org.jetbrains.dokka")
}

android {
    namespace = "com.example.bankapp2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bankapp2"
        minSdk = 26
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
    buildFeatures {
        viewBinding = true
    }



}

tasks.findByName("dokkaHtml") ?: tasks.register<DokkaTask>("dokkaHtml") {
    outputDirectory.set(layout.buildDirectory.dir("javadoc"))
    dokkaSourceSets {
        register("main") {
            displayName.set("Main")
            sourceRoots.from(file("src/main/java"))
            platform.set(org.jetbrains.dokka.Platform.jvm)
            noAndroidSdkLink.set(false)
        }
    }
}

tasks.findByName("javadoc") ?: tasks.register("javadoc", Javadoc::class) {
    val mainSourceSet = android.sourceSets.getByName("main")
    source = project.files(mainSourceSet.java.srcDirs).asFileTree
    classpath = files(android.bootClasspath) + files(mainSourceSet.java.srcDirs)
    isFailOnError=false
}


dependencies {
    implementation(libs.swiperefreshlayout)
    implementation(libs.mpandroidchart)
    implementation(libs.material.v190)
    implementation(libs.logging.interceptor)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.extensions)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.annotation)
    implementation(libs.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.v115)
    androidTestImplementation(libs.espresso.core.v351)
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    }
