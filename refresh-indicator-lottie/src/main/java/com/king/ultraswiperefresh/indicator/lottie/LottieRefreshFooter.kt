package com.king.ultraswiperefresh.indicator.lottie

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.king.ultraswiperefresh.UltraSwipeRefreshState

/**
 * Lottie动画指示器
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
fun LottieRefreshFooter(
    state: UltraSwipeRefreshState,
    modifier: Modifier = Modifier,
    spec: LottieCompositionSpec = LottieCompositionSpec.RawRes(R.raw.usr_lottie_default_refreshing),
    height: Dp = 60.dp,
    alignment: Alignment = Alignment.Center,
    speed: Float = 1f,
    contentScale: ContentScale = ContentScale.Fit,
) {
    LottieRefreshIndicator(
        state = state,
        isFooter = true,
        spec = spec,
        modifier = modifier,
        height = height,
        alignment = alignment,
        speed = speed,
        contentScale = contentScale
    )
}