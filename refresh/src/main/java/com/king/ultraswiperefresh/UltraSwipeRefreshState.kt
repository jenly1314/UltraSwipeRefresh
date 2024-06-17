package com.king.ultraswiperefresh

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.math.absoluteValue

/**
 * 创建一个[UltraSwipeRefreshState]，该状态在重组时会被记住。
 *
 * @param isRefreshing 是否正在刷新；对[isRefreshing]的修改会更新[UltraSwipeRefreshState.isRefreshing]的值
 * @param isLoading 是否正在加载；对[isLoading]的修改会更新[UltraSwipeRefreshState.isLoading]的值
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
fun rememberUltraSwipeRefreshState(
    isRefreshing: Boolean,
    isLoading: Boolean
): UltraSwipeRefreshState {
    return remember { UltraSwipeRefreshState(isRefreshing, isLoading) }.apply {
        this.isRefreshing = isRefreshing
        this.isLoading = isLoading
    }
}

/**
 * 创建一个[UltraSwipeRefreshState]，该状态在重组时会被记住。
 */
@Composable
fun rememberUltraSwipeRefreshState(): UltraSwipeRefreshState {
    return remember { UltraSwipeRefreshState(isRefreshing = false, isLoading = false) }
}

/**
 * 状态：主要用于控制和观察[UltraSwipeRefresh]；比如：控制上拉刷新和下拉加载和观察其状态。
 *
 * @param isRefreshing 是否正在刷新；初始化[UltraSwipeRefreshState.isRefreshing]
 * @param isLoading 是否正在加载；初始化[UltraSwipeRefreshState.isLoading]
 */
@Stable
class UltraSwipeRefreshState(isRefreshing: Boolean, isLoading: Boolean) {

    private val mutatorMutex = MutatorMutex()

    /**
     * 是否正在刷新
     */
    var isRefreshing by mutableStateOf(isRefreshing)

    /**
     * 是否正在加载
     */
    var isLoading by mutableStateOf(isLoading)

    /**
     * 是否正在完成（当[isRefreshing]的值从true变为false或[isLoading]的值从true变为false时，会有一个Header或Footer收起的临时状态，即：[isFinishing]）
     */
    var isFinishing by mutableStateOf(false)
        internal set

    /**
     * 当前是否正在滑动（在单次滑动操作中，会记录[_indicatorOffset]的值发生增量变化时的状态）
     */
    var isSwipeInProgress: Boolean by mutableStateOf(false)
        internal set

    /**
     * Header的状态
     */
    var headerState by mutableStateOf(UltraSwipeHeaderState.PullDownToRefresh)
        internal set

    /**
     * Footer的状态
     */
    var footerState by mutableStateOf(UltraSwipeFooterState.PullUpToLoad)
        internal set

    /**
     * 触发滑动刷新的最小距离
     */
    var refreshTrigger: Float by mutableFloatStateOf(Float.MAX_VALUE)
        internal set

    /**
     * 触发加载更多的最小距离
     */
    var loadMoreTrigger: Float by mutableFloatStateOf(Float.MIN_VALUE)
        internal set

    /**
     * Header可滑动的最大偏移量
     */
    var headerMaxOffset by mutableFloatStateOf(0f)
        internal set

    /**
     * Footer可滑动的最小偏移量
     */
    var footerMinOffset by mutableFloatStateOf(0f)
        internal set

    /**
     * 指示器偏移量
     */
    private val _indicatorOffset = Animatable(0f)

    /**
     * 指示器当前偏移量；当indicatorOffset大于0时，表示Header显示，当indicatorOffset小于0时，表示Footer显示
     */
    val indicatorOffset: Float get() = _indicatorOffset.value

    /**
     * 动画的方式更新指示器的偏移量
     */
    internal suspend fun animateOffsetTo(
        offset: Float,
        priority: MutatePriority = MutatePriority.Default
    ) {
        mutatorMutex.mutate(priority) {
            if (!isFinishing) {
                updateHeaderState()
                updateFooterState()
            }
            _indicatorOffset.animateTo(offset)

            if (isFinishing && indicatorOffset == 0f) {
                isFinishing = false
                updateHeaderState()
                updateFooterState()
            }
        }
    }

    /**
     * 调度触摸事件滚动增量
     */
    internal suspend fun dispatchScrollDelta(delta: Float) {
        mutatorMutex.mutate(MutatePriority.UserInput) {
            _indicatorOffset.snapTo(
                if (indicatorOffset > 0f) {
                    indicatorOffset.plus(delta).coerceIn(0f, headerMaxOffset)
                } else if (indicatorOffset < 0f) {
                    indicatorOffset.plus(delta).coerceIn(footerMinOffset, 0f)
                } else {
                    indicatorOffset.plus(delta).coerceIn(footerMinOffset, headerMaxOffset)
                }.takeIf { it.absoluteValue >= 0.5f } ?: 0f
            )
            if (!isFinishing) {
                updateHeaderState()
                updateFooterState()
            }
        }
    }

    /**
     * 是否达到触发刷新的条件
     *
     */
    internal fun isExceededRefreshTrigger() = indicatorOffset >= refreshTrigger

    /**
     * 是否达到触发加载更多的条件
     */
    internal fun isExceededLoadMoreTrigger() = indicatorOffset <= loadMoreTrigger

    /**
     * 更新Header状态
     */
    private fun updateHeaderState() {
        headerState = when {
            isRefreshing -> UltraSwipeHeaderState.Refreshing
            isSwipeInProgress && isExceededRefreshTrigger() -> UltraSwipeHeaderState.ReleaseToRefresh
            else -> UltraSwipeHeaderState.PullDownToRefresh
        }
    }

    /**
     * 更新Footer状态
     */
    private fun updateFooterState() {
        footerState = when {
            isLoading -> UltraSwipeFooterState.Loading
            isSwipeInProgress && isExceededLoadMoreTrigger() -> UltraSwipeFooterState.ReleaseToLoad
            else -> UltraSwipeFooterState.PullUpToLoad
        }
    }

}