package com.king.ultraswiperefresh

/**
 * Header的状态
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
enum class UltraSwipeHeaderState {
    /**
     * 下拉刷新
     */
    PullDownToRefresh,

    /**
     * 释放立即刷新
     */
    ReleaseToRefresh,

    /**
     * 刷新中
     */
    Refreshing,
}