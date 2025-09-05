package com.king.ultraswiperefresh.indicator.lottie

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCancellationBehavior
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.king.ultraswiperefresh.UltraSwipeFooterState
import com.king.ultraswiperefresh.UltraSwipeHeaderState
import com.king.ultraswiperefresh.UltraSwipeRefreshState
import com.king.ultraswiperefresh.indicator.animationSpec

/**
 * Lottie动画指示器
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
internal fun LottieRefreshIndicator(
    state: UltraSwipeRefreshState,
    isFooter: Boolean,
    spec: LottieCompositionSpec,
    modifier: Modifier = Modifier,
    height: Dp = 60.dp,
    alignment: Alignment = Alignment.Center,
    speed: Float = 1f,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val composition by rememberLottieComposition(spec = spec)

    val isPlaying by remember(isFooter) {
        derivedStateOf {
            if (isFooter) {
                state.footerState == UltraSwipeFooterState.Loading && !state.isFinishing
            } else {
                state.headerState == UltraSwipeHeaderState.Refreshing && !state.isFinishing
            }
        }
    }

    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        restartOnPlay = true,
        speed = speed,
        iterations = LottieConstants.IterateForever,
        cancellationBehavior = LottieCancellationBehavior.OnIterationFinish,
    )

    val targetAlpha by remember(isFooter) {
        derivedStateOf {
            if ((!isFooter && state.indicatorOffset > 0f) || (isFooter && state.indicatorOffset < 0f)) {
                1f
            } else {
                0f
            }
        }
    }

    val alpha by animateFloatAsState(targetValue = targetAlpha, animationSpec = animationSpec)

    Box(
        modifier = Modifier
            .alpha(alpha)
            .fillMaxWidth()
            .height(height),
        contentAlignment = alignment,
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = modifier,
            contentScale = contentScale,
        )
    }
}
