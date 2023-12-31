package com.myprojects.ktornotes.domain.use_case.notes

import com.myprojects.ktornotes.domain.repository.NoteRepository
import com.myprojects.ktornotes.util.LoadingStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class InsertNote @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(title: String, text: String): Flow<LoadingStatus<Unit>> {
        if (title.isBlank()) {
            return flow { emit(LoadingStatus.Error("Title cannot be blank")) }
        }
        if (text.isBlank()) {
            return flow { emit(LoadingStatus.Error("Text cannot be blank")) }
        }
        val currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        return noteRepository.insertNote(title, text, currentTime)
    }
}