package com.myprojects.ktornotes.presentation.note_list

import com.myprojects.ktornotes.domain.model.Note

data class NoteListState(
    val isLoading: Boolean = false,
    val notes: List<Note> = emptyList()
)
