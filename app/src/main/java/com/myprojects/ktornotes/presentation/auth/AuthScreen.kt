@file:OptIn(ExperimentalMaterial3Api::class)

package com.myprojects.ktornotes.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myprojects.ktornotes.R
import com.myprojects.ktornotes.presentation.ui.theme.Orange60
import com.myprojects.ktornotes.presentation.ui.theme.Orange75
import com.myprojects.ktornotes.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onNavigateNext: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(viewModel) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEvent.Authenticated -> {
                    onNavigateNext()
                }

                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }

                else -> Unit
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { paddingValues ->
        if (state.isShowingSplash) {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ktor Notes",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        color = Orange60
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_splash_image),
                        contentDescription = "Ktor Notes logo"
                    )
                    Spacer(modifier = Modifier.height(60.dp))
                    Text(
                        text = "LOADING...",
                        fontSize = 16.sp,
                        color = Orange75
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = {
                            viewModel.onEvent(AuthScreenEvent.EmailChanged(it))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Email")
                        },
                        maxLines = 1,
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = {
                            viewModel.onEvent(AuthScreenEvent.PasswordChanged(it))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (state.isPasswordVisible) {
                            VisualTransformation.None
                        } else PasswordVisualTransformation(),
                        label = {
                            Text(text = "Password")
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.onEvent(AuthScreenEvent.TogglePasswordVisibility)
                                }) {
                                Icon(
                                    imageVector = if (state.isPasswordVisible) {
                                        Icons.Filled.VisibilityOff
                                    } else {
                                        Icons.Filled.Visibility
                                    },
                                    contentDescription = "Toggle visibility"
                                )
                            }
                        },
                        maxLines = 1,
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = {
                                viewModel.onEvent(AuthScreenEvent.SignUp)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Sign Up")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                viewModel.onEvent(AuthScreenEvent.SignIn)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Sign In")
                        }
                    }
                }
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}