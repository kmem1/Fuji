package com.clownteam.ui_collectionlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import com.clownteam.collection_domain.CourseCollection
import com.clownteam.components.DefaultButton
import com.clownteam.core.domain.EventHandler

@Composable
fun CollectionList(
    state: CollectionListState,
    eventHandler: EventHandler<CollectionListEvent>,
    imageLoader: ImageLoader
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
                LinearProgressIndicator()
            }

            is CollectionListState.Data -> {
                CollectionListContent(state.collections, imageLoader)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun CollectionListContent(courseCollectionList: List<CourseCollection>, imageLoader: ImageLoader) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 185.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(courseCollectionList) { item: CourseCollection ->
            CourseCollectionItem(collection = item, imageLoader)
        }
    }
}

@Composable
fun CourseCollectionItem(collection: CourseCollection, imageLoader: ImageLoader) {
    Column(modifier = Modifier.width(185.dp)) {
        Image(
            modifier = Modifier.fillMaxWidth().height(196.dp).clip(RoundedCornerShape(12.dp)),
            painter = rememberImagePainter(collection.imageUrl, imageLoader = imageLoader),
            contentDescription = stringResource(R.string.course_collection_item_img_content_description),
            contentScale = ContentScale.Crop
        )

        Row(modifier = Modifier.fillMaxWidth().height(30.dp)) {
            Text(
                text = collection.title,
                modifier = Modifier.fillMaxWidth().height(30.dp).padding(horizontal = 10.dp)
                    .align(Alignment.CenterVertically),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
            Text(
                text = stringResource(R.string.course_collection_item_author_text),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier.padding(start = 4.dp).weight(1F),
                text = collection.author,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}
