package com.myprojects.ktornotes.domain.use_case.auth

import com.myprojects.ktornotes.data.TokenManager
import javax.inject.Inject

class SignOut @Inject constructor(
    private val tokenManager: TokenManager
) {
    operator fun invoke() {
        tokenManager.deleteToken()
    }
}