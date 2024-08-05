package br.com.fiap.locamail.component.email.card

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.locamail.R
import br.com.fiap.locamail.component.dateUtils.DateUtils
import br.com.fiap.locamail.component.email.manager.EmailSelectionManager
import br.com.fiap.locamail.model.Email

@SuppressLint("MutableCollectionMutableState")
@Composable
fun EmailCard(
    email: Email,
    isSelected: Boolean,
    onSelectionChange: (Int, Boolean) -> Unit,
    atualizar: () -> Unit,
    checkboxVisible: Boolean,
    onEmailClicked: () -> Unit, // Callback para quando um email for clicado
) {
    var isSelected by remember { mutableStateOf(false) }
    isSelected = EmailSelectionManager.getSelection()

    val colorCard = colorResource(id = R.color.white)

    Card(
        modifier = Modifier
            .clickable {
                if (!checkboxVisible) {
                    onEmailClicked()
                } else {
                    isSelected = !isSelected
                    onSelectionChange(email.id.toInt(), isSelected)
                }
                Log.d("EmailCard", "Card clicked - isSelected: $isSelected")
            }
            .fillMaxWidth()
            .height(90.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorCard
        ),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    if (checkboxVisible) {
                        CustomCircularCheckbox(
                            checked = isSelected,
                            onCheckedChange = {
                                isSelected = !isSelected
                                onSelectionChange(email.id.toInt(), isSelected)
                                Log.d("EmailCard", "Card clicked - isSelected: $isSelected")
                                Log.d(
                                    "EmailCard",
                                    "RadioButton clicked - Email ID: ${email.id}, isSelected: ${!isSelected}"
                                )
                            },
                        )
                    }
                }
                Row {
                    Column {
                    }
                    if (checkboxVisible) {
                        val mensagem = email.mensagem
                        val maxChars = 85
                        val mensagemLimitada =
                            if (mensagem.length > maxChars) {
                                mensagem.take(maxChars) + "...."
                            } else {
                                mensagem
                            }

                        Row() {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (!email.lido) {
                                    val circleColor = colorResource(id = R.color.azul_claro)
                                    Canvas(
                                        modifier = Modifier
                                            .padding(start = 12.dp, end = 6.dp, top = 13.dp)
                                    ) {
                                        drawCircle(
                                            color = circleColor,
                                            radius = 5.dp.toPx(),
                                        )
                                    }
                                } else {
                                    val circleColor = Color.Transparent
                                    Canvas(
                                        modifier = Modifier
                                            .padding(start = 12.dp, end = 6.dp, top = 13.dp)
                                    ) {
                                        drawCircle(
                                            color = circleColor,
                                            radius = 5.dp.toPx(),
                                        )
                                    }
                                }
                                Spacer (modifier = Modifier.height(8.dp))
                                if (email.importante) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_star_24),
                                        contentDescription = "Importante",
                                        modifier =
                                        Modifier
                                            .size(24.dp)
                                            .padding(start = 5.dp)
                                            .align(Alignment.CenterHorizontally),
                                        tint = colorResource(id = R.color.right2)
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_star_24),
                                        contentDescription = "Importante",
                                        modifier = Modifier.size(18.dp),
                                        tint = Color.Transparent
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .padding(start = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = email.remetente,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorResource(id = R.color.azul_escuro)
                                    )
                                    Spacer(modifier = Modifier.weight(1f))

                                    val formattedDate = DateUtils.formatEmailDate(email.dataEnvio)
                                    Text(
                                        text = formattedDate,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = colorResource(id = R.color.cinza),
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                                        contentDescription = "Cancelar",
                                        modifier = Modifier
                                            .size(15.dp)
                                            .padding(top = 2.dp),
                                        tint = colorResource(id = R.color.cinza)
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = email.assunto,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorResource(id = R.color.azul_escuro)
                                    )
                                    Spacer(modifier = Modifier.weight(1f))

                                    Text(
                                        text = "Prioridade",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = colorResource(id = R.color.cinza),
                                        modifier = Modifier
                                            .padding(end = 9.dp)
                                    )
                                    var circleColor = Color.Gray

                                    if (email.prioridade == "baixa") {
                                        circleColor =
                                            colorResource(id = R.color.verde_claro)
                                    } else if (email.prioridade == "media") {
                                        circleColor =
                                            colorResource(id = R.color.laranja_escuro)
                                    } else {
                                        circleColor =
                                            colorResource(id = R.color.vermelho_escuro)
                                    }

                                    Canvas(
                                        modifier = Modifier
                                            .padding(top = 2.dp, end = 14.dp)
                                    ) {
                                        drawCircle(
                                            color = circleColor,
                                            radius = 5.dp.toPx(),
                                        )
                                    }
                                }
                                Row {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 2.dp, end = 2.dp)
                                            .weight(2f)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .padding(end = 2.dp)
                                        ) {
                                            Text(
                                                text = mensagemLimitada,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        val mensagem = email.mensagem
                        val maxChars = 90
                        val mensagemLimitada =
                            if (mensagem.length > maxChars) {
                                mensagem.take(maxChars) + "...."
                            } else {
                                mensagem
                            }
                        Row() {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (!email.lido) {
                                    val circleColor = colorResource(id = R.color.azul_claro)
                                    Canvas(
                                        modifier = Modifier
                                            .padding(start = 2.dp, end = 6.dp, top = 13.dp)
                                            .align(Alignment.CenterHorizontally)
                                    ) {
                                        drawCircle(
                                            color = circleColor,
                                            radius = 5.dp.toPx(),
                                        )
                                    }
                                } else {
                                    val circleColor = Color.Transparent
                                    Canvas(
                                        modifier = Modifier
                                            .padding(start = 2.dp, end = 4.dp, top = 12.dp)
                                            .align(Alignment.CenterHorizontally),
                                    ) {
                                        drawCircle(
                                            color = circleColor,
                                            radius = 5.dp.toPx(),
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                if (email.importante) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_star_24),
                                        contentDescription = "Importante",
                                        modifier =
                                        Modifier
                                            .size(23.dp)
                                            .padding(end = 4.dp)
                                            .align(Alignment.CenterHorizontally),
                                        tint = colorResource(id = R.color.right2)
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_star_24),
                                        contentDescription = "Importante",
                                        modifier = Modifier.size(18.dp),
                                        tint = Color.Transparent
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .padding(start = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = email.remetente,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorResource(id = R.color.azul_escuro)
                                    )
                                    Spacer(modifier = Modifier.weight(1f))

                                    val formattedDate = DateUtils.formatEmailDate(email.dataEnvio)
                                    Text(
                                        text = formattedDate,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = colorResource(id = R.color.cinza),
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                                        contentDescription = "Cancelar",
                                        modifier = Modifier
                                            .size(15.dp)
                                            .padding(top = 2.dp),
                                        tint = colorResource(id = R.color.cinza)
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = email.assunto,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorResource(id = R.color.azul_escuro)
                                    )
                                    Spacer(modifier = Modifier.weight(1f))

                                    Text(
                                        text = "Prioridade",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = colorResource(id = R.color.cinza),
                                        modifier = Modifier
                                            .padding(end = 9.dp)
                                    )
                                    var circleColor = Color.Gray

                                    if (email.prioridade == "baixa") {
                                        circleColor =
                                            colorResource(id = R.color.verde_claro)
                                    } else if (email.prioridade == "media") {
                                        circleColor =
                                            colorResource(id = R.color.laranja_escuro)
                                    } else {
                                        circleColor =
                                            colorResource(id = R.color.vermelho_escuro)
                                    }

                                    Canvas(
                                        modifier = Modifier
                                            .padding(top = 2.dp, end = 14.dp)
                                    ) {
                                        drawCircle(
                                            color = circleColor,
                                            radius = 5.dp.toPx(),
                                        )
                                    }
                                }
                                Row {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 2.dp, end = 2.dp)
                                            .weight(2f)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .padding(end = 2.dp)
                                        ) {
                                            Text(
                                                text = mensagemLimitada,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }


                    }
                }
            }
        }
    }

}

@Composable
fun CustomCircularCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val size = 24.dp
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(if (checked) Color.Transparent else Color.Transparent)
            .clickable { onCheckedChange(!checked) }
            .border(
                width = 2.dp,
                color = colorResource(id = R.color.azul_claro),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                painter = painterResource(id = R.drawable.selectall),
                contentDescription = null,
                tint = colorResource(id = R.color.azul_claro),
                modifier = Modifier.size(size)
            )
        }
    }
}

