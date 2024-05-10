package com.synrgy.notetaking.di

import android.content.Context
import com.synrgy.notetaking.data.LoginPreferences
import com.synrgy.notetaking.data.database.AppDatabase
import com.synrgy.notetaking.repository.NoteRepository

object Inject {

    fun provideRepo(context: Context) : NoteRepository {
        val pref = LoginPreferences.getInstance(context)
        val appDatabase = AppDatabase.getDatabase(context)
        return NoteRepository.getInstance(pref,appDatabase)
    }
}