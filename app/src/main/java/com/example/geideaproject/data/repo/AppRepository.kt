package com.example.tempo.data.repo

import com.example.geideaproject.data.api.ApiService
import javax.inject.Inject

class AppRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getAllUsers(pageSize:Int = 1) = apiService.getAllUsers(pageSize)

    suspend fun getUserByID(userID:Int) = apiService.getUserByID(userID)

}
