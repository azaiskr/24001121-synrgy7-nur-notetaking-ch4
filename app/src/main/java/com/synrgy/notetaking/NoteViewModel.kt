package com.synrgy.notetaking

import com.synrgy.notetaking.data.database.Note
import com.synrgy.notetaking.repository.NoteRepository

class NoteViewModel (private val noteRepository: NoteRepository) {

    fun getAllNotes() = noteRepository.getAllNotes()
    fun insertNote(note: Note) = noteRepository.insertNote(note)
    fun updateNote(note: Note) = noteRepository.updateNote(note)
    fun deleteNote(note: Note) = noteRepository.deleteNote(note)
}