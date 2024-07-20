/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.king.ultraswiperefresh.indicator

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.king.ultraswiperefresh.UltraSwipeFooterState
import com.king.ultraswiperefresh.UltraSwipeHeaderState
import com.king.ultraswiperefresh.UltraSwipeRefresh
import com.king.ultraswiperefresh.UltraSwipeRefreshState

/**
 * A class to encapsulate details of different indicator sizes.
 *
 * @param size The overall size of the indicator.
 * @param arcRadius The radius of the arc.
 * @param strokeWidth The width of the arc stroke.
 * @param arrowWidth The width of the arrow.
 * @param arrowHeight The height of the arrow.
 */
@Immutable
private data class SwipeIndicatorSizes(
    val size: Dp,
    val arcRadius: Dp,
    val strokeWidth: Dp,
    val arrowWidth: Dp,
    val arrowHeight: Dp,
)

/**
 * The default/normal size values for [SwipeRefreshIndicator].
 */
private val defaultSizes = SwipeIndicatorSizes(
    size = 40.dp,
    arcRadius = 7.5.dp,
    strokeWidth = 2.5.dp,
    arrowWidth = 10.dp,
    arrowHeight = 5.dp,
)

/**
 * The 'large' size values for [SwipeRefreshIndicator].
 */
private val largeSizes = SwipeIndicatorSizes(
    size = 56.dp,
    arcRadius = 11.dp,
    strokeWidth = 3.dp,
    arrowWidth = 12.dp,
    arrowHeight = 6.dp,
)

/**
 * 基于：google官方的：accompanist-swiperefresh 库中的 SwipeRefreshIndicator 修改而来。
 *
 * Indicator composable which is typically used in conjunction with [UltraSwipeRefresh].
 *
 * @param state The [UltraSwipeRefreshState] passed into the [UltraSwipeRefresh] `indicator` block.
 * @param modifier The modifier to apply to this layout.
 * @param fade Whether the arrow should fade in/out as it is scrolled in. Defaults to true.
 * @param scale Whether the indicator should scale up/down as it is scrolled in. Defaults to false.
 * @param arrowEnabled Whether an arrow should be drawn on the indicator. Defaults to true.
 * @param backgroundColor The color of the indicator background surface.
 * @param contentColor The color for the indicator's contents.
 * @param shape The shape of the indicator background surface. Defaults to [CircleShape].
 * @param largeIndication Whether the indicator should be 'large' or not. Defaults to false.
 * @param elevation The size of the shadow below the indicator.
 */
@Composable
internal fun SwipeRefreshIndicator(
    state: UltraSwipeRefreshState,
    isFooter: Boolean,
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
    label: String = "Indicator"
) {
    val showElevation by remember(isFooter, state) {
        derivedStateOf {
            if(isFooter) {
                state.isLoading || state.indicatorOffset < 0f
            } else {
                state.isRefreshing || state.indicatorOffset > 0f
            }
        }
    }
    val sizes = if (largeIndication) largeSizes else defaultSizes
    val indicatorHeight = with(LocalDensity.current) { sizes.size.roundToPx() }

    val slingshot = if (isFooter) {
        rememberUpdatedSlingshot(
            offsetY = -state.indicatorOffset,
            maxOffsetY = -state.loadMoreTrigger,
            height = indicatorHeight,
        )
    } else {
        rememberUpdatedSlingshot(
            offsetY = state.indicatorOffset,
            maxOffsetY = state.refreshTrigger,
            height = indicatorHeight,
        )
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
        Surface(
            modifier = Modifier
                .alpha(alphaState.value)
                .size(size = sizes.size)
                .graphicsLayer {
                    val scaleFraction = when {
                        !scale -> 1f
                        !isFooter && state.headerState != UltraSwipeHeaderState.Refreshing -> {
                            LinearOutSlowInEasing
                                .transform(
                                    state.indicatorOffset.div(state.refreshTrigger.coerceAtLeast(1f))
                                )
                                .coerceIn(0f, 1f)
                        }

                        isFooter && state.footerState != UltraSwipeFooterState.Loading -> {
                            LinearOutSlowInEasing
                                .transform(
                                    state.indicatorOffset.div(state.loadMoreTrigger.coerceAtMost(-1f))
                                )
                                .coerceIn(0f, 1f)
                        }

                        else -> 1f
                    }

                    scaleX = scaleFraction
                    scaleY = scaleFraction
                },
            shape = shape,
            color = backgroundColor,
            elevation = if(showElevation) elevation else 0.dp
        ) {
            val painter = remember { CircularProgressPainter() }
            painter.arcRadius = sizes.arcRadius
            painter.strokeWidth = sizes.strokeWidth
            painter.arrowWidth = sizes.arrowWidth
            painter.arrowHeight = sizes.arrowHeight
            painter.color = contentColor
            if (isFooter) {
                painter.arrowEnabled =
                    arrowEnabled && state.footerState != UltraSwipeFooterState.Loading
                val alpha = if (fade) {
                    (state.indicatorOffset / state.loadMoreTrigger).coerceIn(0f, 1f)
                } else {
                    1f
                }
                painter.alpha = alpha
            } else {
                painter.arrowEnabled =
                    arrowEnabled && state.headerState != UltraSwipeHeaderState.Refreshing
                val alpha = if (fade) {
                    (state.indicatorOffset / state.refreshTrigger).coerceIn(0f, 1f)
                } else {
                    1f
                }
                painter.alpha = alpha
            }

            painter.startTrim = slingshot.startTrim
            painter.endTrim = slingshot.endTrim
            painter.rotation = slingshot.rotation
            painter.arrowScale = slingshot.arrowScale

            // This shows either an Image with CircularProgressPainter or a CircularProgressIndicator,
            // depending on refresh state
            Crossfade(
                targetState = if (isFooter) {
                    state.footerState == UltraSwipeFooterState.Loading
                } else {
                    state.headerState == UltraSwipeHeaderState.Refreshing
                },
                animationSpec = tween(durationMillis = CrossFadeDurationMs),
                label = label
            ) { refreshing ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (refreshing) {
                        val circleSize = (sizes.arcRadius + sizes.strokeWidth) * 2
                        CircularProgressIndicator(
                            color = contentColor,
                            strokeWidth = sizes.strokeWidth,
                            modifier = Modifier.size(circleSize),
                        )
                    } else {
                        Image(painter = painter, contentDescription = "Indicator")
                    }
                }
            }
        }
    }
}

@Composable
internal fun Surface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = Color.White,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .surface(
                shape = shape,
                backgroundColor = color,
                border = border,
                elevation = elevation
            )
            .semantics(mergeDescendants = false) {}
            .pointerInput(Unit) {},
        propagateMinConstraints = true
    ) {
        content()
    }
}

private fun Modifier.surface(
    shape: Shape,
    backgroundColor: Color,
    border: BorderStroke?,
    elevation: Dp
) = this
    .shadow(elevation, shape, clip = false)
    .then(if (border != null) Modifier.border(border, shape) else Modifier)
    .background(color = backgroundColor, shape = shape)
    .clip(shape)

const val CrossFadeDurationMs = 100
