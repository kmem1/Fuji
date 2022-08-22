package com.clownteam.ui_collectionaction.add_to_collection

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.clownteam.collection_domain.CourseCollection
import com.clownteam.components.AutoResizeText
import com.clownteam.components.DefaultButton
import com.clownteam.components.FontSizeRange
import com.clownteam.components.header.DefaultHeader
import com.clownteam.components.utils.showToast
import com.clownteam.core.domain.EventHandler
import com.clownteam.ui_collectionaction.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

private sealed class NavigationRoute {
    object Login : NavigationRoute()
    object Back : NavigationRoute()
    class CreateCollection(val courseId: String) : NavigationRoute()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddToCollectionScreen(
    state: AddToCollectionScreenState,
    eventHandler: EventHandler<AddToCollectionScreenEvent>,
    imageLoader: ImageLoader,
    collectionsFlow: Flow<PagingData<CourseCollection>>,
    onBack: () -> Unit,
    navigateToCreateCollection: (courseId: String) -> Unit,
    navigateToLogin: () -> Unit
) {
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 80.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Bottom Sheet")
            }
        },
        scrimColor = Color.Black.copy(alpha = 0.7F),
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = MaterialTheme.colors.background
    ) {
        ScreenContent(
            state = state,
            eventHandler = eventHandler,
            imageLoader = imageLoader,
            collectionsFlow = collectionsFlow,
            onBack = onBack,
            navigateToCreateCollection = navigateToCreateCollection,
            navigateToLogin = navigateToLogin,
            onSortClick = {
                coroutineScope.launch {
                    bottomState.show()
                }
            }
        )
    }
}

@Composable
private fun ScreenContent(
    state: AddToCollectionScreenState,
    eventHandler: EventHandler<AddToCollectionScreenEvent>,
    imageLoader: ImageLoader,
    collectionsFlow: Flow<PagingData<CourseCollection>>,
    onBack: () -> Unit,
    navigateToCreateCollection: (courseId: String) -> Unit,
    navigateToLogin: () -> Unit,
    onSortClick: () -> Unit
) {
    var navigationRoute by remember { mutableStateOf<NavigationRoute?>(null) }

    LaunchedEffect(key1 = navigationRoute) {
        navigationRoute?.let {
            when (it) {
                NavigationRoute.Back -> {
                    onBack()
                }

                NavigationRoute.Login -> {
                    navigateToLogin()
                }

                is NavigationRoute.CreateCollection -> {
                    navigateToCreateCollection(it.courseId)
                }
            }
        }
    }

    when {
        state.isSuccess -> {
            showToast(text = stringResource(R.string.add_to_collection_success_message))
            navigationRoute = NavigationRoute.Back
        }

        state.isUnauthorized -> navigationRoute = NavigationRoute.Login
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.secondary
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            DefaultHeader(
                titleText = "Выбор подборки",
                onArrowClick = { navigationRoute = NavigationRoute.Back }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp)
            ) {
                DefaultButton(
                    text = stringResource(R.string.create_collection_btn_text),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        navigationRoute = NavigationRoute.CreateCollection(state.courseId)
                    }
                )

                Spacer(Modifier.size(40.dp))

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    val (searchBar, sortButton) = createRefs()

                    SearchBar(
                        state = state,
                        eventHandler = eventHandler,
                        modifier = Modifier.constrainAs(searchBar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(sortButton.start, margin = 10.dp)
                            width = Dimension.fillToConstraints
                        }
                    )

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(MaterialTheme.colors.primary)
                            .clickable { onSortClick() }
                            .constrainAs(sortButton) {
                                top.linkTo(searchBar.top)
                                bottom.linkTo(searchBar.bottom)
                                end.linkTo(parent.end)
                                width = Dimension.ratio("1:1")
                                height = Dimension.fillToConstraints
                            },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sort),
                            contentDescription = null
                        )
                    }
                }

                Spacer(Modifier.size(32.dp))

                CollectionList(
                    state = state,
                    collectionsFlow = collectionsFlow,
                    imageLoader = imageLoader,
                    onCollectionClick = {
                        eventHandler.obtainEvent(
                            AddToCollectionScreenEvent.AddToCollection(it)
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    state: AddToCollectionScreenState,
    eventHandler: EventHandler<AddToCollectionScreenEvent>
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colors.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .padding(start = 10.dp)
                .height(24.dp)
                .width(24.dp),
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = stringResource(R.string.search_icon_content_description)
        )

        var isTextFieldFocused by remember { mutableStateOf(false) }

        val fieldTextStyle = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            color = Color.White
        )

        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

        BasicTextField(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .onFocusChanged { isTextFieldFocused = it.isFocused }
                .fillMaxWidth(),
            value = state.searchQuery,
            onValueChange = { eventHandler.obtainEvent(AddToCollectionScreenEvent.SetSearchQuery(it)) },
            cursorBrush = SolidColor(Color.White),
            textStyle = fieldTextStyle,
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.padding(vertical = 8.dp)) {
                    val animatedColor =
                        animateColorAsState(if (isTextFieldFocused) Color.Gray else Color.LightGray)

                    if (state.searchQuery.isEmpty()) {
                        Text(
                            stringResource(R.string.search_collection_bar_hint),
                            color = animatedColor.value,
                            style = fieldTextStyle
                        )
                    }

                    innerTextField()
                }
            }
        )
    }
}

@Composable
private fun CollectionList(
    state: AddToCollectionScreenState,
    collectionsFlow: Flow<PagingData<CourseCollection>>,
    imageLoader: ImageLoader,
    onCollectionClick: (CourseCollection) -> Unit
) {
    val collectionItems = collectionsFlow.collectAsLazyPagingItems()

    LaunchedEffect(key1 = state.searchQuery, key2 = state.shouldSearchItems) {
        if (state.shouldSearchItems) {
            collectionItems.refresh()
        }
    }

    val isLoading =
        collectionItems.loadState.refresh is LoadState.Loading || state.isCollectionListLoading
    val isError = collectionItems.loadState.refresh is LoadState.Error

    if (!isLoading && !isError) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(collectionItems) { collection ->
                collection?.let {
                    CollectionRowItem(
                        collection = collection,
                        imageLoader = imageLoader,
                        onCollectionClick = onCollectionClick
                    )
                }
            }

            with(collectionItems) {
                when (loadState.append) {
                    is LoadState.Loading -> {
                        item { AppendLoadingItem() }
                    }

                    is LoadState.Error -> {
                        item {
                            val message = getLoadCollectionsErrorMessage(state)
                            ErrorItem(message = message, modifier = Modifier.fillMaxWidth()) {
                                retry()
                            }
                        }
                    }

                    else -> {}
                }
            }
        }

        if (collectionItems.itemCount == 0 && !isLoading && !isError) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.nothing_found_message),
                    fontWeight = FontWeight.W800,
                    fontSize = 16.sp
                )
            }
        }
    } else if (isLoading) {
        LoadingCollectionsView()
    } else if (isError) {
        ErrorItem(getLoadCollectionsErrorMessage(state), modifier = Modifier.fillMaxSize()) {
            collectionItems.retry()
        }
    }
}

@Composable
fun LoadingCollectionsView() {
    val shimmerColors = listOf(
        MaterialTheme.colors.primary.copy(alpha = 0.8f),
        MaterialTheme.colors.primary.copy(alpha = 0.4f),
        MaterialTheme.colors.primary.copy(alpha = 0.8f),
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        repeat(7) {
            CollectionLoadingItem(brush)
        }
    }
}

@Composable
private fun CollectionLoadingItem(brush: Brush) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp, start = 32.dp, end = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .size(65.dp)
                .clip(RoundedCornerShape(corner = CornerSize(14.dp)))
                .background(brush)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(verticalArrangement = Arrangement.Center) {
            Spacer(
                modifier = Modifier
                    .height(16.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(fraction = 0.8f)
                    .background(brush)
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Spacer(
                modifier = Modifier
                    .height(16.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(fraction = 0.6f)
                    .background(brush)
            )
        }
    }
}

@Composable
private fun getLoadCollectionsErrorMessage(state: AddToCollectionScreenState): String {
    return if (state.getCollectionsErrorMessage != null) {
        state.getCollectionsErrorMessage.asString(LocalContext.current)
    } else {
        stringResource(id = R.string.unknown_error)
    }
}

@Composable
private fun CollectionRowItem(
    collection: CourseCollection,
    imageLoader: ImageLoader,
    onCollectionClick: (CourseCollection) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .clickable { onCollectionClick(collection) }
    ) {
        Image(
            modifier = Modifier
                .size(65.dp)
                .clip(RoundedCornerShape(CornerSize(10.dp)))
                .background(MaterialTheme.colors.primary),
            painter = rememberAsyncImagePainter(
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

@Composable
fun AppendLoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colors.secondary)
    }
}

@Composable
fun ErrorItem(message: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, fontSize = 16.sp)
        Spacer(modifier = Modifier.size(8.dp))
        DefaultButton(text = stringResource(R.string.retry), onClick = onClick)
    }
}