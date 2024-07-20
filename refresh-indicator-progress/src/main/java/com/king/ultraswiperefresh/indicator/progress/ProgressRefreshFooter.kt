package com.king.ultraswiperefresh.indicator.progress

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.king.ultraswiperefresh.UltraSwipeRefreshState

/**
 * 进度条样式的指示器
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
fun ProgressRefreshFooter(
    state: UltraSwipeRefreshState,
    modifier: Modifier = Modifier,
    height: Dp = 60.dp,
    color: Color = Color(0xFF00CCFF),
) {
    ProgressRefreshIndicator(
        state = state,
        isFooter = true,
        modifier = modifier,
        height = height,
        color = color,
        label = "FooterIndicator"
    )
}