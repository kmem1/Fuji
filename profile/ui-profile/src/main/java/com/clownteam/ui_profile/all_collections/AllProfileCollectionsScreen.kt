package com.clownteam.ui_profile.all_collections

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.clownteam.components.DefaultButton
import com.clownteam.components.header.DefaultHeader
import com.clownteam.core.domain.EventHandler
import com.clownteam.profile_domain.ProfileCollection
import com.clownteam.ui_profile.R

private sealed class NavigationRoute {
    class Collection(val collectionId: String) : NavigationRoute()
}

@Composable
fun AllProfileCollectionsScreen(
    state: AllProfileCollectionsScreenState,
    eventHandler: EventHandler<AllProfileCollectionsScreenEvent>,
    imageLoader: ImageLoader,
    navigateBack: () -> Unit,
    navigateToCollection: (String) -> Unit = {},
) {
    var navigationRoute by remember { mutableStateOf<NavigationRoute?>(null) }

    LaunchedEffect(key1 = navigationRoute) {
        navigationRoute?.let {
            when (it) {
                is NavigationRoute.Collection -> {
                    navigateToCollection(it.collectionId)
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DefaultHeader(
            titleText = stringResource(R.string.all_collections_title),
            onArrowClick = navigateBack
        )

        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.errorMessage != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column {
                    Text(text = state.errorMessage.asString(LocalContext.current))

                    Spacer(Modifier.size(8.dp))

                    DefaultButton(text = stringResource(R.string.retry)) {
                        eventHandler.obtainEvent(AllProfileCollectionsScreenEvent.GetCollections)
                    }
                }
            }
        } else {
            if (state.collections.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.no_collections_added),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                CollectionList(
                    state.collections,
                    imageLoader,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    onItemClick = { navigationRoute = NavigationRoute.Collection(it.collectionId) }
                )
            }
        }
    }
}

@Composable
fun CollectionList(
    collections: List<ProfileCollection>,
    imageLoader: ImageLoader,
    onItemClick: (ProfileCollection) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 145.dp),
        contentPadding = PaddingValues(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(collections.size) { index ->
            ProfileCollectionItem(
                collection = collections[index],
                imageLoader = imageLoader,
                onClick = onItemClick
            )
        }
    }
}

@Composable
fun ProfileCollectionItem(
    collection: ProfileCollection,
    imageLoader: ImageLoader,
    onClick: (ProfileCollection) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(185.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF282828))
            .clickable { onClick(collection) }
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(176.dp)
                .clip(RoundedCornerShape(12.dp)),
            painter = rememberAsyncImagePainter(collection.imageUrl, imageLoader = imageLoader),
            contentDescription = stringResource(R.string.course_collection_item_img_content_description),
            contentScale = ContentScale.Crop,
            alpha = 1.0F
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
        ) {
            Text(
                text = collection.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .align(Alignment.CenterVertically),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(bottom = 4.dp)
        ) {
            Text(
                text = stringResource(R.string.course_collection_item_author_text),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .weight(1F),
                text = collection.authorName,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}