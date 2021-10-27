package com.onyx.notes.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.onyx.notes.models.Hashtag
import com.onyx.notes.models.Note
import com.onyx.notes.models.NoteWithHashTags

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    @Transaction
    fun getNotesWithHashtags(): LiveData<List<NoteWithHashTags>>

    @Insert
    suspend fun addNote(note: Note) : Long

    @Insert
    suspend fun addHashtags(hashTags: List<Hashtag>)

    @Transaction
    suspend fun addNoteWithHashtags(note: Note, hashTags: List<Hashtag>) {
        val noteId = addNote(note)
        hashTags.forEach{ it.noteId = noteId.toInt() }
        addHashtags(hashTags)
    }

}