package prm.pro2.fastnote.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val coordinates = "${location.latitude},${location.longitude}"
                        val city = getCityFromCoordinates(location.latitude, location.longitude)
                        addNote(noteText, authorText, coordinates, city)
                    } else {
                        Toast.makeText(this, "Pobranie lokalizacji nie powiodlo sie", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun getCityFromCoordinates(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        return if (addresses != null && addresses.isNotEmpty()) {
            addresses[0].locality ?: "Nieznane miasto"
        } else {
            "Nieznane miasto"
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
                    backToMain()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddNoteActivity, "Dodawanie notatki nie powiodlo sie", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun backToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocationAndAddNote(
                    findViewById<EditText>(R.id.editTextNote).text.toString(),
                    findViewById<EditText>(R.id.editTextAuthor).text.toString()
                )
            } else {
                Toast.makeText(this, "Uprawnienia do lokalizacji są wymagane", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
