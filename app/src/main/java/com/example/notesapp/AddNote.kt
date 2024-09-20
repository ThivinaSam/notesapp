//package com.example.notesapp
//
//import android.os.Bundle
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//
//class AddNote : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_add_note)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }
//}

package com.example.notesapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddNote : AppCompatActivity() {

    private lateinit var noteEditText: EditText
    private lateinit var saveNoteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        noteEditText = findViewById(R.id.noteEditText)
        saveNoteButton = findViewById(R.id.saveNoteButton)

        saveNoteButton.setOnClickListener {
            val note = noteEditText.text.toString()
            if (note.isNotEmpty()) {
                val resultIntent = Intent()
                resultIntent.putExtra("new_note", note)
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // Close the activity and return the note
//                val intent = Intent(this, MainHome::class.java)
//                startActivity(intent)
            } else {
                Toast.makeText(this, "Enter a note!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
