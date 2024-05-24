package com.king.ultraswiperefresh

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 主要用于处理和协调Header或Footer与内容多个元素之间的滚动事件。
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
internal class UltraSwipeRefreshNestedScrollConnection(
    val state: UltraSwipeRefreshState,
    private val coroutineScope: CoroutineScope,
    private val onRefresh: () -> Unit,
    private val onLoadMore: () -> Unit,
) : NestedScrollConnection {

    var dragMultiplier = 0.5f
    var refreshEnabled: Boolean = false
    var loadMoreEnabled: Boolean = false

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource
    ): Offset = when {
        // 当下拉刷新和上拉加载都未启用时，则不进行消费，直接拦截
        !(refreshEnabled || loadMoreEnabled) -> Offset.Zero
        // 当正在刷新或正在加载或处理正在完成时，交由[onPostScroll]去处理
        state.isRefreshing || state.isLoading || state.isFinishing -> available
        // 当都Header和Footer都未显示时，则不进行消费，直接拦截
        state.indicatorOffset == 0f -> Offset.Zero
        // 当正在拖动时，则进行滚动处理
        source == NestedScrollSource.Drag -> onScroll(available)
        else -> Offset.Zero
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset = when {
        // 当下拉刷新和上拉加载都未启用时，则不进行消费，直接拦截
        !(refreshEnabled || loadMoreEnabled) -> Offset.Zero
        // 当正在刷新或正在加载或处理正在完成时，则不进行消费，直接拦截
        state.isRefreshing || state.isLoading || state.isFinishing -> Offset.Zero
        // 当正在拖动时，则进行滚动处理
        source == NestedScrollSource.Drag -> onScroll(available)
        else -> Offset.Zero
    }

    private fun onScroll(available: Offset): Offset {
        if (available.y != 0f) {

            if (state.indicatorOffset <= 0f && available.y < 0f && !loadMoreEnabled) {
                return Offset.Zero
            }
            if (state.indicatorOffset >= 0f && available.y > 0f && !refreshEnabled) {
                return Offset.Zero
            }

            state.isSwipeInProgress = true
            val dragConsumed = available.y * dragMultiplier
            coroutineScope.launch {
                state.dispatchScrollDelta(dragConsumed)
            }
            return available.copy(x = 0f)
        }
        return Offset.Zero
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        if (state.isRefreshing || state.isLoading || state.isFinishing) {
            state.isSwipeInProgress = false
            return available
        }
        when {
            refreshEnabled && state.isExceededRefreshTrigger() -> onRefresh()
            loadMoreEnabled && state.isExceededLoadMoreTrigger() -> onLoadMore()
            state.indicatorOffset != 0f && !state.isSwipeInProgress -> state.animateOffsetTo(0f)
        }
        state.isSwipeInProgress = false
        return Velocity.Zero
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        if (state.isRefreshing || state.isLoading || state.isFinishing) {
            return available
        }
        return Velocity.Zero
    }
}