package com.myprojects.ktornotes.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myprojects.ktornotes.presentation.add_edit_note.NoteDetailScreen
import com.myprojects.ktornotes.presentation.auth.AuthScreen
import com.myprojects.ktornotes.presentation.note_list.NoteListScreen
import com.myprojects.ktornotes.presentation.ui.theme.KtorNotesTheme
import com.myprojects.ktornotes.util.Constants
import com.myprojects.ktornotes.util.NavigationRoutes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KtorNotesTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NavigationRoutes.AUTH_SCREEN
                ) {
                    composable(route = NavigationRoutes.AUTH_SCREEN) {
                        AuthScreen(
                            onNavigateNext = {
                                navController.navigate(NavigationRoutes.NOTE_LIST_SCREEN) {
                                    popUpTo(NavigationRoutes.AUTH_SCREEN) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                    composable(route = NavigationRoutes.NOTE_LIST_SCREEN) {
                        NoteListScreen(
                            onNavigateToAuth = {
                                navController.navigate(NavigationRoutes.AUTH_SCREEN) {
                                    popUpTo(NavigationRoutes.NOTE_LIST_SCREEN) {
                                        inclusive = true
                                    }
                                }
                            },
                            onNavigateToNoteById = { noteId ->
                                navController.navigate(
                                    NavigationRoutes.ADD_EDIT_NOTE_SCREEN + "?${noteId}"
                                )
                            },
                            onAddNote = {
                                navController.navigate(
                                    NavigationRoutes.ADD_EDIT_NOTE_SCREEN
                                )
                            }
                        )
                    }
                    composable(
                        route = NavigationRoutes.ADD_EDIT_NOTE_SCREEN + "?{${Constants.PARAM_NOTE_ID}}"
                    ) {
                        NoteDetailScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}