package com.example.securenotesapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.securenotesapp.repository.NoteRepository
import com.google.firebase.firestore.FirebaseFirestore

class BackupWorker(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun doWork(): Result {
        Log.e("Android","Back up started")
        val repository = NoteRepository(applicationContext as Application)
        val notes = repository.getAllNotes().value
        notes?.forEach { note ->
            db.collection("encrypted_notes")
                .document(note.id.toString())
                .set(note)
            Log.e("Android", "Back up started note$note")
        }
        showNotification(context,"Backup Success")

        return Result.success()
    }

    private fun showNotification(context: Context, message: String) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "backup_channel",
                "Backup Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "backup_channel")
            .setContentTitle("Backup Status")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1, notification)
    }
}