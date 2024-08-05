package br.com.fiap.locamail

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.locamail.component.canvas.Canvas
import br.com.fiap.locamail.component.canvas.ColorsScreen
import br.com.fiap.locamail.screens.MenuScreen
import br.com.fiap.locamail.screens.OpeningScreen
import br.com.fiap.locamail.ui.theme.LocawebMailTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        setContent {

            LocawebMailTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(id = R.color.canva_one)
                ) {
                    val colors = ColorsScreen()
                    Canvas(colors = colors).drawCanvas()
                    val navController = rememberNavController()

                    val onBackPressedCallback = object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                        }
                    }
                    onBackPressedDispatcher.addCallback(onBackPressedCallback)

                    NavHost(
                        navController = navController,
                        startDestination = "opening",
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                                tween(100),
                            ) + fadeOut(animationSpec = tween(100))
                        },
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                tween(800),
                            ) + fadeIn(animationSpec = tween(800))
                        }
                    )
                    {
                        composable(route = "opening") {
                            OpeningScreen(navController)
                        }
                        composable(route = "menu") {
                            MenuScreen(navController)
                        }
                    }
                }
            }
        }
    }

}