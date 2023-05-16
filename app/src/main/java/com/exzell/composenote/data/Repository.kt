package com.exzell.composenote.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.exzell.composenote.data.database.NoteDatabase
import com.exzell.composenote.domain.Note
import data.Note_db
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
        private val database: NoteDatabase
) {

    private val queries = database.noteQueries

    fun getAllNotes(): Flow<List<Note>> {
        return database.noteQueries.selectAll()
                .asFlow()
                .mapToList(Dispatchers.IO)
                .map { it.toNotes() }
    }

    fun saveNote(note: Note) {
        if(note.id == -1L) queries.insertNote(note.title, note.body, note.colorFirst, note.colorSecond)
        else queries.updateNote(note.title, note.body, note.id)
    }

    private fun Note_db.toNote(): Note {
        return let {
            Note(
                    id = it.id,

                    title = it.title,

                    body = it.body,

                    createdAt = it.created_at.toLong(),

                    updatedAt = it.updated_at.toLong(),

                    colorFirst = it.color_first,

                    colorSecond = it.color_second
            )
        }
    }

    private fun List<Note_db>.toNotes(): List<Note> {
        return map { it.toNote() }
    }
}