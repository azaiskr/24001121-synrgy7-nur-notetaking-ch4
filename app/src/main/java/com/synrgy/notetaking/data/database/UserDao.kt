package com.synrgy.notetaking.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Query("Select * from user where username = :username and password = :password")
    fun getUser(username: String, password: String): User

}