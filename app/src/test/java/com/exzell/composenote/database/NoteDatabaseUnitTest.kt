package com.exzell.composenote.database

import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.exzell.composenote.data.database.NoteDatabase
import org.junit.Before
import org.junit.Test

class NoteDatabaseUnitTest {

    private lateinit var database: NoteDatabase

    private fun SqlDriver.hasTable(name: String): Boolean {
        val mapper: (SqlCursor) -> Boolean = { it.getLong(0) != null && it.getLong(0)!! > 0  }

        return executeQuery(null, "SELECT count(name) FROM sqlite_master WHERE type = 'table' AND name = '$name';", mapper, 0).value
    }

    private fun SqlDriver.getUserVersion(): Int {
        val mapper: (SqlCursor) -> Int = { it.getLong(0)!!.toInt() }

        return executeQuery(null, "PRAGMA user_version;", mapper, 0, null).value
    }

    fun SqlDriver.setUserVersion(version: Int) {
        execute(null, "PRAGMA user_version = $version;", 0)
    }

    @Before
    fun init() {
        val driver = JdbcSqliteDriver("jdbc:sqlite:../note_test.db")

        val currentVersion = driver.getUserVersion()

        if(currentVersion == 0 && !driver.hasTable("note_db")) {
            NoteDatabase.Schema.create(driver)
            driver.setUserVersion(1)
        }
        else {
            val schemaVersion = NoteDatabase.Schema.version

            if(schemaVersion > currentVersion) {
                NoteDatabase.Schema.migrate(driver, currentVersion, schemaVersion)

                driver.setUserVersion(schemaVersion)
            }
        }

        database = NoteDatabase(driver)
    }

    @Test
    fun insertNote() {
        database.noteQueries.also {
            it.insertNote("Test Note", "This is a body testing insertion of a new note", 0xFFFFFFFF, 0xFF000000)
        }
    }

    @Test
    fun getNote() {
        val note = database.noteQueries.getNote(1).executeAsOne()

        assert(note.id == 1L)
    }

    @Test
    fun selectAllNotes() {
        val notes = database.noteQueries.selectAll().executeAsList()

        assert(notes.isNotEmpty())
    }

    @Test
    fun selectNoteByDeleteState() {
        val deletedNotes = database.noteQueries.selectByDeleteCategory(true).executeAsList()

        val unDeletedNotes = database.noteQueries.selectByDeleteCategory(false).executeAsList()

        assert(deletedNotes.all { it.is_deleted })

        assert(unDeletedNotes.all { !it.is_deleted })
    }

    @Test
    fun updateNote() {
        val newTitle = "Welcome"
        val newBody = "Watashi ga kita"

        val updatedNote = database.transactionWithResult {
            database.noteQueries.updateNote(newTitle, newBody, 2)

            database.noteQueries.getNote(2).executeAsOne()
        }

        assert(updatedNote.title == newTitle)
        assert(updatedNote.body == newBody)
    }

    @Test
    fun bulkDeleteNotes() {
        database.noteQueries.bulkUpdateNoteToDelete(true, listOf(1, 3))

        val note = database.noteQueries.getNote(1).executeAsOne()

        assert(note.is_deleted)
    }

    @Test
    fun searchNoteWithDeleteStatus() {
        //First get notes that are not deleted
        val notDeletedNotes = database.noteQueries.searchNoteWithDeleteStatus("test", false).executeAsList()

        assert(notDeletedNotes.all { !it.is_deleted })
        assert(notDeletedNotes.all { it.title.startsWith("test", ignoreCase = true) || it.body.startsWith("test", ignoreCase = true) })

        val deletedNotes = database.noteQueries.searchNoteWithDeleteStatus("test", true).executeAsList()

        assert(deletedNotes.all { it.is_deleted })
        assert(deletedNotes.all { it.title.startsWith("test", ignoreCase = true) || it.body.startsWith("test", ignoreCase = true) })
    }
}