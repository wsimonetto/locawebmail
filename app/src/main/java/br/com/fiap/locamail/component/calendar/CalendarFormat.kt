package br.com.fiap.locamail.component.calendar

import androidx.compose.runtime.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CalendarFormat() {
    val today = LocalDate.now()
    val formattedDate = today.format(DateTimeFormatter.ofPattern("EEEE, dd",
        java.util.Locale("pt", "BR")
    ))
}