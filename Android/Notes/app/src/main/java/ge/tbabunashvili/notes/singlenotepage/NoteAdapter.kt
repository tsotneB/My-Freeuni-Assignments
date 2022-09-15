package ge.tbabunashvili.notes.singlenotepage

import android.content.res.Resources
import android.graphics.Color
import android.media.Image
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ge.tbabunashvili.notes.R
import ge.tbabunashvili.notes.data.entity.Note

class NoteAdapter(var items: MutableList<Note>, val listener: NoteListener)
        : RecyclerView.Adapter<NoteViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.note_item,parent,false)

        return NoteViewHolder(view)
    }


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.text.setText(items[holder.adapterPosition].text)
        holder.done.isChecked = items[holder.adapterPosition].done
        holder.text.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                val it = items[holder.adapterPosition]
                val newItem = Note(it.id,holder.text.text.toString(), it.parentId,it.done, it.pinned)
                items[holder.adapterPosition] = newItem
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

            }
        })
        holder.done.setOnClickListener{
            listener.onCheckboxChange(items[holder.adapterPosition],holder.adapterPosition)
        }
        holder.text.setText(items[holder.adapterPosition].text)
        if (position == itemCount-1) {
            holder.text.requestFocus()
            holder.remove.visibility = View.VISIBLE
        }   else {
            holder.remove.visibility = View.INVISIBLE
        }
        if (items[holder.adapterPosition].done) {
            holder.text.isEnabled = false
            holder.remove.visibility = View.INVISIBLE
        }
        holder.remove.setOnClickListener {
            listener.onRemoveClick(items[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


}

class NoteViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    var done = itemView.findViewById<CheckBox>(R.id.itemDone)
    var text = itemView.findViewById<EditText>(R.id.editnote)
    var remove = itemView.findViewById<ImageView>(R.id.remove)
}

interface NoteListener{
    fun onCheckboxChange(note: Note, position: Int)
    fun onRemoveClick(note: Note)
}