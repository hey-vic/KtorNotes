package com.myprojects.ktornotes.data.remote

import com.myprojects.ktornotes.domain.model.Note
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class NoteResponse(
    val title: String,
    val text: String,
    val id: String,
    val modifiedDateTime: String
) {
    fun toNote(): Note {
        val modifiedDateTime = LocalDateTime.parse(
            modifiedDateTime,
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
        )
        return Note(title, text, id, modifiedDateTime)
    }
}
