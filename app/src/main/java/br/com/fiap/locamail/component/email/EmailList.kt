package br.com.fiap.locamail.component.email

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import br.com.fiap.locamail.R
import br.com.fiap.locamail.cardsview.EmailView
import br.com.fiap.locamail.component.dragAnimation.DragAnchors
import br.com.fiap.locamail.component.dragAnimation.DraggableItem
import br.com.fiap.locamail.component.dragAnimation.SaveActionLeftBoxOne
import br.com.fiap.locamail.component.dragAnimation.SaveActionLeftBoxTwo
import br.com.fiap.locamail.component.dragAnimation.SaveActionRightTwo
import br.com.fiap.locamail.component.dragAnimation.SaveActionRightOne
import br.com.fiap.locamail.component.dragAnimation.SaveActionRightThree
import br.com.fiap.locamail.component.email.card.EmailCard
import br.com.fiap.locamail.component.email.state.EmailListState
import br.com.fiap.locamail.model.Email
import kotlin.math.roundToInt

@SuppressLint("MutableCollectionMutableState", "RememberReturnType")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmailList(
    listaEmailState: MutableState<List<Email>>,
    atualizar: () -> Unit,
    checkboxVisible: Boolean,
    onEmailClicked: (Int) -> Unit, // Callback para quando um email for clicado
    onEmailLeftBoxOne: (Email) -> Unit, // Callback
    onEmailLeftBoxTwo: (Email) -> Unit, // Callback
    onClickRightBoxOne: (Email) -> Unit, // Callback
    onClickRightBoxTwo: (Email) -> Unit, // Callback
    onClickRightBoxThree: (Email) -> Unit, // Callback
    currentView: EmailView, // Verificar os INBOX aberto
    isItemBeingDragged: MutableState<Boolean>, // Estado compartilhado para o arrasto
    resetItemsToCenter: MutableState<Boolean>,
    listaEmailsLidos: MutableState<List<Email>>,
    ) {

    val iconTrash = painterResource(id = R.drawable.trash_send)
    val iconDelete = painterResource(id = R.drawable.trash_delete)
    val iconSpam = painterResource(id = R.drawable.spam_besouro)
    val iconArchive = painterResource(id = R.drawable.archive)
    val iconRead = painterResource(id = R.drawable.baseline_mark_email_read_24)
    val iconNotRead = painterResource(id = R.drawable.baseline_mark_email_unread_24)

    val iconImpotant = painterResource(id = R.drawable.baseline_star_24)
    val iconRestore = painterResource(id = R.drawable.send_inbox)

    var iconLeft1 = painterResource(id = R.drawable.trash_send)
    var iconLeft2 = painterResource(id = R.drawable.trash_delete)
    var iconRight1 = painterResource(id = R.drawable.baseline_edit_24)
    var iconRight2 = painterResource(id = R.drawable.baseline_star_24)
    var iconRight3 = painterResource(id = R.drawable.archive)

    val textTrash by remember {
        mutableStateOf("Lixeira")
    }
    val textSpam by remember {
        mutableStateOf("Spam")
    }
    val textDelete by remember {
        mutableStateOf("Deletar")
    }
    val textRead by remember {
        mutableStateOf("Lido")
    }
    val textNotRead by remember {
        mutableStateOf("Não Lido")
    }
    val textArchive by remember {
        mutableStateOf("Arquivar")
    }
    val textImportant by remember {
        mutableStateOf("Importante")
    }
    val textRestore by remember {
        mutableStateOf("Restaurar")
    }
    var textLeft1 by remember {
        mutableStateOf("")
    }
    var textLeft2 by remember {
        mutableStateOf("")
    }
    var textRight1 by remember {
        mutableStateOf("")
    }
    var textRight2 by remember {
        mutableStateOf("")
    }
    var textRight3 by remember {
        mutableStateOf("")
    }

    when (currentView) {

        EmailView.INBOX_MAIL -> {

            iconLeft1 = iconTrash
            iconLeft2 = iconSpam
            iconRight1 = iconRead
            iconRight2 = iconImpotant
            iconRight3 = iconArchive

            textLeft1 = textTrash
            textLeft2 = textSpam
            textRight1 = textRead
            textRight2 = textImportant
            textRight3 = textArchive

        }

        EmailView.MESSAGE -> {

        }

        EmailView.INBOX_TRASH -> {

            iconLeft1 = iconDelete
            iconLeft2 = iconSpam
            iconRight1 = iconRead
            iconRight2 = iconRestore

            textLeft1 = textDelete
            textLeft2 = textSpam
            textRight1 = textRead
            textRight2 = textRestore
            textRight3 = textRestore

        }

        EmailView.INBOX_SPAM -> {

            iconLeft1 = iconDelete
            iconLeft2 = iconSpam
            iconRight1 = iconRead
            iconRight2 = iconRestore

            textLeft1 = textDelete
            textLeft2 = textSpam
            textRight1 = textRead
            textRight2 = textRestore
        }

        EmailView.INBOX_SKETCH -> {

            iconLeft1 = iconDelete
            iconLeft2 = iconSpam
            iconRight1 = iconRead
            iconRight2 = iconArchive

            textLeft1 = textDelete
            textLeft2 = textSpam
            textRight1 = textRead
            textRight2 = textArchive
        }

        EmailView.INBOX_SENT -> {

            iconLeft1 = iconDelete
            iconLeft2 = iconSpam
            iconRight1 = iconRead
            iconRight2 = iconRestore

            textLeft1 = textDelete
            textLeft2 = textSpam
            textRight1 = textRead
            textRight2 = textRestore

        }

        EmailView.INBOX_ARCHIVE -> {

            iconLeft1 = iconTrash
            iconLeft2 = iconSpam
            iconRight1 = iconRead
            iconRight2 = iconImpotant
            iconRight3 = iconRestore

            textLeft1 = textTrash
            textLeft2 = textSpam
            textRight1 = textRead
            textRight2 = textImportant
            textRight3 = textRestore

        }

        EmailView.INBOX_CALENDAR -> {

            iconLeft1 = iconTrash
            iconLeft2 = iconSpam
            iconRight1 = iconRead
            iconRight2 = iconImpotant
            iconRight3 = iconArchive

            textLeft1 = textTrash
            textLeft2 = textSpam
            textRight1 = textRead
            textRight2 = textImportant
            textRight3 = textArchive
        }

        EmailView.INBOX_RESPOND -> TODO()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        val currentlyDraggedItem =
            remember { mutableStateOf<Long?>(null) }//

        for (email in listaEmailState.value) {

            val isEmailRead = email.lido
            val iconRight1 = if (isEmailRead) iconRead else iconNotRead
            val textRight1 = if (isEmailRead) textRead else textNotRead
            //Log.d("EmailList", "Before - isEmailRead: $isEmailRead, iconRight1: $iconRight1, textRight1: $textRight1")

            val density = LocalDensity.current
            val defaultActionSize = 80.dp
            val actionSizePx = with(density) { defaultActionSize.toPx() }
            val startActionSizePx = with(density) { (defaultActionSize * 2).toPx() }
            val endActionSizePx = with(density) { (defaultActionSize * 3).toPx() }

            val state = remember {
                AnchoredDraggableState(
                    initialValue = DragAnchors.Center,
                    anchors = DraggableAnchors {
                        if (currentView == EmailView.INBOX_SPAM) {
                            DragAnchors.Start at -actionSizePx
                            DragAnchors.Center at 0f
                            DragAnchors.End at actionSizePx
                        } else if (currentView == EmailView.INBOX_SKETCH) {
                            DragAnchors.Start at -actionSizePx
                            DragAnchors.Center at 0f
                        } else if (currentView == EmailView.INBOX_TRASH) {
                            DragAnchors.Start at -actionSizePx
                            DragAnchors.Center at 0f
                            DragAnchors.End at actionSizePx
                        } else if (currentView == EmailView.INBOX_SENT) {
                            DragAnchors.Start at -actionSizePx
                            DragAnchors.Center at 0f
                        } else {
                            DragAnchors.Start at -startActionSizePx
                            DragAnchors.Center at 0f
                            DragAnchors.End at endActionSizePx
                        }
                    },
                    positionalThreshold = { distance: Float -> distance * 0.5f },
                    velocityThreshold = { with(density) { 100.dp.toPx() } },
                    animationSpec = tween()
                )
            }

            var resetToCenter by remember { mutableStateOf(false) }

            LaunchedEffect(resetToCenter) {
                if (resetToCenter) {
                    Log.d("EmailList", "Resetting drag state...")
                    state.animateTo(DragAnchors.Center)
                    resetToCenter = false
                }
            }

            LaunchedEffect(state) {
                snapshotFlow { state.offset }
                    .collect { offset ->
                        isItemBeingDragged.value = offset != 0f
                        // Se o item está sendo arrastado e não é o atualmente arrastado, resetar o estado do item anterior
                        if (offset != 0f && currentlyDraggedItem.value != email.id) {
                            currentlyDraggedItem.value?.let {
                                resetItemsToCenter.value = true
                            }
                            currentlyDraggedItem.value = email.id
                        }
                    }
            }

            LaunchedEffect(resetItemsToCenter.value) {
                if (resetItemsToCenter.value) {
                    state.animateTo(DragAnchors.Center)
                }
                resetItemsToCenter.value = false
            }

            DraggableItem(
                state = state,
                draggable = !checkboxVisible,
                startAction = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .align(Alignment.CenterStart)
                    ) {
                        if (currentView == EmailView.INBOX_SPAM) {
                            SaveActionLeftBoxOne(
                                Modifier
                                    .width(defaultActionSize)
                                    .fillMaxHeight()
                                    .offset {
                                        IntOffset(
                                            ((-state
                                                .requireOffset() - actionSizePx))
                                                .roundToInt(), 0
                                        )
                                    }
                                    .clickable {
                                        onEmailLeftBoxOne(email)
                                        resetToCenter = true
                                    },
                                icon = iconLeft1,
                                text = textLeft1
                            )
                        } else if (currentView == EmailView.INBOX_SKETCH) {
                            SaveActionLeftBoxOne(
                                Modifier
                                    .width(defaultActionSize)
                                    .fillMaxHeight()
                                    .offset {
                                        IntOffset(
                                            ((-state
                                                .requireOffset() - actionSizePx))
                                                .roundToInt(), 0
                                        )
                                    }
                                    .clickable {
                                        onEmailLeftBoxOne(email)
                                        resetToCenter = true
                                    },
                                icon = iconLeft1,
                                text = textLeft1
                            )
                        } else if (currentView == EmailView.INBOX_SENT) {
                            SaveActionLeftBoxOne(
                                Modifier
                                    .width(defaultActionSize)
                                    .fillMaxHeight()
                                    .offset {
                                        IntOffset(
                                            ((-state
                                                .requireOffset() - actionSizePx))
                                                .roundToInt(), 0
                                        )
                                    }
                                    .clickable {
                                        onEmailLeftBoxOne(email)
                                        resetToCenter = true
                                    },
                                icon = iconLeft1,
                                text = textLeft1
                            )
                        } else if (currentView == EmailView.INBOX_TRASH) {
                            SaveActionLeftBoxOne(
                                Modifier
                                    .width(defaultActionSize)
                                    .fillMaxHeight()
                                    .offset {
                                        IntOffset(
                                            ((-state
                                                .requireOffset() - actionSizePx))
                                                .roundToInt(), 0
                                        )
                                    }
                                    .clickable {
                                        onEmailLeftBoxOne(email)
                                        resetToCenter = true
                                    },
                                icon = iconLeft1,
                                text = textLeft1
                            )
                        } else {
                            SaveActionLeftBoxTwo(
                                Modifier
                                    .width(defaultActionSize)
                                    .fillMaxHeight()
                                    .offset {
                                        IntOffset(
                                            ((-state
                                                .requireOffset() - actionSizePx))
                                                .roundToInt(), 0
                                        )
                                    }
                                    .clickable {
                                        onEmailLeftBoxTwo(email)
                                        resetToCenter = true
                                    },
                                icon = iconLeft2,
                                text = textLeft2
                            )
                            SaveActionLeftBoxOne(
                                Modifier
                                    .width(defaultActionSize)
                                    .fillMaxHeight()
                                    .offset {
                                        IntOffset(
                                            ((-state
                                                .requireOffset() * 0.5f) - actionSizePx)
                                                .roundToInt(), 0
                                        )
                                    }
                                    .clickable {
                                        onEmailLeftBoxOne(email)
                                        resetToCenter = true
                                    },
                                icon = iconLeft1,
                                text = textLeft1
                            )
                        }
                    }
                },
                endAction = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .align(Alignment.CenterEnd),
                    ) {
                        if (currentView == EmailView.INBOX_MAIL ||
                            currentView == EmailView.INBOX_CALENDAR ||
                            currentView == EmailView.INBOX_ARCHIVE
                        ) {
                            SaveActionRightOne(
                                Modifier
                                    .width(defaultActionSize)
                                    .fillMaxHeight()
                                    .offset {
                                        IntOffset(
                                            ((-state
                                                .requireOffset()) + actionSizePx + 15)
                                                .roundToInt(), 0
                                        )
                                    }
                                    .clickable {
                                        onClickRightBoxOne(email)
                                        resetToCenter = true
                                    },
                                icon = iconRight1,
                                text = textRight1
                            )
                            SaveActionRightThree(
                                Modifier
                                    .width(defaultActionSize)
                                    .fillMaxHeight()
                                    .offset {
                                        IntOffset(
                                            ((-state
                                                .requireOffset() * 0.658f) + actionSizePx)
                                                .roundToInt(), 0
                                        )
                                    }
                                    .clickable {
                                        onClickRightBoxThree(email)
                                        resetToCenter = true
                                    },
                                icon = iconRight2,
                                text = textRight2
                            )
                            SaveActionRightTwo(
                                Modifier
                                    .width(defaultActionSize)
                                    .fillMaxHeight()
                                    .offset {
                                        IntOffset(
                                            ((-state
                                                .requireOffset() * 0.3298f) + actionSizePx)
                                                .roundToInt(), 0
                                        )
                                    }
                                    .clickable {
                                        onClickRightBoxTwo(email)
                                        resetToCenter = true
                                    },
                                icon = iconRight3,
                                text = textRight3
                            )
                        } else if (currentView == EmailView.INBOX_SPAM) {
                            SaveActionRightTwo(
                                Modifier
                                    .width(defaultActionSize)
                                    .fillMaxHeight()
                                    .offset {
                                        IntOffset(
                                            ((-state
                                                .requireOffset()) + actionSizePx)
                                                .roundToInt(), 0
                                        )
                                    }
                                    .clickable {
                                        onClickRightBoxTwo(email)
                                        resetToCenter = true
                                    },
                                icon = iconRight2,
                                text = textRight2
                            )
                        } else if (currentView == EmailView.INBOX_TRASH) {
                            SaveActionRightTwo(
                                Modifier
                                    .width(defaultActionSize)
                                    .fillMaxHeight()
                                    .offset {
                                        IntOffset(
                                            ((-state
                                                .requireOffset()) + actionSizePx)
                                                .roundToInt(), 0
                                        )
                                    }
                                    .clickable {
                                        onClickRightBoxTwo(email)
                                        resetToCenter = true
                                    },
                                icon = iconRight2,
                                text = textRight2
                            )
                        }
                    }
                },
                content = {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        EmailCard(
                            email = email,
                            isSelected =
                            EmailListState.getEmailIds().contains(email.id.toInt()),
                            onSelectionChange = { _, isSelected ->
                                if (isSelected) {
                                    EmailListState.addEmailId(email.id.toInt())
                                    Log.d("EmailList", "Email ID: ${email.id}")
                                } else {
                                    EmailListState.removeEmailId(email.id.toInt())
                                    Log.d("EmailList", "Email ID: ${email.id}")
                                }
                            },
                            atualizar = atualizar,
                            checkboxVisible = checkboxVisible,
                            onEmailClicked = {
                                onEmailClicked(email.id.toInt())
                            },
                        )
                    }
                })

            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp

            val isScreen = screenWidth >= 448
            val line = if (isScreen) 385.dp else if (screenWidth >= 412) 365.dp else 345.dp

            Spacer(
                modifier = Modifier
                    .width(line)
                    .height(0.6.dp)
                    .background(colorResource(id = R.color.cinza_claro))
                    .padding(start = 15.dp)
                    .align(Alignment.End)
            )

        }
    }
}
