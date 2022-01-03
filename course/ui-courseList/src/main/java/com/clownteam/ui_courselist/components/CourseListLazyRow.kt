package com.clownteam.ui_courselist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.clownteam.course_domain.Course

@Composable
internal fun CourseListLazyRow(
    itemList: List<Course>,
    itemComposable: @Composable (Course) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(24.dp),
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        contentPadding = contentPadding
    ) {
        items(itemList) { item ->
            itemComposable(item)
        }
    }
}