package com.example.securenotesapp.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.securenotesapp.BackupWorker
import com.example.securenotesapp.adapter.NotesAdapter
import com.example.securenotesapp.databinding.NoteActivityBinding
import com.example.securenotesapp.viewmodel.NoteViewModel
import java.util.concurrent.TimeUnit

class NoteActivity : AppCompatActivity() {


    private lateinit var viewBinding: NoteActivityBinding

    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = NoteActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        notesAdapter = NotesAdapter(this)
        viewBinding.notesRecycleView.adapter = notesAdapter

        viewBinding.addButton.setOnClickListener { addNote() }

        noteViewModel.allNotes.observe(this) { notes ->
            notes?.let { notesAdapter.setNotes(it) }
        }
        notificationPermissionAtRunTime()
        scheduleBackup()

    }

    /**
     * enable run time permission of notification
     */
    private fun notificationPermissionAtRunTime(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }

    /**
     * insert a note data
     */
    private fun addNote() {
        val noteText = viewBinding.noteEditText.text.toString()
        val categoryText = viewBinding.categoryEditText.text.toString()

        if (TextUtils.isEmpty(noteText)) {
            Toast.makeText(this, "Note cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("Andorid","note text .." + noteText + "categoryText,,..." +categoryText)
            noteViewModel.insert(noteText, categoryText)
        }
        viewBinding.noteEditText.text.clear()
        viewBinding.categoryEditText.text.clear()
    }

    /**
     * backup schedule of data
     */
    private fun scheduleBackup() {
        val backupWorkRequest = PeriodicWorkRequestBuilder<BackupWorker>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance(this).enqueue(backupWorkRequest)
    }
}