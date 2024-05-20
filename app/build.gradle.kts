plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    kotlin("kapt")
}

android {
    namespace = "com.example.collegeconnected"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.collegeconnected"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //circular image
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //loading button
    implementation ("br.com.simplepass:loading-button-android:2.2.0")

    //kotlin("kapt")
    // def nav_version = "2.5.2"
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.7")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    //circular image
    implementation ("de.hdodenhof:circleimageview:3.1.0")


    //viewpager2 indicator
    implementation ("io.github.vejei.viewpagerindicator:viewpagerindicator:1.0.0-alpha.1")

    //stepView
    implementation ("com.shuhart.stepview:stepview:1.5.1")

    //Android Ktx
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")

    //Dagger hilt
    implementation ("com.google.dagger:hilt-android:2.48.1")
    kapt ("com.google.dagger:hilt-compiler:2.48.1")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // bottom nav
    implementation ("com.github.ibrahimsn98:SmoothBottomBar:1.7.9")
}