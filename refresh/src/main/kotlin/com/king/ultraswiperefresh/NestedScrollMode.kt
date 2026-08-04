package com.king.ultraswiperefresh

/**
 * 嵌套滑动模式：在进行下拉刷新或上拉加载时，Header和Footer都可以设置相应的[NestedScrollMode]，
 * 不同的模式会决定Header和Footer与内容进行的联动效果有所不同。
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
enum class NestedScrollMode {

    /**
     * 平移； 即：Header或 Footer与内容一起滚动
     */
    Translate,

    /**
     * 固定内容；即：内容固定，Header或 Footer进行滚动
     */
    FixedContent,

    /**
     * 固定在背后；即：Header或 Footer固定，仅内容滚动
     */
    FixedBehind,

    /**
     * 固定在前面；即：Header或 Footer和内容都固定，仅改变状态
     */
    FixedFront,

}