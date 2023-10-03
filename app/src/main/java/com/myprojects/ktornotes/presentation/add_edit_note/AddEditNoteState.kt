package com.myprojects.ktornotes.presentation.add_edit_note

data class AddEditNoteState(
    val isLoading: Boolean = false,
    val title: String = "",
    val text: String = "",
    val noteId: String? = null
)
