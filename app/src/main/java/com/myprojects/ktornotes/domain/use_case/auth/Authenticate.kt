package com.myprojects.ktornotes.domain.use_case.auth

import com.myprojects.ktornotes.domain.repository.AuthRepository
import com.myprojects.ktornotes.util.AuthResult
import com.myprojects.ktornotes.util.LoadingStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Authenticate @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<LoadingStatus<AuthResult>> {
        return authRepository.authenticate()
    }
}