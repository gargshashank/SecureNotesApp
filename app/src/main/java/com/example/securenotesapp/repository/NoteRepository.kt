package com.example.securenotesapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.securenotesapp.roomdb.Note
import com.example.securenotesapp.roomdb.NoteDao
import com.example.securenotesapp.roomdb.NoteDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(application: Application) {
    private val noteDao: NoteDao
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val allNotes: LiveData<List<Note>>

    init {
        val database = NoteDatabase.getInstance(application)
        noteDao = database.noteDao()
        allNotes = noteDao.getAllNotes()
    }

    suspend fun insert(note: Note) {
        withContext(Dispatchers.IO) {
           val id =  noteDao.insert(note)
            val noteWithId = note.copy(id = id.toInt())
            backupNoteToFirebase(noteWithId)
        }
    }

    fun getAllNotes(): LiveData<List<Note>> = allNotes

    fun getAllNotesSync(): List<Note> {
        return noteDao.getAllNotesSync()
    }

    /**
     * set note data on firebase
     */
    private fun backupNoteToFirebase(note: Note) {
        db.collection("encrypted_notes")
            .document(note.id.toString())
            .set(note)
    }
}