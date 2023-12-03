package com.king.ultraswiperefresh.app.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.king.ultraswiperefresh.NestedScrollMode
import com.king.ultraswiperefresh.UltraSwipeRefresh
import com.king.ultraswiperefresh.app.component.ColumnItem
import com.king.ultraswiperefresh.indicator.SwipeRefreshFooter
import com.king.ultraswiperefresh.indicator.SwipeRefreshHeader
import com.king.ultraswiperefresh.rememberUltraSwipeRefreshState
import kotlinx.coroutines.delay

/**
 * 官方默认的刷新样式示例
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
fun SwipeRefreshIndicatorSample() {

    val state = rememberUltraSwipeRefreshState()

    var itemCount by remember { mutableIntStateOf(20) }

    var hasMoreData by remember { mutableStateOf(true) }

    LaunchedEffect(state.isRefreshing) {
        if (state.isRefreshing) {
            delay(2000)
            itemCount = 20
            hasMoreData = true
            state.isRefreshing = false
        }
    }

    LaunchedEffect(state.isLoading) {
        if (state.isLoading) {
            delay(2000)
            itemCount += 20
            state.isLoading = false
        }
    }

    LaunchedEffect(state.isFinishing) {
        if (itemCount > 50 && !state.isFinishing) {
            hasMoreData = false
        }
    }

    UltraSwipeRefresh(
        state = state,
        onRefresh = {
            state.isRefreshing = true
        },
        onLoadMore = {
            state.isLoading = true
        },
        headerScrollMode = NestedScrollMode.FixedContent,
        footerScrollMode = NestedScrollMode.FixedContent,
        loadMoreEnabled = hasMoreData,
        modifier = Modifier.background(color = Color(0x7FEEEEEE)),
        headerIndicator = {
            SwipeRefreshHeader(it)
        },
        footerIndicator = {
            SwipeRefreshFooter(it)
        }
    ) {
        LazyColumn(Modifier.background(color = Color.White)) {
            repeat(itemCount) {
                item {
                    val title = "UltraSwipeRefresh列表标题${it + 1}"
                    val content = "UltraSwipeRefresh列表内容${it + 1}"
                    ColumnItem(title = title, content = content)
                    Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF2F3F6))
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