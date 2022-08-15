package com.clownteam.ui_search.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.clownteam.components.AutoResizeText
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
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    state: SearchState,
    eventHandler: EventHandler<SearchEvent>,
    imageLoader: ImageLoader,
    navigateToLogin: () -> Unit,
    navigateToCourse: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchHeader(
            state = state,
            eventHandler = eventHandler,
            onFilterClicked = { index ->
                val filter = when (index) {
                    0 -> SearchFilter.Courses
                    1 -> SearchFilter.Collections
                    else -> SearchFilter.Courses
                }
                eventHandler.obtainEvent(SearchEvent.SetSearchFilter(filter))
            },
        )

        if (state.isLoading) {
            LoadingWindow(state.searchFilter)
        } else {
            if (state.resultItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        stringResource(R.string.empty_search_query_message),
                        fontWeight = FontWeight.W800,
                        fontSize = 18.sp
                    )
                }
            } else {
                SearchItems(state, imageLoader, navigateToCourse)
            }
        }
    }
}

@Composable
fun SearchItems(state: SearchState, imageLoader: ImageLoader, navigateToCourse: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        itemsIndexed(state.resultItems) { index, resultItem ->
            Spacer(modifier = Modifier.size(12.dp))

            when (resultItem) {
                is SearchResultItem.Course -> {
                    ColumnCourseListItem(resultItem, imageLoader, navigateToCourse)
                }

                is SearchResultItem.Author -> {}

                is SearchResultItem.Collection -> {}
            }

            if (index == state.resultItems.lastIndex) {
                Spacer(modifier = Modifier.size(12.dp))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColumnCourseListItem(
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

@Composable
private fun LoadingWindow(searchFilter: SearchFilter) {
    val shimmerColors = listOf(
        MaterialTheme.colors.primary.copy(alpha = 0.6f),
        MaterialTheme.colors.primary.copy(alpha = 0.2f),
        MaterialTheme.colors.primary.copy(alpha = 0.6f),
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
                SearchFilter.All -> {}
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
                .size(80.dp)
                .clip(CircleShape)
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
private fun SearchHeader(
    state: SearchState,
    eventHandler: EventHandler<SearchEvent>,
    onFilterClicked: (Int) -> Unit
) {
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
        FilterCategoryItems(
            filters = filters,
            onFilterClicked = onFilterClicked
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
private fun FilterCategoryItems(filters: List<String>, onFilterClicked: (Int) -> Unit) {
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
                        onFilterClicked(index)
                    }
                }
            )
        }
    }

    HorizontalPager(state = pagerState, count = filters.size) {}
}

