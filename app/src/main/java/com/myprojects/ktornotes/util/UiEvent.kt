package com.myprojects.ktornotes.util

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
    object Authenticated : UiEvent
    object SignedOut : UiEvent
    object OnNavigateBack : UiEvent
}
