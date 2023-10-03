package com.myprojects.ktornotes.data.repository

import com.myprojects.ktornotes.data.TokenManager
import com.myprojects.ktornotes.data.remote.AuthRequest
import com.myprojects.ktornotes.data.remote.ClientApi
import com.myprojects.ktornotes.domain.repository.AuthRepository
import com.myprojects.ktornotes.util.AuthResult
import com.myprojects.ktornotes.util.LoadingStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: ClientApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override fun signUp(
        email: String, password: String
    ): Flow<LoadingStatus<AuthResult>> = flow {
        emit(LoadingStatus.Loading())
        try {
            api.signUp(
                request = AuthRequest(
                    email = email,
                    password = password
                )
            )
            signIn(email, password).collect { emit(it) }
        } catch (e: HttpException) {
            if (e.code() == 401) {
                emit(LoadingStatus.Success(AuthResult.Unauthorized))
            } else if (e.code() == 409) {
                emit(LoadingStatus.Error("User with this email already exists"))
            } else {
                emit(LoadingStatus.Error(e.message ?: "Network error"))
            }
        } catch (e: Exception) {
            emit(LoadingStatus.Error())
        }
    }

    override fun signIn(email: String, password: String): Flow<LoadingStatus<AuthResult>> = flow {
        emit(LoadingStatus.Loading())
        try {
            val response = api.signIn(
                request = AuthRequest(
                    email = email,
                    password = password
                )
            )
            tokenManager.saveToken(response.token)
            emit(LoadingStatus.Success(AuthResult.Authorized))
        } catch (e: HttpException) {
            if (e.code() == 401) {
                emit(LoadingStatus.Success(AuthResult.Unauthorized))
            } else if (e.code() == 409) {
                emit(LoadingStatus.Error("Invalid password"))
            } else if (e.code() == 404) {
                emit(LoadingStatus.Error("User with this email not found"))
            } else {
                emit(LoadingStatus.Error(e.message ?: "Network error"))
            }
        } catch (e: Exception) {
            emit(LoadingStatus.Error())
        }
    }

    override fun authenticate(): Flow<LoadingStatus<AuthResult>> = flow {
        emit(LoadingStatus.Loading())
        try {
            api.authenticate()
            emit(LoadingStatus.Success(AuthResult.Authorized))
        } catch (e: HttpException) {
            if (e.code() == 401) {
                emit(LoadingStatus.Success(AuthResult.Unauthorized))
            } else {
                emit(LoadingStatus.Error(e.message ?: "Network error"))
            }
        } catch (e: Exception) {
            emit(LoadingStatus.Error())
        }
    }

}