package com.drubico.pokeapi.data.network

sealed class ApiResponse<T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error<Nothing>(val errors: List<String>) : ApiResponse<Nothing>()
}