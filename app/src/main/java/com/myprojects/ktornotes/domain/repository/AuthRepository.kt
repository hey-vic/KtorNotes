package com.myprojects.ktornotes.domain.repository

import com.myprojects.ktornotes.util.AuthResult
import com.myprojects.ktornotes.util.LoadingStatus
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signUp(email: String, password: String): Flow<LoadingStatus<AuthResult>>
    fun signIn(email: String, password: String): Flow<LoadingStatus<AuthResult>>
    fun authenticate(): Flow<LoadingStatus<AuthResult>>
}