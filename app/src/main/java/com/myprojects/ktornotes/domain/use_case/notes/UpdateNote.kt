package com.myprojects.ktornotes.domain.use_case.notes

import com.myprojects.ktornotes.domain.repository.NoteRepository
import com.myprojects.ktornotes.util.LoadingStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateNote @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(id: String, title: String, text: String): Flow<LoadingStatus<Unit>> {
        if (title.isBlank()) {
            return flow { emit(LoadingStatus.Error("Title cannot be blank")) }
        }
        if (text.isBlank()) {
            return flow { emit(LoadingStatus.Error("Text cannot be blank")) }
        }
        return noteRepository.updateNote(id, title, text)
    }
}