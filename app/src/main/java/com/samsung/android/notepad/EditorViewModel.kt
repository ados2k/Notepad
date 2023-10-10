package com.samsung.android.notepad

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samsung.android.notepad.data.AppDatabase
import com.samsung.android.notepad.data.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditorViewModel(app: Application) : AndroidViewModel(app) {
    private val database = AppDatabase.getInstance(app)
    private var note: NoteEntity? = null
    val text = MutableLiveData("")
    fun setNoteId(noteId: Int) {
        if (note != null) return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                note = if (noteId != NEW_NOTE_ID) {
                    database.noteDao()?.getNoteById(noteId)
                } else {
                    NoteEntity()
                }
                text.postValue(note!!.text)
            }
        }
    }

    fun saveNote() {
        note?.let {
            it.text = text.value.toString()
            if (it.id == NEW_NOTE_ID && it.text.isEmpty()) return
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    if (it.text.isEmpty()) {
                        database.noteDao()?.deleteNote(it)
                    } else {
                        database.noteDao()?.insertNote(it)
                    }
                }
            }
        }
    }
}