package prm.pro2.fastnote.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import prm.pro2.fastnote.R
import prm.pro2.fastnote.api.RetrofitInstance.api
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val editTextNote = findViewById<EditText>(R.id.editTextNote)
        val editTextAuthor = findViewById<EditText>(R.id.editTextAuthor)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val noteText = editTextNote.text.toString()
            val authorText = editTextAuthor.text.toString()

            if (noteText.isNotEmpty() && authorText.isNotEmpty()) {
                getLocationAndAddNote(noteText, authorText)
            } else {
                Toast.makeText(this, "Wypelnij wszystkie pola", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getLocationAndAddNote(noteText: String, authorText: String) {
        if (ContextCompat
            .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                .requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val values = geocoder.getFromLocation(location.latitude, location.longitude,1)
                        val city = values!![0].locality ?: "Nienznae miasto"
                        addNote(noteText, authorText, "${location.latitude},${location.longitude}", city)
                    } else {
                        Toast.makeText(this, "Pobranie lokalizacji nie powiodlo sie", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun addNote(noteText: String, authorText: String, coordinates: String, city: String) {
        val note = mapOf(
            "text" to noteText,
            "author" to authorText,
            "coordinates" to coordinates,
            "city" to city
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.addNote(note)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddNoteActivity, "Notatka dodana pomyslnie", Toast.LENGTH_SHORT).show()
                    backToMain(findViewById(R.id.buttonBack))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddNoteActivity, "Dodawanie notatki nie powiodlo sie", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun backToMain(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
