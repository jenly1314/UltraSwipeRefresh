package com.king.ultraswiperefresh.app.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.king.ultraswiperefresh.NestedScrollMode
import com.king.ultraswiperefresh.UltraSwipeRefresh
import com.king.ultraswiperefresh.app.component.ColumnItem
import com.king.ultraswiperefresh.app.ext.showToast
import com.king.ultraswiperefresh.app.navigation.NavRoute
import com.king.ultraswiperefresh.indicator.SwipeRefreshFooter
import com.king.ultraswiperefresh.indicator.SwipeRefreshHeader
import com.king.ultraswiperefresh.rememberUltraSwipeRefreshState
import com.king.ultraswiperefresh.theme.UltraSwipeRefreshTheme
import kotlinx.coroutines.delay

/**
 * UltraSwipeRefresh 示例
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
fun UltraSwipeRefreshSample(navController: NavController) {

    val state = rememberUltraSwipeRefreshState()

    LaunchedEffect(state.isRefreshing) {
        if (state.isRefreshing) {
            delay(2000)
            state.isRefreshing = false
        }
    }

    LaunchedEffect(state.isLoading) {
        if (state.isLoading) {
            delay(2000)
            state.isLoading = false
        }
    }

    var headerScrollMode by remember {
        mutableStateOf(NestedScrollMode.FixedContent)
    }
    var footerScrollMode by remember {
        mutableStateOf(NestedScrollMode.FixedContent)
    }

    val context = LocalContext.current

    UltraSwipeRefresh(
        state = state,
        onRefresh = {
            state.isRefreshing = true
        },
        onLoadMore = {
            state.isLoading = true
        },
        headerScrollMode = headerScrollMode,
        footerScrollMode = footerScrollMode,
        modifier = Modifier.background(color = Color(0x7FEEEEEE)),
        headerIndicator = {
            SwipeRefreshHeader(it)
        },
        footerIndicator = {
            SwipeRefreshFooter(it)
        }
    ) {
        val map = mutableMapOf<NavRoute, Pair<String, String>>().apply {
            put(
                NavRoute.SwipeRefreshIndicatorSample,
                "默认刷新样式示例" to "使用NestedScrollMode.FixedContent；特点：固定内容；即：内容固定，Header或 Footer进行滑动"
            )
            put(
                NavRoute.ClassicRefreshIndicatorSample,
                "经典刷新样式示例" to "使用NestedScrollMode.Translate；特点：平移； 即：Header或 Footer与内容一起滑动"
            )
            put(
                NavRoute.ProgressRefreshIndicatorSample,
                "进度条刷新样式示例" to "使用NestedScrollMode.FixedFront；特点：固定在前面；即：Header或 Footer和内容都固定，仅改变状态"
            )
            put(
                NavRoute.LottieRefreshIndicatorSample,
                "Lottie动画刷新样式示例" to "使用NestedScrollMode.FixedBehind；特点：固定在背后；即：Header或 Footer固定，仅内容滑动"
            )
            put(
                NavRoute.CustomLottieRefreshIndicatorSample,
                "自定义Lottie动画刷新样式示例" to "随机切换滑动模式，Header与Footer与内容的联动效果由滑动模式[NestedScrollMode]来决定"
            )
            put(
                NavRoute.SwipeRefreshSample,
                "Google官方的SwipeRefresh示例" to "只支持下拉刷新，此示例主要用于与UltraSwipeRefresh进行效果对比（后续可能会移除）"
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {

            item {
                ColumnItem(
                    title = "UltraSwipeRefresh：一个可带来极致体验的Compose刷新组件；支持下拉刷新和上拉加载，可完美替代官方的SwipeRefresh；并且支持的功能更多，可扩展性更强。",
                    content = "[headerIndicator] 和 [footerIndicator]可随意定制，并且[Header]和[Footer]样式与滑动模式可随意组合。"
                ) {
                    val vibrateEnabled = !UltraSwipeRefreshTheme.config.vibrateEnabled
                    UltraSwipeRefreshTheme.config = UltraSwipeRefreshTheme.config.copy(vibrateEnabled = vibrateEnabled)
                    if(vibrateEnabled) {
                        context.showToast("已全局启用振动效果")
                    } else {
                        context.showToast("已全局关闭振动效果")
                    }
                }
                Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF2F3F6))
            }

            item {
                val nestedScrollModes = remember { NestedScrollMode.values() }
                ColumnItem(
                    title = "默认刷新样式 + 随机滑动模式",
                    content = "\n当前页所选的滑动模式\n" +
                            "headerScrollMode = NestedScrollMode.${headerScrollMode.name}\n" +
                            "footerScrollMode = NestedScrollMode.${footerScrollMode.name}\n" +
                            "点击此项会随机修改当前页Header和Footer的滑动模式"
                ) {
                    headerScrollMode = nestedScrollModes.random()
                    footerScrollMode = nestedScrollModes.random()
                    context.showToast("滑动模式已随机")
                }
                Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF2F3F6))
            }

            map.forEach { (key, value) ->
                item {
                    ColumnItem(title = value.first, content = value.second) {
                        navController.navigate(route = key.name)
                    }
                    Divider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFF2F3F6)
                    )
                }
            }
        }
    }
}
