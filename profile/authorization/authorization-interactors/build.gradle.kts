apply {
    from("$rootDir/library-build.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.authorizationDomain))
    "implementation"(project(Modules.authorizationDataSource))

    "implementation"(Kotlinx.coroutinesCore) // need for flows

    "testImplementation"(Junit.junit4)
}