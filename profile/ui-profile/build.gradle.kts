apply {
    from("$rootDir/android-library-build.gradle")
}

dependencies {
    "implementation"(project(Modules.components))
    "implementation"(project(Modules.core))

    "implementation"(project(Modules.profileDomain))
    "implementation"(project(Modules.profileInteractors))

    "implementation"(Coil.coil)
}