plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id ("kotlin-parcelize")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.service.composesample"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.service.composesample"
        minSdk = 24
        targetSdk = 34
        versionCode = 5
        versionName = "1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    implementation("androidx.compose.material3:material3-android:1.2.1")
    val dagger_version = "2.48"
    val room_version = "2.6.1"
    val retrofit = "2.9.0"
    val okhttp = "4.11.0"
    val kotlin_coroutines = "1.8.0"

    implementation("androidx.core:core-ktx:1.13.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material:1.6.7")


    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.7")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    //testing impl
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    //Dagger
    implementation ("com.google.dagger:dagger:$dagger_version")
    ksp ("com.google.dagger:dagger-compiler:$dagger_version")

    //Room DB
    implementation ( "androidx.room:room-runtime:$room_version")
    ksp ( "androidx.room:room-compiler:$room_version")
    implementation ( "androidx.room:room-ktx:$room_version")
    implementation ( "androidx.room:room-rxjava2:$room_version")

    //Retrofit
    implementation (  "com.squareup.retrofit2:retrofit:$retrofit")
    implementation (  "com.squareup.okhttp3:okhttp:$okhttp")
    implementation (  "com.squareup.retrofit2:converter-gson:$retrofit")

    implementation ( "org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlin_coroutines")
    implementation ("com.google.accompanist:accompanist-permissions:0.30.0")

    //implementation ("com.google.firebase:firebase-database-ktx:21.0.0")
    implementation ("com.google.firebase:firebase-firestore-ktx:25.0.0")
    implementation ("com.google.firebase:firebase-auth-ktx:23.0.0")


    implementation ("com.google.accompanist:accompanist-pager:0.28.0")

    implementation ("com.google.accompanist:accompanist-coil:0.15.0")

    implementation("me.onebone:toolbar-compose:2.3.5")

}
