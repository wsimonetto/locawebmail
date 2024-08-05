package br.com.fiap.locamail.cardsview

import androidx.compose.runtime.Composable
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.locamail.R
import br.com.fiap.locamail.database.repository.EmailRepository
import br.com.fiap.locamail.model.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("MutableCollectionMutableState")
@Composable
fun CardMessageSentView(
    initialEmailId: Int,
    onCloseCard: () -> Unit,
) {
    val context = LocalContext.current
    val emailRepository = EmailRepository(context)

    var emailId by remember { mutableStateOf(initialEmailId) }
    var email by remember { mutableStateOf<Email?>(null) }
    var isLastEmailId by remember { mutableStateOf(false) }

    suspend fun loadEmail(emailId: Int, setEmailId: suspend (Int) -> Unit) {
        val loadedEmail = withContext(Dispatchers.IO) {
            emailRepository.buscarEmailEnvidadoPeloId(emailId)
        }
        if (loadedEmail != null) {
            setEmailId(emailId)
            email = loadedEmail
        } else {
            Log.d("CardMessageSentView", "Nenhum e-mail encontrado para o ID: $emailId")
        }
    }

    LaunchedEffect(emailId) {
        loadEmail(emailId) { updatedEmailId ->
            emailId = updatedEmailId
        }
        val isLast = emailRepository.isLastEmail(emailId)
        isLastEmailId = isLast
        Log.d("CardMessageSentView", "isLastEmailId: $isLastEmailId")
    }

    val coroutineScope = rememberCoroutineScope()

    val radioButtonVisible by remember { mutableStateOf(false) } // Estado para controlar a visibilidade do RadioButton

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {}
                )
            },
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(top = 10.dp, start = 15.dp, end = 15.dp, bottom = 20.dp)
                .fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                onCloseCard()
                            },
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_arrow_circle_left_24),
                                contentDescription = "Enviados",
                                modifier = Modifier.size(35.dp),
                                tint = colorResource(id = R.color.azul_claro)
                            )
                        }
                        Text(
                            text = "Enviados",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.azul_escuro),
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (!isLastEmailId) {

                                    coroutineScope.launch {
                                        try {
                                            val nextEmailId = withContext(Dispatchers.IO) {
                                                emailRepository.findNextAvailableEmailId(emailId)
                                            }
                                            if (nextEmailId != null) {
                                                Log.d(
                                                    "CardMessageSentView",
                                                    "Carregando email com ID: $nextEmailId"
                                                )
                                                loadEmail(nextEmailId) { updatedEmailId ->
                                                    emailId = updatedEmailId
                                                }
                                            } else {
                                                Log.d(
                                                    "CardMessageSentView",
                                                    "Nenhum próximo email encontrado"
                                                )
                                            }
                                        } catch (e: Exception) {
                                            Log.e(
                                                "CardMessageSentView",
                                                "Erro ao buscar próximo emailId",
                                                e
                                            )
                                        }
                                    }
                                }
                            },
                            enabled = !isLastEmailId
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_arrow_circle_left_24),
                                contentDescription = "Editar",
                                modifier = Modifier
                                    .size(35.dp)
                                    .graphicsLayer(rotationZ = 90f),
                                tint = colorResource(id = R.color.verde_claro)
                            )
                        }

                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        var prevEmailId = emailId - 1
                                        while (prevEmailId > 0 && withContext(Dispatchers.IO) {
                                                emailRepository.buscarEmailPeloId(
                                                    prevEmailId
                                                )
                                            } == null) {
                                            prevEmailId--
                                        }
                                        if (prevEmailId > 0) {
                                            Log.d(
                                                "CardMessageSentView",
                                                "Carregando email com ID: $prevEmailId"
                                            )
                                            loadEmail(prevEmailId) { updatedEmailId ->
                                                emailId = updatedEmailId
                                            }
                                        } else {
                                            Log.d(
                                                "CardMessageSentView",
                                                "Nenhum próximo email encontrado"
                                            )
                                        }
                                    } catch (e: Exception) {
                                        Log.e(
                                            "CardMessageSentView",
                                            "Erro ao buscar próximo emailId",
                                            e
                                        )
                                    }
                                }
                            },
                            enabled = (emailId > 1)  //true

                        ) {
                            Icon(
                                painter =
                                painterResource(id = R.drawable.baseline_arrow_circle_left_24),
                                contentDescription = "Editar",
                                modifier = Modifier
                                    .size(35.dp)
                                    .graphicsLayer(rotationZ = 270f),
                                tint = colorResource(id = R.color.verde_claro)
                            )
                        }

                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            email?.let { loadedEmail ->

                Text(
                    text = "De: ${email?.remetente}",
                    style = TextStyle(fontSize = 20.sp),
                    modifier = Modifier.padding(end = 8.dp),
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = "Para: ${email?.destinatario}",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(end = 8.dp),
                    color = Color.Black
                )

                Text(
                    text = "Responder A: ${email?.remetente}",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(end = 8.dp),
                    color = Color.Black
                )

                val timestamp = email?.dataEnvio
                val emailDate = timestamp?.let { Date(it) }

                if (emailDate != null) {
                    val dateFormat =
                        SimpleDateFormat("dd 'de' MMMM 'de' yyyy 'às' HH:mm", Locale("pt", "BR"))
                    val formattedDate = dateFormat.format(emailDate)

                    Text(
                        text = formattedDate,
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.padding(end = 8.dp),
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${email?.assunto}",
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.padding(end = 8.dp),
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${email?.mensagem}",
                style = TextStyle(fontSize = 18.sp),
                modifier = Modifier.padding(end = 8.dp),
                color = Color.Black,
                //fontWeight = FontWeight.Bold
            )

            // Exibir o ID do email com negrito
            /*Text(
                text = " Email ID: $emailId",
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.azul_escuro),
                fontSize = 14.sp
            )*/

        } /////////////////////////

        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxHeight()
        )
        {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .border(width = 0.9.dp, colorResource(id = R.color.canva_one))
                    .background(color = colorResource(id = R.color.canva_one))
            ) {
                IconButton(
                    onClick = {

                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.trash_send),
                        contentDescription = "Enviar para Lixeira",
                        modifier = Modifier
                            .size(35.dp),
                        tint = if (radioButtonVisible) colorResource(id = R.color.vermelho_escuro) else colorResource(
                            id = R.color.cinza
                        )
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(25.dp)
                        .width(1.dp)
                        .background(Color.Gray)
                )
                IconButton(
                    onClick = {

                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.archive),
                        contentDescription = "Enviar para Arquivo",
                        modifier = Modifier
                            .size(34.dp),
                        tint = if (radioButtonVisible) colorResource(id = R.color.azul_claro) else colorResource(
                            id = R.color.cinza
                        )
                    )
                }
            }
        }
    }

}