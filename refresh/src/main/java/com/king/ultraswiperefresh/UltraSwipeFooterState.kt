package com.king.ultraswiperefresh

/**
 * Footer的状态
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
enum class UltraSwipeFooterState {
    /**
     * 上拉加载
     */
    PullUpToLoad,

    /**
     * 释放立即加载
     */
    ReleaseToLoad,

    /**
     * 加载中
     */
    Loading,
}