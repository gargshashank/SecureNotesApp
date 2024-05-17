package com.example.securenotesapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.securenotesapp.EncryptionHelper
import com.example.securenotesapp.R
import com.example.securenotesapp.roomdb.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotesAdapter(private val context: Context) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private var notesList: List<Note> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setNotes(notes: List<Note>) {
        this.notesList = notes
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NotesAdapter.ViewHolder, position: Int) {
        val note = notesList[position]

        try {
            val encryptedNote = Base64.decode(note.encryptedNote, Base64.DEFAULT)
            val decryptedNote = EncryptionHelper.decrypt(encryptedNote)
            holder.noteTextView.text = decryptedNote
        } catch (e: Exception) {
            e.printStackTrace()
            holder.noteTextView.text = "Error decrypting note"
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val date = Date(note.timestamp.toLong())
        holder.timestampTextView.text = sdf.format(date)
        holder.categoryTextView.text = note.category
    }

    override fun getItemId(position: Int): Long {
        return notesList[position].id.toLong()
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTextView = itemView.findViewById<TextView>(R.id.noteTextView)
        val timestampTextView = itemView.findViewById<TextView>(R.id.timestampTextView)
        val categoryTextView = itemView.findViewById<TextView>(R.id.categoryTextView)
    }
}