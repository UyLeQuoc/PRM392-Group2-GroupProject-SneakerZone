plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.group2.prm392_group2_sneakerzone"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.group2.prm392_group2_sneakerzone"
        minSdk = 26
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
}


dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)


    // Glide
    implementation(libs.glide)
    implementation(files("libs/zpdk-release-v3.1.aar"))

    implementation(files("libs/zpdk-release-v3.1.aar"))
    implementation(libs.okhttp.v460)
    implementation(libs.commons.codec)

// or latest version
    annotationProcessor(libs.glide.compiler)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}