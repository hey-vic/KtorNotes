package com.myprojects.ktornotes.presentation.add_edit_note

import java.time.LocalDateTime

data class AddEditNoteState(
    val isLoading: Boolean = false,
    val title: String = "",
    val text: String = "",
    val modifiedDateTime: LocalDateTime? = null,
    val noteId: String? = null
)
