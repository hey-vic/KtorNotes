package com.myprojects.ktornotes.presentation.note_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myprojects.ktornotes.domain.use_case.auth.AuthUseCases
import com.myprojects.ktornotes.domain.use_case.notes.NoteUseCases
import com.myprojects.ktornotes.util.LoadingStatus
import com.myprojects.ktornotes.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NoteListState())
    val state: State<NoteListState> = _state

    private val _uiEventChannel = Channel<UiEvent>()
    val uiEvents = _uiEventChannel.receiveAsFlow()

    init {
        getNotes()
    }

    fun onEvent(event: NoteListEvent) {
        when (event) {
            is NoteListEvent.OnSignOutClick -> {
                viewModelScope.launch {
                    authUseCases.signOut()
                    _uiEventChannel.send(UiEvent.SignedOut)
                }
            }

            is NoteListEvent.OnDeleteNoteClick -> {
                noteUseCases.deleteNote(event.note.id).onEach { loadingStatus ->
                    when (loadingStatus) {
                        is LoadingStatus.Error -> {
                            _uiEventChannel.send(
                                UiEvent.ShowSnackbar(
                                    loadingStatus.message ?: "An unexpected error occurred"
                                )
                            )
                        }

                        is LoadingStatus.Loading -> {
                            _state.value = _state.value.copy(isLoading = true)
                        }

                        is LoadingStatus.Success -> {
                            getNotes()
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    fun getNotes() {
        noteUseCases.getAllUserNotes().onEach { loadingStatus ->
            when (loadingStatus) {
                is LoadingStatus.Error -> {
                    _uiEventChannel.send(
                        UiEvent.ShowSnackbar(
                            loadingStatus.message ?: "An unexpected error occurred"
                        )
                    )
                }

                is LoadingStatus.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }

                is LoadingStatus.Success -> {
                    _state.value = NoteListState(notes = loadingStatus.data ?: emptyList())
                }
            }
        }.launchIn(viewModelScope)
    }
}