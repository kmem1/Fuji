package com.clownteam.ui_collectionlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.clownteam.collection_domain.CourseCollection
import com.clownteam.components.DefaultButton
import com.clownteam.components.header.DefaultHeader
import com.clownteam.core.domain.EventHandler

@Composable
fun CollectionList(
    state: CollectionListState,
    eventHandler: EventHandler<CollectionListEvent>,
    imageLoader: ImageLoader,
    navigateToDetailed: (String) -> Unit = {}
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        eventHandler.obtainEvent(CollectionListEvent.GetCollections)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is CollectionListState.Error -> {
                DefaultButton(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Retry",
                    onClick = {}
                )
            }

            CollectionListState.Loading -> {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            is CollectionListState.Data -> {
                Column(modifier = Modifier.fillMaxWidth()) {
                    DefaultHeader(
                        titleText = stringResource(R.string.archive_screen_title),
                        showArrow = false,
                        bgColor = MaterialTheme.colors.primary
                    )

                    CollectionListContent(state.collections, imageLoader, navigateToDetailed)
                }
            }
        }
    }
}

@Composable
fun CollectionListContent(
    courseCollectionList: List<CourseCollection>,
    imageLoader: ImageLoader,
    navigateToDetailed: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 145.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(courseCollectionList) { item: CourseCollection ->
            CourseCollectionItem(collection = item, imageLoader) { navigateToDetailed(it.id) }
        }
    }
}

@Composable
fun CourseCollectionItem(
    collection: CourseCollection,
    imageLoader: ImageLoader,
    onClick: (CourseCollection) -> Unit
) {
    Column(
        modifier = Modifier.width(185.dp).clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF282828)).clickable { onClick(collection) }
    ) {
        Image(
            modifier = Modifier.fillMaxWidth().height(196.dp).clip(RoundedCornerShape(12.dp)),
            painter = rememberAsyncImagePainter(collection.imageUrl, imageLoader = imageLoader),
            contentDescription = stringResource(R.string.course_collection_item_img_content_description),
            contentScale = ContentScale.Crop,
            alpha = 1.0F
        )

        Row(modifier = Modifier.fillMaxWidth().height(36.dp)) {
            Text(
                text = collection.title,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                    .align(Alignment.CenterVertically),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).padding(bottom = 4.dp)) {
            Text(
                text = stringResource(R.string.course_collection_item_author_text),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier.padding(start = 4.dp).weight(1F),
                text = collection.author.name,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}
