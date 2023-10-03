package com.myprojects.ktornotes.presentation.add_edit_note

sealed interface AddEditNoteEvent {
    data class TitleChanged(val newValue: String) : AddEditNoteEvent
    data class TextChanged(val newValue: String) : AddEditNoteEvent
    object OnSaveNoteClick : AddEditNoteEvent
}