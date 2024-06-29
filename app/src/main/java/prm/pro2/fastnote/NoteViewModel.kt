package prm.pro2.fastnote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import prm.pro2.fastnote.api.RetrofitInstance
import prm.pro2.fastnote.entity.Note

class NoteViewModel : ViewModel() {

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    init {
        refreshNotes()
    }

    fun refreshNotes() {
        viewModelScope.launch {
            try {
                _notes.value = RetrofitInstance.api.getNotes()
            } catch (e: Exception) {
                Log.d("ERROR","Blad podczas refreshowania notatki")

            }
        }
    }

    fun deleteNote(noteId: Int) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.deleteNote(noteId)
                refreshNotes()
            } catch (e: Exception) {
                Log.d("ERROR","Blad podczas usuwania notatki")
            }
        }
    }

}
