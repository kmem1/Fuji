package com.clownteam.ui_profile.change_profile

import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.clownteam.components.ImagesProviderUtils
import com.clownteam.components.header.DefaultHeader
import com.clownteam.core.domain.EventHandler
import com.clownteam.ui_profile.R

@Composable
fun ChangeProfileScreen(
    state: ChangeProfileScreenState,
    eventHandler: EventHandler<ChangeProfileScreenEvent>,
    imageLoader: ImageLoader,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = state.message) {
        if (state.message != null) {
            Toast.makeText(context, state.message.asString(context), Toast.LENGTH_SHORT).show()
            eventHandler.obtainEvent(ChangeProfileScreenEvent.MessageShown)
        }
    }

    val getPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val bitmap = ImagesProviderUtils.getImageBitmapByUri(context, uri)
            val imageFile = ImagesProviderUtils.getImageFileByUri(context, uri)

            eventHandler.obtainEvent(ChangeProfileScreenEvent.SetImage(imageFile, bitmap))
        }
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
                titleText = stringResource(R.string.change_profile_title_text),
                onArrowClick = { navigateBack() }
            )

            Spacer(Modifier.size(40.dp))

            AvatarView(
                avatarUrl = state.avatarUrl,
                imageLoader = imageLoader,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { getPhotoLauncher.launch("image/*") },
                newImageBitmap = state.imageFileBitmap
            )

            UsernameTextField(
                state = state,
                eventHandler = eventHandler,
                modifier = Modifier
                    .padding(top = 46.dp)
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
            eventHandler.obtainEvent(ChangeProfileScreenEvent.ApplyChanges)
        }
    }
}

@Composable
fun AvatarView(
    avatarUrl: String?,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    newImageBitmap: Bitmap? = null
) {
    val imagePainterModel = newImageBitmap ?: avatarUrl

    Box(
        modifier = modifier
            .width(120.dp)
            .height(126.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(imagePainterModel, imageLoader = imageLoader),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable { onClick() },
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .padding(end = 12.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF404040))
                .align(Alignment.BottomEnd)
                .clickable { onClick() }
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
fun UsernameTextField(
    state: ChangeProfileScreenState,
    eventHandler: EventHandler<ChangeProfileScreenEvent>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(modifier = modifier) {
        Text(
            text = "Псевдоним",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(Modifier.size(8.dp))

        DefaultTextField(
            value = state.username,
            onValueChange = { eventHandler.obtainEvent(ChangeProfileScreenEvent.SetUsername(it)) },
            hint = "",
            isError = state.usernameError != null,
            errorText = state.usernameError?.asString(context) ?: "",
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        )
    }
}
