package com.king.ultraswiperefresh.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.king.ultraswiperefresh.app.navigation.NavRoute
import com.king.ultraswiperefresh.app.navigation.noteNavGraph
import com.king.ultraswiperefresh.app.ui.theme.RefreshLayoutTheme

/**
 * UltraSwipeRefresh 使用示例
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
private fun MainScreen() {
    RefreshLayoutTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
        ) {
            Column {
                TopAppBar(title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontSize = 18.sp,
                        color = Color.White
                    )
                })

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NavRoute.Root.name
                ) {
                    noteNavGraph(navController)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}