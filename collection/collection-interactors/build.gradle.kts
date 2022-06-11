apply {
    from("$rootDir/library-build.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.collectionDomain))
    "implementation"(project(Modules.collectionDataSource))

    "implementation"(project(Modules.courseDomain))

    "testImplementation"(Junit.junit4)
}