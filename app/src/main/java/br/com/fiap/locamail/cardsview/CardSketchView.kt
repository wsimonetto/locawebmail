package br.com.fiap.locamail.cardsview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.locamail.R
import br.com.fiap.locamail.component.email.EmailList
import br.com.fiap.locamail.component.email.state.EmailListState
import br.com.fiap.locamail.component.email.manager.EmailSelectionManager
import br.com.fiap.locamail.database.repository.EmailRepository
import br.com.fiap.locamail.model.Email

@Composable
fun CardSketchView(
    onCloseCard: () -> Unit,
    cardVisible: Boolean,
    onEmailClicked: (Any?) -> Unit, // Callback para quando um email for clicado
    resetItemsToCenter: MutableState<Boolean>,
    isItemBeingDragged: MutableState<Boolean>, // Estado compartilhado para o arrasto
) {
    val context = LocalContext.current
    val emailRepository = remember { EmailRepository(context) }
    val listaEmailState =
        remember { mutableStateOf(emailRepository.listarEmailsNoRascunhoOrdenadosPorIdDecrescente()) }
    var checkboxVisible by remember { mutableStateOf(false) }
    var selectedEmail by remember { mutableStateOf<Email?>(null) }

    // Função chamada quando um email é selecionado na lista
    val onEmailSelected: (Email) -> Unit = { email ->
        selectedEmail = email // Atribuir o email selecionado à variável selectedEmail
    }

    var search by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Email>>(emptyList()) }

    LaunchedEffect(Unit) {
        listaEmailState.value = emailRepository.listarEmailsNoRascunhoOrdenadosPorIdDecrescente()
    }
    LaunchedEffect(search) {
        if (search.isNotBlank()) {
            val results = emailRepository.buscarEmails(search)
            listaEmailState.value = results
        } else {
            listaEmailState.value =
                emailRepository.listarEmailsNoRascunhoOrdenadosPorIdDecrescente()
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
                .padding(top = 10.dp, start = 0.dp, end = 0.dp, bottom = 20.dp)
                .fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (checkboxVisible) {
                            Text(
                                text = "Selecione",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge,
                                color = colorResource(id = R.color.azul_escuro),
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .padding(start = 14.dp),
                            )
                        } else {
                            IconButton(
                                modifier = Modifier
                                    .padding(start = 14.dp),
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
                        if (!checkboxVisible) {
                            Text(
                                text = "Editar",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge,
                                color = colorResource(id = R.color.azul_escuro),
                                fontSize = 14.sp,
                            )
                            IconButton(
                                modifier = Modifier
                                    .padding(end = 14.dp),
                                onClick = {
                                    checkboxVisible = !checkboxVisible
                                    EmailListState.clearEmailIds()
                                    EmailSelectionManager.setSelection(false)
                                    resetItemsToCenter.value = true
                                    isItemBeingDragged.value = true
                                },
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_edit_24),
                                    contentDescription = "Editar",
                                    modifier = Modifier.size(35.dp),
                                    tint = colorResource(id = R.color.verde_claro)
                                )
                            }
                        } else {
                            Text(
                                text = "Cancelar",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge,
                                color = colorResource(id = R.color.azul_escuro),
                                fontSize = 14.sp,
                            )
                            IconButton(
                                modifier = Modifier
                                    .padding(end = 14.dp),
                                onClick = {
                                    checkboxVisible = !checkboxVisible
                                    EmailListState.clearEmailIds()
                                    EmailSelectionManager.setSelection(false)
                                    resetItemsToCenter.value = false
                                    isItemBeingDragged.value = false
                                },
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_edit_24),
                                    contentDescription = "Editar",
                                    modifier = Modifier.size(35.dp),
                                    tint = colorResource(id = R.color.vermelho_escuro)
                                )
                            }
                        }
                    }
                }
            }

            Text(
                text = "Rascunhos",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(id = R.color.azul_escuro),
                modifier = Modifier
                    .padding(bottom = 8.dp, start = 14.dp),
                fontSize = 24.sp
            )
            Box(
                modifier = Modifier
                    .padding(start = 14.dp, end = 14.dp)
                    .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(4.dp))
                    .height(35.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BasicTextField(
                        value = search,
                        onValueChange = { newSearch ->
                            search = newSearch
                            searchResults = if (newSearch.isNotBlank()) {

                                listaEmailState.value.filter { email ->
                                    email.assunto.contains(newSearch, ignoreCase = true)
                                }
                            } else {
                                listaEmailState.value
                            }
                            listaEmailState.value = if (searchResults.isNotEmpty()) {
                                searchResults
                            } else {
                                emailRepository.listarEmailsNoRascunhoOrdenadosPorIdDecrescente()
                            }
                        },
                        textStyle = TextStyle(fontSize = 12.sp),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp, top = 4.dp, bottom = 4.dp),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (search.isEmpty()) {
                                    Text(
                                        text = "Digite sua pesquisa aqui pelo assunto...",
                                        style = TextStyle(fontSize = 12.sp),
                                        color = Color.Gray
                                    )
                                }
                                innerTextField()
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        enabled = true
                    )
                    IconButton(
                        onClick = {},
                        modifier = Modifier.align(Alignment.CenterVertically),
                        enabled = false
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_search_24),
                            contentDescription = "Cancelar",
                            modifier = Modifier.size(25.dp),
                            tint = Color.Gray
                        )
                    }
                }
            }
            Spacer(
                modifier = Modifier
                    .padding(top = 10.dp)
            )
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp
            val isScreen = screenWidth >= 448
            val line = if (isScreen) 385.dp else if (screenWidth >= 412) 365.dp else 345.dp
            Spacer(
                modifier = Modifier
                    .width(line)
                    .height(0.6.dp)
                    .background(colorResource(id = R.color.cinza_claro))
                    .padding(top = 10.dp, start = 10.dp)
                    .align(Alignment.End)
            )
            if (listaEmailState.value.isEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Sem rascunhos para exibir",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorResource(id = R.color.cinza_claro),
                        modifier = Modifier
                            .padding(top = 20.dp)
                    )
                }
            }
            val listaEmailsLidos = remember { mutableStateOf<List<Email>>(emptyList()) }

            EmailList(
                listaEmailState = if (search.isNotBlank()) {
                    remember { mutableStateOf(searchResults) }
                } else {
                    listaEmailState
                },
                atualizar = {
                    listaEmailState.value =
                        emailRepository.listarEmailsNoRascunhoOrdenadosPorIdDecrescente()
                },
                checkboxVisible = checkboxVisible,
                onEmailClicked = onEmailClicked,
                onEmailLeftBoxOne = { email ->
                    emailRepository.excluir(email)
                    listaEmailState.value =
                        emailRepository.listarEmailsNoRascunhoOrdenadosPorIdDecrescente()
                    checkboxVisible = false
                    EmailSelectionManager.setSelection(false)
                    EmailListState.clearEmailIds()
                },
                onEmailLeftBoxTwo = onEmailClicked,
                onClickRightBoxOne = { },
                onClickRightBoxTwo = { },
                onClickRightBoxThree = { },
                currentView = EmailView.INBOX_SKETCH,
                isItemBeingDragged = isItemBeingDragged,
                resetItemsToCenter = resetItemsToCenter,
                listaEmailsLidos = listaEmailsLidos
            )
        }

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
                        val selectedIds = EmailListState.getEmailIds()
                        emailRepository.excluirEmailsPorIds(selectedIds)
                        listaEmailState.value =
                            emailRepository.listarEmailsNoRascunhoOrdenadosPorIdDecrescente()
                        checkboxVisible = false
                        EmailSelectionManager.setSelection(false)
                        EmailListState.clearEmailIds()
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.trash_delete),
                        contentDescription = "Deletar da Lixeira",
                        modifier = Modifier
                            .size(35.dp),
                        tint = if (checkboxVisible) colorResource(id = R.color.vermelho_escuro) else colorResource(
                            id = R.color.cinza
                        )
                    )
                }
            }
        }
    }

}