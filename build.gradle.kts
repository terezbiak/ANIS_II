// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
    id ("com.android.library") version "8.1.1" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
}
buildscript {
    dependencies {
        classpath  ("com.google.gms:google-services:4.3.14")
        classpath ("com.android.tools.build:gradle:4.0.0")
    }
}