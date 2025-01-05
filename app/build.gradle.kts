plugins {
    id("com.android.application")
    //new
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

//apply plugin: ("com.google.gms.google-services")

android {
    namespace = "sk.tuke.zadanie_zajko"
    compileSdk = 34

    defaultConfig {
        applicationId = "sk.tuke.zadanie_zajko"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    val room_version = "2.6.1"

    //tieto 4 su nove
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics:18.6.1")
    implementation ("com.google.firebase:firebase-auth:21.0.1")
    implementation ("com.google.firebase:firebase-storage:20.3.0")
    implementation ("com.google.android.gms:play-services-auth:20.1.0")
    implementation ("com.google.firebase:firebase-database:20.1.0")
    implementation ("com.google.firebase:firebase-firestore:24.1.0")
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:1.1.5")
    //implementation ("com.firebaseui:firebase-ui-auth:4.3.2")

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
