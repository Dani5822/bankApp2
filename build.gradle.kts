// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    kotlin("jvm")
}



    buildscript {
        repositories {
            mavenCentral()
        }
        dependencies {
            classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.9.0")
        }
    }


dependencies {
    implementation(kotlin("stdlib-jdk8"))
}
kotlin {
    jvmToolchain(8)
}