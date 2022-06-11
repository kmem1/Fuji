apply {
    from("$rootDir/android-library-build.gradle")
}

dependencies {
    "implementation"(project(Modules.components))
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.collectionDomain))
    "implementation"(project(Modules.collectionInteractors))

    "implementation"(project(Modules.courseDomain))
    "implementation"(project(Modules.ui_courseList))

    "implementation"(Coil.coil)

    "testImplementation"(Junit.junit4)
    "testImplementation"(Mockito.mockitoKotlin)
    "testImplementation"(Testing.coroutines)
    "testImplementation"(Testing.coreTesting)
    "testImplementation"(ComposeTest.uiTestJunit4)
}