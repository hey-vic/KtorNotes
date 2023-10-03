package com.myprojects.ktornotes.presentation.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myprojects.ktornotes.domain.use_case.auth.AuthUseCases
import com.myprojects.ktornotes.util.AuthResult
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
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _state = mutableStateOf(AuthScreenState())
    val state: State<AuthScreenState> = _state

    private val _uiEventChannel = Channel<UiEvent>()
    val uiEvents = _uiEventChannel.receiveAsFlow()

    init {
        authenticate()
    }

    fun onEvent(event: AuthScreenEvent) {
        when (event) {
            is AuthScreenEvent.EmailChanged -> {
                _state.value = _state.value.copy(email = event.newValue)
            }

            is AuthScreenEvent.PasswordChanged -> {
                _state.value = _state.value.copy(password = event.newValue)
            }

            is AuthScreenEvent.SignUp -> {
                signUp()
            }

            is AuthScreenEvent.SignIn -> {
                signIn()
            }

            is AuthScreenEvent.TogglePasswordVisibility -> {
                _state.value =
                    _state.value.copy(isPasswordVisible = !_state.value.isPasswordVisible)
            }
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = authUseCases.signUp(
                email = _state.value.email,
                password = _state.value.password
            )
            result.onEach { loadingStatus ->
                when (loadingStatus) {
                    is LoadingStatus.Error -> {
                        _uiEventChannel.send(
                            UiEvent.ShowSnackbar(loadingStatus.message ?: "Network error")
                        )
                    }

                    is LoadingStatus.Success -> {
                        when (loadingStatus.data) {
                            is AuthResult.Authorized -> _uiEventChannel.send(UiEvent.Authenticated)
                            else -> {
                                UiEvent.ShowSnackbar("Authorization error")
                            }
                        }
                    }

                    else -> Unit
                }
            }.launchIn(this)
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = authUseCases.signIn(
                email = _state.value.email,
                password = _state.value.password
            )
            result.onEach { loadingStatus ->
                when (loadingStatus) {
                    is LoadingStatus.Error -> {
                        _uiEventChannel.send(
                            UiEvent.ShowSnackbar(loadingStatus.message ?: "Network error")
                        )
                    }

                    is LoadingStatus.Success -> {
                        when (loadingStatus.data) {
                            is AuthResult.Authorized -> _uiEventChannel.send(UiEvent.Authenticated)
                            else -> {
                                UiEvent.ShowSnackbar("Authorization error")
                            }
                        }
                    }

                    else -> Unit
                }
            }.launchIn(this)
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private fun authenticate() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isShowingSplash = true)
            val result = authUseCases.authenticate()
            result.onEach { loadingStatus ->
                when (loadingStatus) {
                    is LoadingStatus.Error -> {
                        _uiEventChannel.send(
                            UiEvent.ShowSnackbar(loadingStatus.message ?: "Network error")
                        )
                    }

                    is LoadingStatus.Success -> {
                        when (loadingStatus.data) {
                            is AuthResult.Authorized -> _uiEventChannel.send(UiEvent.Authenticated)
                            else -> {
                                _state.value = _state.value.copy(isShowingSplash = false)
                            }
                        }
                    }

                    else -> Unit
                }
            }.launchIn(this)
        }
    }
}