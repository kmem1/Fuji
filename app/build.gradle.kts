plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Android.compileSdk
    buildToolsVersion = Android.buildTools

    defaultConfig {
        applicationId = Android.appId
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
        versionCode = Android.versionCode
        versionName = Android.versionName

        testInstrumentationRunner = "com.clownteam.fuji.CustomTestRunner"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Compose.composeVersion
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
}

dependencies {

    implementation(project(Modules.core))

    implementation(project(Modules.courseDataSource))
    implementation(project(Modules.courseDomain))
    implementation(project(Modules.courseInteractors))
    implementation(project(Modules.ui_courseList))
    implementation(project(Modules.ui_courseDetailed))

    implementation(project(Modules.authorizationDataSource))
    implementation(project(Modules.authorizationDomain))
    implementation(project(Modules.authorizationInteractors))
    implementation(project(Modules.ui_authorization))

    implementation(AndroidX.coreKtx)
    implementation(AndroidX.coreSplashScreen)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.constraintLayout)

    implementation(Accompanist.animations)

    implementation(Google.material)

    implementation(Compose.activity)
    implementation(Compose.ui)
    implementation(Compose.constraintLayout)
    implementation(Compose.material)
    implementation(Compose.tooling)
    implementation(Compose.navigation)
    implementation(Compose.hiltNavigation)

    implementation(Hilt.android)
    kapt(Hilt.compiler)

    implementation(Coil.coil)

    implementation(Retrofit.retrofit)
    implementation(Retrofit.retrofitGsonConverter)

    testImplementation(Junit.junit4)
    androidTestImplementation(AndroidXTest.extJunit)
    androidTestImplementation(AndroidXTest.espressoCore)
    androidTestImplementation(ComposeTest.uiTestJunit4)
    debugImplementation(ComposeTest.uiTestManifest)
}