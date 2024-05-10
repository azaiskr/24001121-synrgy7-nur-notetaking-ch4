package com.synrgy.notetaking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synrgy.notetaking.data.database.User
import com.synrgy.notetaking.repository.NoteRepository
import kotlinx.coroutines.launch

class UserViewModel (private val repo: NoteRepository): ViewModel() {

    fun login(username: String, password: String) = viewModelScope.launch {
        repo.login(username, password)
    }
    fun register(user: User) = repo.register(user)
    suspend fun saveSession(user: User) = repo.saveSession(user)

}