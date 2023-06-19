package com.exzell.composenote.data

import com.exzell.composenote.data.database.NoteDatabase
import com.exzell.composenote.domain.Note
import migrations.Note_db
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val database: NoteDatabase) {

    private val queries = database.noteQueries

    fun getNotesByDeletion(isDeleted: Boolean): List<Note> {
        return database.noteQueries.selectByDeleteCategory(isDeleted)
                .executeAsList()
                .map { it.toNote() }
    }

    fun saveNote(note: Note) {
        if (note.id == -1L) queries.insertNote(note.title, note.body, note.colorFirst, note.colorSecond)
        else queries.updateNote(note.title, note.body, note.id)
    }

    fun setNoteDeleteStatus(isDeleted: Boolean, ids: List<Long>) {
        queries.bulkUpdateNoteToDelete(isDeleted, ids)
    }

    fun searchNoteWithDeleteStatus(text: String, isDeleted: Boolean): List<Note> {
        //the * after text is the wildcard. If it is not present then the query matches only the entire word
        //would have preferred if this could just be in the sql statement but unfortunately
        return queries.searchNoteWithDeleteStatus("$text*", isDeleted)
                .executeAsList()
                .map { it.toNote() }
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