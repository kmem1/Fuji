apply {
    from("$rootDir/android-library-build.gradle")
}

dependencies {
    "implementation"(project(Modules.core))

    "implementation"(AndroidX.paging)
    "implementation"(Retrofit.retrofitGsonConverter)
}