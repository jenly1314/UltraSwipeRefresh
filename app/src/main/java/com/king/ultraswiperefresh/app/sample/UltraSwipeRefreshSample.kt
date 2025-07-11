package com.king.ultraswiperefresh.app.sample

import android.os.VibrationEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.king.ultraswiperefresh.NestedScrollMode
import com.king.ultraswiperefresh.PullRefreshVibrations.default
import com.king.ultraswiperefresh.PullRefreshVibrations.hapticFeedback
import com.king.ultraswiperefresh.PullRefreshVibrations.none
import com.king.ultraswiperefresh.PullRefreshVibrations.vibrationEffect
import com.king.ultraswiperefresh.UltraSwipeRefresh
import com.king.ultraswiperefresh.app.component.ColumnItem
import com.king.ultraswiperefresh.app.ext.showToast
import com.king.ultraswiperefresh.app.navigation.NavRoute
import com.king.ultraswiperefresh.indicator.SwipeRefreshFooter
import com.king.ultraswiperefresh.indicator.SwipeRefreshHeader
import com.king.ultraswiperefresh.theme.UltraSwipeRefreshTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * UltraSwipeRefresh 示例
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
fun UltraSwipeRefreshSample(navController: NavController) {

    var isRefreshing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    var headerScrollMode by remember {
        mutableStateOf(NestedScrollMode.FixedContent)
    }
    var footerScrollMode by remember {
        mutableStateOf(NestedScrollMode.FixedContent)
    }

    var vibrateActionIndex by remember { mutableIntStateOf(0) }
    val vibrateActionList = listOf(
        default(),
        hapticFeedback(),
        vibrationEffect(200, VibrationEffect.EFFECT_DOUBLE_CLICK),
        vibrationEffect(40, VibrationEffect.EFFECT_HEAVY_CLICK),
        vibrationEffect(40, VibrationEffect.EFFECT_CLICK),
        none()
    )

    val context = LocalContext.current

    val map = remember {
        mutableMapOf<NavRoute, Pair<String, String>>().apply {
            put(
                NavRoute.SwipeRefreshIndicatorSample,
                "默认刷新样式示例" to "使用NestedScrollMode.FixedContent；特点：固定内容；即：内容固定，Header或 Footer进行滑动"
            )
            put(
                NavRoute.ClassicRefreshIndicatorSample,
                "经典刷新样式示例" to "使用NestedScrollMode.Translate；特点：平移； 即：Header或 Footer与内容一起滑动"
            )
            put(
                NavRoute.ClassicRefreshAutoLoadSample,
                "经典刷新自动加载示例" to "使用NestedScrollMode.Translate；特点：平移； 即：Header或 Footer与内容一起滑动，并可自动加载更多"
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
                "Accompanist中的SwipeRefresh示例" to "只支持下拉刷新，此示例主要用于与UltraSwipeRefresh进行效果对比（后续可能会移除）"
            )
            put(
                NavRoute.PullRefreshSample,
                "Material中的Modifier.pullRefresh示例" to "只支持下拉刷新，此示例主要用于与UltraSwipeRefresh进行效果对比（后续可能会移除）"
            )
        }
    }

    UltraSwipeRefresh(
        isRefreshing = isRefreshing,
        isLoading = isLoading,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                // TODO 刷新的逻辑处理，此处的延时只是为了演示效果
                delay(2000)
                isRefreshing = false
            }
        },
        onLoadMore = {
            coroutineScope.launch {
                isLoading = true
                // TODO 加载更多的逻辑处理，此处的延时只是为了演示效果
                delay(2000)
                isLoading = false
            }
        },
        modifier = Modifier.background(color = Color(0x7FEEEEEE)),
        headerScrollMode = headerScrollMode,
        footerScrollMode = footerScrollMode,
        headerIndicator = {
            SwipeRefreshHeader(it)
        },
        footerIndicator = {
            SwipeRefreshFooter(it)
        }
    ) {
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
                    UltraSwipeRefreshTheme.config =
                        UltraSwipeRefreshTheme.config.copy(vibrateEnabled = vibrateEnabled)
                    if (vibrateEnabled) {
                        context.showToast("已全局启用振动效果")
                    } else {
                        context.showToast("已全局关闭振动效果")
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color(0xFFF2F3F6)
                )
            }

            item {
                ColumnItem(
                    title = "切换全局震动效果",
                    content = "当前震动效果:${vibrateActionIndex}"
                ) {
                    vibrateActionIndex = vibrateActionIndex.inc() % vibrateActionList.size
                    UltraSwipeRefreshTheme.config =
                        UltraSwipeRefreshTheme.config.copy(vibrateAction = vibrateActionList[vibrateActionIndex])
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color(0xFFF2F3F6)
                )
            }

            item {
                val nestedScrollModes = remember { NestedScrollMode.entries }
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
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color(0xFFF2F3F6)
                )
            }

            map.forEach { (key, value) ->
                item {
                    ColumnItem(title = value.first, content = value.second) {
                        navController.navigate(route = key.name)
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFF2F3F6)
                    )
                }
            }
        }
    }
}
