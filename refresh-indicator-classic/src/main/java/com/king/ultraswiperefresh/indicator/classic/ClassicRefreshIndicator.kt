package com.king.ultraswiperefresh.indicator.classic

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.king.ultraswiperefresh.UltraSwipeFooterState
import com.king.ultraswiperefresh.UltraSwipeHeaderState
import com.king.ultraswiperefresh.UltraSwipeRefreshState
import com.king.ultraswiperefresh.indicator.CrossFadeDurationMs

/**
 * 经典样式的指示器
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
internal fun ClassicRefreshIndicator(
    state: UltraSwipeRefreshState,
    isFooter: Boolean,
    tipContent: String,
    tipTime: String,
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
    label: String = "Indicator"
) {
    val arrowDegrees = remember { Animatable(0f) }

    if (isFooter) {
        LaunchedEffect(state.footerState) {
            when (state.footerState) {
                UltraSwipeFooterState.ReleaseToLoad -> {
                    arrowDegrees.animateTo(0f)
                }

                else -> arrowDegrees.animateTo(180f)
            }
        }
    } else {
        LaunchedEffect(state.headerState) {
            when (state.headerState) {
                UltraSwipeHeaderState.ReleaseToRefresh -> {
                    arrowDegrees.animateTo(180f)
                }

                else -> arrowDegrees.animateTo(0f)
            }
        }
    }

    val alphaState = remember {
        derivedStateOf {
            if ((!isFooter && state.indicatorOffset > 0f) || (isFooter && state.indicatorOffset < 0f)) {
                1f
            } else {
                0f
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.alpha(alphaState.value),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Crossfade(
                targetState = if (isFooter) state.isLoading else state.isRefreshing,
                animationSpec = tween(durationMillis = CrossFadeDurationMs),
                label = label
            ) {
                if (it) {
                    val transition = rememberInfiniteTransition(label = "InfiniteTransition")
                    val rotate by transition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 1000,
                                easing = LinearEasing
                            )
                        ),
                        label = "RotateAnimation"
                    )
                    Image(
                        painter = loadingIconPainter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(iconSize)
                            .rotate(rotate),
                        colorFilter = iconColorFilter,
                    )

                } else {
                    Image(
                        painter = arrowIconPainter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(iconSize)
                            .rotate(arrowDegrees.value),
                        colorFilter = iconColorFilter,
                    )
                }
            }
            Column(
                modifier = Modifier.padding(horizontal = 6.dp),
                verticalArrangement = Arrangement.Center
            ) {
                BasicText(
                    text = tipContent,
                    style = tipContentStyle,
                )
                if (tipTimeVisible) {
                    Spacer(modifier = Modifier.size(2.dp))
                    BasicText(
                        text = tipTime,
                        style = tipTimeStyle,
                    )
                }
            }
        }
    }
}
