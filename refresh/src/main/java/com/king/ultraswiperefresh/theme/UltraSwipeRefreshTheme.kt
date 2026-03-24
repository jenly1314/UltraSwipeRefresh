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
import com.king.ultraswiperefresh.SecondaryBehavior
import com.king.ultraswiperefresh.UltraSwipeFooterState
import com.king.ultraswiperefresh.UltraSwipeHeaderState
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
 * @param headerScrollMode 下拉刷新时 Header 的滑动模式；详细说明见 [NestedScrollMode]
 * @param footerScrollMode 上拉加载时 Footer 的滑动模式；详细说明见 [NestedScrollMode]
 * @param refreshEnabled 是否启用下拉刷新
 * @param loadMoreEnabled 是否启用上拉加载
 * @param refreshTriggerRate 触发下拉刷新的最小滑动比例，基于 [headerIndicator] 的高度；默认值：1
 * @param loadMoreTriggerRate 触发上拉加载的最小滑动比例，基于 [footerIndicator] 的高度；默认值：1
 * @param headerSecondaryEnabled 是否启用 Header 二级内容功能
 * @param footerSecondaryEnabled 是否启用 Footer 二级内容功能
 * @param headerSecondaryBehavior Header 二级内容交互行为模式；详细说明见 [SecondaryBehavior]
 * @param footerSecondaryBehavior Footer 二级内容交互行为模式；详细说明见 [SecondaryBehavior]
 * @param headerSecondaryPreview 在 [UltraSwipeHeaderState.ReleaseToSecondary] 状态下是否可提前预览 Header 二级内容
 * @param footerSecondaryPreview 在 [UltraSwipeFooterState.ReleaseToSecondary] 状态下是否可提前预览 Footer 二级内容
 * @param headerSecondaryTriggerRate 触发 Header 二级内容的最小滑动比例，基于 [headerIndicator] 的高度；默认值：2
 * @param footerSecondaryTriggerRate 触发 Footer 二级内容的最小滑动比例，基于 [footerIndicator] 的高度；默认值：2
 * @param headerMaxOffsetRate 下拉时 [headerIndicator] 的最大滑动偏移比例，基于其自身高度；默认值：3
 * @param footerMaxOffsetRate 上拉时 [footerIndicator] 的最大滑动偏移比例，基于其自身高度；默认值：3
 * @param dragMultiplier 滑动时的阻力系数，值越小阻力越大；默认值：0.5
 * @param finishDelayMillis 完成状态的停留时长（毫秒），便于展示提示内容；默认值：500
 * @param vibrationEnabled 是否启用振动反馈。启用后，滑动偏移量达到阈值时将触发振动；默认值：false
 * @param vibrationMillis 触发刷新或加载时的振动时长（毫秒）；默认值：25
 * @param alwaysScrollable 是否始终允许滚动。设为 true 时，不受刷新/加载状态限制，始终可滚动；默认值：false
 * @param headerIndicator 下拉刷新时顶部显示的 Header 指示器
 * @param footerIndicator 上拉加载时底部显示的 Footer 指示器
 * @param contentContainer 内容的父容器，便于统一管理
 */

data class UltraSwipeRefreshConfig(
    val headerScrollMode: NestedScrollMode = NestedScrollMode.Translate,
    val footerScrollMode: NestedScrollMode = NestedScrollMode.Translate,
    val refreshEnabled: Boolean = true,
    val loadMoreEnabled: Boolean = true,
    @param:FloatRange(from = 0.0, fromInclusive = false) val refreshTriggerRate: Float = 1f,
    @param:FloatRange(from = 0.0, fromInclusive = false) val loadMoreTriggerRate: Float = 1f,
    val headerSecondaryEnabled: Boolean = false,
    val footerSecondaryEnabled: Boolean = false,
    val headerSecondaryBehavior: SecondaryBehavior = SecondaryBehavior.Slide,
    val footerSecondaryBehavior: SecondaryBehavior = SecondaryBehavior.Slide,
    val headerSecondaryPreview: Boolean = false,
    val footerSecondaryPreview: Boolean = false,
    @param:FloatRange(from = 1.0, fromInclusive = false) val headerSecondaryTriggerRate: Float = 2f,
    @param:FloatRange(from = 1.0, fromInclusive = false) val footerSecondaryTriggerRate: Float = 2f,
    @param:FloatRange(from = 1.0) val headerMaxOffsetRate: Float = 3f,
    @param:FloatRange(from = 1.0) val footerMaxOffsetRate: Float = 3f,
    @param:FloatRange(from = 0.0, to = 2.0, fromInclusive = false) val dragMultiplier: Float = 0.5f,
    @param:IntRange(from = 0, to = 2000) val finishDelayMillis: Long = 500,
    val vibrationEnabled: Boolean = false,
    @param:IntRange(from = 1, to = 50) val vibrationMillis: Long = 25,
    val alwaysScrollable: Boolean = false,
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
