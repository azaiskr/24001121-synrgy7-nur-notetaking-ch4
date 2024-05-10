package com.synrgy.notetaking

import com.synrgy.notetaking.data.database.User
import com.synrgy.notetaking.repository.NoteRepository

class UserViewModel (private val repo: NoteRepository) {

    suspend fun login(username: String, password: String) = repo.login(username, password)
    fun register(user: User) = repo.register(user)
    suspend fun saveSession(user: User) = repo.saveSession(user)
    fun getSession() = repo.getSession()
    suspend fun logout() = repo.logout()
}