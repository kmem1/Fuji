apply {
    from("$rootDir/library-build.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.userArchiveDomain))

    "implementation"(Retrofit.retrofit)
    "implementation"(Retrofit.retrofitGsonConverter)
}