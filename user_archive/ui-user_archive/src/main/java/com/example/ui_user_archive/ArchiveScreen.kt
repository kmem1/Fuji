package com.example.ui_user_archive

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.clownteam.components.DefaultButton
import com.clownteam.components.SearchTabRow
import com.clownteam.components.pagerTabIndicatorOffset
import com.clownteam.core.domain.EventHandler
import com.clownteam.ui_search.styles.ArchiveScreenColors
import com.clownteam.user_archive_domain.ArchiveCategory
import com.clownteam.user_archive_domain.ArchiveItem
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

private sealed class NavigationRoute {
    object Login : NavigationRoute()
    class CourseDetailed(val courseId: String) : NavigationRoute()
    class CollectionDetailed(val collectionId: String) : NavigationRoute()
}

@Composable
fun ArchiveScreen(
    state: ArchiveScreenState,
    eventHandler: EventHandler<ArchiveScreenEvent>,
    imageLoader: ImageLoader,
    coursesFlow: Flow<PagingData<ArchiveItem.Course>>,
    collectionsFlow: Flow<PagingData<ArchiveItem.Collection>>,
    navigateToLogin: () -> Unit,
    navigateToCourse: (String) -> Unit,
    navigateToCollection: (String) -> Unit
) {
    var navigationRoute by remember { mutableStateOf<NavigationRoute?>(null) }

    LaunchedEffect(key1 = navigationRoute) {
        navigationRoute?.let {
            when (it) {
                is NavigationRoute.CollectionDetailed -> navigateToCollection(it.collectionId)
                is NavigationRoute.CourseDetailed -> navigateToCourse(it.courseId)
                NavigationRoute.Login -> navigateToLogin()
            }
        }
    }

    if (state.isUnauthorized) {
        navigationRoute = NavigationRoute.Login
    }

    var showSearchBar by remember { mutableStateOf(state.query.isNotEmpty()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            val (titleText, searchBtn) = createRefs()

            Text(
                "Мой архив",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(titleText) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            IconButton(
                onClick = {
                    showSearchBar = !showSearchBar
                    if (!showSearchBar) eventHandler.obtainEvent(ArchiveScreenEvent.SetQuery(""))
                },
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(searchBtn) {
                        end.linkTo(parent.end, 14.dp)
                    }
            ) {
                if (showSearchBar) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = null
                    )
                } else {
                    Icon(
                        painterResource(id = R.drawable.ic_search),
                        contentDescription = null
                    )
                }
            }
        }

        AnimatedVisibility(visible = showSearchBar) {
            SearchBar(
                modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 12.dp),
                state = state,
                eventHandler = eventHandler
            )
        }

        Spacer(modifier = Modifier.size(24.dp))

        val filters = listOf("Курсы", "Подборки")

        MainPager(
            screenState = state,
            filters = filters,
            imageLoader = imageLoader,
            coursesFlow = coursesFlow,
            collectionsFlow = collectionsFlow,
            navigateToCourse = { navigationRoute = NavigationRoute.CourseDetailed(it) },
            navigateToCollection = { navigationRoute = NavigationRoute.CollectionDetailed(it) }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    state: ArchiveScreenState,
    eventHandler: EventHandler<ArchiveScreenEvent>
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(ArchiveScreenColors.SearchBarBackgroundColor),
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
            fontSize = 16.sp,
            color = Color.White
        )

        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

        BasicTextField(
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .fillMaxWidth()
                .onFocusChanged { isTextFieldFocused = it.isFocused },
            value = state.query,
            onValueChange = { eventHandler.obtainEvent(ArchiveScreenEvent.SetQuery(it)) },
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

                    if (state.query.isEmpty()) {
                        Text(
                            stringResource(R.string.search_bar_hint),
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

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MainPager(
    screenState: ArchiveScreenState,
    imageLoader: ImageLoader,
    coursesFlow: Flow<PagingData<ArchiveItem.Course>>,
    collectionsFlow: Flow<PagingData<ArchiveItem.Collection>>,
    navigateToCourse: (String) -> Unit,
    navigateToCollection: (String) -> Unit,
    filters: List<String>
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()

    SearchTabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.Transparent,
        divider = {},
        edgePadding = 30.dp,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier
                    .pagerTabIndicatorOffset(pagerState, tabPositions)
                    .clip(RoundedCornerShape(8.dp)),
                height = 4.dp,
                color = MaterialTheme.colors.secondary
            )
        }
    ) {
        filters.forEachIndexed { index, s ->
            Tab(
                selected = pagerState.currentPage == index,
                content = {
                    Text(
                        text = s,
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.padding(bottom = 10.dp, start = 6.dp, end = 6.dp)
                    )
                },
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }

    HorizontalPager(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        state = pagerState,
        count = filters.size,
        verticalAlignment = Alignment.Top
    ) { page ->
        val filter = when (page) {
            0 -> ArchiveCategory.Courses
            1 -> ArchiveCategory.Collections
            else -> ArchiveCategory.Courses
        }

        SearchResultsWindow(
            state = screenState,
            currentFilter = filter,
            imageLoader = imageLoader,
            coursesFlow = coursesFlow,
            collectionsFlow = collectionsFlow,
            navigateToCourse = navigateToCourse,
            navigateToCollection = navigateToCollection
        )
    }
}

@Composable
private fun SearchResultsWindow(
    state: ArchiveScreenState,
    currentFilter: ArchiveCategory,
    imageLoader: ImageLoader,
    coursesFlow: Flow<PagingData<ArchiveItem.Course>>,
    collectionsFlow: Flow<PagingData<ArchiveItem.Collection>>,
    navigateToCourse: (String) -> Unit,
    navigateToCollection: (String) -> Unit
) {
    when (currentFilter) {
        ArchiveCategory.Collections -> {
            val collectionPagingItems = collectionsFlow.collectAsLazyPagingItems()
            ItemsList(
                state,
                collectionPagingItems,
                imageLoader,
                navigateToCollection,
                itemContent = { collection, _imageLoader, navigateToDetailed ->
                    CourseCollectionItem(collection, _imageLoader, navigateToDetailed)
                },
                loadingState = { LoadingWindow() },
                noItemsMessage = stringResource(R.string.no_added_collections_message)
            )
        }

        ArchiveCategory.Courses -> {
            val coursePagingItems = coursesFlow.collectAsLazyPagingItems()
            ItemsList(
                state,
                coursePagingItems,
                imageLoader,
                navigateToCourse,
                itemContent = { course, _imageLoader, navigateToDetailed ->
                    CourseItem(course, _imageLoader, navigateToDetailed)
                },
                loadingState = { LoadingWindow() },
                noItemsMessage = stringResource(R.string.no_added_courses_message)
            )
        }
    }
}

@Composable
private fun <T : Any> ItemsList(
    state: ArchiveScreenState,
    pagingItems: LazyPagingItems<T>,
    imageLoader: ImageLoader,
    navigateAction: (id: String) -> Unit,
    itemContent: @Composable (T, ImageLoader, (String) -> Unit) -> Unit,
    loadingState: @Composable () -> Unit,
    noItemsMessage: String
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = state.query, key2 = state.shouldSearchItems) {
        if (state.shouldSearchItems) {
            pagingItems.refresh()
        }
    }

    val isLoading = pagingItems.loadState.refresh is LoadState.Loading || state.isLoading
    val isError = pagingItems.loadState.refresh is LoadState.Error

    if (!isLoading && !isError) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 145.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(pagingItems.itemCount) { index ->
                val item = pagingItems[index]
                item?.let { itemContent(item, imageLoader, navigateAction) }
            }

            with(pagingItems) {
                when (loadState.append) {
                    is LoadState.Loading -> {
                        items(3) {
                            AppendLoadingItem()
                        }
                    }

                    is LoadState.Error -> {
                        item {
                            val message = if (state.errorMessage != null) {
                                state.errorMessage.asString(context)
                            } else {
                                stringResource(id = R.string.unknown_error)
                            }

                            ErrorItem(message = message, modifier = Modifier.fillMaxWidth()) {
                                retry()
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    } else if (isLoading) {
        loadingState()
    } else if (isError) {
        val message = if (state.errorMessage != null) {
            state.errorMessage.asString(context)
        } else {
            stringResource(id = R.string.unknown_error)
        }

        ErrorItem(message, modifier = Modifier.fillMaxSize()) {
            pagingItems.retry()
        }
    }

    if (pagingItems.itemCount == 0 && !isLoading && !isError) {
        if (state.query.isNotEmpty()) {
            MessageBox(
                message = stringResource(R.string.nothing_found_message),
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            )
        } else {
            MessageBox(
                message = noItemsMessage,
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            )
        }
    }
}

@Composable
fun MessageBox(message: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = message,
            fontWeight = FontWeight.W800,
            fontSize = 18.sp
        )
    }
}

//@Composable
//fun AppendLoadingItem() {
////    Box(
////        modifier = Modifier
////            .fillMaxWidth()
////            .padding(vertical = 24.dp),
////        contentAlignment = Alignment.Center
////    ) {
////        CircularProgressIndicator(color = MaterialTheme.colors.secondary)
////    }
//
//    LoadingItem1(brush = )
//}

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

@Composable
private fun AppendLoadingItem() {
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

    LoadingItem1(brush)
}

@Composable
private fun getAnimationBrush(): Brush {
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

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )
}

@Composable
private fun LoadingItem(brush: Brush) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
    ) {
        Spacer(
            modifier = Modifier
                .width(185.dp)
                .height(88.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(brush)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(verticalArrangement = Arrangement.Center) {
            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(fraction = 0.7f)
                    .background(brush)
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(fraction = 0.9f)
                    .background(brush)
            )
        }
    }
}

@Composable
private fun LoadingItem1(brush: Brush) {
    Box(
        Modifier
            .height(230.dp)
            .width(185.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(brush)
    )
}

@Composable
private fun LoadingWindow() {
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

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 145.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        repeat(7) {
            item { LoadingItem1(brush) }
        }
    }
}

@Composable
fun CourseCollectionItem(
    collection: ArchiveItem.Collection,
    imageLoader: ImageLoader,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(185.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF282828))
            .clickable { onClick(collection.collectionId) }
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(176.dp)
                .clip(RoundedCornerShape(12.dp)),
            painter = rememberAsyncImagePainter(collection.imgUrl, imageLoader = imageLoader),
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

@Composable
fun CourseItem(
    course: ArchiveItem.Course,
    imageLoader: ImageLoader,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(185.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF282828))
            .clickable { onClick(course.courseId) }
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(176.dp)
                .clip(RoundedCornerShape(12.dp)),
            painter = rememberAsyncImagePainter(course.imgUrl, imageLoader = imageLoader),
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
                text = course.title,
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
                text = course.authorName,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}