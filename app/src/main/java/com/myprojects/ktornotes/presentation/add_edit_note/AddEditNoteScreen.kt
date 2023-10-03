package com.myprojects.ktornotes.presentation.add_edit_note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myprojects.ktornotes.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(viewModel) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }

                is UiEvent.OnNavigateBack -> {
                    onNavigateBack()
                }

                else -> Unit
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(AddEditNoteEvent.OnSaveNoteClick)
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                item {
                    TextField(
                        value = state.title,
                        onValueChange = {
                            viewModel.onEvent(AddEditNoteEvent.TitleChanged(it))
                        },
                        placeholder = {
                            Text(
                                text = "Note title",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    )
                }
                item {
                    Divider(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp))
                }
                item {
                    TextField(
                        value = state.text,
                        onValueChange = {
                            viewModel.onEvent(AddEditNoteEvent.TextChanged(it))
                        },
                        placeholder = {
                            Text(text = "Note text")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        )
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}