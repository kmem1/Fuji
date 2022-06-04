apply {
    from("$rootDir/library-build.gradle")
}

dependencies {
    "implementation"(Retrofit.retrofitGsonConverter)
}