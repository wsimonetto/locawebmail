package br.com.fiap.locamail.database.dao

import android.content.Context
import androidx.room.Database
import androidx.room.TypeConverters
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.fiap.locamail.model.Email

@Database(entities = [Email::class], version = 3)
@TypeConverters(ConversaoDate::class)
abstract class EmailDb : RoomDatabase(){

    abstract fun emailDao(): EmailDao

    companion object {

        private lateinit var instance: EmailDb

        fun getDatabase(context: Context): EmailDb {
            if (!::instance.isInitialized) {
                instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        EmailDb::class.java,
                        "email_db"
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}
