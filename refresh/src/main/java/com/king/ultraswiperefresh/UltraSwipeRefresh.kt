package com.king.ultraswiperefresh

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.zIndex
import com.king.ultraswiperefresh.theme.UltraSwipeRefreshTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * UltraSwipeRefresh：一个可带来极致体验的 Compose 刷新组件，支持下拉刷新和上拉加载，
 * 可完美替代官方的 SwipeRefresh，功能更丰富，扩展性更强。
 *
 * @param state 状态对象，用于控制和观察 [UltraSwipeRefresh] 的状态，如下拉刷新和上拉加载的触发与控制
 * @param onRefresh 下拉刷新手势触发完成时的回调
 * @param onLoadMore 上拉加载手势触发完成时的回调
 * @param modifier 修饰符，用于装饰或扩展 Compose UI 元素的行为；详细说明见 [Modifier]
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
 * @param onCollapseScroll 可选回调，当 Header/Footer 收起时用于同步调整列表位置，消除视觉回弹
 * @param headerIndicator 下拉刷新时顶部显示的 Header 指示器
 * @param footerIndicator 上拉加载时底部显示的 Footer 指示器
 * @param headerSecondaryContent Header 二级内容（可选）
 * @param footerSecondaryContent Footer 二级内容（可选）
 * @param contentContainer [content] 的父容器，便于统一管理
 * @param content 可进行刷新或加载所包含的内容区域
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
    headerSecondaryEnabled: Boolean = UltraSwipeRefreshTheme.config.headerSecondaryEnabled,
    footerSecondaryEnabled: Boolean = UltraSwipeRefreshTheme.config.footerSecondaryEnabled,
    headerSecondaryBehavior: SecondaryBehavior = UltraSwipeRefreshTheme.config.headerSecondaryBehavior,
    footerSecondaryBehavior: SecondaryBehavior = UltraSwipeRefreshTheme.config.footerSecondaryBehavior,
    headerSecondaryPreview: Boolean = UltraSwipeRefreshTheme.config.headerSecondaryPreview,
    footerSecondaryPreview: Boolean = UltraSwipeRefreshTheme.config.footerSecondaryPreview,
    @FloatRange(from = 1.0, fromInclusive = false)
    headerSecondaryTriggerRate: Float = UltraSwipeRefreshTheme.config.headerSecondaryTriggerRate,
    @FloatRange(from = 1.0, fromInclusive = false)
    footerSecondaryTriggerRate: Float = UltraSwipeRefreshTheme.config.footerSecondaryTriggerRate,
    @FloatRange(from = 1.0)
    headerMaxOffsetRate: Float = UltraSwipeRefreshTheme.config.headerMaxOffsetRate,
    @FloatRange(from = 1.0)
    footerMaxOffsetRate: Float = UltraSwipeRefreshTheme.config.footerMaxOffsetRate,
    @FloatRange(from = 0.0, to = 2.0, fromInclusive = false)
    dragMultiplier: Float = UltraSwipeRefreshTheme.config.dragMultiplier,
    @IntRange(from = 0, to = 2000)
    finishDelayMillis: Long = UltraSwipeRefreshTheme.config.finishDelayMillis,
    vibrationEnabled: Boolean = UltraSwipeRefreshTheme.config.vibrationEnabled,
    @IntRange(from = 1, to = 50)
    vibrationMillis: Long = UltraSwipeRefreshTheme.config.vibrationMillis,
    alwaysScrollable: Boolean = UltraSwipeRefreshTheme.config.alwaysScrollable,
    onCollapseScroll: (suspend (Float) -> Unit)? = null,
    headerIndicator: @Composable (UltraSwipeRefreshState) -> Unit = UltraSwipeRefreshTheme.config.headerIndicator,
    footerIndicator: @Composable (UltraSwipeRefreshState) -> Unit = UltraSwipeRefreshTheme.config.footerIndicator,
    headerSecondaryContent: (@Composable (UltraSwipeRefreshState) -> Unit)? = null,
    footerSecondaryContent: (@Composable (UltraSwipeRefreshState) -> Unit)? = null,
    contentContainer: @Composable (@Composable () -> Unit) -> Unit = UltraSwipeRefreshTheme.config.contentContainer,
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val updatedOnRefresh = rememberUpdatedState(onRefresh)
    val updateOnLoadMore = rememberUpdatedState(onLoadMore)

    Box(modifier) {
        RefreshSubComposeLayout(
            headerIndicator = {
                headerIndicator(state)
            },
            footerIndicator = {
                footerIndicator(state)
            },
        ) { headerHeight, footerHeight ->

            val nestedScrollConnection = remember(state, coroutineScope) {
                UltraSwipeRefreshNestedScrollConnection(
                    state = state,
                    coroutineScope = coroutineScope,
                    onRefresh = {
                        updatedOnRefresh.value.invoke()
                    },
                    onLoadMore = {
                        updateOnLoadMore.value.invoke()
                    })
            }.apply {
                this.dragMultiplier = dragMultiplier
                this.refreshEnabled = refreshEnabled
                this.loadMoreEnabled = loadMoreEnabled
                this.alwaysScrollable = alwaysScrollable
                this.headerSecondaryEnabled = headerSecondaryEnabled
                this.footerSecondaryEnabled = footerSecondaryEnabled
            }

            SideEffect {
                state.refreshTrigger = headerHeight.times(refreshTriggerRate).coerceAtLeast(1f)
                state.loadMoreTrigger = -(footerHeight.times(loadMoreTriggerRate).coerceAtLeast(1f))
                state.headerMaxOffset = headerHeight.times(headerMaxOffsetRate)
                state.footerMinOffset = -footerHeight.times(footerMaxOffsetRate)

                // 二级触发阈值
                if (headerSecondaryEnabled) {
                    state.headerSecondaryTrigger =
                        headerHeight.times(headerSecondaryTriggerRate).coerceAtLeast(1f)
                    // 确保 headerMaxOffset 能超过二级触发点
                    state.headerMaxOffset =
                        maxOf(state.headerMaxOffset, state.headerSecondaryTrigger)
                } else {
                    state.headerSecondaryTrigger = Float.MAX_VALUE
                }
                if (footerSecondaryEnabled) {
                    state.footerSecondaryTrigger =
                        -(footerHeight.times(footerSecondaryTriggerRate).coerceAtLeast(1f))
                    // 确保 footerMinOffset 能超过二级触发点
                    state.footerMinOffset =
                        minOf(state.footerMinOffset, state.footerSecondaryTrigger)
                } else {
                    state.footerSecondaryTrigger = Float.NEGATIVE_INFINITY
                }
            }

            LaunchedEffect(
                state.isSwipeInProgress,
                state.isRefreshing,
                state.isLoading,
                state.isHeaderSecondary,
                state.isFooterSecondary
            ) {
                if (!state.isSwipeInProgress) {
                    when {
                        state.isHeaderSecondary -> state.updateHeaderState()
                        state.isFooterSecondary -> state.updateFooterState()
                        state.isRefreshing -> state.animateOffsetTo(headerHeight.toFloat())
                        state.isLoading -> state.animateOffsetTo(-footerHeight.toFloat())
                        state.headerState == UltraSwipeHeaderState.Refreshing || state.footerState == UltraSwipeFooterState.Loading -> {
                            coroutineScope.launch {
                                state.isFinishing = true
                                if (state.headerState == UltraSwipeHeaderState.Refreshing) {
                                    state.animateOffsetTo(headerHeight.toFloat())
                                } else if (state.footerState == UltraSwipeFooterState.Loading) {
                                    state.animateOffsetTo(-footerHeight.toFloat())
                                }
                                delay(finishDelayMillis)
                                onCollapseScroll?.also {
                                    coroutineScope.launch {
                                        it.invoke(-state.indicatorOffset)
                                    }
                                }
                                state.animateOffsetTo(0f, MutatePriority.PreventUserInput)
                            }
                        }

                        else -> state.animateOffsetTo(0f)
                    }
                }
            }

            VibrationLaunchedEffect(vibrationEnabled, vibrationMillis, state)

            val headerZIndex = remember(headerScrollMode) { obtainZIndex(headerScrollMode) }
            val footerZIndex = remember(footerScrollMode) { obtainZIndex(footerScrollMode) }

            var boxSize by remember { mutableStateOf(IntSize.Zero) }

            Box(
                modifier = Modifier
                    .nestedScroll(nestedScrollConnection)
                    .clipToBounds()
                    .onSizeChanged { boxSize = it }
            ) {

                // Header 二级内容
                HeaderSecondaryContent(
                    state = state,
                    size = boxSize,
                    headerSecondaryEnabled = headerSecondaryEnabled,
                    headerSecondaryBehavior = headerSecondaryBehavior,
                    headerSecondaryPreview = headerSecondaryPreview,
                    headerSecondaryContent = headerSecondaryContent,
                )

                // Footer 二级内容
                FooterSecondaryContent(
                    state = state,
                    size = boxSize,
                    footerSecondaryEnabled = footerSecondaryEnabled,
                    footerSecondaryBehavior = footerSecondaryBehavior,
                    footerSecondaryPreview = footerSecondaryPreview,
                    footerSecondaryContent = footerSecondaryContent,
                )

                val secondaryTransaction = updateTransition(
                    Pair(
                        state.headerState == UltraSwipeHeaderState.Secondary,
                        state.footerState == UltraSwipeFooterState.Secondary
                    )
                )

                val contentOffsetProgress by secondaryTransaction.animateFloat {
                    when {
                        it.first -> 1f
                        it.second -> -1f
                        else -> 0f
                    }
                }

                // Header 指示器
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .graphicsLayer {
                            translationY = obtainHeaderOffset(state, headerScrollMode, headerHeight)
                            alpha = if (contentOffsetProgress != 0f) 0f else 1f
                        }
                        .zIndex(headerZIndex)
                ) {
                    headerIndicator(state)
                }

                // Footer 指示器
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .graphicsLayer {
                            translationY = obtainFooterOffset(state, footerScrollMode, footerHeight)
                            alpha = if (contentOffsetProgress != 0f) 0f else 1f
                        }
                        .zIndex(footerZIndex)
                ) {
                    footerIndicator(state)
                }

                // 内容
                Box(modifier = Modifier.graphicsLayer {
                    val baseOffset = obtainContentOffset(state, headerScrollMode, footerScrollMode)
                    translationY = baseOffset + contentOffsetProgress * size.height
                }) {
                    contentContainer(content)
                }
            }
        }
    }
}

/**
 * UltraSwipeRefresh：一个可带来极致体验的 Compose 刷新组件，支持下拉刷新和上拉加载，
 * 可完美替代官方的 SwipeRefresh，功能更丰富，扩展性更强。
 *
 * @param isRefreshing 是否正在刷新
 * @param isLoading 是否正在加载
 * @param onRefresh 下拉刷新手势触发完成时的回调
 * @param onLoadMore 上拉加载手势触发完成时的回调
 * @param modifier 修饰符，用于装饰或扩展 Compose UI 元素的行为；详细说明见 [Modifier]
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
 * @param onCollapseScroll 可选回调，当 Header/Footer 收起时用于同步调整列表位置，消除视觉回弹
 * @param headerIndicator 下拉刷新时顶部显示的 Header 指示器
 * @param footerIndicator 上拉加载时底部显示的 Footer 指示器
 * @param headerSecondaryContent Header 二级内容（可选）
 * @param footerSecondaryContent Footer 二级内容（可选）
 * @param contentContainer [content] 的父容器，便于统一管理
 * @param content 可进行刷新或加载所包含的内容区域
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
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
    headerSecondaryEnabled: Boolean = UltraSwipeRefreshTheme.config.headerSecondaryEnabled,
    footerSecondaryEnabled: Boolean = UltraSwipeRefreshTheme.config.footerSecondaryEnabled,
    headerSecondaryBehavior: SecondaryBehavior = UltraSwipeRefreshTheme.config.headerSecondaryBehavior,
    footerSecondaryBehavior: SecondaryBehavior = UltraSwipeRefreshTheme.config.footerSecondaryBehavior,
    headerSecondaryPreview: Boolean = UltraSwipeRefreshTheme.config.headerSecondaryPreview,
    footerSecondaryPreview: Boolean = UltraSwipeRefreshTheme.config.footerSecondaryPreview,
    @FloatRange(from = 1.0, fromInclusive = false)
    headerSecondaryTriggerRate: Float = UltraSwipeRefreshTheme.config.headerSecondaryTriggerRate,
    @FloatRange(from = 1.0, fromInclusive = false)
    footerSecondaryTriggerRate: Float = UltraSwipeRefreshTheme.config.footerSecondaryTriggerRate,
    @FloatRange(from = 1.0)
    headerMaxOffsetRate: Float = UltraSwipeRefreshTheme.config.headerMaxOffsetRate,
    @FloatRange(from = 1.0)
    footerMaxOffsetRate: Float = UltraSwipeRefreshTheme.config.footerMaxOffsetRate,
    @FloatRange(from = 0.0, to = 2.0, fromInclusive = false)
    dragMultiplier: Float = UltraSwipeRefreshTheme.config.dragMultiplier,
    @IntRange(from = 0, to = 2000)
    finishDelayMillis: Long = UltraSwipeRefreshTheme.config.finishDelayMillis,
    vibrationEnabled: Boolean = UltraSwipeRefreshTheme.config.vibrationEnabled,
    @IntRange(from = 1, to = 50)
    vibrationMillis: Long = UltraSwipeRefreshTheme.config.vibrationMillis,
    alwaysScrollable: Boolean = UltraSwipeRefreshTheme.config.alwaysScrollable,
    onCollapseScroll: (suspend (Float) -> Unit)? = null,
    headerIndicator: @Composable (UltraSwipeRefreshState) -> Unit = UltraSwipeRefreshTheme.config.headerIndicator,
    footerIndicator: @Composable (UltraSwipeRefreshState) -> Unit = UltraSwipeRefreshTheme.config.footerIndicator,
    headerSecondaryContent: (@Composable (UltraSwipeRefreshState) -> Unit)? = null,
    footerSecondaryContent: (@Composable (UltraSwipeRefreshState) -> Unit)? = null,
    contentContainer: @Composable (@Composable () -> Unit) -> Unit = UltraSwipeRefreshTheme.config.contentContainer,
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
        headerSecondaryEnabled = headerSecondaryEnabled,
        footerSecondaryEnabled = footerSecondaryEnabled,
        headerSecondaryBehavior = headerSecondaryBehavior,
        footerSecondaryBehavior = footerSecondaryBehavior,
        headerSecondaryPreview = headerSecondaryPreview,
        footerSecondaryPreview = footerSecondaryPreview,
        headerSecondaryTriggerRate = headerSecondaryTriggerRate,
        footerSecondaryTriggerRate = footerSecondaryTriggerRate,
        headerMaxOffsetRate = headerMaxOffsetRate,
        footerMaxOffsetRate = footerMaxOffsetRate,
        dragMultiplier = dragMultiplier,
        finishDelayMillis = finishDelayMillis,
        vibrationEnabled = vibrationEnabled,
        vibrationMillis = vibrationMillis,
        alwaysScrollable = alwaysScrollable,
        onCollapseScroll = onCollapseScroll,
        headerIndicator = headerIndicator,
        footerIndicator = footerIndicator,
        headerSecondaryContent = headerSecondaryContent,
        footerSecondaryContent = footerSecondaryContent,
        contentContainer = contentContainer,
        content = content
    )
}

@Composable
private fun HeaderSecondaryContent(
    state: UltraSwipeRefreshState,
    size: IntSize,
    headerSecondaryEnabled: Boolean,
    headerSecondaryBehavior: SecondaryBehavior,
    headerSecondaryPreview: Boolean,
    headerSecondaryContent: (@Composable (UltraSwipeRefreshState) -> Unit)?,
) {
    if (!headerSecondaryEnabled || headerSecondaryContent == null) return

    val showHeaderSecondary by remember(state.headerState, headerSecondaryPreview) {
        derivedStateOf {
            when {
                headerSecondaryEnabled -> {
                    when {
                        state.headerState == UltraSwipeHeaderState.Secondary -> true
                        headerSecondaryPreview && state.headerState == UltraSwipeHeaderState.ReleaseToSecondary -> true
                        else -> false
                    }
                }

                else -> false
            }
        }
    }

    val headerTransaction = updateTransition(state.headerState == UltraSwipeHeaderState.Secondary)
    val headerOffset by headerTransaction.animateFloat { if (it) 0f else -size.height.toFloat() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                translationY = if (headerSecondaryBehavior == SecondaryBehavior.Slide) {
                    if (state.headerState == UltraSwipeHeaderState.ReleaseToSecondary) {
                        -size.height + state.indicatorOffset
                    } else {
                        headerOffset
                    }
                } else {
                    0f
                }
            }
            .zIndex(if (state.headerState == UltraSwipeHeaderState.Secondary) 1f else 0f)
    ) {
        AnimatedVisibility(
            visible = showHeaderSecondary,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            headerSecondaryContent(state)
        }
    }
}

@Composable
private fun FooterSecondaryContent(
    state: UltraSwipeRefreshState,
    size: IntSize,
    footerSecondaryEnabled: Boolean,
    footerSecondaryBehavior: SecondaryBehavior,
    footerSecondaryPreview: Boolean,
    footerSecondaryContent: (@Composable (UltraSwipeRefreshState) -> Unit)?,
) {
    if (!footerSecondaryEnabled || footerSecondaryContent == null) return

    val showFooterSecondary by remember(state.footerState, footerSecondaryPreview) {
        derivedStateOf {
            when {
                footerSecondaryEnabled -> {
                    when {
                        state.footerState == UltraSwipeFooterState.Secondary -> true
                        footerSecondaryPreview && state.footerState == UltraSwipeFooterState.ReleaseToSecondary -> true
                        else -> false
                    }
                }

                else -> false
            }
        }
    }

    val footerTransaction = updateTransition(state.footerState == UltraSwipeFooterState.Secondary)
    val footerOffset by footerTransaction.animateFloat { if (it) 0f else size.height + state.indicatorOffset }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                translationY = if (footerSecondaryBehavior == SecondaryBehavior.Slide) {
                    if (state.footerState == UltraSwipeFooterState.ReleaseToSecondary) {
                        size.height + state.indicatorOffset
                    } else {
                        footerOffset
                    }
                } else {
                    0f
                }
            }
            .zIndex(if (state.footerState == UltraSwipeFooterState.Secondary) 1f else 0f)
    ) {
        AnimatedVisibility(
            visible = showFooterSecondary,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            footerSecondaryContent(state)
        }
    }
}


/**
 * 获取Header的偏移量
 */
private fun obtainHeaderOffset(
    state: UltraSwipeRefreshState,
    headerScrollMode: NestedScrollMode,
    headerHeight: Int
): Float {
    return when (headerScrollMode) {
        NestedScrollMode.FixedContent, NestedScrollMode.Translate -> state.indicatorOffset - headerHeight
        NestedScrollMode.FixedBehind, NestedScrollMode.FixedFront -> 0f
    }
}

/**
 * 获取内容的偏移量
 */
private fun obtainContentOffset(
    state: UltraSwipeRefreshState,
    headerScrollMode: NestedScrollMode,
    footerScrollMode: NestedScrollMode
): Float {
    val indicatorOffset = state.indicatorOffset
    return if (indicatorOffset > 0f) {
        when (headerScrollMode) {
            NestedScrollMode.Translate, NestedScrollMode.FixedBehind -> indicatorOffset
            NestedScrollMode.FixedContent, NestedScrollMode.FixedFront -> 0f
        }
    } else {
        when (footerScrollMode) {
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
    footerScrollMode: NestedScrollMode,
    footerHeight: Int
): Float {
    return when (footerScrollMode) {
        NestedScrollMode.Translate, NestedScrollMode.FixedContent -> state.indicatorOffset + footerHeight
        NestedScrollMode.FixedBehind, NestedScrollMode.FixedFront -> 0f
    }
}

/**
 * 获取Header或Footer的层级
 */
private fun obtainZIndex(nestedScrollMode: NestedScrollMode): Float {
    return when (nestedScrollMode) {
        NestedScrollMode.FixedContent, NestedScrollMode.FixedFront -> 1f
        NestedScrollMode.Translate, NestedScrollMode.FixedBehind -> 0f
    }
}

/**
 * 通过[SubcomposeLayout]测量子布局[headerIndicator]和[footerIndicator]的高度
 */
@Composable
private fun RefreshSubComposeLayout(
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
                    headerMeasurable?.height ?: 0,
                    footerMeasurable?.height ?: 0
                )
            }).map { it.measure(constraints) }.first()

        layout(width = contentMeasurable.width, height = contentMeasurable.height) {
            contentMeasurable.placeRelative(0, 0)
        }
    }
}

/**
 * 振动效果反馈
 */
@Suppress("DEPRECATION")
@Composable
private fun VibrationLaunchedEffect(
    vibrationEnabled: Boolean,
    vibrationMillis: Long,
    state: UltraSwipeRefreshState
) {
    val vibrator = rememberVibrator()

    if (!vibrationEnabled || !vibrator.hasVibrator()) return

    LaunchedEffect(state.headerState, state.footerState) {
        if (state.headerState == UltraSwipeHeaderState.ReleaseToRefresh ||
            state.footerState == UltraSwipeFooterState.ReleaseToLoad ||
            state.headerState == UltraSwipeHeaderState.ReleaseToSecondary ||
            state.footerState == UltraSwipeFooterState.ReleaseToSecondary
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        vibrationMillis,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(vibrationMillis)
            }
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
    return remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
}

