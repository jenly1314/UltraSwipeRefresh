package com.king.ultraswiperefresh.app.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.king.ultraswiperefresh.app.component.ColumnItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * SwipeRefresh示例
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Suppress("DEPRECATION")
@Composable
fun SwipeRefreshSample() {

    var isRefreshing by remember { mutableStateOf(false) }
    var itemCount by remember { mutableIntStateOf(20) }
    val coroutineScope = rememberCoroutineScope()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                // TODO 刷新的逻辑处理，此处的延时只是为了演示效果
                delay(2000)
                itemCount = 20
                isRefreshing = false
            }
        }
    ) {
        LazyColumn(Modifier.background(color = Color.White)) {
            repeat(itemCount) {
                item {
                    val title = "SwipeRefresh列表标题${it + 1}"
                    val content = "SwipeRefresh列表内容${it + 1}"
                    ColumnItem(title = title, content = content)
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFF2F3F6)
                    )
                }
            }
        }
    }
}