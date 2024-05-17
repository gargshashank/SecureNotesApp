package com.example.securenotesapp.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.securenotesapp.EncryptionHelper
import com.example.securenotesapp.repository.NoteRepository
import com.example.securenotesapp.roomdb.Note
import kotlinx.coroutines.launch
import java.util.*

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository = NoteRepository(application)
    val allNotes: LiveData<List<Note>> = repository.getAllNotes()

    /**
     * encrypted a note text format
     * set current timestamp also
     * set catgory name also
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun insert(noteText: String, category: String) {
        viewModelScope.launch {
            val encryptedNote = EncryptionHelper.encrypt(noteText)
            val note = Note(
                encryptedNote = Base64.getEncoder().encodeToString(encryptedNote),
                timestamp = System.currentTimeMillis().toString(),
                category = category
            )

            Log.e("Andorid", "note  ..$note")
            repository.insert(note)
        }
    }
}