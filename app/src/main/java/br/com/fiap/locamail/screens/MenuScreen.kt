package br.com.fiap.locamail.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.locamail.cardsview.CardInboxView
import br.com.fiap.locamail.R
import br.com.fiap.locamail.cardsview.CardArchiveView
import br.com.fiap.locamail.cardsview.CardCalendarView
import br.com.fiap.locamail.cardsview.CardMessageArchiveView
import br.com.fiap.locamail.cardsview.CardMessageCalendarView
import br.com.fiap.locamail.cardsview.CardMessageSentView
import br.com.fiap.locamail.cardsview.CardMessageSpamView
import br.com.fiap.locamail.cardsview.CardMessageTrashView
import br.com.fiap.locamail.cardsview.CardMessageInboxView
import br.com.fiap.locamail.cardsview.CardNewEmailView
import br.com.fiap.locamail.cardsview.CardSentView
import br.com.fiap.locamail.cardsview.CardSketchView
import br.com.fiap.locamail.cardsview.CardSpamView
import br.com.fiap.locamail.cardsview.CardToRespondEmailView
import br.com.fiap.locamail.cardsview.CardTrashView
import br.com.fiap.locamail.cardsview.EmailView
import br.com.fiap.locamail.component.canvas.Canvas
import br.com.fiap.locamail.component.canvas.ColorsScreen
import br.com.fiap.locamail.component.PulsatingCircle
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun MenuScreen(
    navController: NavController,
) {
    // canvas variable
    val colors = ColorsScreen()

    // calendar variables
    val today = LocalDate.now()
    val dayOfWeek = today.format(DateTimeFormatter.ofPattern("EEEE", Locale("pt", "BR")))
    val dayOfMonth = today.format(DateTimeFormatter.ofPattern("dd", Locale("pt", "BR")))

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val cardSize = if (screenWidthDp > 600) screenWidthDp / 3 else screenWidthDp / 2

    // Variáveis Controle Visible
    var cardVisibleInboxEmail by remember { mutableStateOf(false) }
    var cardVisibleNewEmail by remember { mutableStateOf(false) }
    var cardVisibleSketch by remember { mutableStateOf(false) }
    var cardVisibleSent by remember { mutableStateOf(false) }
    var cardVisibleTrash by remember { mutableStateOf(false) }
    var cardVisibleSpam by remember { mutableStateOf(false) }
    var cardVisibleArchive by remember { mutableStateOf(false) }
    var cardVisibleCalendar by remember { mutableStateOf(false) }

    // Variáveis Controle estado Atual
    var currentInboxEmailView by remember { mutableStateOf(EmailView.INBOX_MAIL) }
    var currentTrashView by remember { mutableStateOf(EmailView.INBOX_TRASH) }
    var currentSpamView by remember { mutableStateOf(EmailView.INBOX_SPAM) }
    var currentSketchView by remember { mutableStateOf(EmailView.INBOX_SKETCH) }
    var currentSentView by remember { mutableStateOf(EmailView.INBOX_SENT) }
    var currentArchiveView by remember { mutableStateOf(EmailView.INBOX_ARCHIVE) }
    var currentCalendarView by remember { mutableStateOf(EmailView.INBOX_CALENDAR) }

    val resetItemsToCenter = remember { mutableStateOf(false) }
    val isItemBeingDragged = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {

        Canvas(colors = colors).drawCanvas()
        Image(
            painter = painterResource(id = R.drawable.inicialize),
            contentDescription = "Boot Image",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )

        Column(
            modifier = Modifier
                .padding(top = 25.dp, start = 25.dp, end = 25.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // FIRST CARD
            Card(
                modifier = Modifier
                    .clickable { cardVisibleInboxEmail = true }
                    .fillMaxWidth()
                    .size(180.dp)
                    .shadow(5.dp, shape = RoundedCornerShape(8.dp)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)),
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = "Caixa de entrada",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.azul_escuro),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        //FOTO PERFIL
                        Column (
                            modifier = Modifier
                                .padding(start = 35.dp, top = 5.dp)
                        ){
                           PulsatingCircle()
                        }
                    }
                    Card(
                        modifier = Modifier
                            .clickable { cardVisibleCalendar = true }
                            .size(155.dp)
                            .shadow(8.dp, shape = RoundedCornerShape(8.dp))
                            .padding(top = 6.dp, end = 6.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(id = R.color.canva_one)
                        ),
                    ) {
                        Text(
                            text = "Calendário",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.white),
                            modifier = Modifier
                                .padding(top = 8.dp, start = 8.dp)
                        )
                        Column(
                            modifier = Modifier
                                .padding(top = 8.dp, start = 8.dp),
                        )
                        {
                            Text(
                                text = dayOfWeek,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier
                                    .padding(bottom = 4.dp),
                                textAlign = TextAlign.Center,
                                color = colorResource(id = R.color.white)
                            )
                            Text(
                                text = dayOfMonth,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                fontSize = 50.sp,
                                modifier = Modifier
                                    .padding(bottom = 20.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                color = colorResource(id = R.color.verde_claro)
                            )
                        }
                    }
                }
            }

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // SECOND CARD
                    Card(
                        modifier = Modifier
                            .clickable { /*navController.navigate("novoEmail")*/
                                cardVisibleNewEmail = true
                            }
                            .size(cardSize.dp / 6 * 5)
                            .shadow(5.dp, shape = RoundedCornerShape(8.dp)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))
                    ) {
                        Text(
                            text = "E-mail",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.azul_escuro),
                            modifier = Modifier.padding(top = 8.dp, start = 8.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.novo_email),
                                contentDescription = "Novo E-mail",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(bottom = 10.dp, start = 5.dp),
                                tint = colorResource(id = R.color.azul_escuro)
                            )
                        }
                    }
                    // THIRD CARD
                    Card(
                        modifier = Modifier
                            .clickable { cardVisibleSketch = true }
                            .size(cardSize.dp / 6 * 5)
                            .shadow(5.dp, shape = RoundedCornerShape(8.dp)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))
                    ) {
                        Text(
                            text = "Rascunhos",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.azul_escuro),
                            modifier = Modifier.padding(top = 8.dp, start = 8.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.rascunho),
                                contentDescription = "Rascunhos",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(bottom = 18.dp, start = 5.dp),
                                tint = colorResource(id = R.color.azul_escuro)
                            )
                        }
                    }
                }
            }

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // FOURTH CARD
                    Card(
                        modifier = Modifier
                            .clickable { cardVisibleSent = true }
                            .size(cardSize.dp / 6 * 5)
                            .shadow(5.dp, shape = RoundedCornerShape(8.dp)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))
                    ) {
                        Text(
                            text = "Enviados",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.azul_escuro),
                            modifier = Modifier.padding(top = 8.dp, start = 8.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.enviados),
                                contentDescription = "Enviados",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(bottom = 10.dp, start = 5.dp),
                                tint = colorResource(id = R.color.azul_escuro)
                            )
                        }
                    }
                    // FIFTH CARD
                    Card(
                        modifier = Modifier
                            .clickable { cardVisibleTrash = true }
                            .size(cardSize.dp / 6 * 5)
                            .shadow(5.dp, shape = RoundedCornerShape(8.dp)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))
                    ) {
                        Text(
                            text = "Lixeira",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.azul_escuro),
                            modifier = Modifier.padding(
                                top = 8.dp, start = 8.dp
                            )
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.lixeira),
                                contentDescription = "Lixeira",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(bottom = 10.dp, start = 5.dp),
                                tint = colorResource(id = R.color.azul_escuro)
                            )
                        }
                    }
                }
            }
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // SIXTH CARD
                    Card(
                        modifier = Modifier
                            .clickable { cardVisibleSpam = true }
                            .size(cardSize.dp / 6 * 5)
                            .shadow(5.dp, shape = RoundedCornerShape(8.dp)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))
                    ) {
                        Text(
                            text = "Spam",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.azul_escuro),
                            modifier = Modifier
                                .padding(top = 8.dp, start = 8.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.spam),
                                contentDescription = "Spam",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(bottom = 10.dp, start = 5.dp),
                                tint = colorResource(id = R.color.azul_escuro)
                            )
                        }
                    }
                    // SEVENTH CARD
                    Card(
                        modifier = Modifier
                            .clickable { cardVisibleArchive = true }
                            .size(cardSize.dp / 6 * 5)
                            .shadow(5.dp, shape = RoundedCornerShape(8.dp)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))
                    ) {
                        Text(
                            text = "Arquivo",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.azul_escuro),
                            modifier = Modifier.padding(top = 8.dp, start = 8.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.arquivo),
                                contentDescription = "Spam",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(bottom = 10.dp, start = 5.dp),
                                tint = colorResource(id = R.color.azul_escuro)
                            )
                        }
                    }
                }
            }
        }

        // Funções para alternar entre as visualizações
        fun showInboxEmailView(emailView: EmailView) {
            currentInboxEmailView = emailView
        }

        fun showtrashView(emailView: EmailView) {
            currentTrashView = emailView
        }

        fun showSpamView(emailView: EmailView) {
            currentSpamView = emailView
        }

        fun showSketchView(emailView: EmailView) {
            currentSketchView = emailView
        }

        fun showSentView(emailView: EmailView) {
            currentSentView = emailView
        }

        fun showArchiveView(emailView: EmailView) {
            currentArchiveView = emailView
        }

        fun showCalendarView(emailView: EmailView) {
            currentCalendarView = emailView
        }

        var selectedEmailId by remember { mutableStateOf<Int?>(null) }

        fun showInboxEmailView(view: EmailView, emailId: Any? = null) {
            currentInboxEmailView = view
            selectedEmailId = emailId as Int?
        }

        fun showTrashView(view: EmailView, emailId: Any? = null) {
            currentTrashView = view
            selectedEmailId = emailId as Int?
        }

        fun showSpamView(view: EmailView, emailId: Any? = null) {
            currentSpamView = view
            selectedEmailId = emailId as Int?
        }

        fun showSketchView(view: EmailView, emailId: Any? = null) {
            currentSketchView = view
            selectedEmailId = emailId as Int?
        }

        fun showSentView(view: EmailView, emailId: Any? = null) {
            currentSentView = view
            selectedEmailId = emailId as Int?
        }

        fun showArchiveView(view: EmailView, emailId: Any? = null) {
            currentArchiveView = view
            selectedEmailId = emailId as Int?
        }

        fun showCalendarView(view: EmailView, emailId: Any? = null) {
            currentCalendarView = view
            selectedEmailId = emailId as Int?
        }

        // GERENCIAMENTO DAS VIEWS
        when (currentInboxEmailView) {

            EmailView.INBOX_MAIL -> {
                CustomCard(
                    isVisible = cardVisibleInboxEmail,
                    onClose = { cardVisibleInboxEmail = false }
                ) {
                    CardInboxView(
                        cardVisible = cardVisibleInboxEmail,
                        onCloseCard = { cardVisibleInboxEmail = false },
                        onEmailClicked = { emailId ->
                            // Quando um e-mail é clicado, exibe a view de mensagem passando o emailId
                            showInboxEmailView(EmailView.MESSAGE, emailId)
                        },
                        resetItemsToCenter = resetItemsToCenter,
                        isItemBeingDragged = isItemBeingDragged
                    )
                }
            }

            EmailView.MESSAGE -> {
                CustomCard(
                    isVisible = currentInboxEmailView == EmailView.MESSAGE,
                    onClose = { showInboxEmailView(EmailView.INBOX_MAIL) }
                ) {
                    CardMessageInboxView(
                        initialEmailId = selectedEmailId ?: error("No email selected"),
                        onCloseCard = {
                            showInboxEmailView(EmailView.INBOX_MAIL)
                        },
                        cardVisible = true,
                        onRespondClick = { emailId ->
                            showInboxEmailView(EmailView.INBOX_RESPOND, emailId)
                        }
                    )
                }
            }

            EmailView.INBOX_RESPOND -> {
                CustomCard(
                    isVisible = currentInboxEmailView == EmailView.INBOX_RESPOND,
                    onClose = { showInboxEmailView(EmailView.MESSAGE) }
                ) {
                    CardToRespondEmailView(
                        initialEmailId = selectedEmailId ?: error("No email selected"),
                        onCloseCard = {
                            showInboxEmailView(EmailView.MESSAGE)
                        },
                        cardVisible = true
                    )
                }
            }

            else -> {
            }
        }

        when (currentTrashView) {
            EmailView.INBOX_TRASH -> {
                AnimatedVisibility(
                    visible = cardVisibleTrash,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    CustomCard(
                        isVisible = cardVisibleTrash,
                        onClose = { cardVisibleTrash = false }
                    ) {
                        CardTrashView(
                            cardVisible = cardVisibleTrash,
                            onCloseCard = { cardVisibleTrash = false },
                            onEmailClicked = { emailId ->
                                // Quando um e-mail é clicado, exibe a view de mensagem passando o emailId
                                showTrashView(EmailView.MESSAGE, emailId)
                            },
                            resetItemsToCenter = resetItemsToCenter,
                            isItemBeingDragged = isItemBeingDragged
                        )
                    }
                }
            }

            EmailView.MESSAGE -> {
                AnimatedVisibility(
                    visible = currentTrashView == EmailView.MESSAGE,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    CustomCard(
                        isVisible = currentTrashView == EmailView.MESSAGE,
                        onClose = { currentTrashView = EmailView.INBOX_TRASH }
                    ) {
                        CardMessageTrashView(
                            initialEmailId = selectedEmailId ?: error("No email selected"),
                            onCloseCard = {
                                currentTrashView = EmailView.INBOX_TRASH
                            }
                        )
                    }
                }
            }

            else -> {
            }
        }

        when (currentSpamView) {
            EmailView.INBOX_SPAM -> {
                AnimatedVisibility(
                    visible = cardVisibleSpam,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    CustomCard(
                        isVisible = cardVisibleSpam,
                        onClose = { cardVisibleSpam = false }
                    ) {
                        CardSpamView(
                            cardVisible = cardVisibleSpam,
                            onCloseCard = { cardVisibleSpam = false },
                            onEmailClicked = { emailId ->
                                showSpamView(EmailView.MESSAGE, emailId)
                            },
                            resetItemsToCenter = resetItemsToCenter,
                            isItemBeingDragged = isItemBeingDragged
                        )
                    }
                }
            }

            EmailView.MESSAGE -> {
                AnimatedVisibility(
                    visible = currentSpamView == EmailView.MESSAGE,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    CustomCard(
                        isVisible = currentSpamView == EmailView.MESSAGE,
                        onClose = { currentSpamView = EmailView.INBOX_SPAM }
                    ) {
                        CardMessageSpamView(
                            initialEmailId = selectedEmailId ?: error("No email selected"),
                            onCloseCard = {
                                currentSpamView = EmailView.INBOX_SPAM
                            }
                        )
                    }
                }
            }

            else -> {
            }
        }

        when (currentSketchView) {
            EmailView.INBOX_SKETCH -> {
                AnimatedVisibility(
                    visible = cardVisibleSketch,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    CustomCard(
                        isVisible = cardVisibleSketch,
                        onClose = { cardVisibleSketch = false }
                    ) {
                        CardSketchView(
                            cardVisible = cardVisibleSketch,
                            onCloseCard = { cardVisibleSketch = false },
                            onEmailClicked = { emailId ->
                                showSketchView(EmailView.MESSAGE, emailId)
                            },
                            resetItemsToCenter = resetItemsToCenter,
                            isItemBeingDragged = isItemBeingDragged
                        )
                    }
                }
            }

            EmailView.MESSAGE -> {
                AnimatedVisibility(
                    visible =currentSketchView == EmailView.MESSAGE,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    CustomCard(
                        isVisible = currentSketchView == EmailView.MESSAGE,
                        onClose = { currentSketchView = EmailView.INBOX_SKETCH }
                    ) {
                        CardNewEmailView(
                            cardVisible = cardVisibleNewEmail,
                            initialEmailId = selectedEmailId ?: error("No email selected"),
                            onCloseCard = {
                                currentSketchView = EmailView.INBOX_SKETCH
                            },
                        )

                    }
                }
            }

            else -> {
            }
        }

        when (currentSentView) {
            EmailView.INBOX_SENT -> {
                AnimatedVisibility(
                    visible = cardVisibleSent,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    CustomCard(
                        isVisible = cardVisibleSent,
                        onClose = { cardVisibleSent = false }
                    ) {
                        CardSentView(
                            cardVisible = cardVisibleSent,
                            onCloseCard = { cardVisibleSent = false },
                            onEmailClicked = { emailId ->
                                showSentView(EmailView.MESSAGE, emailId)
                            },
                            resetItemsToCenter = resetItemsToCenter,
                            isItemBeingDragged = isItemBeingDragged
                        )
                    }
                }
            }

            EmailView.MESSAGE -> {
                AnimatedVisibility(
                    visible = currentSentView == EmailView.MESSAGE,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    CustomCard(
                        isVisible = currentSentView == EmailView.MESSAGE,
                        onClose = { currentSentView = EmailView.INBOX_SENT }
                    ) {
                        CardMessageSentView(
                            initialEmailId = selectedEmailId ?: error("No email selected"),
                            onCloseCard = {
                                currentSentView = EmailView.INBOX_SENT
                            }
                        )
                    }
                }
            }

            else -> {
            }
        }

        when (currentArchiveView) {
            EmailView.INBOX_ARCHIVE -> {
                CustomCard(
                    isVisible = cardVisibleArchive,
                    onClose = { cardVisibleArchive = false }
                ) {
                    CardArchiveView(
                        cardVisible = cardVisibleArchive,
                        onCloseCard = { cardVisibleArchive = false },
                        onEmailClicked = { emailId ->
                            // Quando um e-mail é clicado, exibe a view de mensagem passando o emailId
                            showArchiveView(EmailView.MESSAGE, emailId)
                        },
                        resetItemsToCenter = resetItemsToCenter,
                        isItemBeingDragged = isItemBeingDragged
                    )
                }
            }

            EmailView.MESSAGE -> {
                AnimatedVisibility(
                    visible = currentArchiveView == EmailView.MESSAGE,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    CustomCard(
                        isVisible = currentArchiveView == EmailView.MESSAGE,
                        onClose = { currentArchiveView = EmailView.INBOX_ARCHIVE }
                    ) {
                        CardMessageArchiveView(
                            initialEmailId = selectedEmailId ?: error("No email selected"),
                            onCloseCard = {
                                currentArchiveView = EmailView.INBOX_ARCHIVE
                            }
                        )
                    }
                }
            }

            else -> {
            }
        }

        when (currentCalendarView) {
            EmailView.INBOX_CALENDAR -> {
                AnimatedVisibility(
                    visible = cardVisibleCalendar,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    CustomCard(
                        isVisible = cardVisibleCalendar,
                        onClose = { cardVisibleCalendar = false }
                    ) {
                        CardCalendarView(
                            cardVisible = cardVisibleCalendar,
                            onCloseCard = { cardVisibleCalendar = false },
                            onEmailClicked = { emailId ->
                                showCalendarView(EmailView.MESSAGE, emailId)
                            },
                            resetItemsToCenter = resetItemsToCenter,
                            isItemBeingDragged = isItemBeingDragged
                        )
                    }
                }
            }

            EmailView.MESSAGE -> {
                AnimatedVisibility(
                    visible = currentCalendarView == EmailView.MESSAGE,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    CustomCard(
                        isVisible = currentCalendarView == EmailView.MESSAGE,
                        onClose = { currentCalendarView = EmailView.INBOX_CALENDAR }
                    ) {
                        CardMessageCalendarView(
                            initialEmailId = selectedEmailId ?: error("No email selected"),
                            onCloseCard = {
                                currentCalendarView = EmailView.INBOX_CALENDAR
                            }
                        )
                    }
                }
            }

            else -> {
            }
        }

        AnimatedVisibility(
            visible = cardVisibleNewEmail ||
                    cardVisibleCalendar,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
        ) {
            Box {
                when {
                    cardVisibleNewEmail -> {
                        CustomCard(
                            isVisible = cardVisibleNewEmail,
                            onClose = { cardVisibleNewEmail = false }
                        ) {
                            CardNewEmailView(
                                cardVisible = cardVisibleNewEmail,
                                onCloseCard = { cardVisibleNewEmail = false },
                                initialEmailId = 0
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomCard(
    isVisible: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        Card(
            modifier = modifier
                .padding(top = 32.dp, start = 25.dp, end = 25.dp)
                .shadow(
                    10.dp,
                    ambientColor = Color.White,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 15.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.white).copy(alpha = 1f),
            ),
            shape = RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 8.dp
            )
        ) {
            Box(modifier = Modifier.clickable(onClick = onClose)) {
                content()
            }
        }
    }
}
