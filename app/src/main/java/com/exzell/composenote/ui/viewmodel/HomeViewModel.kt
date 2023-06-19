package com.exzell.composenote.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exzell.composenote.core.di.Constants
import com.exzell.composenote.data.PreferenceManager
import com.exzell.composenote.data.Repository
import com.exzell.composenote.domain.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
        private val repo: Repository,
        private val prefManager: PreferenceManager
) : ViewModel() {

    private val _notesFlow = MutableStateFlow<List<Note>>(emptyList())

    val notesFlow = _notesFlow.asStateFlow()

    init {
        getNotes()
    }

    fun getNotes(isDeleted: Boolean = false, search: String = "") {
        viewModelScope.launch {
            _notesFlow.value = if (search.isBlank()) repo.getNotesByDeletion(isDeleted)
            else repo.searchNoteWithDeleteStatus(search, isDeleted)
        }
    }

    fun getDisplayMode(): Flow<Int> {
        return prefManager.getDisplayMode()
    }

    fun switchDisplayMode(mode: Int) {
        val newMode = if (mode == Constants.DISPLAY_MODE_GRID) Constants.DISPLAY_MODE_LIST
        else Constants.DISPLAY_MODE_GRID

        prefManager.setDisplayMode(newMode)
    }

    fun saveNote(note: Note) {
        viewModelScope.launch {
            if (note.title.isEmpty() && note.body.isEmpty()) return@launch

            repo.saveNote(note)
        }
    }

    fun deleteNotesWithIds(ids: List<Long>) {
        viewModelScope.launch {
            repo.setNoteDeleteStatus(true, ids)
        }
    }
}