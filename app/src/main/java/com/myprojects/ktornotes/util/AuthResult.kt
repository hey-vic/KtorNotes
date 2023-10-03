package com.myprojects.ktornotes.util

sealed interface AuthResult {
    object Authorized : AuthResult
    object Unauthorized : AuthResult
}
