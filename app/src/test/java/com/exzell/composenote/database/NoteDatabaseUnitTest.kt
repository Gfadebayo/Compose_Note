package com.exzell.composenote.database

import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.exzell.composenote.data.database.NoteDatabase
import org.junit.Before
import org.junit.Test
import java.time.format.DateTimeFormatter

class NoteDatabaseUnitTest {

    private lateinit var database: NoteDatabase

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

    @Test
    fun `test note is inserted`() {
        database.noteQueries.also {
            it.insertNote("Test Note", "This is a body testing insertion of a new note", 0xFFFFFFFF, 0xFF000000)
    }

    @Test
    fun `test select all notes`() {
        val notes = database.noteQueries.selectAll().executeAsList()

        assert(notes.isNotEmpty())
    }

    @Test
    fun `test bulk delete notes`() {
        database.noteQueries.bulkUpdateNoteToDelete(true, listOf(1, 3))
    }
}