package prm.pro2.fastnote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import prm.pro2.fastnote.entity.Note

class NoteAdapter(private val onLongItemClick: (Note) -> Unit) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(NotesComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.author, current.text, current.city)
        holder.itemView.setOnLongClickListener {
            onLongItemClick(current)
            true
        }
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val authorItemView: TextView = itemView.findViewById(R.id.tvAuthor)
        private val textItemView: TextView = itemView.findViewById(R.id.tvText)
        private val cityItemView: TextView = itemView.findViewById(R.id.tvCity)

        fun bind(author: String?, text: String?, city: String?) {
            authorItemView.text = author
            textItemView.text = text
            cityItemView.text = city
        }

        companion object {
            fun create(parent: ViewGroup): NoteViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_note, parent, false)
                return NoteViewHolder(view)
            }
        }
    }

    class NotesComparator : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
