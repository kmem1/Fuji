apply {
    from("$rootDir/library-build.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.courseDomain))

    "implementation"(Retrofit.retrofit)
    "implementation"(Retrofit.retrofitGsonConverter)
}