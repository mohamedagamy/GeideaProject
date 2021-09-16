package com.example.geideaproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.geideaproject.data.api.ApiResult
import com.example.geideaproject.data.model.Resource
import com.example.tempo.data.repo.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class AppViewModel  @Inject constructor(private val appRepository: AppRepository) : ViewModel() {
    val newsList:MutableLiveData<ApiResult> = MutableLiveData()

    fun getAllUsers(pageNum: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result = appRepository.getAllUsers(pageNum)
            newsList.postValue(result)
            emit(Resource.success(data = result))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getUserByID(userID: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getUserByID(userID)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}
