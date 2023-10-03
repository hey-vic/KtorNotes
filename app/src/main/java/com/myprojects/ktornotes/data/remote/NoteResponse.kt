package com.myprojects.ktornotes.data.remote

import com.myprojects.ktornotes.domain.model.Note

data class NoteResponse(
    val title: String,
    val text: String,
    val id: String
) {
    fun toNote() = Note(title, text, id)
}
