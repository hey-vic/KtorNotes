package com.myprojects.ktornotes.domain.use_case.auth

import android.util.Patterns
import com.myprojects.ktornotes.domain.repository.AuthRepository
import com.myprojects.ktornotes.util.AuthResult
import com.myprojects.ktornotes.util.LoadingStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUp @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<LoadingStatus<AuthResult>> {
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return flow { emit(LoadingStatus.Error<AuthResult>("Invalid email")) }
        }
        if (password.length < 8) {
            return flow { emit(LoadingStatus.Error<AuthResult>("Password is too short")) }
        }
        return authRepository.signUp(email, password)
    }
}