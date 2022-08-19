package com.clownteam.ui_search.ui

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.clownteam.components.AutoResizeText
import com.clownteam.components.DefaultButton
import com.clownteam.components.FontSizeRange
import com.clownteam.components.PriceText
import com.clownteam.components.utils.getMembersCountString
import com.clownteam.core.domain.EventHandler
import com.clownteam.search_domain.SearchFilter
import com.clownteam.search_domain.SearchResultItem
import com.clownteam.ui_search.R
import com.clownteam.ui_search.components.SearchTabRow
import com.clownteam.ui_search.components.pagerTabIndicatorOffset
import com.clownteam.ui_search.styles.SearchScreenColors
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
fun SearchScreen(
    state: SearchState,
    eventHandler: EventHandler<SearchEvent>,
    imageLoader: ImageLoader,
    coursesFlow: Flow<PagingData<SearchResultItem.Course>>,
    collectionsFlow: Flow<PagingData<SearchResultItem.Collection>>,
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
    ) {
        Spacer(modifier = Modifier.size(22.dp))

        SearchBar(
            modifier = Modifier.padding(horizontal = 14.dp),
            state = state,
            eventHandler = eventHandler
        )

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
    state: SearchState,
    eventHandler: EventHandler<SearchEvent>
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SearchScreenColors.SearchBarBackgroundColor),
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
            onValueChange = { eventHandler.obtainEvent(SearchEvent.SetQuery(it)) },
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
    screenState: SearchState,
    imageLoader: ImageLoader,
    coursesFlow: Flow<PagingData<SearchResultItem.Course>>,
    collectionsFlow: Flow<PagingData<SearchResultItem.Collection>>,
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
        count = filters.size
    ) { page ->
        val filter = when (page) {
            0 -> SearchFilter.Courses
            1 -> SearchFilter.Collections
            else -> SearchFilter.Courses
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
    state: SearchState,
    currentFilter: SearchFilter,
    imageLoader: ImageLoader,
    coursesFlow: Flow<PagingData<SearchResultItem.Course>>,
    collectionsFlow: Flow<PagingData<SearchResultItem.Collection>>,
    navigateToCourse: (String) -> Unit,
    navigateToCollection: (String) -> Unit
) {
    when (currentFilter) {
        SearchFilter.Authors -> {}

        SearchFilter.Collections -> {
            val collectionPagingItems = collectionsFlow.collectAsLazyPagingItems()
            ItemsList(
                state,
                collectionPagingItems,
                imageLoader,
                navigateToCollection,
                itemContent = { collection, _imageLoader, navigateToDetailed ->
                    ColumnCollectionListItem(collection, _imageLoader, navigateToDetailed)
                },
                loadingState = { LoadingWindow(searchFilter = SearchFilter.Collections) }
            )
        }

        SearchFilter.Courses -> {
            val coursePagingItems = coursesFlow.collectAsLazyPagingItems()
            ItemsList(
                state,
                coursePagingItems,
                imageLoader,
                navigateToCourse,
                itemContent = { course, _imageLoader, navigateToDetailed ->
                    ColumnCourseListItem(course, _imageLoader, navigateToDetailed)
                },
                loadingState = { LoadingWindow(searchFilter = SearchFilter.Courses) }
            )
        }
    }
}

@Composable
private fun <T : Any> ItemsList(
    state: SearchState,
    pagingItems: LazyPagingItems<T>,
    imageLoader: ImageLoader,
    navigateAction: (id: String) -> Unit,
    itemContent: @Composable (T, ImageLoader, (String) -> Unit) -> Unit,
    loadingState: @Composable () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = state.query, key2 = state.shouldSearchItems) {
        if (state.query.isNotEmpty() && state.shouldSearchItems) {
            pagingItems.refresh()
        }
    }

    val isLoading = pagingItems.loadState.refresh is LoadState.Loading || state.isLoading
    val isError = pagingItems.loadState.refresh is LoadState.Error

    if (state.query.isNotEmpty()) {
        if (!isLoading && !isError) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
            ) {
                itemsIndexed(pagingItems) { index, item ->
                    Spacer(modifier = Modifier.size(12.dp))

                    item?.let { itemContent(item, imageLoader, navigateAction) }

                    if (index + 1 == pagingItems.itemCount) {
                        Spacer(modifier = Modifier.size(12.dp))
                    }
                }

                with(pagingItems) {
                    when (loadState.append) {
                        is LoadState.Loading -> {
                            item { AppendLoadingItem() }
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
            Log.d("Kmem", "EMPTY")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.nothing_found_message),
                    fontWeight = FontWeight.W800,
                    fontSize = 18.sp
                )
            }
        }
    } else {
        val messageText = stringResource(R.string.empty_search_query_message)

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = messageText,
                fontWeight = FontWeight.W800,
                fontSize = 18.sp
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColumnCourseListItem(
    course: SearchResultItem.Course,
    imageLoader: ImageLoader,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit = {}
) {
    // Main Row
    Row(
        modifier = Modifier.combinedClickable(
            onClick = { onClick(course.courseId) },
            onLongClick = { onLongClick(course.courseId) })
    ) {
        // Logo
        Image(
            modifier = Modifier
                .width(148.dp)
                .height(88.dp)
                .clip(RoundedCornerShape(corner = CornerSize(12.dp)))
                .background(MaterialTheme.colors.primary)
                .align(Alignment.CenterVertically),
            painter = rememberAsyncImagePainter(
                course.imgUrl,
                imageLoader = imageLoader
            ),
            contentDescription = stringResource(R.string.course_image_content_description),
            contentScale = ContentScale.Crop
        )

        // Content column
        Column(modifier = Modifier.padding(start = 9.dp)) {
            // Title
            AutoResizeText(
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .fillMaxWidth(),
                text = course.title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSizeRange = FontSizeRange(min = 10.sp, max = 16.sp)
            )
            // Sub-Contents row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left sub-content
                Column {
                    // Rating
                    Row {
                        Text(
                            text = course.rating.toString(),
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.W400,
                            color = Color(0xFFF2FF5F)
                        )
                        Image(
                            modifier = Modifier.height(20.dp),
                            painter = painterResource(id = R.drawable.ic_baseline_star_rate_24),
                            contentDescription = stringResource(R.string.star_icon_content_description)
                        )
                    }

                    // Members count
                    Row {
                        Image(
                            modifier = Modifier.height(20.dp),
                            painter = painterResource(id = R.drawable.ic_baseline_people_24),
                            contentDescription = stringResource(R.string.people_icon_content_description)
                        )
                        Text(
                            modifier = Modifier.padding(start = 3.dp),
                            text = getMembersCountString(course.membersAmount),
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.Light
                        )
                    }
                }

                // Right sub-content
                Column(
                    modifier = Modifier.padding(start = 24.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Row {
                        Text(
                            text = course.authorName,
                            style = MaterialTheme.typography.caption,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row {
                        PriceText(course.price.toInt())
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColumnCollectionListItem(
    collection: SearchResultItem.Collection,
    imageLoader: ImageLoader,
    onClick: (String) -> Unit
) {
    // Main Row
    Row(
        modifier = Modifier.combinedClickable(
            onClick = { onClick(collection.collectionId) }
        )
    ) {
        // Logo
        Image(
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(corner = CornerSize(14.dp)))
                .background(MaterialTheme.colors.primary)
                .align(Alignment.CenterVertically),
            painter = rememberAsyncImagePainter(
                collection.imgUrl,
                imageLoader = imageLoader
            ),
            contentDescription = stringResource(R.string.course_image_content_description),
            contentScale = ContentScale.Crop
        )

        // Content column
        Column(modifier = Modifier.padding(start = 14.dp)) {
            // Title
            AutoResizeText(
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .fillMaxWidth(),
                text = collection.title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSizeRange = FontSizeRange(min = 10.sp, max = 16.sp)
            )

            // Collection Label
            Text(
//                stringResource(R.string.collection_label),
                text = collection.authorName,
                fontSize = 12.sp,
                fontWeight = FontWeight.W800,
                color = MaterialTheme.colors.secondary
            )

            Spacer(modifier = Modifier.size(5.dp))

            // Members count
            Row {
                Image(
                    modifier = Modifier.height(20.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_people_24),
                    contentDescription = stringResource(R.string.people_icon_content_description)
                )
                Text(
                    modifier = Modifier.padding(start = 3.dp),
                    text = getMembersCountString(collection.membersAmount),
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Composable
private fun LoadingWindow(searchFilter: SearchFilter) {
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
            when (searchFilter) {
                SearchFilter.Authors -> {}
                SearchFilter.Collections -> {
                    CollectionLoadingItem(brush)
                }
                SearchFilter.Courses -> {
                    CourseLoadingItem(brush)
                }
            }
        }
    }
}

@Composable
private fun CourseLoadingItem(brush: Brush) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .width(148.dp)
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
private fun CollectionLoadingItem(brush: Brush) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(corner = CornerSize(14.dp)))
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
