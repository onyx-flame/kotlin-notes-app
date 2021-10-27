package com.onyx.notes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.onyx.notes.models.Hashtag
import com.onyx.notes.models.Note
import com.onyx.notes.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(
    app: Application,
    private val noteRepository: NoteRepository
): AndroidViewModel(app) {
    fun addNote(note: Note, hashtags: List<Hashtag>) = viewModelScope.launch {
        noteRepository.addNote(note, hashtags)
    }
    fun getAllNotes() = noteRepository.getAllNotes()
    fun updateNote(note: Note, hashtags: List<Hashtag>) = viewModelScope.launch {
        noteRepository.updateNote(note, hashtags)
    }
    fun deleteNote(note: Note) = viewModelScope.launch {
        noteRepository.deleteNote(note)
    }
    fun getNotesSortedByName(query: String? = "") = noteRepository.getNotesSortedByName(query)
    fun getNotesSortedByDate(query: String? = "") = noteRepository.getNotesSortedByDate(query)
}