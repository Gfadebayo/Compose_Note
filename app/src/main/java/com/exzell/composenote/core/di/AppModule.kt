package com.exzell.composenote.core.di

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.exzell.composenote.data.database.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): NoteDatabase {
        val driver = AndroidSqliteDriver(NoteDatabase.Schema, context, name = "note_db")

        return NoteDatabase(driver)
    }
}