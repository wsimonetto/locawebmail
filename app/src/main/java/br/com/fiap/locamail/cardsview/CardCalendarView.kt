package br.com.fiap.locamail.cardsview

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.locamail.R
import br.com.fiap.locamail.component.calendar.Calendar
import br.com.fiap.locamail.component.email.EmailList
import br.com.fiap.locamail.component.email.state.EmailListState
import br.com.fiap.locamail.database.repository.EmailRepository
import br.com.fiap.locamail.model.Email
import br.com.fiap.locamail.component.email.manager.EmailSelectionManager
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.todayAt
import kotlinx.datetime.toJavaInstant
import kotlinx.coroutines.launch

@Composable
fun CardCalendarView(
    onCloseCard: () -> Unit,
    cardVisible: Boolean,
    onEmailClicked: (Any?) -> Unit,
    resetItemsToCenter: MutableState<Boolean>,
    isItemBeingDragged: MutableState<Boolean>, // Estado compartilhado para o arrasto
) {
    val context = LocalContext.current

    val emailRepository = EmailRepository(context)
    val listaEmailState = remember { mutableStateOf(emptyList<Email>()) }
    val coroutineScope = rememberCoroutineScope()
    val currentDate by remember { mutableStateOf(Clock.System.todayAt(TimeZone.currentSystemDefault())) }
    var displayedDate by remember { mutableStateOf(currentDate) }

    // Função para carregar os e-mails para a data exibida
    fun loadEmailsForDisplayedDate() {
        val instant = displayedDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toJavaInstant()
        val epochMilliseconds = instant.toEpochMilli()
        coroutineScope.launch {
            val emailsPorData = emailRepository.buscarEmailsPorDataRecebimento(epochMilliseconds)
            listaEmailState.value = emailsPorData
        }
    }

    // Carregar os e-mails para a data exibida quando o CardCalendarView é criado
    LaunchedEffect(Unit) {
        loadEmailsForDisplayedDate()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onPress = {})
            },
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(top = 10.dp, start = 0.dp, end = 0.dp, bottom = 20.dp)
                .fillMaxWidth(),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            modifier = Modifier
                                .padding(start = 14.dp),
                            onClick = {
                                onCloseCard()
                            }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_arrow_circle_left_24),
                                contentDescription = "Caixas",

                                tint = colorResource(id = R.color.azul_claro),
                                modifier = Modifier
                                    .size(35.dp)
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
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Row(horizontalArrangement = Arrangement.End) {
                        Text(
                            text = "Calendário",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.azul_escuro),
                            modifier = Modifier.padding(bottom = 1.dp, end = 14.dp),
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(top = 40.dp)) {
            Calendar(

                initialDate = displayedDate,
                onDateSelected = { date ->
                    displayedDate = date
                    val localDateTime = date.atStartOfDayIn(TimeZone.currentSystemDefault())
                    val instant = localDateTime.toJavaInstant()
                    val epochMilliseconds = instant.toEpochMilli()

                    coroutineScope.launch {
                        Log.d("CardCalendarView", "Epoch milliseconds: $epochMilliseconds")
                        val emailsPorData =
                            emailRepository.buscarEmailsPorDataRecebimento(epochMilliseconds)
                        Log.d("CardCalendarView", "Emails encontrados: $emailsPorData")
                        listaEmailState.value = emailsPorData
                    }
                },
                onMonthChanged = { date ->
                    displayedDate = date
                    val localDateTime = date.atStartOfDayIn(TimeZone.currentSystemDefault())
                    val instant = localDateTime.toJavaInstant()
                    val epochMilliseconds = instant.toEpochMilli()

                    coroutineScope.launch {
                        Log.d("CardCalendarView", "Epoch milliseconds: $epochMilliseconds")
                        val emailsPorData =
                            emailRepository.buscarEmailsPorDataRecebimento(epochMilliseconds)
                        Log.d("CardCalendarView", "Emails encontrados: $emailsPorData")
                        listaEmailState.value = emailsPorData
                    }
                }
            )
            if (listaEmailState.value.isEmpty()) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, start = 15.dp, end = 15.dp)
                        .height(0.6.dp)
                        .background(Color.Gray)
                )

            } else {
                Spacer(
                    modifier = Modifier
                        .padding(top = 5.dp)
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
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                // Verificar se há email na lista
                if (listaEmailState.value.isEmpty()) {
                    Text(
                        text = "Não há emails para esta data.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 10.dp, end = 0.dp, bottom = 20.dp)
                    .fillMaxWidth(),
            ) {
                val listaEmailsLidos = remember { mutableStateOf<List<Email>>(emptyList()) }

                EmailList(
                    listaEmailState = listaEmailState,
                    atualizar = {
                        listaEmailState.value =
                            emailRepository.listarEmailsCaixaEntradaOrdenadosPorIdDecrescente()
                    },
                    checkboxVisible = false,
                    onEmailClicked = onEmailClicked,

                    onEmailLeftBoxOne = { email ->
                        emailRepository.atualizarEmailsNaLixeiraPush(email, true)
                        listaEmailState.value =
                            emailRepository.listarEmailsCaixaEntradaOrdenadosPorIdDecrescente()
                        EmailSelectionManager.setSelection(false)
                        EmailListState.clearEmailIds()
                    },

                    onEmailLeftBoxTwo = { email ->
                        emailRepository.atualizarEmailsNoSpamPush(email, true)
                        listaEmailState.value =
                            emailRepository.listarEmailsCaixaEntradaOrdenadosPorIdDecrescente()
                        EmailSelectionManager.setSelection(false)
                        EmailListState.clearEmailIds()
                    },

                    onClickRightBoxOne = { email ->
                        if (!email.lido) {
                            emailRepository.atualizarEmailsLidoPush(email, true)
                        } else {
                            emailRepository.atualizarEmailsLidoPush(email, false)
                        }
                        listaEmailState.value =
                            emailRepository.listarEmailsCaixaEntradaOrdenadosPorIdDecrescente()
                        EmailSelectionManager.setSelection(false)
                    },

                    onClickRightBoxTwo = { email ->
                        emailRepository.atualizarEmailsNoArquivoPush(email, true)
                        listaEmailState.value =
                            emailRepository.listarEmailsCaixaEntradaOrdenadosPorIdDecrescente()
                        EmailSelectionManager.setSelection(false)
                        EmailListState.clearEmailIds()
                    },

                    onClickRightBoxThree = { email ->
                        if (email.importante) {
                            emailRepository.atualizarEmailsImportantePush(email, false)
                        } else {
                            emailRepository.atualizarEmailsImportantePush(email, true)
                        }
                        listaEmailState.value =
                            emailRepository.listarEmailsCaixaEntradaOrdenadosPorIdDecrescente()
                        EmailSelectionManager.setSelection(false)
                        EmailListState.clearEmailIds()
                    },
                    currentView = EmailView.INBOX_CALENDAR,
                    isItemBeingDragged = isItemBeingDragged,
                    resetItemsToCenter = resetItemsToCenter,
                    listaEmailsLidos = listaEmailsLidos
                )
            }
        }
    }

}