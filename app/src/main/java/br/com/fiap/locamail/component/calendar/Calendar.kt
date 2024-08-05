package br.com.fiap.locamail.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.locamail.R
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.*
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun Calendar(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChanged: (LocalDate) -> Unit
) {
    var currentDate by remember { mutableStateOf(initialDate) }
    var currentMonth = currentDate.monthNumber
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val weeks = getWeeksInMonth(currentDate.year, currentDate.monthNumber)
    val monthName = Month.values()[currentDate.monthNumber - 1].getDisplayName(
        TextStyle.FULL,
        Locale("pt", "BR")
    )
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    LaunchedEffect(currentDate) {
        onMonthChanged(currentDate)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "${currentDate.year}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(id = R.color.azul_claro),
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    currentDate = currentDate.minus(DatePeriod(months = 1))
                    currentMonth = currentDate.monthNumber
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_keyboard_double_arrow_left_24),
                    contentDescription = "Anterior",
                    modifier = Modifier.size(40.dp),
                    tint = colorResource(id = R.color.azul_claro)
                )
            }
            Text(
                text = monthName,
                fontSize = 35.sp,
                color = colorResource(id = R.color.azul_escuro),
            )
            IconButton(
                onClick = {
                    currentDate = currentDate.plus(DatePeriod(months = 1))
                    currentMonth = currentDate.monthNumber
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_keyboard_double_arrow_right_24),
                    contentDescription = "Próximo",
                    modifier = Modifier.size(40.dp),
                    tint = colorResource(id = R.color.azul_claro)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            listOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        weeks.forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                week.forEach { day ->
                    val isCurrentDay = day == now && currentMonth == now.monthNumber
                    val isCurrentMonth = day?.monthNumber == currentMonth
                    val isSelectedDay = day == selectedDate

                    val configuration = LocalConfiguration.current
                    val screenWidth = configuration.screenWidthDp
                    val isBox = screenWidth >= 448
                    val box = if (isBox) 50.dp else 47.dp

                    Box(
                        modifier = Modifier
                            .size(box)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(25.dp))
                            .background(
                                when {
                                    isCurrentDay -> colorResource(id = R.color.azul_claro)
                                    isSelectedDay -> colorResource(id = R.color.verde_claro)
                                    isCurrentMonth && day != null -> colorResource(id = R.color.canva_one)
                                    else -> colorResource(id = R.color.cinza_claro)
                                }
                            )
                            .then(
                                if (isCurrentMonth && day != null) {
                                    Modifier.clickable {
                                        selectedDate = day
                                        onDateSelected(day)
                                    }
                                } else {
                                    Modifier
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day?.dayOfMonth?.toString() ?: "",
                            fontWeight = when {
                                isCurrentDay -> FontWeight.ExtraBold
                                isSelectedDay -> FontWeight.ExtraBold
                                else -> FontWeight.Normal
                            },
                            color = if (isCurrentDay || isSelectedDay) colorResource(id = R.color.canva_one) else colorResource(id = R.color.white)
                        )
                    }
                }
            }
        }
    }

}

fun getDaysInMonth(year: Int, month: Int): List<LocalDate> {
    val firstDayOfMonth = LocalDate(year, month, 1)
    val lastDayOfMonth = firstDayOfMonth.plus(DatePeriod(months = 1)).minus(DatePeriod(days = 1))
    val daysInMonth = firstDayOfMonth.daysUntil(lastDayOfMonth.plus(DatePeriod(days = 1)))
    return (0 until daysInMonth).map { firstDayOfMonth.plus(DatePeriod(days = it)) }
}

fun getWeeksInMonth(year: Int, month: Int): List<List<LocalDate?>> {
    val daysInMonth = getDaysInMonth(year, month)
    val firstDayOfWeek = daysInMonth.first().dayOfWeek.isoDayNumber

    val paddingDaysStart = firstDayOfWeek % 7

    val previousMonth = if (month == 1) 12 else month - 1
    val previousYear = if (month == 1) year - 1 else year
    val daysInPreviousMonth = getDaysInMonth(previousYear, previousMonth)
    val lastDaysOfPreviousMonth = daysInPreviousMonth.takeLast(paddingDaysStart)

    val daysWithPaddingStart = lastDaysOfPreviousMonth + daysInMonth

    val totalDays = daysWithPaddingStart.size
    val nextMonthPaddingDays = (7 - totalDays % 7) % 7
    val nextMonthDays = List(nextMonthPaddingDays) { dayIndex ->
        LocalDate(year, month, daysInMonth.size).plus(DatePeriod(days = dayIndex + 1))
    }

    val daysWithPadding = daysWithPaddingStart + nextMonthDays

    return daysWithPadding.chunked(7)
}
