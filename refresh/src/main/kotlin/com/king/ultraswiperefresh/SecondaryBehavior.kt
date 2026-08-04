package com.king.ultraswiperefresh

/**
 * 二级内容交互行为模式
 *
 * 定义 Header/Footer 二级内容与主内容之间的交互方式
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
enum class SecondaryBehavior {

    /**
     * 滑动模式
     *
     * Header 或 Footer 的二级内容从屏幕外平移进入或退出
     */
    Slide,

    /**
     * 背后模式
     *
     * Header 或 Footer 的二级内容固定在列表内容背后
     */
    Behind

}
