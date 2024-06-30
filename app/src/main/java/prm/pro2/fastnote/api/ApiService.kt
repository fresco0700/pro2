package prm.pro2.fastnote.api

import prm.pro2.fastnote.entity.Note
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("api/notes")
    suspend fun getNotes(): List<Note>

    @POST("api/notes")
    suspend fun addNote(
        @Body note: Map<String, String>
    )

    @DELETE("api/notes/delete/{id}")
    suspend fun deleteNote(
        @Path("id") id: Int
    )

    @PUT("api/notes/edit/{id}")
    suspend fun updateNote(
        @Path("id") id: Int,
        @Body note: Map<String, String>
    )
}
