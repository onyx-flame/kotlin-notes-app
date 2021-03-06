package com.onyx.notes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.onyx.notes.converters.DateConverter
import com.onyx.notes.models.Hashtag
import com.onyx.notes.models.Note

@Database(entities = [Note::class, Hashtag::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    companion object {

        @Volatile
        private var instance: NoteDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance?: synchronized(LOCK) {
            instance?:
            createDatabase(context).also {
                instance = it
            }


        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            NoteDatabase::class.java,
            "notes_db"
        ).build()

    }

}