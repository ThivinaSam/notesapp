//package com.example.notesapp
//
//import android.os.Bundle
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }
//}

package com.example.notesapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private lateinit var notesListView: ListView
    private lateinit var noteEditText: EditText
    private lateinit var saveNoteButton: Button

    private var notes = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteEditText = findViewById(R.id.noteEditText)
        saveNoteButton = findViewById(R.id.saveNoteButton)
        notesListView = findViewById(R.id.notesListView)

        loadNotes()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notes)
        notesListView.adapter = adapter

        saveNoteButton.setOnClickListener {
            val note = noteEditText.text.toString()
            if (note.isNotEmpty()) {
                addNote(note)
                noteEditText.text.clear()
            } else {
                Toast.makeText(this, "Enter a note!", Toast.LENGTH_SHORT).show()
            }
        }

        notesListView.setOnItemClickListener { _, _, position, _ ->
            showEditDeleteDialog(position)
        }
    }

    private fun addNote(note: String) {
        notes.add(note)
        saveNotes()
        adapter.notifyDataSetChanged()
    }

    private fun updateNote(position: Int, newNote: String) {
        notes[position] = newNote
        saveNotes()
        adapter.notifyDataSetChanged()
    }

    private fun deleteNote(position: Int) {
        notes.removeAt(position)
        saveNotes()
        adapter.notifyDataSetChanged()
    }

    private fun showEditDeleteDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit or Delete Note")

        val input = EditText(this)
        input.setText(notes[position])
        builder.setView(input)

        builder.setPositiveButton("Update") { _, _ ->
            val updatedNote = input.text.toString()
            if (updatedNote.isNotEmpty()) {
                updateNote(position, updatedNote)
            } else {
                Toast.makeText(this, "Note cannot be empty!", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Delete") { _, _ ->
            deleteNote(position)
        }

        builder.setNeutralButton("Cancel", null)
        builder.show()
    }

    private fun loadNotes() {
        val sharedPreferences = getSharedPreferences("notes_app", Context.MODE_PRIVATE)
        val notesSet = sharedPreferences.getStringSet("notes", emptySet())
        if (notesSet != null) {
            notes.addAll(notesSet)
        }
    }

    private fun saveNotes() {
        val sharedPreferences = getSharedPreferences("notes_app", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("notes", notes.toSet())
        editor.apply()
    }
}
