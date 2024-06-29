package prm.pro2.fastnote.activity

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import prm.pro2.fastnote.R
import prm.pro2.fastnote.api.RetrofitInstance.api

class EditNoteActivity : AppCompatActivity() {

    private lateinit var editTextNote: EditText
    private var noteId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        editTextNote = findViewById(R.id.editTextNote)
        val buttonSave = findViewById<Button>(R.id.buttonSave)
        val buttonBack = findViewById<Button>(R.id.buttonBack)

        noteId = intent.getIntExtra("note_id", -1)
        val noteText = intent.getStringExtra("note_text")

        if (noteId == -1) {
            Toast.makeText(this, "Bledne id notatki", Toast.LENGTH_SHORT).show()
            finish()
        }

        editTextNote.setText(noteText)
        buttonSave.setOnClickListener {
            val updatedText = editTextNote.text.toString()
            if (updatedText.isNotEmpty()) {
                updateNoteContent(noteId, updatedText)
            } else {
                Toast.makeText(this, "Notatka nie moze byc pusta", Toast.LENGTH_SHORT).show()
            }
        }

        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun updateNoteContent(noteId: Int?, updatedText: String) {
        if (noteId == null) return

        val note = mapOf("text" to updatedText)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.updateNote(noteId, note)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EditNoteActivity, "Zaktualizowano notatke", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EditNoteActivity, "Wystapil blad podczas aktualizowania notatki", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
