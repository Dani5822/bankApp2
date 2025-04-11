// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
}



    buildscript {
        repositories {
            mavenCentral()
        }
        dependencies {
            classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.9.0")
        }
    }


