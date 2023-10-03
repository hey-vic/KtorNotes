package com.myprojects.ktornotes.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myprojects.ktornotes.domain.use_case.notes.NoteUseCases
import com.myprojects.ktornotes.util.Constants
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
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(AddEditNoteState())
    val state: State<AddEditNoteState> = _state

    private val _uiEventChannel = Channel<UiEvent>()
    val uiEvents = _uiEventChannel.receiveAsFlow()

    init {
        savedStateHandle.get<String>(Constants.PARAM_NOTE_ID)?.let { noteId ->
            getNote(noteId)
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.TextChanged -> {
                _state.value = _state.value.copy(text = event.newValue)
            }

            is AddEditNoteEvent.TitleChanged -> {
                _state.value = _state.value.copy(title = event.newValue)
            }

            is AddEditNoteEvent.OnSaveNoteClick -> {
                val result = if (_state.value.noteId != null) {
                    _state.value.noteId?.let {
                        noteUseCases.updateNote(
                            it,
                            _state.value.title,
                            _state.value.text
                        )
                    }
                } else {
                    noteUseCases.insertNote(
                        _state.value.title,
                        _state.value.text
                    )
                }
                result?.onEach { loadingStatus ->
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
                            _uiEventChannel.send(UiEvent.OnNavigateBack)
                        }
                    }
                }?.launchIn(viewModelScope) ?: {
                    viewModelScope.launch {
                        _uiEventChannel.send(
                            UiEvent.ShowSnackbar(
                                "An unexpected error occurred"
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getNote(noteId: String) {
        noteUseCases.getNoteById(noteId).onEach { loadingStatus ->
            when (loadingStatus) {
                is LoadingStatus.Error -> {
                    _uiEventChannel.send(
                        UiEvent.ShowSnackbar(
                            loadingStatus.message ?: "An unexpected error occurred"
                        )
                    )
                }

                is LoadingStatus.Loading -> {
                    _state.value = AddEditNoteState(isLoading = true)
                }

                is LoadingStatus.Success -> {
                    val noteResponse = loadingStatus.data
                    noteResponse?.let { note ->
                        _state.value = AddEditNoteState(
                            title = note.title,
                            text = note.text,
                            noteId = note.id
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}