package prm.pro2.fastnote

data class Note(
    val id: Int,
    val text: String,
    val creationDate: String,
    val editDate: String?,
    val author: String,
    val coordinates: String,
    val city: String
)

