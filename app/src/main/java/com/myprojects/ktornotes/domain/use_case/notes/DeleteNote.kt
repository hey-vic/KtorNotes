package com.myprojects.ktornotes.domain.use_case.notes

import com.myprojects.ktornotes.domain.repository.NoteRepository
import com.myprojects.ktornotes.util.LoadingStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteNote @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(id: String): Flow<LoadingStatus<Unit>> {
        return noteRepository.deleteNote(id)
    }
}