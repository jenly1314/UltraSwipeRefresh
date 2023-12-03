package com.king.ultraswiperefresh.indicator.classic

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.king.ultraswiperefresh.UltraSwipeRefreshState

/**
 * 经典样式的指示器
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
fun ClassicRefreshHeader(
    state: UltraSwipeRefreshState,
    modifier: Modifier = Modifier,
    tipContentStyle: TextStyle = TextStyle.Default.copy(
        fontSize = 15.sp,
        color = Color(0xFF666666)
    ),
    tipTimeStyle: TextStyle = TextStyle.Default.copy(
        fontSize = 12.sp,
        color = Color(0xFF999999)
    ),
    tipTimeVisible: Boolean = true,
    paddingValues: PaddingValues = PaddingValues(12.dp),
    arrowIconPainter: Painter = painterResource(id = R.drawable.usr_classic_arrow),
    loadingIconPainter: Painter = painterResource(id = R.drawable.usr_classic_refreshing),
    iconSize: Dp = 24.dp,
    iconColorFilter: ColorFilter? = null,
) {
    ClassicRefreshIndicator(
        state = state,
        isFooter = false,
        modifier = modifier,
        tipContentStyle = tipContentStyle,
        tipTimeStyle = tipTimeStyle,
        tipTimeVisible = tipTimeVisible,
        paddingValues = paddingValues,
        arrowIconPainter = arrowIconPainter,
        loadingIconPainter = loadingIconPainter,
        iconSize = iconSize,
        iconColorFilter = iconColorFilter,
    )
}
