package com.samsung.android.notepad.data

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun longToDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToLong(date: Date): Long {
        return date.time
    }
}