package br.com.fiap.locamail.component.dragAnimation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.locamail.R

@Composable
fun SaveActionLeftBoxOne(
    modifier: Modifier,
    icon: Painter,
    text: String
) {
    Box(
        modifier = modifier
            .background(colorResource(id = R.color.left1)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .padding(horizontal = 20.dp)
                    .size(22.dp),
                painter = icon,
                contentDescription = text,
                tint = Color.White
            )

            Text(
                text = text,
                color = Color.White,
                fontSize = 12.sp,
            )
        }
    }

}

@Composable
fun SaveActionLeftBoxTwo(
    modifier: Modifier,
    icon: Painter,
    text: String
) {
    Box(
        modifier = modifier.background(colorResource(id = R.color.left2)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .padding(horizontal = 20.dp)
                    .size(22.dp),
                painter = icon,
                contentDescription = text,
                tint = Color.White
            )

            Text(
                text = text,
                color = Color.White,
                fontSize = 12.sp,
            )
        }
    }

}

@Composable
fun SaveActionRightOne(
    modifier: Modifier,
    icon: Painter,
    text: String
) {
    Box(
        modifier = modifier.background(colorResource(id = R.color.right1)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .padding(horizontal = 20.dp)
                    .size(25.dp),
                painter = icon,
                contentDescription = text,
                tint = Color.White
            )

            Text(
                text = text,
                color = Color.White,
                fontSize = 12.sp,
            )
        }
    }

}

@Composable
fun SaveActionRightTwo(
    modifier: Modifier,
    icon: Painter,
    text: String
) {
    Box(
        modifier = modifier.background(colorResource(id = R.color.right3)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .padding(top = 15.dp, bottom = 4.dp)
                    .padding(horizontal = 20.dp)
                    .size(22.dp),
                painter = icon,
                contentDescription = text,
                tint = Color.White
            )

            Text(
                text = text,
                color = Color.White,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
fun SaveActionRightThree(
    modifier: Modifier,
    icon: Painter,
    text: String
) {
    Box(
        modifier = modifier.background(colorResource(id = R.color.right2)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .padding(horizontal = 20.dp)
                    .size(25.dp),
                painter = icon,
                contentDescription = text,
                tint = Color.White
            )

            Text(
                text = text,
                color = Color.White,
                fontSize = 12.sp,
            )
        }
    }
}