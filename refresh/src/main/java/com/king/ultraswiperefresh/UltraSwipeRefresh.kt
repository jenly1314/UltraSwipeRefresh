package com.king.ultraswiperefresh

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.zIndex
import com.king.ultraswiperefresh.theme.UltraSwipeRefreshTheme
import kotlinx.coroutines.delay

/**
 * UltraSwipeRefresh：一个可带来极致体验的Compose刷新组件；支持下拉刷新和上拉加载，可完美替代官方的SwipeRefresh；并且支持的功能更多，可扩展性更强。
 *
 * @param state 状态：主要用于控制和观察[UltraSwipeRefresh]；比如：控制下拉刷新和上拉加载和观察其状态。
 * @param onRefresh 在完成滑动刷新手势时触发调用
 * @param onLoadMore 在完成滑动加载手势时触发调用
 * @param modifier 修饰符：用于装饰或添加Compose UI元素的行为。具体更详细的说明可查看[Modifier]
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
 * @param vibrateEnabled 是否启用振动，如果启用则当滑动偏移量满足触发刷新或触发加载更多时，会有振动效果；默认为：false
 * @param headerIndicator 下拉刷新时顶部显示的Header指示器
 * @param footerIndicator 上拉加载更多时底部显示的Footer指示器
 * @param content 可进行滑动刷新或加载更多所包含的内容
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
fun UltraSwipeRefresh(
    state: UltraSwipeRefreshState,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
    headerScrollMode: NestedScrollMode = UltraSwipeRefreshTheme.config.headerScrollMode,
    footerScrollMode: NestedScrollMode = UltraSwipeRefreshTheme.config.footerScrollMode,
    refreshEnabled: Boolean = UltraSwipeRefreshTheme.config.refreshEnabled,
    loadMoreEnabled: Boolean = UltraSwipeRefreshTheme.config.loadMoreEnabled,
    @FloatRange(from = 0.0, fromInclusive = false)
    refreshTriggerRate: Float = UltraSwipeRefreshTheme.config.refreshTriggerRate,
    @FloatRange(from = 0.0, fromInclusive = false)
    loadMoreTriggerRate: Float = UltraSwipeRefreshTheme.config.loadMoreTriggerRate,
    @FloatRange(from = 1.0)
    headerMaxOffsetRate: Float = UltraSwipeRefreshTheme.config.headerMaxOffsetRate,
    @FloatRange(from = 1.0)
    footerMaxOffsetRate: Float = UltraSwipeRefreshTheme.config.footerMaxOffsetRate,
    @FloatRange(from = 0.0, to = 1.0, fromInclusive = false)
    dragMultiplier: Float = UltraSwipeRefreshTheme.config.dragMultiplier,
    @IntRange(from = 0, to = 2000)
    finishDelayMillis: Long = UltraSwipeRefreshTheme.config.finishDelayMillis,
    vibrateEnabled: Boolean = UltraSwipeRefreshTheme.config.vibrateEnabled,
    headerIndicator: @Composable (UltraSwipeRefreshState) -> Unit = UltraSwipeRefreshTheme.config.headerIndicator,
    footerIndicator: @Composable (UltraSwipeRefreshState) -> Unit = UltraSwipeRefreshTheme.config.footerIndicator,
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val updatedOnRefresh = rememberUpdatedState(onRefresh)
    val updateOnLoadMore = rememberUpdatedState(onLoadMore)

    Box(modifier.clipToBounds()) {
        RefreshSubComposeLayout(
            headerIndicator = {
                headerIndicator(state)
            },
            footerIndicator = {
                footerIndicator(state)
            },
            refreshEnabled = refreshEnabled,
            loadMoreEnabled = loadMoreEnabled,
        ) { headerHeight, footerHeight ->

            val nestedScrollConnection = remember(state, coroutineScope) {
                UltraSwipeRefreshNestedScrollConnection(state = state,
                    coroutineScope = coroutineScope,
                    onRefresh = {
                        updatedOnRefresh.value.invoke()
                    },
                    onLoadMore = {
                        updateOnLoadMore.value.invoke()
                    })
            }.apply {
                this.refreshEnabled = refreshEnabled
                this.loadMoreEnabled = loadMoreEnabled
                this.dragMultiplier = dragMultiplier
            }

            LaunchedEffect(headerHeight, footerHeight, refreshTriggerRate, loadMoreTriggerRate) {
                state.refreshTrigger = headerHeight.times(refreshTriggerRate)
                state.loadMoreTrigger = -footerHeight.times(loadMoreTriggerRate)
            }

            LaunchedEffect(headerHeight, footerHeight, headerMaxOffsetRate, footerMaxOffsetRate) {
                state.headerMaxOffset = headerHeight.times(headerMaxOffsetRate)
                state.footerMinOffset = -footerHeight.times(footerMaxOffsetRate)
            }

            LaunchedEffect(state.isSwipeInProgress, state.isRefreshing, state.isLoading) {
                if (!state.isSwipeInProgress) {
                    when {
                        state.isRefreshing -> state.animateOffsetTo(headerHeight.toFloat())
                        state.isLoading -> state.animateOffsetTo(-footerHeight.toFloat())
                        state.headerState == UltraSwipeHeaderState.Refreshing || state.footerState == UltraSwipeFooterState.Loading -> {
                            state.isFinishing = true
                            delay(finishDelayMillis)
                            state.animateOffsetTo(0f)
                        }

                        else -> state.animateOffsetTo(0f)
                    }
                }
            }

            if (vibrateEnabled) {
                val vibrator = rememberVibrator()
                if (vibrator.hasVibrator()) {
                    val vibrateState = remember {
                        derivedStateOf {
                            state.headerState == UltraSwipeHeaderState.ReleaseToRefresh || state.footerState == UltraSwipeFooterState.ReleaseToLoad
                        }
                    }
                    LaunchedEffect(vibrateState.value) {
                        if (vibrateState.value) {
                            vibrator.vibrate()
                        }
                    }
                }
            }

            Box(modifier = Modifier.nestedScroll(nestedScrollConnection)) {
                Box(modifier = Modifier
                    .align(Alignment.TopCenter)
                    .graphicsLayer {
                        translationY = obtainHeaderOffset(state, headerScrollMode, headerHeight)
                    }
                    .zIndex(obtainZIndex(headerScrollMode))
                ) {
                    if (refreshEnabled) {
                        headerIndicator(state)
                    }
                }
                Box(modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .graphicsLayer {
                        translationY = obtainFooterOffset(state, footerScrollMode, footerHeight)
                    }
                    .zIndex(obtainZIndex(footerScrollMode))
                ) {
                    if (loadMoreEnabled) {
                        footerIndicator(state)
                    }
                }
                Box(modifier = Modifier.graphicsLayer {
                    translationY = obtainContentOffset(state, headerScrollMode, footerScrollMode)
                }) {
                    content()
                }
            }
        }
    }
}

/**
 * UltraSwipeRefresh：一个可带来极致体验的Compose刷新组件；支持下拉刷新和上拉加载，可完美替代官方的SwipeRefresh；并且支持的功能更多，可扩展性更强。
 *
 * @param isRefreshing 是否正在刷新
 * @param isLoading 是否正在加载
 * @param onRefresh 在完成滑动刷新手势时触发调用
 * @param onLoadMore 在完成滑动加载手势时触发调用
 * @param modifier 修饰符：用于装饰或添加Compose UI元素的行为。具体更详细的说明可查看[Modifier]
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
 * @param vibrateEnabled 是否启用振动，如果启用则当滑动偏移量满足触发刷新或触发加载更多时，会有振动效果；默认为：false
 * @param headerIndicator 下拉刷新时顶部显示的Header指示器
 * @param footerIndicator 上拉加载更多时底部显示的Footer指示器
 * @param content 可进行滑动刷新或加载更多所包含的内容
 */
@Composable
fun UltraSwipeRefresh(
    isRefreshing: Boolean,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
    headerScrollMode: NestedScrollMode = UltraSwipeRefreshTheme.config.headerScrollMode,
    footerScrollMode: NestedScrollMode = UltraSwipeRefreshTheme.config.footerScrollMode,
    refreshEnabled: Boolean = UltraSwipeRefreshTheme.config.refreshEnabled,
    loadMoreEnabled: Boolean = UltraSwipeRefreshTheme.config.loadMoreEnabled,
    @FloatRange(from = 0.0, fromInclusive = false)
    refreshTriggerRate: Float = UltraSwipeRefreshTheme.config.refreshTriggerRate,
    @FloatRange(from = 0.0, fromInclusive = false)
    loadMoreTriggerRate: Float = UltraSwipeRefreshTheme.config.loadMoreTriggerRate,
    @FloatRange(from = 1.0)
    headerMaxOffsetRate: Float = UltraSwipeRefreshTheme.config.headerMaxOffsetRate,
    @FloatRange(from = 1.0)
    footerMaxOffsetRate: Float = UltraSwipeRefreshTheme.config.footerMaxOffsetRate,
    @FloatRange(from = 0.0, to = 1.0, fromInclusive = false)
    dragMultiplier: Float = UltraSwipeRefreshTheme.config.dragMultiplier,
    @IntRange(from = 0, to = 2000)
    finishDelayMillis: Long = UltraSwipeRefreshTheme.config.finishDelayMillis,
    vibrateEnabled: Boolean = UltraSwipeRefreshTheme.config.vibrateEnabled,
    headerIndicator: @Composable (UltraSwipeRefreshState) -> Unit = UltraSwipeRefreshTheme.config.headerIndicator,
    footerIndicator: @Composable (UltraSwipeRefreshState) -> Unit = UltraSwipeRefreshTheme.config.footerIndicator,
    content: @Composable () -> Unit,
) {
    val state = rememberUltraSwipeRefreshState(isRefreshing = isRefreshing, isLoading = isLoading)
    UltraSwipeRefresh(
        state = state,
        onRefresh = onRefresh,
        onLoadMore = onLoadMore,
        modifier = modifier,
        headerScrollMode = headerScrollMode,
        footerScrollMode = footerScrollMode,
        refreshEnabled = refreshEnabled,
        loadMoreEnabled = loadMoreEnabled,
        refreshTriggerRate = refreshTriggerRate,
        loadMoreTriggerRate = loadMoreTriggerRate,
        headerMaxOffsetRate = headerMaxOffsetRate,
        footerMaxOffsetRate = footerMaxOffsetRate,
        dragMultiplier = dragMultiplier,
        finishDelayMillis = finishDelayMillis,
        vibrateEnabled = vibrateEnabled,
        headerIndicator = headerIndicator,
        footerIndicator = footerIndicator,
        content = content
    )
}

/**
 * 获取Header的偏移量
 */
private fun obtainHeaderOffset(
    state: UltraSwipeRefreshState,
    headerStyle: NestedScrollMode,
    headerHeight: Int
): Float {
    return when (headerStyle) {
        NestedScrollMode.FixedContent, NestedScrollMode.Translate -> state.indicatorOffset - headerHeight
        NestedScrollMode.FixedBehind, NestedScrollMode.FixedFront -> 0f
    }
}

/**
 * 获取内容的偏移量
 */
private fun obtainContentOffset(
    state: UltraSwipeRefreshState,
    headerStyle: NestedScrollMode,
    footerStyle: NestedScrollMode
): Float {
    val indicatorOffset = state.indicatorOffset
    return if (indicatorOffset > 0f) {
        when (headerStyle) {
            NestedScrollMode.Translate, NestedScrollMode.FixedBehind -> indicatorOffset
            NestedScrollMode.FixedContent, NestedScrollMode.FixedFront -> 0f
        }
    } else {
        when (footerStyle) {
            NestedScrollMode.Translate, NestedScrollMode.FixedBehind -> indicatorOffset
            NestedScrollMode.FixedContent, NestedScrollMode.FixedFront -> 0f
        }
    }
}

/**
 * 获取Footer的偏移量
 */
private fun obtainFooterOffset(
    state: UltraSwipeRefreshState,
    footerStyle: NestedScrollMode,
    footerHeight: Int
): Float {
    return when (footerStyle) {
        NestedScrollMode.Translate, NestedScrollMode.FixedContent -> state.indicatorOffset + footerHeight
        NestedScrollMode.FixedBehind, NestedScrollMode.FixedFront -> 0f
    }
}

/**
 * 获取Header或Footer的层级
 */
private fun obtainZIndex(style: NestedScrollMode): Float {
    return when (style) {
        NestedScrollMode.FixedContent, NestedScrollMode.FixedFront -> 1f
        NestedScrollMode.Translate, NestedScrollMode.FixedBehind -> 0f
    }
}

/**
 * 通过[SubcomposeLayout]测量子组合[headerIndicator]和[footerIndicator]的高度
 */
@Composable
private fun RefreshSubComposeLayout(
    refreshEnabled: Boolean,
    loadMoreEnabled: Boolean,
    headerIndicator: @Composable () -> Unit,
    footerIndicator: @Composable () -> Unit,
    content: @Composable (headerHeight: Int, footerHeight: Int) -> Unit
) {
    SubcomposeLayout { constraints: Constraints ->
        val headerMeasurable = subcompose(
            slotId = "headerIndicator",
            content = headerIndicator
        ).firstOrNull()?.measure(constraints)

        val footerMeasurable = subcompose(
            slotId = "footerIndicator",
            content = footerIndicator
        ).firstOrNull()?.measure(constraints)

        val contentMeasurable = subcompose(
            slotId = "content",
            content = {
                content(
                    headerMeasurable?.height?.takeIf { refreshEnabled } ?: 0,
                    footerMeasurable?.height?.takeIf { loadMoreEnabled } ?: 0
                )
            }).map { it.measure(constraints) }.first()

        layout(width = contentMeasurable.width, height = contentMeasurable.height) {
            contentMeasurable.placeRelative(0, 0)
        }
    }
}

/**
 * Vibrator
 */
@Suppress("DEPRECATION")
@Composable
private fun rememberVibrator(): Vibrator {
    val context = LocalContext.current
    return remember("Vibrator") {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
}

/**
 * 振动
 */
@Suppress("DEPRECATION")
private fun Vibrator.vibrate() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.vibrate(
            VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE)
        )
    } else {
        this.vibrate(20)
    }
}