package com.myprojects.ktornotes.presentation.note_list

import com.myprojects.ktornotes.domain.model.Note

sealed interface NoteListEvent {
    object OnSignOutClick : NoteListEvent
    data class OnDeleteNoteClick(val note: Note) : NoteListEvent
}