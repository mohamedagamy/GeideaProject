package com.example.geideaproject.data.api

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @ColumnInfo(name = "avatar") val avatar: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "first_name") val first_name: String,
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "last_name") val last_name: String
)