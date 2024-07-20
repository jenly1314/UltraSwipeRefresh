package com.king.ultraswiperefresh.app.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.king.ultraswiperefresh.NestedScrollMode
import com.king.ultraswiperefresh.UltraSwipeRefresh
import com.king.ultraswiperefresh.app.R
import com.king.ultraswiperefresh.app.component.ColumnItem
import com.king.ultraswiperefresh.app.ext.showToast
import com.king.ultraswiperefresh.indicator.lottie.LottieRefreshFooter
import com.king.ultraswiperefresh.indicator.lottie.LottieRefreshHeader
import com.king.ultraswiperefresh.rememberUltraSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 自定义Lottie动画刷新样式示例
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
fun CustomLottieRefreshIndicatorSample() {

    val state = rememberUltraSwipeRefreshState()
    var itemCount by remember { mutableIntStateOf(20) }
    var hasMoreData by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    var headerScrollMode by remember {
        mutableStateOf(NestedScrollMode.FixedBehind)
    }
    var footerScrollMode by remember {
        mutableStateOf(NestedScrollMode.FixedBehind)
    }

    LaunchedEffect(state.isFinishing) {
        if (itemCount >= 60 && !state.isFinishing) {
            hasMoreData = false
        }
    }

    val context = LocalContext.current

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
                // TODO 加载更多的逻辑处理，此处的延时只是为了演示效果
                delay(2000)
                itemCount += 20
                state.isLoading = false
            }
        },
        modifier = Modifier.background(color = Color(0x7FEEEEEE)),
        headerScrollMode = headerScrollMode,
        footerScrollMode = footerScrollMode,
        loadMoreEnabled = hasMoreData,
        headerIndicator = {
            LottieRefreshHeader(
                state = it,
                spec = LottieCompositionSpec.RawRes(R.raw.usr_lottie_rhomb)
            )
        },
        footerIndicator = {
            LottieRefreshFooter(
                state = it,
                spec = LottieCompositionSpec.RawRes(R.raw.usr_lottie_sound_wave),
            )
        }
    ) {
        LazyColumn(Modifier.background(color = Color.White)) {
            item {
                val nestedScrollModes = remember { NestedScrollMode.values() }
                ColumnItem(
                    title = "点击此项可随机切换滑动模式",
                    content = "当前所选的滑动模式\n" +
                            "headerScrollMode = NestedScrollMode.${headerScrollMode.name}\n" +
                            "footerScrollMode = NestedScrollMode.${footerScrollMode.name}"
                ) {
                    headerScrollMode = nestedScrollModes.random()
                    footerScrollMode = nestedScrollModes.random()
                    context.showToast("滑动模式已随机")
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color(0xFFF2F3F6)
                )
            }
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