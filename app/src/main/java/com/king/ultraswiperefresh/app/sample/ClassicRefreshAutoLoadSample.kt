package com.king.ultraswiperefresh.app.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.king.ultraswiperefresh.UltraSwipeFooterState
import com.king.ultraswiperefresh.UltraSwipeRefresh
import com.king.ultraswiperefresh.app.component.ColumnItem
import com.king.ultraswiperefresh.indicator.classic.ClassicRefreshFooter
import com.king.ultraswiperefresh.indicator.classic.ClassicRefreshHeader
import com.king.ultraswiperefresh.rememberUltraSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * 经典刷新自动加载示例
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
fun ClassicRefreshAutoLoadSample() {

    val state = rememberUltraSwipeRefreshState()
    var itemCount by remember { mutableIntStateOf(20) }
    var hasMoreData by remember { mutableStateOf(true) }
    var autoLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        snapshotFlow {
            val lazyListItemInfo = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()
            // 此处的 lazyListItemInfo.index > itemCount - 5 只是举个触发自动加载的条件示例；想要什么时候触发自动加载，可结合item高度和偏移量自行决定。
            lazyListItemInfo != null && lazyListItemInfo.index > itemCount - 5
        }.distinctUntilChanged().collect {
            if (!autoLoading && !state.isLoading && it) {
                if (itemCount >= 60) {
                    hasMoreData = false
                } else {
                    autoLoading = true
                    // TODO 自动加载更多的逻辑处理，此处的延时只是为了演示效果
                    delay(200)
                    itemCount += 20
                    autoLoading = false

                    state.isLoading = false
                }
            }
        }
    }

    LaunchedEffect(state.isFinishing) {
        if (itemCount >= 60 && !state.isFinishing) {
            hasMoreData = false
        }
    }

    UltraSwipeRefresh(
        state = state,
        onRefresh = {
            coroutineScope.launch {
                state.isRefreshing = true
                // TODO 刷新的逻辑处理，此处的延时只是为了演示效果
                delay(2000)
                itemCount = 20
                hasMoreData = true
                state.isRefreshing = false
            }
        },
        onLoadMore = {
            coroutineScope.launch {
                state.isLoading = true
                if (!autoLoading) { // 当前非自动加载时，才去加载更多数据
                    // TODO 加载更多的逻辑处理，此处的延时只是为了演示效果
                    delay(2000)
                    itemCount += 20
                    state.isLoading = false
                }
            }
        },
        modifier = Modifier.background(color = Color(0x7FEEEEEE)),
        loadMoreTriggerRate = 0.1f,
        loadMoreEnabled = hasMoreData,
        onCollapseScroll = {
            if(state.footerState == UltraSwipeFooterState.Loading) {
                // 同步滚动列表位置，消除视觉回弹
                lazyListState.animateScrollBy(it)
            }
        },
        headerIndicator = {
            ClassicRefreshHeader(it)
        },
        footerIndicator = {
            ClassicRefreshFooter(it)
        }
    ) {
        LazyColumn(
            modifier = Modifier.background(color = Color.White),
            state = lazyListState,
        ) {
            repeat(itemCount) {
                item {
                    val title = "UltraSwipeRefresh列表标题${it + 1}"
                    val content = "UltraSwipeRefresh列表内容${it + 1}"
                    ColumnItem(title = title, content = content)
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFF2F3F6)
                    )
                }
            }

            if (!hasMoreData) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "没有更多数据了",
                            color = Color(0xFF999999),
                            fontSize = 15.sp,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
