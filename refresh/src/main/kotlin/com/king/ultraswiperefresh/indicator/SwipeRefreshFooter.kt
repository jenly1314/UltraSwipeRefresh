package com.king.ultraswiperefresh.indicator

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.king.ultraswiperefresh.UltraSwipeRefreshState

/**
 * google官方样式；具体实现参见：[SwipeRefreshIndicator]
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
fun SwipeRefreshFooter(
    state: UltraSwipeRefreshState,
    modifier: Modifier = Modifier,
    fade: Boolean = true,
    scale: Boolean = false,
    arrowEnabled: Boolean = true,
    backgroundColor: Color = Color.White,
    contentColor: Color = Color.Black,
    shape: Shape = CircleShape,
    paddingValues: PaddingValues = PaddingValues(12.dp),
    largeIndication: Boolean = false,
    elevation: Dp = 6.dp,
) {
    SwipeRefreshIndicator(
        state = state,
        isFooter = true,
        modifier = modifier,
        fade = fade,
        scale = scale,
        arrowEnabled = arrowEnabled,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        shape = shape,
        paddingValues = paddingValues,
        largeIndication = largeIndication,
        elevation = elevation,
        label = "FooterIndicator"
    )
}