package com.myprojects.ktornotes.domain.repository

import com.myprojects.ktornotes.domain.model.Note
import com.myprojects.ktornotes.util.LoadingStatus
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllUserNotes(): Flow<LoadingStatus<List<Note>>>
    fun getNoteById(id: String): Flow<LoadingStatus<Note>>
    fun insertNote(
        title: String,
        text: String,
        modifiedDateTime: String
    ): Flow<LoadingStatus<Unit>>

    fun updateNote(
        id: String,
        title: String,
        text: String,
        modifiedDateTime: String
    ): Flow<LoadingStatus<Unit>>

    fun deleteNote(id: String): Flow<LoadingStatus<Unit>>
}