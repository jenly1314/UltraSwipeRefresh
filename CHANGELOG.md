## 版本日志

#### v1.3.0 ：2024-7-20
* 更新compose至v1.6.0 (v1.5.0 -> v1.6.0) （[#13](https://github.com/jenly1314/UltraSwipeRefresh/issues/13)）
* 新增参数`alwaysScrollable`：是否始终可以滚动
* 优化一些细节

#### v1.2.0 ：2024-7-1
* 新增参数`contentContainer`：内容的父容器，便于统一管理
* 修复了一些已知问题

#### v1.1.3 ：2024-6-2
* 修复BUG：禁用下拉刷新或上拉加载时，上下滑动的边界值判定问题。（[#8](https://github.com/jenly1314/UltraSwipeRefresh/issues/8)）
* 优化一些细节

#### v1.1.2 ：2024-5-22
* 修复BUG：部分机型在某些特定场景下，出现“无法再次触发下拉刷新”的问题。（[#7](https://github.com/jenly1314/UltraSwipeRefresh/issues/7)）

#### v1.1.1 ：2024-4-20
* 修复BUG：刷新状态变化太快时，导致”完成时的定格提示动画”不执行的问题。（[#4](https://github.com/jenly1314/UltraSwipeRefresh/issues/4)）
* 优化一些细节

#### v1.1.0 ：2023-12-17
* 新增`UltraSwipeRefreshTheme`：用于统一管理全局默认配置
* 新增参数`finishDelayMillis`：完成时延时时间（可用于定格展示提示内容）
* 新增参数`vibrateEnabled`：是否启用振动（当滑动偏移量满足触发刷新或触发加载更多时，会有振动效果）

#### v1.0.0 ：2023-12-3
* UltraSwipeRefresh初始版本
