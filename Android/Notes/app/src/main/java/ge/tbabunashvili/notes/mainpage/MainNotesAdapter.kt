package ge.tbabunashvili.notes.mainpage

import android.opengl.Visibility
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.tbabunashvili.notes.R
import ge.tbabunashvili.notes.data.entity.Note


class MainNotesAdapter(var list: MutableList<List<Note>>, val listener: MainNoteListener) : RecyclerView.Adapter<MainNoteViewHolder>() {
    private val COUNT_OF_NOTES = " checked items"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainNoteViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.main_page_item,parent,false)
        return MainNoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainNoteViewHolder, position: Int) {
        holder.first.visibility = View.GONE
        holder.second.visibility = View.GONE
        holder.third.visibility = View.GONE
        holder.dots.visibility = View.GONE
        holder.count.visibility = View.GONE
        val currNote = list[holder.adapterPosition]
        holder.itemView.setOnClickListener{
            listener.onClickListener(currNote[0])
        }
        holder.title.setText(currNote[0].text)
        if (currNote.size < 2) {
            return
        }
        holder.first.visibility = View.VISIBLE
        holder.first.setText(currNote[1].text)
        holder.first.isChecked = currNote[1].done

        if (currNote.size < 3) {
            return
        }
        holder.second.visibility = View.VISIBLE
        holder.second.setText(currNote[2].text)
        holder.second.isChecked = currNote[2].done
        if (currNote.size < 4) {
            return
        }
        holder.third.visibility = View.VISIBLE
        holder.third.setText(currNote[3].text)
        holder.third.isChecked = currNote[3].done
        if (currNote.size<5) {
            return
        }
        holder.dots.visibility = View.VISIBLE
        holder.count.text = "+" + (currNote.size - 4).toString() + COUNT_OF_NOTES
        holder.count.visibility = View.VISIBLE
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class MainNoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var title = itemView.findViewById<TextView>(R.id.parentNote)
    var first = itemView.findViewById<CheckBox>(R.id.fitem)
    var second = itemView.findViewById<CheckBox>(R.id.sitem)
    var third = itemView.findViewById<CheckBox>(R.id.titem)
    var dots = itemView.findViewById<TextView>(R.id.dots)
    var count = itemView.findViewById<TextView>(R.id.count)
}

interface MainNoteListener{
    fun onClickListener(note: Note)
}