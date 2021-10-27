package com.onyx.notes.repository

import com.onyx.notes.db.NoteDatabase
import com.onyx.notes.models.Hashtag
import com.onyx.notes.models.Note

class NoteRepository(private val db: NoteDatabase)  {
    fun getAllNotes() = db.getNoteDao().getNotesWithHashtags()
    suspend fun addNote(note: Note, hashtags: List<Hashtag>) = db.getNoteDao().addNoteWithHashtags(note, hashtags)
}