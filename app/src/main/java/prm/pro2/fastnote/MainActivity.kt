package prm.pro2.fastnote

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import prm.pro2.fastnote.NoteAdapter
import prm.pro2.fastnote.NoteViewModel

class MainActivity : AppCompatActivity() {

    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = NoteAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        noteViewModel.notes.observe(this) { notes ->
            notes?.let { adapter.submitList(it) }
        }
    }
}
