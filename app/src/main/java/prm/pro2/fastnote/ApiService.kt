import prm.pro2.fastnote.Note
import retrofit2.http.GET

interface ApiService {
    @GET("api/notes")
    suspend fun getNotes(): List<Note>
}
