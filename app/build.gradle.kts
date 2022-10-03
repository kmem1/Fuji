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

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
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
    implementation(project(Modules.ui_coursePassing))

    implementation(project(Modules.authorizationDataSource))
    implementation(project(Modules.authorizationDomain))
    implementation(project(Modules.authorizationInteractors))
    implementation(project(Modules.ui_authorization))

    implementation(project(Modules.profileDataSource))
    implementation(project(Modules.profileDomain))
    implementation(project(Modules.profileInteractors))
    implementation(project(Modules.ui_profile))

    implementation(project(Modules.collectionDataSource))
    implementation(project(Modules.collectionDomain))
    implementation(project(Modules.collectionInteractors))
    implementation(project(Modules.ui_collectionList))
    implementation(project(Modules.ui_collectionDetailed))
    implementation(project(Modules.ui_collectionAction))

    implementation(project(Modules.searchDomain))
    implementation(project(Modules.searchInteractors))
    implementation(project(Modules.searchDatasource))
    implementation(project(Modules.ui_search))

    implementation(project(Modules.userArchiveDomain))
    implementation(project(Modules.userArchiveInteractors))
    implementation(project(Modules.userArchiveDatasource))
    implementation(project(Modules.ui_userArchive))

    implementation(AndroidX.coreKtx)
    implementation(AndroidX.coreSplashScreen)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.constraintLayout)

    implementation(Accompanist.animations)
    implementation(Accompanist.pager)
    implementation(Accompanist.pagerIndicators)

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
    implementation(Retrofit.okhttpLoggingInterceptor)

    implementation(Kotpref.core)

    testImplementation(Junit.junit4)
    androidTestImplementation(AndroidXTest.extJunit)
    androidTestImplementation(AndroidXTest.espressoCore)
    androidTestImplementation(ComposeTest.uiTestJunit4)
    debugImplementation(ComposeTest.uiTestManifest)
}