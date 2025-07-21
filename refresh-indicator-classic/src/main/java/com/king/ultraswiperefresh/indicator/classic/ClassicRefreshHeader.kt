package com.king.ultraswiperefresh.indicator.classic

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.king.ultraswiperefresh.UltraSwipeHeaderState
import com.king.ultraswiperefresh.UltraSwipeRefreshState
import java.text.SimpleDateFormat
import java.util.Locale

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
    tipContent: @Composable () -> String = {
        obtainHeaderTipContent(state)
    },
    tipTime: @Composable () -> String = {
        obtainLastRefreshTime(state)
    },
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
    loadingIconPainter: Painter = painterResource(id = R.drawable.usr_classic_spinner),
    tipMinWidth: Dp = 96.dp,
    iconSize: Dp = 24.dp,
    iconColorFilter: ColorFilter? = null,
) {
    ClassicRefreshIndicator(
        state = state,
        isFooter = false,
        tipContent = tipContent(),
        tipTime = tipTime(),
        modifier = modifier,
        tipContentStyle = tipContentStyle,
        tipTimeStyle = tipTimeStyle,
        tipTimeVisible = tipTimeVisible,
        paddingValues = paddingValues,
        arrowIconPainter = arrowIconPainter,
        loadingIconPainter = loadingIconPainter,
        tipMinWidth = tipMinWidth,
        iconSize = iconSize,
        iconColorFilter = iconColorFilter,
        label = "HeaderIndicator"
    )
}

/**
 * 根据[UltraSwipeRefreshState]获取提示内容
 */
@Composable
private fun obtainHeaderTipContent(state: UltraSwipeRefreshState): String {
    val textRes = when (state.headerState) {
        UltraSwipeHeaderState.PullDownToRefresh -> R.string.usr_pull_down_to_refresh
        UltraSwipeHeaderState.ReleaseToRefresh -> R.string.usr_release_to_refresh
        UltraSwipeHeaderState.Refreshing -> {
            if (state.isFinishing) {
                R.string.usr_refresh_completed
            } else {
                R.string.usr_refreshing
            }
        }
    }
    return stringResource(id = textRes)
}

/**
 * 获取上次刷新时间
 */
@Composable
private fun obtainLastRefreshTime(state: UltraSwipeRefreshState): String {
    var lastRefreshTime by remember("lastRefreshTime") {
        mutableLongStateOf(System.currentTimeMillis())
    }
    LaunchedEffect(state.headerState) {
        if (state.headerState == UltraSwipeHeaderState.Refreshing) {
            lastRefreshTime = System.currentTimeMillis()
        }
    }
    val context = LocalContext.current
    val dateFormat = remember {
        SimpleDateFormat(
            context.getString(R.string.usr_time_format_pattern),
            Locale.getDefault()
        )
    }
    return "${context.getString(R.string.usr_last_refresh_time)}${dateFormat.format(lastRefreshTime)}"
}
