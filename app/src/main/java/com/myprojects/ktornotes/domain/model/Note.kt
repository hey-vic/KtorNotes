package com.myprojects.ktornotes.domain.model

import java.time.LocalDateTime

data class Note(
    val title: String,
    val text: String,
    val id: String,
    val modifiedDateTime: LocalDateTime
)
