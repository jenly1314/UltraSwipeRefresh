
## ~待发布版本~（已发布）

待发布版本暂使用 **Jit Pack** 仓库；待收集的一些问题测试稳定后，再统一发布正式版本至 **Maven Central** 仓库。

## 引入

### Gradle:

1. 在Project的 **build.gradle** 或 **setting.gradle** 中添加远程仓库

    ```gradle
    repositories {
        //...
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
    ```

2. 在Module的 **build.gradle** 里面添加引入依赖项

    ```gradle
    // 极致体验的Compose刷新组件 (*必须)
    implementation 'com.github.jenly1314.UltraSwipeRefresh:refresh:8bd8003347'

    // 经典样式的指示器 (可选)
    implementation 'com.github.jenly1314.UltraSwipeRefresh:refresh-indicator-classic:8bd8003347'
    // Lottie动画指示器 (可选)
    implementation 'com.github.jenly1314.UltraSwipeRefresh:refresh-indicator-lottie:8bd8003347'
    // 进度条样式的指示器 (可选)
    implementation 'com.github.jenly1314.UltraSwipeRefresh:refresh-indicator-progress:8bd8003347'
    ```
