package com.myprojects.ktornotes.presentation.auth

data class AuthScreenState(
    val isLoading: Boolean = false,
    val isShowingSplash: Boolean = true,
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false
)