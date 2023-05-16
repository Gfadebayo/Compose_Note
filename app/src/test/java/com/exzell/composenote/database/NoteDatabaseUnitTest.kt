package com.exzell.composenote.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.exzell.composenote.data.database.NoteDatabase
import org.junit.Before
import org.junit.Test

class NoteDatabaseUnitTest {

    private lateinit var database: NoteDatabase

    @Before
    fun init() {
        val driver = JdbcSqliteDriver("jdbc:sqlite:../note_test.db")

        database = NoteDatabase(driver)
        NoteDatabase.Schema.create(driver)
    }

    @Test
    fun `test note is inserted`() {

        database.noteQueries.also {
            val insertedRow = it.insertNote("Test Note", "This is a body testing insertion of a new note", 0xFFFFFFFF, 0xFF000000)

            println("Inserted row is $insertedRow")
        }
    }

    @Test
    fun `test select all notes`() {
        val notes = database.noteQueries.selectAll().executeAsList()

        assert(notes.isNotEmpty())

        println(notes.joinToString(separator = "\n"))
    }
}