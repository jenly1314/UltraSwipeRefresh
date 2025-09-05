package com.king.ultraswiperefresh.indicator.progress

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.king.ultraswiperefresh.UltraSwipeFooterState
import com.king.ultraswiperefresh.UltraSwipeHeaderState
import com.king.ultraswiperefresh.UltraSwipeRefreshState
import com.king.ultraswiperefresh.indicator.LinearProgressIndicator
import com.king.ultraswiperefresh.indicator.animationSpec

/**
 * 进度条样式的指示器
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
internal fun ProgressRefreshIndicator(
    state: UltraSwipeRefreshState,
    isFooter: Boolean,
    modifier: Modifier = Modifier,
    height: Dp = 60.dp,
    color: Color = Color(0xFF00CCFF),
    label: String = "Indicator"
) {
    val progress by remember(isFooter) {
        derivedStateOf {
            when {
                !isFooter && state.indicatorOffset > 0f -> {
                    state.indicatorOffset.div(
                        state.refreshTrigger.coerceAtLeast(1f)
                    ).coerceIn(0f, 1f)
                }

                isFooter && state.indicatorOffset < 0f -> {
                    state.indicatorOffset.div(
                        state.loadMoreTrigger.coerceAtMost(-1f)
                    ).coerceIn(0f, 1f)
                }

                else -> 0f
            }
        }
    }

    val brush by remember(isFooter) {
        derivedStateOf {
            if (isFooter) {
                Brush.verticalGradient(
                    0f to color.copy(alpha = 0f),
                    1f to color.copy(alpha = 0.5f)
                )
            } else {
                Brush.verticalGradient(
                    0f to color.copy(alpha = 0.5f),
                    1f to color.copy(alpha = 0f)
                )
            }
        }
    }

    val isInProgress by remember(isFooter) {
        derivedStateOf {
            if (isFooter) {
                state.footerState == UltraSwipeFooterState.Loading && !state.isFinishing
            } else {
                state.headerState == UltraSwipeHeaderState.Refreshing && !state.isFinishing
            }
        }
    }

    val alpha by animateFloatAsState(
        targetValue = if (progress > 0f) 1f else 0f,
        animationSpec = animationSpec
    )

    Box(modifier = modifier) {
        Box(
            Modifier
                .alpha(alpha)
                .fillMaxWidth()
                .height(height)
                .drawWithCache {
                    onDrawBehind {
                        if (state.isSwipeInProgress) {
                            drawRect(
                                brush = brush,
                                alpha = FastOutSlowInEasing.transform(progress)
                            )
                        }
                    }
                },
            contentAlignment = if (isFooter) Alignment.BottomCenter else Alignment.TopCenter
        ) {
            Crossfade(
                targetState = isInProgress,
                animationSpec = animationSpec,
                label = label
            ) {
                if (it) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = color
                    )
                } else {
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier.fillMaxWidth(),
                        color = color,
                    )
                }
            }
        }
    }
}
