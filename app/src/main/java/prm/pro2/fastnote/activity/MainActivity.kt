package prm.pro2.fastnote.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import prm.pro2.fastnote.NoteAdapter
import prm.pro2.fastnote.NoteViewModel
import prm.pro2.fastnote.R
import prm.pro2.fastnote.entity.Note

class MainActivity : AppCompatActivity() {

    private val noteViewModel: NoteViewModel by viewModels()

    companion object {
        const val EDIT_NOTE_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = NoteAdapter { note -> showNoteOptionsDialog(note) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        noteViewModel.notes.observe(this)
        { notes -> notes?.let { adapter.submitList(it) } }

        findViewById<Button>(R.id.buttonMap).setOnClickListener { openMap() }
        findViewById<Button>(R.id.buttonAdd).setOnClickListener { openAddForm() }
        findViewById<Button>(R.id.buttonSettings).setOnClickListener { openSettings() }
    }

    private fun openMap() {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
    }

    private fun openAddForm() {
        val intent = Intent(this, AddNoteActivity::class.java)
        startActivity(intent)
    }

    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun showNoteOptionsDialog(note: Note) {
        val options = arrayOf("Edytuj notatke", "Usun notatke")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Wybierz opcje")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> editNote(note)
                1 -> deleteNote(note)
            }
        }
        builder.show()
    }

    private fun editNote(note: Note) {
        val intent = Intent(this, EditNoteActivity::class.java).apply {
            putExtra("note_id", note.id)
            putExtra("note_text", note.text)
            putExtra("note_author", note.author)
        }
        startActivityForResult(intent, EDIT_NOTE_REQUEST_CODE)
    }

    private fun deleteNote(note: Note) {
        noteViewModel.deleteNote(note.id)
        Toast.makeText(this, "Notatka usunięta: ${note.text}", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_NOTE_REQUEST_CODE && resultCode ==Activity.RESULT_OK) {
            noteViewModel.refreshNotes()
        }
    }
}
