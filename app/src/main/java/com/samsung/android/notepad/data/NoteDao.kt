package com.samsung.android.notepad.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: NoteEntity)

    @Query("SELECT * FROM notes ORDER BY date ASC")
    fun getAll(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById(id: Int): NoteEntity?

    @Query("DELETE FROM notes")
    fun deleteAll()

    @Delete
    fun deleteNotes(selectedNotes: List<NoteEntity>)

    @Delete
    fun deleteNote(note: NoteEntity)
}