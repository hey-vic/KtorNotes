package com.myprojects.ktornotes.data.repository

import com.myprojects.ktornotes.data.remote.ClientApi
import com.myprojects.ktornotes.data.remote.NoteRequest
import com.myprojects.ktornotes.domain.model.Note
import com.myprojects.ktornotes.domain.repository.NoteRepository
import com.myprojects.ktornotes.util.LoadingStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val api: ClientApi
) : NoteRepository {

    override fun getAllUserNotes(): Flow<LoadingStatus<List<Note>>> = flow {
        emit(LoadingStatus.Loading())
        try {
            val notes = api.getAllUserNotes().map { it.toNote() }
            emit(LoadingStatus.Success(notes))
        } catch (e: HttpException) {
            emit(LoadingStatus.Error(e.message ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(LoadingStatus.Error("Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(LoadingStatus.Error("An unexpected error occurred"))
        }
    }

    override fun getNoteById(id: String): Flow<LoadingStatus<Note>> = flow {
        emit(LoadingStatus.Loading())
        try {
            val note = api.getNoteById(id).toNote()
            emit(LoadingStatus.Success(note))
        } catch (e: HttpException) {
            emit(LoadingStatus.Error(e.message ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(LoadingStatus.Error("Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(LoadingStatus.Error("An unexpected error occurred"))
        }
    }

    override fun insertNote(
        title: String,
        text: String,
        modifiedDateTime: String
    ): Flow<LoadingStatus<Unit>> = flow {
        emit(LoadingStatus.Loading())
        try {
            api.insertNote(
                NoteRequest(
                    title = title,
                    text = text,
                    modifiedDateTime = modifiedDateTime
                )
            )
            emit(LoadingStatus.Success())
        } catch (e: HttpException) {
            emit(LoadingStatus.Error(e.message ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(LoadingStatus.Error("Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(LoadingStatus.Error("An unexpected error occurred"))
        }
    }

    override fun updateNote(
        id: String,
        title: String,
        text: String,
        modifiedDateTime: String
    ): Flow<LoadingStatus<Unit>> = flow {
        emit(LoadingStatus.Loading())
        try {
            api.updateNote(id, NoteRequest(title, text, modifiedDateTime))
            emit(LoadingStatus.Success())
        } catch (e: HttpException) {
            emit(LoadingStatus.Error(e.message ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(LoadingStatus.Error("Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(LoadingStatus.Error("An unexpected error occurred"))
        }
    }

    override fun deleteNote(id: String): Flow<LoadingStatus<Unit>> = flow {
        emit(LoadingStatus.Loading())
        try {
            api.deleteNote(id)
            emit(LoadingStatus.Success())
        } catch (e: HttpException) {
            emit(LoadingStatus.Error(e.message ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(LoadingStatus.Error("Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(LoadingStatus.Error("An unexpected error occurred"))
        }
    }
}