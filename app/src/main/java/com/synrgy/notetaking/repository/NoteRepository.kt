package com.synrgy.notetaking.repository

import androidx.lifecycle.LiveData
import com.synrgy.notetaking.data.LoginPreferences
import com.synrgy.notetaking.data.database.Note
import com.synrgy.notetaking.data.database.NoteDao
import com.synrgy.notetaking.data.database.AppDatabase
import com.synrgy.notetaking.data.database.User
import com.synrgy.notetaking.data.database.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteRepository(
    private val loginPreferences: LoginPreferences,
    appDatabase: AppDatabase,
) {
    private val mNoteDao: NoteDao = appDatabase.noteDao()
    private val mUserDao: UserDao = appDatabase.userDao()

    //User
    fun register(user: User) = executeInBackground { mUserDao.insert(user) }
    suspend fun login(email: String, password: String): User = withContext(Dispatchers.IO) {
        mUserDao.getUser(email, password)
    }

    suspend fun logout() = loginPreferences.clearLoginPref()
    suspend fun saveSession(user: User) = loginPreferences.saveLoginPref(user)
    fun getSession(): Flow<User> {
        return loginPreferences.getLoginPref()
    }

    //Note
    fun getAllNotes(): LiveData<List<Note>> = mNoteDao.getAllNotes()
    fun insertNote(note: Note) = executeInBackground { mNoteDao.insert(note) }
    fun updateNote(note: Note) = executeInBackground { mNoteDao.update(note) }
    fun deleteNote(note: Note) = executeInBackground { mNoteDao.delete(note) }


    private fun executeInBackground(block: suspend () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                block()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: NoteRepository? = null

        fun getInstance(
            loginPreferences: LoginPreferences,
            appDatabase: AppDatabase,
        ): NoteRepository =
            INSTANCE ?: synchronized(this) {
                NoteRepository(
                    loginPreferences,
                    appDatabase
                ).also { INSTANCE = it }
            }
    }
}