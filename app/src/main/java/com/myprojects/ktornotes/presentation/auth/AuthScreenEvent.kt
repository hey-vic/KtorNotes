package com.myprojects.ktornotes.presentation.auth

sealed interface AuthScreenEvent {
    data class EmailChanged(val newValue: String) : AuthScreenEvent
    data class PasswordChanged(val newValue: String) : AuthScreenEvent
    object SignUp: AuthScreenEvent
    object SignIn: AuthScreenEvent
    object TogglePasswordVisibility: AuthScreenEvent
}
