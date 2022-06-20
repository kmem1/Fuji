package com.clownteam.ui_collectionaction.add_to_collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import com.clownteam.collection_domain.CourseCollection
import com.clownteam.components.AutoResizeText
import com.clownteam.components.DefaultButton
import com.clownteam.components.FontSizeRange
import com.clownteam.components.header.DefaultHeader
import com.clownteam.core.domain.EventHandler
import com.clownteam.ui_collectionaction.R

@Composable
fun AddToCollectionScreen(
    state: AddToCollectionScreenState,
    eventHandler: EventHandler<AddToCollectionScreenEvent>,
    imageLoader: ImageLoader,
    onBack: () -> Unit,
    navigateToCreateCollection: (courseId: String) -> Unit
) {
    when (state) {
        is AddToCollectionScreenState.Data -> {
            Column(modifier = Modifier.fillMaxSize()) {
                DefaultHeader(titleText = "Выбор подборки", onArrowClick = onBack)

                Column(modifier = Modifier.fillMaxSize().padding(top = 24.dp)) {
                    DefaultButton(
                        text = stringResource(R.string.create_collection_btn_text),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = { navigateToCreateCollection(state.courseId) }
                    )

                    Spacer(Modifier.size(40.dp))

                    if (state.collections.isNotEmpty()) {
                        CollectionList(state.collections, imageLoader) {
                            eventHandler.obtainEvent(
                                AddToCollectionScreenEvent.AddToCollection(it)
                            )
                        }
                    } else {
                        Text(
                            stringResource(R.string.no_collections_error_text),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }

        AddToCollectionScreenState.Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                DefaultButton(text = "Retry", onClick = { })
            }
        }

        AddToCollectionScreenState.Loading -> {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        AddToCollectionScreenState.Unauthorized -> {
            Box(modifier = Modifier.fillMaxSize()) {
                DefaultButton(text = "Unauthorized", onClick = { })
            }
        }
    }
}

@Composable
private fun CollectionList(
    collections: List<CourseCollection>,
    imageLoader: ImageLoader,
    onCollectionClick: (CourseCollection) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(collections) { collection ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
                    .clickable { onCollectionClick(collection) }
            ) {
                Image(
                    modifier = Modifier
                        .size(65.dp)
                        .clip(RoundedCornerShape(CornerSize(10.dp)))
                        .background(MaterialTheme.colors.primary),
                    painter = rememberImagePainter(
                        collection.imageUrl,
                        imageLoader = imageLoader
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(start = 16.dp)) {
                    AutoResizeText(
                        text = collection.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.body2,
                        fontSizeRange = FontSizeRange(min = 12.sp, max = 14.sp),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.size(2.dp))

                    Text(
                        text = collection.author.name,
                        maxLines = 2,
                        style = MaterialTheme.typography.caption,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}