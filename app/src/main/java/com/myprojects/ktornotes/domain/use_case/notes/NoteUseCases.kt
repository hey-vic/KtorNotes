package com.myprojects.ktornotes.domain.use_case.notes

data class NoteUseCases(
    val getAllUserNotes: GetAllUserNotes,
    val getNoteById: GetNoteById,
    val deleteNote: DeleteNote,
    val insertNote: InsertNote,
    val updateNote: UpdateNote
)