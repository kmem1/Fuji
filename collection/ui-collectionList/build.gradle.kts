apply {
    from("$rootDir/android-library-build.gradle")
}

dependencies {
    "implementation"(project(Modules.components))
    "implementation"(project(Modules.core))

    "implementation"(project(Modules.collectionDomain))
    "implementation"(project(Modules.collectionInteractors))

    "implementation"(Coil.coil)
}