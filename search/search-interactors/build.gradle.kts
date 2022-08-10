apply {
    from("$rootDir/library-build.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.searchDatasource))
    "implementation"(project(Modules.searchDomain))

    "implementation"(Kotlinx.coroutinesCore) // need for flows

    "testImplementation"(Junit.junit4)
}