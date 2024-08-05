package br.com.fiap.locamail.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import br.com.fiap.locamail.R
import androidx.compose.ui.layout.ContentScale
import br.com.fiap.locamail.component.canvas.Canvas
import br.com.fiap.locamail.component.canvas.ColorsScreen
import kotlinx.coroutines.delay

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun OpeningScreen(navController: NavController) {
    // Define as cores usadas na tela
    val colors = ColorsScreen()

    // Variável para controlar a visibilidade da imagem
    var isVisible by remember { mutableStateOf(true) }

    // Tela de abertura
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Desenha o fundo usando um Canvas
        Canvas(colors = colors).drawCanvas()

        // Animação de entrada da imagem
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(initialAlpha = 100f, animationSpec = tween(600)), // Efeito de fade-in
            exit = fadeOut(targetAlpha = 0f, animationSpec = tween(600)) // Efeito de fade-out
        ) {
            // Imagem de abertura
            Image(
                painter = painterResource(id = R.drawable.inicialize),
                contentDescription = "image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
            )
        }
    }

    // Inicia um efeito que executa uma única vez após app iniciado.
    LaunchedEffect(key1 = true) {
        delay(800) // Aguarda 800ms antes de exibir a imagem
        isVisible = true // Torna a imagem visível
        // Navega para a próxima tela após um atraso
        navController.navigate("menu") {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }
}
