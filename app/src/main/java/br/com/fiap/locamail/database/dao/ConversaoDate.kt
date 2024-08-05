package br.com.fiap.locamail.database.dao

import androidx.room.TypeConverter
import java.sql.Date

class ConversaoDate {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}
