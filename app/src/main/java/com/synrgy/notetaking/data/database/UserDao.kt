package com.synrgy.notetaking.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("Select * from user where email = :email and password = :password")
    suspend fun getUser(email: String, password: String): User

}