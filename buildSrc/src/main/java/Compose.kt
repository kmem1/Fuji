object Compose {
    private const val activityComposeVersion = "1.6.1"
    const val activity = "androidx.activity:activity-compose:$activityComposeVersion"

    const val composeVersion = "1.3.2"
    const val ui = "androidx.compose.ui:ui:$composeVersion"
    const val tooling = "androidx.compose.ui:ui-tooling:$composeVersion"

    private const val composeMaterialVersion = "1.3.1"
    const val material = "androidx.compose.material:material:$composeMaterialVersion"

    // androidx.compose.compiler:compiler
    const val composeCompilerVersion = "1.3.2"

    private const val constraintLayoutVersion = "1.0.1"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:$constraintLayoutVersion"

    private const val navigationVersion = "2.5.3"
    const val navigation = "androidx.navigation:navigation-compose:$navigationVersion"

    private const val hiltNavigationComposeVersion = "1.0.0"
    const val hiltNavigation = "androidx.hilt:hilt-navigation-compose:$hiltNavigationComposeVersion"
}

object ComposeTest {
    const val uiTestJunit4 = "androidx.compose.ui:ui-test-junit4:${Compose.composeVersion}"
    const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:${Compose.composeVersion}"
}