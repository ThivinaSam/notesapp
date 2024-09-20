//package com.example.notesapp
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//
//class MainHome : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main_home)
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
////            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
////            insets
////        }
//
//        val btnNavigateAddNote: Button = findViewById(R.id.addNoteButton)
//        btnNavigateAddNote.setOnClickListener {
//            val intent = Intent(this, AddNote::class.java)
//            startActivity(intent)
//        }
//    }
//}

package com.example.notesapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainHome : AppCompatActivity() {

    private lateinit var notesListView: ListView
    private lateinit var addNoteButton: Button

    private var notes = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home)

        addNoteButton = findViewById(R.id.addNoteButton)
        notesListView = findViewById(R.id.notesListView)

        loadNotes()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notes)
        notesListView.adapter = adapter

        addNoteButton.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
            startActivityForResult(intent, 1)
        }

        notesListView.setOnItemClickListener { _, _, position, _ ->
            showEditDeleteDialog(position)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val newNote = data?.getStringExtra("new_note")
            if (newNote != null) {
                addNote(newNote)
            }
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
