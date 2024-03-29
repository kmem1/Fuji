apply {
    from("$rootDir/library-build.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.profileDomain))
    "implementation"(project(Modules.profileDataSource))

    "testImplementation"(Junit.junit4)
}