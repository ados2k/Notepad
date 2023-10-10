package com.samsung.android.notepad.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.samsung.android.notepad.NEW_NOTE_ID
import java.util.*

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var date: Date,
    var text: String,
    @Ignore
    var selected: Boolean = false
) {
    constructor() : this(NEW_NOTE_ID, Date(), "")
    constructor(date: Date, text: String) : this(NEW_NOTE_ID, date, text)
}
