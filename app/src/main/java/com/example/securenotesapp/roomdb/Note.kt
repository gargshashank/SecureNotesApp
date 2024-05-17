package com.example.securenotesapp.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val encryptedNote: String,
    val timestamp: String,
    val category: String
)