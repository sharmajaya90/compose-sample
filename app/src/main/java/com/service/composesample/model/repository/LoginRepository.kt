package com.service.composesample.model.repository

interface LoginRepository {
    suspend fun login(email: String, password: String): Boolean
}