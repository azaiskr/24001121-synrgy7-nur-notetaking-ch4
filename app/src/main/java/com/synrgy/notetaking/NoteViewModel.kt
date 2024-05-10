package com.synrgy.notetaking

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.synrgy.notetaking.data.database.Note
import com.synrgy.notetaking.data.database.User
import com.synrgy.notetaking.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel (private val noteRepository: NoteRepository):ViewModel() {

    fun getAllNotes(): LiveData<List<Note>> = noteRepository.getAllNotes()

    fun insertNote(note: Note) = noteRepository.insertNote(note).asLiveData()
    fun updateNote(note: Note) = noteRepository.updateNote(note)
    fun deleteNote(note: Note) = noteRepository.deleteNote(note)

    fun checkSession(): LiveData<User>{
        return noteRepository.getSession().asLiveData()
    }

    fun clearSession() = viewModelScope.launch {
        noteRepository.logout()
    }
}