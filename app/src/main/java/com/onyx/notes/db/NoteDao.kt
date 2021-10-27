package com.onyx.notes.db

import androidx.lifecycle.LiveData
import androidx.room.*
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

    @Update
    suspend fun updateNote(note: Note)

    @Query("DELETE FROM hashtags WHERE noteId = :noteId")
    suspend fun deleteNoteHashTags(noteId: Int)

    @Transaction
    suspend fun updateNoteWithHashtags(note: Note, hashTags: List<Hashtag>) {
        deleteNoteHashTags(note.id)
        updateNote(note)
        hashTags.forEach{ it.noteId = note.id }
        addHashtags(hashTags)
    }

    @Delete
    suspend fun deleteNote(note: Note)

    @Transaction
    suspend fun deleteNoteWithHashtags(note: Note) {
        deleteNoteHashTags(note.id)
        deleteNote(note)
    }

}