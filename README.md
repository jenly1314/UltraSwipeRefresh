# UltraSwipeRefresh

[![MavenCentral](https://img.shields.io/maven-central/v/com.github.jenly1314.UltraSwipeRefresh/refresh?logo=sonatype)](https://repo1.maven.org/maven2/com/github/jenly1314/UltraSwipeRefresh)
[![JitPack](https://img.shields.io/jitpack/v/github/jenly1314/UltraSwipeRefresh?logo=jitpack)](https://jitpack.io/#jenly1314/UltraSwipeRefresh)
[![CI](https://img.shields.io/github/actions/workflow/status/jenly1314/UltraSwipeRefresh/build.yml?logo=github)](https://github.com/jenly1314/UltraSwipeRefresh/actions/workflows/build.yml)
[![Download](https://img.shields.io/badge/download-APK-brightgreen?logo=github)](https://raw.githubusercontent.com/jenly1314/UltraSwipeRefresh/master/app/release/app-release.apk)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen?logo=android)](https://developer.android.com/guide/topics/manifest/uses-sdk-element#ApiLevels)
[![License](https://img.shields.io/github/license/jenly1314/UltraSwipeRefresh?logo=open-source-initiative)](https://opensource.org/licenses/mit)

**UltraSwipeRefresh**：一个可带来极致体验的 **Compose** 刷新组件；支持下拉刷新和上拉加载，可完美替代官方的 **SwipeRefresh**；并且支持的功能更多，可扩展性更强。

> **UltraSwipeRefresh** 在设计之初，主要参考了谷歌官方的 [SwipeRefresh](https://github.com/google/accompanist/tree/v0.36.0/swiperefresh) 和第三方的 [SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout) ，在充分借鉴两者优秀设计理念的基础上，致力于打造一个拥有极致体验的 **Compose** 刷新组件。

## 功能特色

- 🌀 **多种滑动模式**：提供`Translate`(平移)、`FixedContent`(固定内容)、`FixedBehind`(固定在背后)、`FixedFront`(固定在前面) 等滑动交互模式，适配各类使用场景。
- 🎨 **可完全定制UI**：支持通过 `headerIndicator`/`footerIndicator` 完全自定义指示器，满足深度定制需求。
- ✨ **灵活配置方案**：可自由组合任意滑动模式与指示器样式，轻松实现个性化刷新效果。

## 效果展示

![Image](art/UltraSwipeRefresh.gif)

> 你也可以直接下载 [演示App](https://raw.githubusercontent.com/jenly1314/UltraSwipeRefresh/master/app/release/app-release.apk) 体验效果

## 引入

### Gradle:

1. 在Project的 **build.gradle** 或 **setting.gradle** 中添加远程仓库

    ```gradle
    repositories {
        //...
        mavenCentral()
    }
    ```

2. 在Module的 **build.gradle** 中添加依赖项

    ```gradle
    // 极致体验的Compose刷新组件 (*必须)
    implementation 'com.github.jenly1314.UltraSwipeRefresh:refresh:1.4.2'

    // 经典样式的指示器 (可选)
    implementation 'com.github.jenly1314.UltraSwipeRefresh:refresh-indicator-classic:1.4.2'
    // Lottie动画指示器 (可选)
    implementation 'com.github.jenly1314.UltraSwipeRefresh:refresh-indicator-lottie:1.4.2'
    // 进度条样式的指示器 (可选)
    implementation 'com.github.jenly1314.UltraSwipeRefresh:refresh-indicator-progress:1.4.2'
    ```

## 使用

### UltraSwipeRefresh

**Compose** 组件的使用大都比较直观，一般看一下 **Composable** 函数对应的参数说明基本就会使用了。

#### UltraSwipeRefresh参数说明

```kotlin
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
 * @param vibrationEnabled 是否启用振动，如果启用则当滑动偏移量满足触发刷新或触发加载更多时，会有振动效果；默认为：false
 * @param vibrationMillis 触发刷新或触发加载更多时的振动时长（毫秒）默认：25毫秒
 * @param alwaysScrollable 是否始终可以滚动；当为true时，则会忽略刷新中或加载中的状态限制，始终可以进行滚动；默认为：false
 * @param onCollapseScroll 可选回调，当Header/Footer收起时需要同步调整列表位置以消除视觉回弹时使用
 * @param headerIndicator 下拉刷新时顶部显示的Header指示器
 * @param footerIndicator 上拉加载更多时底部显示的Footer指示器
 * @param contentContainer 内容的父容器，便于统一管理
 * @param content 可进行刷新或加载更多所包含的内容
 */
```

#### UltraSwipeRefresh使用示例

使用`UltraSwipeRefresh`实现一个经典样式的刷新与加载示例：

```kotlin
@Composable
fun UltraSwipeRefreshSample() {

    val state = rememberUltraSwipeRefreshState()
    var itemCount by remember { mutableIntStateOf(20) }
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    UltraSwipeRefresh(
        state = state,
        onRefresh = {
            coroutineScope.launch {
                state.isRefreshing = true
                // TODO 刷新的逻辑处理，此处的延时只是为了演示效果
                delay(2000)
                itemCount = 20
                state.isRefreshing = false
            }
        },
        onLoadMore = {
            coroutineScope.launch {
                state.isLoading = true
                // TODO 加载更多的逻辑处理，此处的延时只是为了演示效果
                delay(2000)
                itemCount += 20
                state.isLoading = false
            }
        },
        modifier = Modifier.background(color = Color(0x7FEEEEEE)),
        headerScrollMode = NestedScrollMode.Translate,
        footerScrollMode = NestedScrollMode.Translate,
        onCollapseScroll = {
            // 小于0时表示：由下拉刷新收起时触发的，大于0时表示：由上拉加载收起时触发的
            if (it > 0) {
                // 指示器收起时滚动列表位置，消除视觉回弹
                lazyListState.animateScrollBy(it)
            }
        },
        headerIndicator = {
            ClassicRefreshHeader(it)
        },
        footerIndicator = {
            ClassicRefreshFooter(it)
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(color = Color.White),
            state = lazyListState,
        ) {
            repeat(itemCount) {
                item {
                    Text(
                        text = "UltraSwipeRefresh列表Item${it + 1}",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        color = Color(0xFF333333),
                        fontSize = 16.sp
                    )
                    Divider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFF2F3F6)
                    )
                }
            }
        }
    }
}

```

> 使用小提示：**headerIndicator/footerIndicator** 与 **headerScrollMode/footerScrollMode** 组合使用，以获得最佳滑动体验！

### UltraSwipeRefreshTheme

UltraSwipeRefreshTheme：主要用于统一管理全局默认配置。

> 通常情况下，一个App使用的刷新样式是统一的，如果你需要进行全局统一刷新组件的样式时，可以通过`UltraSwipeRefreshTheme.config`来动态修改`UltraSwipeRefresh`的全局默认配置。

#### UltraSwipeRefreshTheme使用示例

```kotlin
// 全局设置默认的滑动模式 （建议在Application的onCreate中进行配置）
UltraSwipeRefreshTheme.config = UltraSwipeRefreshTheme.config.copy(
   headerScrollMode = NestedScrollMode.Translate,
   footerScrollMode = NestedScrollMode.Translate,
)
```
> 更多参数配置可查看：`UltraSwipeRefreshTheme.config` 的定义，这里就不一一举例了。

### 指示器样式

这里罗列一下目前 **UltraSwipeRefresh** 所提供的一些Header和Footer指示器样式与示例，以供参考。

| 默认官方样式                                                                                                | 经典样式                                                                                                                                                                                                           |
|:------------------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| SwipeRefreshHeader                                                                                    | ClassicRefreshHeader                                                                                                                                                                                           |
| ![Image](art/SwipeRefreshIndicatorSample.gif)                                                         | ![Image](art/ClassicRefreshIndicatorSample.gif)                                                                                                                                                                |
| SwipeRefreshFooter                                                                                    | ClassicRefreshFooter                                                                                                                                                                                           |
| [官方默认的刷新样式示例](app/src/main/java/com/king/ultraswiperefresh/app/sample/SwipeRefreshIndicatorSample.kt) | [经典刷新样式示例](app/src/main/java/com/king/ultraswiperefresh/app/sample/ClassicRefreshIndicatorSample.kt)  /  [经典刷新自动加载示例](app/src/main/java/com/king/ultraswiperefresh/app/sample/ClassicRefreshAutoLoadSample.kt) |

| 进度条样式                                                                                           | Lottie动画样式                                                                                                                            |
|:-----------------------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------------------|
| ProgressRefreshHeader                                                                                | LottieRefreshHeader                                                                                                                   |
| ![Image](art/ProgressRefreshIndicatorSample.gif)                                                     | ![Image](art/LottieRefreshIndicatorSample.gif)                                                                                        |
| ProgressRefreshFooter                                                                                | LottieRefreshFooter                                                                                                                   |
| [进度条刷新样式示例](app/src/main/java/com/king/ultraswiperefresh/app/sample/ProgressRefreshIndicatorSample.kt) | [Lottie动画刷新样式示例](app/src/main/java/com/king/ultraswiperefresh/app/sample/LottieRefreshIndicatorSample.kt)                                    |

> 如果以上的指示器效果都不满足你的需求，你可以自定义去实现Header和Footer对应的指示器，也可以直接使用Lottie动画样式的指示器，来快速接入任何Lottie动画。

更多使用详情，请查看[app](app)中的源码使用示例或直接查看 [API帮助文档](https://jenly1314.github.io/UltraSwipeRefresh/api/)

## 相关推荐

- [CodeTextField](https://github.com/jenly1314/CodeTextField)一个使用 Compose 实现的验证码输入框。
- [compose-component](https://github.com/jenly1314/compose-component) 一个Jetpack Compose的组件库；主要提供了一些小组件，便于快速使用。
- [SuperSwipeRefreshLayout](https://github.com/jenly1314/SuperSwipeRefreshLayout) 是在SwipeRefreshLayout的基础之上扩展修改，让其支持上拉刷新。
- [SuperSlidingPaneLayout](https://github.com/jenly1314/SuperSlidingPaneLayout) 是在SlidingPaneLayout的基础之上扩展修改，新增几种不同的侧滑效果。
- [SuperTextView](https://github.com/jenly1314/SuperTextView) 一个在TextView的基础上扩展了几种动画效果的控件。
- [LoadingView](https://github.com/jenly1314/LoadingView) 一个圆弧加载过渡动画，圆弧个数，大小，弧度，渐变颜色，完全可配。
- [WaveView](https://github.com/jenly1314/WaveView) 一个水波纹动画控件视图，支持波纹数，波纹振幅，波纹颜色，波纹速度，波纹方向等属性完全可配。
- [GiftSurfaceView](https://github.com/jenly1314/GiftSurfaceView) 一个适用于直播间送礼物拼图案的动画控件。
- [FlutteringLayout](https://github.com/jenly1314/FlutteringLayout) 一个适用于直播间点赞桃心飘动效果的控件。
- [DragPolygonView](https://github.com/jenly1314/DragPolygonView) 一个支持可拖动多边形，支持通过拖拽多边形的角改变其形状的任意多边形控件。
- [CircleProgressView](https://github.com/jenly1314/CircleProgressView) 一个圆形的进度动画控件，动画效果纵享丝滑。
- [ArcSeekBar](https://github.com/jenly1314/ArcSeekBar) 一个弧形的拖动条进度控件，配置参数完全可定制化。
- [DrawBoard](https://github.com/jenly1314/DrawBoard) 一个自定义View实现的画板；方便对图片进行编辑和各种涂鸦相关操作。

<!-- end -->

## 版本日志

#### v1.4.2 ：2025-9-6
* 优化显示细节（[#38](https://github.com/jenly1314/UltraSwipeRefresh/issues/38)）

#### [查看更多版本日志](CHANGELOG.md)

---

![footer](https://jenly1314.github.io/page/footer.svg)
