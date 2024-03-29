@file:OptIn(ExperimentalFoundationApi::class)

package com.smarttoolfactory.composegesture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.composegesture.demo.MoveMotionEventDemo
import com.smarttoolfactory.composegesture.demo.TouchDelegateDemo
import com.smarttoolfactory.composegesture.demo.TransformMotionEventDemo
import com.smarttoolfactory.composegesture.ui.theme.ComposeGestureExtendedTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeGestureExtendedTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        HomeContent()
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeContent() {

    val pagerState: PagerState = rememberPagerState(initialPage = 0)

    val coroutineScope = rememberCoroutineScope()

    ScrollableTabRow(
        backgroundColor = Color(0xff03a9f4),
        contentColor = Color.White,
        edgePadding = 8.dp,
        // Our selected tab is our current page
        selectedTabIndex = pagerState.currentPage,
        // Override the indicator, using the provided pagerTabIndicatorOffset modifier
        indicator = {}
    ) {
        // Add tabs for all of our pages
        tabList.forEachIndexed { index, title ->
            Tab(
                text = { Text(title) },
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }

    HorizontalPager(
        state = pagerState,
        pageCount = tabList.size
    ) { page: Int ->

        when (page) {
            0 -> MoveMotionEventDemo()
            1 -> TransformMotionEventDemo()
            else -> TouchDelegateDemo()
        }
    }
}

internal val tabList =
    listOf(
        "Motion Events",
        "Transform Gestures",
        "TouchDelegate",
    )