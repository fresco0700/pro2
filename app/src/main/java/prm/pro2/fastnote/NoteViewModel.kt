package prm.pro2.fastnote


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import prm.pro2.fastnote.Note
import prm.pro2.fastnote.RetrofitInstance

class NoteViewModel : ViewModel() {

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    init {
        viewModelScope.launch {
            _notes.value = RetrofitInstance.api.getNotes()
        }
    }
}