dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Fuji"
include(":app")
include(":components")
include(":course")
include(":course:course-domain")
include(":course:course-datasource")
include(":course:course-interactors")
include(":course:ui-courseList")
include(":course:ui-courseDetailed")
include(":constants")
include(":core")
include(":profile")
include(":profile:authorization")
include(":profile:authorization:authorization-domain")
include(":profile:authorization:authorization-interactors")
include(":profile:authorization:authorization-datasource")
include(":profile:authorization:ui-authorization")
