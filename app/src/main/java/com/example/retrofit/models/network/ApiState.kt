package com.example.retrofit.models.network

sealed class ApiState {
    data object Loading : ApiState()
    data class Success(val data: Any) : ApiState()
    data class Error(val message: String) : ApiState()
    data object Empty : ApiState()
}