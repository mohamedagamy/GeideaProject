package com.example.geideaproject.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.geideaproject.data.api.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Insert
    fun insertAll(vararg users: User)
}