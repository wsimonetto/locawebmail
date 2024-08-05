package br.com.fiap.locamail.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.com.fiap.locamail.R

@Composable
fun PulsatingCircle() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    //val pulse by infiniteTransition.animateFloat(
    val color by infiniteTransition.animateColor(
        /*initialValue = 0.5f,
        targetValue = 1f,*/
        initialValue = colorResource(id = R.color.verde_claro),
        targetValue =colorResource(id = R.color.azul_escuro),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    Box(
        modifier = Modifier
            .size(100.dp)
            .border(5.dp, color, CircleShape)
            //.border(4.dp, colorResource(id = R.color.canva_one).copy(alpha = pulse), CircleShape)
            .clip(CircleShape)
            .background(colorResource(id = R.color.white))
    ) {
        Image(
            painter = painterResource(id = R.drawable.foto_usuario),
            contentDescription = "Imagem Perfil",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}
