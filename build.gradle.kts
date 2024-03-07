// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21"
    id("com.google.dagger.hilt.android") version "2.50" apply false
    kotlin("kapt") version "1.9.20" apply false

}
buildscript {
    val objectboxVersion by extra("3.8.0") // For KTS build scripts
    repositories {
        mavenCentral()
        // Note: 2.9.0 and older are available on jcenter()
    }
    dependencies {
        // Android Gradle Plugin 4.1.0 or later supported.
        classpath("com.android.tools.build:gradle:8.3.0")
        classpath("io.objectbox:objectbox-gradle-plugin:$objectboxVersion")
    }
}


