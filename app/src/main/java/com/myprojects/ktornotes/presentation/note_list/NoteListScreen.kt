package com.myprojects.ktornotes.presentation.note_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myprojects.ktornotes.presentation.note_list.components.NoteListItem
import com.myprojects.ktornotes.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    onNavigateToAuth: () -> Unit,
    onNavigateToNoteById: (String) -> Unit,
    onAddNote: () -> Unit,
    viewModel: NoteListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(true) {
        viewModel.getNotes()
    }

    LaunchedEffect(viewModel) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEvent.SignedOut -> {
                    onNavigateToAuth()
                }

                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }

                else -> Unit
            }
        }
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Your notes")
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(NoteListEvent.OnSignOutClick)
                        }) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Sign out"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onAddNote()
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.notes) { note ->
                    NoteListItem(
                        note = note,
                        onItemClick = {
                            onNavigateToNoteById(note.id)
                        },
                        onDeleteClick = {
                            viewModel.onEvent(NoteListEvent.OnDeleteNoteClick(note))
                        }
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