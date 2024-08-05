package br.com.fiap.locamail.component.canvas

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import br.com.fiap.locamail.R

@SuppressLint("ComposableNaming")
@Composable
fun ColorsScreen(): List<Color>{
    val color1 = colorResource(id = R.color.canva_three)
    val color2 = colorResource(id = R.color.canva_two)
    val color3 = colorResource(id = R.color.canva_one)

    return listOf(color1, color2, color3)
}