package com.clownteam.ui_profile.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.clownteam.components.header.DefaultHeader
import com.clownteam.core.domain.EventHandler
import com.clownteam.ui_profile.R

private val mainItems = listOf(
    UiSettingsItem.ChangeProfile(),
    UiSettingsItem.ChangePassword()
)

private val bottomItems = listOf(
    UiSettingsItem.SignOut()
)

private sealed class NavigationRoute {
    object Login : NavigationRoute()
    object ChangeProfile : NavigationRoute()
    object ChangePassword : NavigationRoute()
}

@Composable
fun SettingsScreen(
    state: SettingsScreenState,
    eventHandler: EventHandler<SettingsScreenEvent>,
    navigateToLogin: () -> Unit,
    navigateToChangeProfile: () -> Unit,
    navigateToChangePassword: () -> Unit,
    navigateBack: () -> Unit
) {

    var navigationRoute by remember { mutableStateOf<NavigationRoute?>(null) }

    LaunchedEffect(key1 = navigationRoute) {
        when (navigationRoute) {
            NavigationRoute.Login -> {
                navigateToLogin()
            }

            NavigationRoute.ChangeProfile -> {
                navigateToChangeProfile()
            }

            NavigationRoute.ChangePassword -> {
                navigateToChangePassword()
            }

            null -> {}
        }
    }

    if (state.signedOut) navigationRoute = NavigationRoute.Login

    val onSettingsItemClick: (UiSettingsItem) -> Unit = {
        when (it) {
            is UiSettingsItem.ChangePassword -> {
                navigationRoute = NavigationRoute.ChangePassword
            }

            is UiSettingsItem.ChangeProfile -> {
                navigationRoute = NavigationRoute.ChangeProfile
            }

            is UiSettingsItem.SignOut -> {
                eventHandler.obtainEvent(SettingsScreenEvent.SignOut)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DefaultHeader(titleText = stringResource(R.string.settings_title), onArrowClick = { navigateBack() })

        Spacer(Modifier.size(32.dp))

        SettingsItems(items = mainItems, onClick = onSettingsItemClick)

        Spacer(modifier = Modifier.weight(1F))

        SettingsItems(items = bottomItems, onClick = onSettingsItemClick)

        Spacer(Modifier.size(26.dp))
    }
}

@Composable
private fun ColumnScope.SettingsItems(
    items: List<UiSettingsItem>,
    onClick: (UiSettingsItem) -> Unit
) {
    for ((index, item) in items.withIndex()) {
        SettingsItem(
            item = item,
            onClick = { onClick(item) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        if (index != items.lastIndex) {
            Spacer(modifier = Modifier.size(12.dp))
        }
    }
}

@Composable
private fun SettingsItem(
    item: UiSettingsItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colors.primary)
            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = item.iconResId),
            contentDescription = null,
            modifier = Modifier.size(30.dp),
            tint = item.contentColor
        )

        Spacer(Modifier.size(8.dp))

        Text(
            text = stringResource(id = item.titleTextResId),
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.h5,
            color = item.contentColor
        )

        Spacer(modifier = Modifier.weight(1F))

        if (item.showArrow) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_right),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(20.dp)
            )
        }
    }
}

sealed class UiSettingsItem(
    @DrawableRes
    val iconResId: Int,
    @StringRes
    val titleTextResId: Int,
    val contentColor: Color = Color.White,
    val showArrow: Boolean = true
) {
    class ChangeProfile : UiSettingsItem(
        iconResId = R.drawable.ic_person,
        titleTextResId = R.string.change_profile_text
    )

    class ChangePassword : UiSettingsItem(
        iconResId = R.drawable.ic_key,
        titleTextResId = R.string.change_password_text
    )

    class SignOut : UiSettingsItem(
        iconResId = R.drawable.ic_logout,
        titleTextResId = R.string.sign_out,
        contentColor = Color(0xFFFF5252),
        showArrow = false
    )
}