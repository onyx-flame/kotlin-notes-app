package com.onyx.notes.repository

import com.onyx.notes.db.NoteDatabase
import com.onyx.notes.models.Hashtag
import com.onyx.notes.models.Note

class NoteRepository(private val db: NoteDatabase)  {
    fun getAllNotes() = db.getNoteDao().getNotesWithHashtags()
    suspend fun addNote(note: Note, hashtags: List<Hashtag>) = db.getNoteDao().addNoteWithHashtags(note, hashtags)
    suspend fun updateNote(note: Note, hashtags: List<Hashtag>) = db.getNoteDao().updateNoteWithHashtags(note, hashtags)
    suspend fun deleteNote(note: Note) = db.getNoteDao().deleteNoteWithHashtags(note)
    fun getNotesSortedByName(query: String?) = db.getNoteDao().getNotesWithHashtagsSortedByName(query)
    fun getNotesSortedByDate(query: String?) = db.getNoteDao().getNotesWithHashtagsSortedByDate(query)
    fun getNotesSortedByNameByHashtag(query: String?) = db.getNoteDao().getNotesWithHashtagsSortedByNameByHashtag(query)
    fun getNotesSortedByDateByHashtag(query: String?) = db.getNoteDao().getNotesWithHashtagsSortedByDateByHashtag(query)
}