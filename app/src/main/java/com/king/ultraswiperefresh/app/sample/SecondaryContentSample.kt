package com.king.ultraswiperefresh.app.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.king.ultraswiperefresh.NestedScrollMode
import com.king.ultraswiperefresh.SecondaryBehavior
import com.king.ultraswiperefresh.UltraSwipeFooterState
import com.king.ultraswiperefresh.UltraSwipeHeaderState
import com.king.ultraswiperefresh.UltraSwipeRefresh
import com.king.ultraswiperefresh.UltraSwipeRefreshState
import com.king.ultraswiperefresh.app.component.ColumnItem
import com.king.ultraswiperefresh.app.ext.showToast
import com.king.ultraswiperefresh.indicator.classic.ClassicRefreshFooter
import com.king.ultraswiperefresh.indicator.classic.ClassicRefreshHeader
import com.king.ultraswiperefresh.rememberUltraSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 二级内容示例：演示下拉进入Header二级内容和上拉进入Footer二级内容
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
fun SecondaryContentSample() {

    val state = rememberUltraSwipeRefreshState()
    var itemCount by remember { mutableIntStateOf(20) }
    var hasMoreData by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    var headerSecondaryBehavior by remember {
        mutableStateOf(SecondaryBehavior.Behind)
    }
    var footerSecondaryBehavior by remember {
        mutableStateOf(SecondaryBehavior.Behind)
    }

    var secondaryPreview by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(state.isFinishing) {
        if (itemCount >= 60 && !state.isFinishing) {
            hasMoreData = false
        }
    }

    val context = LocalContext.current

    UltraSwipeRefresh(
        state = state,
        onRefresh = {
            coroutineScope.launch {
                state.isRefreshing = true
                // TODO 刷新的逻辑处理，此处的延时只是为了演示效果
                delay(2000)
                itemCount = 20
                hasMoreData = true
                state.isRefreshing = false
            }
        },
        onLoadMore = {
            if (hasMoreData) {
                coroutineScope.launch {
                    state.isLoading = true
                    // TODO 加载更多的逻辑处理，此处的延时只是为了演示效果
                    delay(2000)
                    if (itemCount < 60) {
                        itemCount += 20
                    }
                    state.isLoading = false
                }
            }
        },
        modifier = Modifier.background(color = Color(0x7FEEEEEE)),
        headerScrollMode = NestedScrollMode.Translate,
        footerScrollMode = NestedScrollMode.Translate,
        loadMoreEnabled = hasMoreData,
        onCollapseScroll = {
            // 指示器收起时滚动列表位置，消除视觉回弹
            lazyListState.animateScrollBy(it)
        },
        headerIndicator = {
            ClassicRefreshHeader(it, secondaryPreview)
        },
        footerIndicator = {
            ClassicRefreshFooter(it, secondaryPreview)
        },
        // 启用Header二级功能
        headerSecondaryEnabled = true,
        headerSecondaryBehavior = headerSecondaryBehavior,
        headerSecondaryPreview = secondaryPreview, // 是否启用预览
        headerSecondaryContent = {
            // Header二级内容：类似淘宝二楼
            HeaderSecondaryContent(it)
        },
        // 启用Footer二级功能
        footerSecondaryEnabled = true,
        footerSecondaryBehavior = footerSecondaryBehavior,
        footerSecondaryPreview = secondaryPreview, // 是否启用预览
        footerSecondaryContent = {
            // Footer二级内容：地下室
            FooterSecondaryContent(it)
        }
    ) {
        LazyColumn(Modifier.background(color = Color.White), state = lazyListState) {
            item {
                ColumnItem(
                    title = "点击此项可动态改变二级内容相关配置参数，体验效果差异",
                    content = "当前所选的交互行为模式\n" +
                        "headerSecondaryBehavior = SecondaryBehavior.${headerSecondaryBehavior.name}\n" +
                        "footerSecondaryBehavior = SecondaryBehavior.${footerSecondaryBehavior.name}\n" +
                        "是否可预览二级内容\n" +
                        "headerSecondaryPreview = $secondaryPreview\n" +
                        "footerSecondaryPreview = $secondaryPreview"
                ) {
                    headerSecondaryBehavior = SecondaryBehavior.entries.random()
                    footerSecondaryBehavior = SecondaryBehavior.entries.random()
                    secondaryPreview = !secondaryPreview
                    context.showToast("二级内容行为交互模式已随机")
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color(0xFFF2F3F6)
                )
            }
            repeat(itemCount) {
                item {
                    val title = "UltraSwipeRefresh列表标题${it + 1}"
                    val content = "UltraSwipeRefresh列表内容${it + 1}"
                    ColumnItem(title = title, content = content)
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFF2F3F6)
                    )
                }
            }
        }
    }
}

@Composable
private fun ClassicRefreshHeader(
    state: UltraSwipeRefreshState,
    secondaryPreview: Boolean,
) {
    val tipContentStyle by remember(state.headerState, secondaryPreview) {
        derivedStateOf {
            when {
                secondaryPreview && state.headerState == UltraSwipeHeaderState.ReleaseToSecondary -> {
                    TextStyle.Default.copy(
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

                else -> TextStyle.Default.copy(
                    fontSize = 15.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }

    val iconColorFilter by remember(state.headerState, secondaryPreview) {
        derivedStateOf {
            when {
                secondaryPreview && state.headerState == UltraSwipeHeaderState.ReleaseToSecondary -> {
                    ColorFilter.tint(Color.White)
                }

                else -> null
            }
        }
    }
    ClassicRefreshHeader(
        state = state,
        tipContentStyle = tipContentStyle,
        tipTimeVisible = false,
        tipMinWidth = 110.dp,
        iconColorFilter = iconColorFilter,
    )
}

@Composable
private fun ClassicRefreshFooter(
    state: UltraSwipeRefreshState,
    secondaryPreview: Boolean,
) {
    val tipContentStyle by remember(state.footerState, secondaryPreview) {
        derivedStateOf {
            when {
                secondaryPreview && state.footerState == UltraSwipeFooterState.ReleaseToSecondary -> {
                    TextStyle.Default.copy(
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

                else -> TextStyle.Default.copy(
                    fontSize = 15.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }

    val iconColorFilter by remember(state.footerState, secondaryPreview) {
        derivedStateOf {
            when {
                secondaryPreview && state.footerState == UltraSwipeFooterState.ReleaseToSecondary -> {
                    ColorFilter.tint(Color.White)
                }

                else -> null
            }
        }
    }
    ClassicRefreshFooter(
        state = state,
        tipContentStyle = tipContentStyle,
        tipTimeVisible = false,
        tipMinWidth = 110.dp,
        iconColorFilter = iconColorFilter,
    )
}

@Composable
private fun HeaderSecondaryContent(state: UltraSwipeRefreshState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFF4ECDC4)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎉 欢迎来到二楼 🎉",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "这里是Header二级内容区域",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
            Button(
                onClick = {
                    state.closeSecondary()
                },
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Text("回到列表")
            }
        }
    }
}

@Composable
private fun FooterSecondaryContent(state: UltraSwipeRefreshState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFF0F96DC)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🏠 欢迎来到地下室 🏠",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "这里是Footer二级内容区域",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
            Button(
                onClick = {
                    state.closeSecondary()
                },
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Text("回到列表")
            }
        }
    }
}
