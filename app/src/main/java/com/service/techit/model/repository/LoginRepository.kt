package com.service.techit.model.repository

interface LoginRepository {
    suspend fun login(email: String, password: String): Boolean
}