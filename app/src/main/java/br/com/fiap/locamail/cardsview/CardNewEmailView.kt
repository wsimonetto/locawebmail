package br.com.fiap.locamail.cardsview

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.fiap.locamail.R
import br.com.fiap.locamail.component.email.EmailValidator
import br.com.fiap.locamail.database.repository.EmailRepository
import br.com.fiap.locamail.model.Email
import java.io.File
import java.util.Calendar

@SuppressLint("MutableCollectionMutableState")
@Composable
fun CardNewEmailView(
    initialEmailId: Int,
    onCloseCard: () -> Unit,
    cardVisible: Boolean,
) {
    //Obter o contexto
    val context = LocalContext.current
    val emailRepository = EmailRepository(context)

    // Variáveis verificação
    var assuntoMsg by remember { mutableStateOf("Nova Mensagem") }
    var assuntoPreenchido by remember { mutableStateOf(false) }
    //Variáveis campos do email
    var remetente by remember { mutableStateOf("rm99307@fiap.com.br") }
    var destinatario by remember { mutableStateOf("") }
    var destinatarioPreenchido by remember { mutableStateOf(false) }
    var copiaPara by remember { mutableStateOf("") }
    var assunto by remember { mutableStateOf("") }
    var mensagem by remember { mutableStateOf("") }
    // Obter instância do calendário atual
    val calendario = Calendar.getInstance()
    val dataEnvio: Long = calendario.timeInMillis
    val horaEnvio: Long = calendario.get(Calendar.HOUR_OF_DAY).toLong() // Convertendo para Long
    val dataRecebimento: Long = calendario.timeInMillis
    val horaRecebimento: Long = calendario.get(Calendar.HOUR_OF_DAY).toLong()
    var prioridade by remember { mutableStateOf("baixa") }
    val estadoResposta by remember { mutableStateOf(false) }
    val dataResposta: Long = calendario.timeInMillis
    val horaResposta: Long = calendario.get(Calendar.HOUR_OF_DAY).toLong()
    var lido by remember { mutableStateOf(false) }
    var lixeira by remember { mutableStateOf(false) }
    var arquivo by remember { mutableStateOf(false) }
    var spam by remember { mutableStateOf(false) }
    var rascunho by remember { mutableStateOf(false) }
    var importante by remember { mutableStateOf(false) }

    var isValidEmail by remember { mutableStateOf(false) }
    var emailErrorText by remember { mutableStateOf("") }

    //Arquivo anexo
    var nomeArquivo: String? by remember { mutableStateOf(null) }
    // Extrair apenas o nome do arquivo anexo
    nomeArquivo?.let { uri ->
        val file = File(uri)
        file.name
    }
    //Buscar arquivo
    val filePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            // Lidar com o arquivo selecionado aqui
            nomeArquivo = uri?.lastPathSegment // Obtém o nome do arquivo do URI
        }
    //Formatação
    var bold by remember { mutableStateOf(false) }
    var italic by remember { mutableStateOf(false) }
    var underline by remember { mutableStateOf(false) }

    //RadioButtons
    var isLowSelected by remember { mutableStateOf(true) }
    var isMediumSelected by remember { mutableStateOf(false) }
    var isHighSelected by remember { mutableStateOf(false) }

    var isDisplayDialog by remember {
        mutableStateOf(false)
    }
    var initialId by remember { mutableStateOf(initialEmailId) }

    LaunchedEffect(initialId) {
        if (initialId > 0) {
            val email = emailRepository.getEmailById(initialId)
            isValidEmail = true
            email?.let {
                remetente = it.remetente
                destinatario = it.destinatario
                copiaPara = it.copiaPara.toString()
                assuntoMsg = it.assunto
                assuntoPreenchido = true
                destinatarioPreenchido = true
                prioridade = it.prioridade
                mensagem = it.mensagem
                assunto = it.assunto
                lido = it.lido
                // outras variáveis
            } ?: run {
                Log.e("CardNewEmailView", "Email not found with ID $initialId")
            }
        }
    }

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
                .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (destinatarioPreenchido && assuntoPreenchido) {
                            IconButton(
                                onClick = {
                                    if (destinatarioPreenchido && assuntoPreenchido) {
                                        isDisplayDialog = true
                                    } else {
                                        onCloseCard()
                                    }
                                },
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_cancel_24),
                                    contentDescription = "Cancelar",
                                    modifier = Modifier.size(35.dp),
                                    tint = colorResource(id = R.color.vermelho_escuro)
                                )
                            }
                            Text(
                                text = "Cancelar",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge,
                                color = colorResource(id = R.color.azul_escuro),
                                fontSize = 14.sp
                            )
                            /*Text(
                                text = " - Id: ${initialEmailId}",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge,
                                color = colorResource(id = R.color.azul_escuro),
                                fontSize = 14.sp
                            )*/
                        } else {
                            /*Text(
                                text = " - Id: ${initialEmailId}",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge,
                                color = colorResource(id = R.color.azul_escuro),
                                fontSize = 14.sp
                            )*/
                            IconButton(
                                onClick = {
                                    onCloseCard()
                                },
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_arrow_circle_left_24),
                                    contentDescription = "Caixas",
                                    modifier = Modifier.size(35.dp),
                                    tint = colorResource(id = R.color.azul_claro)
                                )
                            }
                            Text(
                                text = "Caixas",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge,
                                color = colorResource(id = R.color.azul_escuro),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Enviar",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.azul_escuro),
                            fontSize = 14.sp
                        )

                        if (isDisplayDialog) {
                            Dialog(onDismissRequest = {
                                isDisplayDialog = false
                            }) {
                                Column(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(3))
                                        .size(300.dp)
                                        .background(colorResource(id = R.color.canva_one))
                                        .padding(25.dp)
                                ) {
                                    FilledTonalButton(
                                        onClick = {
                                            if (destinatarioPreenchido && !isValidEmail) {
                                                emailErrorText = "Endereço de e-mail inválido"
                                            } else {
                                                val email = Email(
                                                    remetente = remetente,
                                                    destinatario = destinatario,
                                                    copiaPara = copiaPara,
                                                    assunto = assunto,
                                                    mensagem = mensagem,
                                                    dataEnvio = dataEnvio,
                                                    horaEnvio = horaEnvio,
                                                    dataRecebimento = dataRecebimento,
                                                    horaRecebimento = horaRecebimento,
                                                    prioridade = prioridade,
                                                    estadoResposta = estadoResposta,
                                                    dataResposta = dataResposta,
                                                    horaResposta = horaResposta,
                                                    lido = false,
                                                    lixeira = lixeira,
                                                    arquivo = arquivo,
                                                    spam = spam,
                                                    rascunho = true,
                                                    importante = importante
                                                )
                                                if (initialId > 0) {
                                                    email.id = initialId.toLong()
                                                    emailRepository.excluir(email)
                                                } else {
                                                    emailRepository.salvar(email)
                                                }
                                                initialId = 0
                                                onCloseCard()
                                            }
                                            isDisplayDialog = false
                                            onCloseCard()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = colorResource(id = R.color.vermelho_escuro),
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 10.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.baseline_arrow_circle_right_24),
                                                contentDescription = "Apagar",
                                                modifier = Modifier.size(35.dp),
                                                tint = colorResource(id = R.color.white)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(text = "Apagar Rascunho")
                                        }
                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .weight(1f)
                                    )
                                    FilledTonalButton(
                                        onClick = {
                                            if (destinatarioPreenchido && !isValidEmail) {
                                                emailErrorText = "Endereço de e-mail inválido"
                                            } else {
                                                val email = Email(
                                                    remetente = remetente,
                                                    destinatario = destinatario,
                                                    copiaPara = copiaPara,
                                                    assunto = assunto,
                                                    mensagem = mensagem,
                                                    dataEnvio = dataEnvio,
                                                    horaEnvio = horaEnvio,
                                                    dataRecebimento = dataRecebimento,
                                                    horaRecebimento = horaRecebimento,
                                                    prioridade = prioridade,
                                                    estadoResposta = estadoResposta,
                                                    dataResposta = dataResposta,
                                                    horaResposta = horaResposta,
                                                    lido = false,
                                                    lixeira = lixeira,
                                                    arquivo = arquivo,
                                                    spam = spam,
                                                    rascunho = true,
                                                    importante = importante,
                                                )
                                                if (initialId > 0) {
                                                    email.id = initialId.toLong()
                                                    emailRepository.atualizar(email)
                                                } else {
                                                    emailRepository.salvar(email)
                                                }
                                                initialId = 0
                                                onCloseCard()
                                            }
                                            isDisplayDialog = false
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = colorResource(id = R.color.verde_claro)
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 10.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.baseline_arrow_circle_right_24),
                                                contentDescription = "Salvar",
                                                modifier = Modifier.size(35.dp),
                                                tint = colorResource(id = R.color.white)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(text = "Salvar Rascunho")
                                        }
                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .weight(1f)
                                    )

                                    FilledTonalButton(
                                        onClick = { isDisplayDialog = false },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = colorResource(id = R.color.azul_claro)
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 10.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.baseline_arrow_circle_right_24),
                                                contentDescription = "Continuar Editando",
                                                modifier = Modifier.size(35.dp),
                                                tint = colorResource(id = R.color.white)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Continuar Editando",
                                                color = colorResource(id = R.color.white)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        IconButton(
                            onClick = {
                                if (destinatarioPreenchido && !isValidEmail) {
                                    emailErrorText = "Endereço de e-mail inválido"
                                } else {
                                    val email = Email(
                                        remetente = remetente,
                                        destinatario = destinatario,
                                        copiaPara = copiaPara,
                                        assunto = assunto,
                                        mensagem = mensagem,
                                        dataEnvio = dataEnvio,
                                        horaEnvio = horaEnvio,
                                        dataRecebimento = dataRecebimento,
                                        horaRecebimento = horaRecebimento,
                                        prioridade = prioridade,
                                        estadoResposta = estadoResposta,
                                        dataResposta = dataResposta,
                                        horaResposta = horaResposta,
                                        lido = false,
                                        lixeira = lixeira,
                                        arquivo = arquivo,
                                        spam = spam,
                                        rascunho = rascunho,
                                        importante = importante,
                                    )
                                    if (initialId > 0) {
                                        email.id = initialId.toLong()
                                        emailRepository.atualizar(email)
                                    } else {
                                        emailRepository.salvar(email)
                                    }
                                    initialId = 0
                                    onCloseCard()
                                }
                            },
                            enabled = assuntoPreenchido && destinatarioPreenchido
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_arrow_circle_left_24),
                                contentDescription = "Enviar",
                                modifier = Modifier
                                    .size(35.dp)
                                    .graphicsLayer(rotationZ = 90f),
                                tint = if (assuntoPreenchido && destinatarioPreenchido) colorResource(
                                    id = R.color.verde_claro
                                ) else colorResource(
                                    id = R.color.cinza
                                )
                            )
                        }
                    }
                }
            }
            Text(
                text = assuntoMsg,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(id = R.color.azul_escuro),
                modifier = Modifier
                    .padding(bottom = 8.dp),
                fontSize = 24.sp
            )
            if (emailErrorText.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = colorResource(id = R.color.vermelho_escuro)),
                        contentAlignment = Alignment.Center,
                    )
                    {
                        Text(
                            text = emailErrorText,
                            color = colorResource(id = R.color.white),
                            modifier = Modifier
                                .padding(top = 3.dp, bottom = 3.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Para:",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(end = 8.dp),
                    color = Color.Gray
                )
                BasicTextField(
                    value = destinatario,
                    onValueChange = {
                        destinatario = it
                        destinatarioPreenchido = destinatario.isNotEmpty()
                        isValidEmail = EmailValidator.isValidEmail(destinatario)

                        if (destinatarioPreenchido && !isValidEmail) {
                            emailErrorText = "Endereço de e-mail inválido"
                        } else {
                            emailErrorText = ""
                        }
                    },

                    textStyle = TextStyle(fontSize = 16.sp),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 2.dp),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = "",
                                style = TextStyle(fontSize = 16.sp),
                                color = Color.Gray
                            )
                            innerTextField()
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    enabled = true
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.8.dp)
                    .background(Color.Gray)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "De:",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(end = 8.dp),
                    color = Color.Gray

                )
                BasicTextField(
                    value = remetente,
                    onValueChange = { remetente = it },
                    textStyle = TextStyle(fontSize = 16.sp),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 2.dp),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = remetente,
                                style = TextStyle(fontSize = 16.sp),
                                color = Color.Gray
                            )
                            innerTextField()
                        }
                    },
                    enabled = false
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.8.dp)
                    .background(Color.Gray)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cópia para:",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(end = 8.dp),
                    color = Color.Gray
                )
                BasicTextField(
                    value = copiaPara,
                    onValueChange = { copiaPara = it },
                    textStyle = TextStyle(fontSize = 16.sp),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 2.dp),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = "",
                                style = TextStyle(fontSize = 16.sp),
                                color = Color.Gray
                            )
                            innerTextField()
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    enabled = true
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.8.dp)
                    .background(Color.Gray)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Assunto:",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(end = 8.dp),
                    color = Color.Gray
                )
                BasicTextField(
                    value = assunto,
                    onValueChange = {
                        assunto = it
                        assuntoPreenchido = it.isNotEmpty()
                        assuntoMsg = if (it.isNotEmpty()) assunto else "Mensagem"
                    },
                    textStyle = TextStyle(fontSize = 14.sp),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 2.dp),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = "",
                                style = TextStyle(fontSize = 16.sp),
                                color = Color.Gray
                            )
                            innerTextField()
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    enabled = true
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.8.dp)
                    .background(Color.Gray)
            )
            // FORMATAÇÃO
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Formatação:",
                    style = TextStyle(fontSize = 16.sp),
                    color = Color.Gray
                )
                IconButton(
                    onClick = { bold = !bold },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_format_bold_24),
                        contentDescription = "Negrito",
                        modifier = Modifier
                            .padding(end = 15.dp),
                        tint = if (bold) Color.Black else Color.Gray // cor do ícone com base no estado
                    )
                }
                IconButton(
                    onClick = { italic = !italic },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_format_italic_24),
                        contentDescription = "Itálico",
                        modifier = Modifier
                            .padding(end = 15.dp),
                        tint = if (italic) Color.Black else Color.Gray
                    )
                }
                IconButton(
                    onClick = { underline = !underline },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_format_underlined_24),
                        contentDescription = "Sublinhado",
                        modifier = Modifier
                            .padding(end = 15.dp),
                        tint = if (underline) Color.Black else Color.Gray
                    )
                }
            }

            // RADIOBUTTONS
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Prioridade: ",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                RadioButton(
                    selected = isLowSelected,
                    onClick = {
                        isLowSelected = true
                        isMediumSelected = false
                        isHighSelected = false
                        prioridade = "baixa"
                    },
                    modifier = Modifier
                        .size(20.dp)
                        .padding(start = 20.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colorResource(id = R.color.verde_claro),
                        unselectedColor = Color.Gray
                    )
                )
                Text(
                    text = "Baixa",
                    modifier = Modifier
                        .padding(start = 16.dp, end = 4.dp),
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                RadioButton(
                    selected = isMediumSelected,
                    onClick = {
                        isLowSelected = false
                        isMediumSelected = true
                        isHighSelected = false
                        prioridade = "media"
                    },
                    modifier = Modifier
                        .size(20.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colorResource(id = R.color.laranja_escuro),
                        unselectedColor = Color.Gray
                    )
                )
                Text(
                    "Média",
                    modifier = Modifier
                        .padding(start = 6.dp, end = 4.dp),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                RadioButton(
                    selected = isHighSelected,
                    onClick = {
                        isLowSelected = false
                        isMediumSelected = false
                        isHighSelected = true
                        prioridade = "alta"
                    },
                    modifier = Modifier
                        .size(20.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colorResource(id = R.color.vermelho_escuro), // Vermelho escuro
                        unselectedColor = Color.Gray
                    )
                )
                Text(
                    "Alta",
                    modifier = Modifier
                        .padding(start = 6.dp, end = 4.dp),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Anexo:",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(end = 8.dp),
                    color = Color.Gray
                )
                nomeArquivo?.let { uri ->
                    val file = File(uri)

                    IconButton(
                        onClick = { nomeArquivo = null },
                        modifier = Modifier
                            .padding(start = 20.dp, end = 2.dp)
                    ) {
                        val iconPainter =
                            painterResource(id = R.drawable.baseline_attachment_24)
                        val iconTint =
                            colorResource(id = R.color.vermelho_escuro)

                        Icon(
                            painter = iconPainter,
                            contentDescription = "Remover arquivo",
                            tint = iconTint
                        )
                    }
                    Text(
                        text = file.name,
                        style = TextStyle(fontSize = 12.sp),
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } ?: run {
                    IconButton(
                        onClick = { filePickerLauncher.launch("*/*") },
                        modifier = Modifier
                            .padding(start = 20.dp, end = 2.dp)
                    ) {
                        val iconPainter =
                            painterResource(id = R.drawable.baseline_attachment_24)
                        val iconTint = Color.Gray

                        Icon(
                            painter = iconPainter,
                            contentDescription = "Anexar arquivo",
                            tint = iconTint
                        )
                    }
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.8.dp)
                    .background(Color.Gray)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mensagem:",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.padding(end = 8.dp),
                        color = Color.Gray
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = mensagem,
                        onValueChange = { mensagem = it },
                        textStyle = TextStyle(
                            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
                            fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
                            textDecoration = if (underline) TextDecoration.Underline else TextDecoration.None
                        ),
                        singleLine = false,
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 8.dp, bottom = 5.dp),
                        decorationBox = { innerTextField ->

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.TopStart
                            ) {
                                Text(
                                    text = " ",
                                    style = TextStyle(fontSize = 12.sp),
                                    color = Color.Gray
                                )
                                innerTextField()
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        ),
                        enabled = true
                    )
                }
            }
        }
    }

}