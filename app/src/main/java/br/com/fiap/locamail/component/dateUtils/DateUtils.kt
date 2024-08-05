package br.com.fiap.locamail.component.dateUtils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun formatEmailDate(emailTimestamp: Long): String {
        // Criar um objeto Date com a data de envio do email
        val emailDate = Date(emailTimestamp)
        // Criar um objeto Calendar com a data atual
        val currentCalendar = Calendar.getInstance()
        // Criar um objeto Calendar com a data de envio do email
        val emailCalendar = Calendar.getInstance()
        emailCalendar.time = emailDate

        // Verificar se o email chegou hoje
        val isToday = currentCalendar.get(Calendar.DAY_OF_YEAR) == emailCalendar.get(Calendar.DAY_OF_YEAR) &&
                currentCalendar.get(Calendar.YEAR) == emailCalendar.get(Calendar.YEAR)

        // Obter o formato da hora
        val timeFormat = SimpleDateFormat("HH:mm", Locale("pt", "BR"))
        // Obter o formato do dia da semana
        val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale("pt", "BR"))

        // Se o e-mail chegou hoje, exibir apenas o horário; senão, exibir o dia da semana
        return if (isToday) {
            timeFormat.format(emailDate)
        } else {
            dayOfWeekFormat.format(emailDate)
        }
    }

}