package com.clownteam.ui_collectiondetailed.ui.edit

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.clownteam.components.DefaultButton
import com.clownteam.components.DefaultTextField
import com.clownteam.components.header.DefaultHeader
import com.clownteam.core.domain.EventHandler
import com.example.ui_collectiondetailed.R

private sealed class NavigationRoute {
    object Login : NavigationRoute()
}

@Composable
fun EditCollectionScreen(
    state: EditCollectionScreenState,
    eventHandler: EventHandler<EditCollectionScreenEvent>,
    imageLoader: ImageLoader,
    navigateBack: () -> Unit,
    navigateToLogin: () -> Unit
) {
    val context = LocalContext.current

    var navigationRoute by remember { mutableStateOf<NavigationRoute?>(null) }

    LaunchedEffect(key1 = navigationRoute) {
        navigationRoute?.let {
            when (it) {
                NavigationRoute.Login -> {
                    navigateToLogin()
                }
            }
        }
    }

    LaunchedEffect(key1 = state.message) {
        if (state.message != null) {
            Toast.makeText(context, state.message.asString(context), Toast.LENGTH_SHORT).show()
            eventHandler.obtainEvent(EditCollectionScreenEvent.MessageShown)
        }
    }

    if (state.isUnauthorized) {
        navigationRoute = NavigationRoute.Login
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (mainLayout, saveBtn) = createRefs()

        Column(modifier = Modifier.constrainAs(mainLayout) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            if (state.isLoading) {
                LinearProgressIndicator(
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            DefaultHeader(
                titleText = stringResource(R.string.edit_collection_header_text),
                onArrowClick = { navigateBack() }
            )

            Spacer(Modifier.size(40.dp))

            AvatarView(
                avatarUrl = state.collectionImgUrl,
                imageLoader = imageLoader,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            TitleTextField(
                state = state,
                eventHandler = eventHandler,
                modifier = Modifier
                    .padding(top = 46.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )

            DescriptionTextField(
                state = state,
                eventHandler = eventHandler,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )
        }

        DefaultButton(
            text = stringResource(R.string.save),
            modifier = Modifier.constrainAs(saveBtn) {
                bottom.linkTo(parent.bottom, 30.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            enabled = !state.isLoading
        ) {
            eventHandler.obtainEvent(EditCollectionScreenEvent.ApplyChanges)
        }
    }
}

@Composable
fun AvatarView(avatarUrl: String?, imageLoader: ImageLoader, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(120.dp)
            .height(126.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(avatarUrl, imageLoader = imageLoader),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )

        Box(
            modifier = Modifier
                .padding(end = 12.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF404040))
                .align(Alignment.BottomEnd)
                .padding(7.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun TitleTextField(
    state: EditCollectionScreenState,
    eventHandler: EventHandler<EditCollectionScreenEvent>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.title),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(Modifier.size(8.dp))

        DefaultTextField(
            value = state.title,
            onValueChange = { eventHandler.obtainEvent(EditCollectionScreenEvent.SetTitle(it)) },
            hint = "",
            isError = state.titleError != null,
            errorText = state.titleError?.asString(context) ?: "",
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        )
    }
}

@Composable
fun DescriptionTextField(
    state: EditCollectionScreenState,
    eventHandler: EventHandler<EditCollectionScreenEvent>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.description),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(Modifier.size(8.dp))

        DefaultTextField(
            value = state.description,
            onValueChange = { eventHandler.obtainEvent(EditCollectionScreenEvent.SetDescription(it)) },
            hint = stringResource(R.string.description_hint),
            isError = state.descriptionError != null,
            errorText = state.descriptionError?.asString(context) ?: "",
            hintDisabledColor = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
            textFieldModifier = Modifier.height(140.dp),
            enabled = !state.isLoading
        )
    }
}
