package com.king.ultraswiperefresh.app.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.king.ultraswiperefresh.app.sample.ClassicRefreshIndicatorSample
import com.king.ultraswiperefresh.app.sample.CustomLottieRefreshIndicatorSample
import com.king.ultraswiperefresh.app.sample.LottieRefreshIndicatorSample
import com.king.ultraswiperefresh.app.sample.ProgressRefreshIndicatorSample
import com.king.ultraswiperefresh.app.sample.UltraSwipeRefreshSample
import com.king.ultraswiperefresh.app.sample.SwipeRefreshIndicatorSample
import com.king.ultraswiperefresh.app.sample.SwipeRefreshSample

/**
 * 导航图
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
fun NavGraphBuilder.noteNavGraph(navController: NavController) {

    navigation(
        startDestination = NavRoute.UltraSwipeRefreshSample.name,
        route = NavRoute.Root.name
    ) {
        composable(route = NavRoute.UltraSwipeRefreshSample.name) {
            UltraSwipeRefreshSample(navController)
        }
        composable(route = NavRoute.SwipeRefreshIndicatorSample.name) {
            SwipeRefreshIndicatorSample()
        }
        composable(route = NavRoute.ClassicRefreshIndicatorSample.name) {
            ClassicRefreshIndicatorSample()
        }
        composable(route = NavRoute.ProgressRefreshIndicatorSample.name) {
            ProgressRefreshIndicatorSample()
        }
        composable(route = NavRoute.LottieRefreshIndicatorSample.name) {
            LottieRefreshIndicatorSample()
        }
        composable(route = NavRoute.CustomLottieRefreshIndicatorSample.name) {
            CustomLottieRefreshIndicatorSample()
        }
        composable(route = NavRoute.SwipeRefreshSample.name) {
            SwipeRefreshSample()
        }
    }
}
