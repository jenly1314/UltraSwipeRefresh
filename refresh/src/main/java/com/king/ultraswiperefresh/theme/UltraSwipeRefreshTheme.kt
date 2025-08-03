package com.king.ultraswiperefresh.theme

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.king.ultraswiperefresh.NestedScrollMode
import com.king.ultraswiperefresh.UltraSwipeRefresh
import com.king.ultraswiperefresh.UltraSwipeRefreshState
import com.king.ultraswiperefresh.indicator.SwipeRefreshFooter
import com.king.ultraswiperefresh.indicator.SwipeRefreshHeader

/**
 * UltraSwipeRefresh主题：主要用于统一管理全局默认配置。
 * 通常情况下，一个App使用的刷新样式是统一的，如果你需要进行全局统一刷新组件的样式时，
 * 可以通过[UltraSwipeRefreshTheme.config]来动态修改[UltraSwipeRefresh]的全局默认配置。
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
object UltraSwipeRefreshTheme {

    /**
     * [UltraSwipeRefresh]的配置，此配置可动态修改[UltraSwipeRefresh]的全局默认配置
     */
    var config by mutableStateOf(UltraSwipeRefreshConfig())
}

/**
 * [UltraSwipeRefresh] 的配置
 *
 * @param headerScrollMode 在进行滑动刷新时Header的滑动模式；具体更详细的样式说明可查看[NestedScrollMode]
 * @param footerScrollMode 在进行滑动加载更多时Footer的滑动模式；具体更详细的样式说明可查看[NestedScrollMode]
 * @param refreshEnabled 是否启用下拉刷新
 * @param loadMoreEnabled 是否启用上拉加载
 * @param refreshTriggerRate 触发滑动刷新的最小滑动比例；比例基于[headerIndicator]的高度；默认为：1
 * @param loadMoreTriggerRate 触发滑动加载更多最小滑动比例；比例基于[footerIndicator]的高度；默认为：1
 * @param headerMaxOffsetRate 向下滑动时[headerIndicator]可滑动的最大偏移比例；比例基于[headerIndicator]的高度；默认为：2
 * @param footerMaxOffsetRate 向上滑动时[footerIndicator]可滑动的最大偏移比例；比例基于[footerIndicator]的高度；默认为：2
 * @param dragMultiplier 触发下拉刷新或上拉加载时的阻力系数；值越小，阻力越大；默认为：0.5
 * @param finishDelayMillis 完成时延时时间；让完成时的中间状态[UltraSwipeRefreshState.isFinishing]停留一会儿，定格的展示提示内容；默认：500毫秒
 * @param vibrationEnabled 是否启用振动，如果启用则当滑动偏移量满足触发刷新或触发加载更多时，会有振动效果；默认为：false
 * @param vibrationMillis 触发刷新或触发加载更多时的振动时长（毫秒）默认：25毫秒
 * @param alwaysScrollable 是否始终可以滚动；当为true时，则会忽略刷新中或加载中的状态限制，始终可以进行滚动；默认为：false
 * @param onCollapseScroll 可选回调，当Header/Footer收起时需要同步调整列表位置以消除视觉回弹时使用
 * @param headerIndicator 下拉刷新时顶部显示的Header指示器
 * @param footerIndicator 上拉加载更多时底部显示的Footer指示器
 * @param contentContainer 内容的父容器，便于统一管理
 */
data class UltraSwipeRefreshConfig(
    val headerScrollMode: NestedScrollMode = NestedScrollMode.Translate,
    val footerScrollMode: NestedScrollMode = NestedScrollMode.Translate,
    val refreshEnabled: Boolean = true,
    val loadMoreEnabled: Boolean = true,
    @FloatRange(from = 0.0, fromInclusive = false) val refreshTriggerRate: Float = 1f,
    @FloatRange(from = 0.0, fromInclusive = false) val loadMoreTriggerRate: Float = 1f,
    @FloatRange(from = 1.0) val headerMaxOffsetRate: Float = 2f,
    @FloatRange(from = 1.0) val footerMaxOffsetRate: Float = 2f,
    @FloatRange(from = 0.0, to = 1.0, fromInclusive = false) val dragMultiplier: Float = 0.5f,
    @IntRange(from = 0, to = 2000) val finishDelayMillis: Long = 500,
    val vibrationEnabled: Boolean = false,
    @IntRange(from = 1, to = 50) val vibrationMillis: Long = 25,
    val alwaysScrollable: Boolean = false,
    val onCollapseScroll: (suspend (Float) -> Unit)? = null,
    val headerIndicator: @Composable (UltraSwipeRefreshState) -> Unit = {
        SwipeRefreshHeader(it)
    },
    val footerIndicator: @Composable (UltraSwipeRefreshState) -> Unit = {
        SwipeRefreshFooter(it)
    },
    val contentContainer: @Composable (@Composable () -> Unit) -> Unit = {
        NoOverscrollEffect(it)
    }
)

/**
 * 无过度滚动效果
 */
@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun NoOverscrollEffect(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalOverscrollConfiguration provides null, content = content)
}
