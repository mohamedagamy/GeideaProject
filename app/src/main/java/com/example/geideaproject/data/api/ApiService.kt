package com.example.geideaproject.data.api
import retrofit2.http.*

interface ApiService {
    @GET("users")
    suspend fun getAllUsers(@Query("per_page") pageSize: Int): ApiResult

    @GET("users/{id}")
    suspend fun getUserByID(@Path("id") userID: Int): UserDetails
}