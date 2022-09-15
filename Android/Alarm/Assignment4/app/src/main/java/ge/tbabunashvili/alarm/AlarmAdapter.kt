package ge.tbabunashvili.alarm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import ge.tbabunashvili.alarm.data.entity.Alarm


class AlarmAdapter(var items: MutableList<Alarm>, val listener: AlarmListener)
    : RecyclerView.Adapter<AlarmViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_item,parent,false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val hour = getString(items[holder.adapterPosition].hour)
        val minute = getString(items[holder.adapterPosition].minute)
        holder.timeLabel.setText(hour + ':' + minute)
        holder.active.isChecked = items[holder.adapterPosition].active
        holder.itemView.setOnLongClickListener(OnLongClickListener {
            listener.onRemoveClick(items[holder.adapterPosition], position)
            true
        })
        holder.active.setOnClickListener {
            listener.onCheckboxChange(items[holder.adapterPosition], position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


    companion object {
        fun getString(number: Int) : String {
            var answer = number.toString()
            if (number < 10) {
                answer = '0'+answer
            }
            return answer
        }
    }
}

class AlarmViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    var timeLabel = itemView.findViewById<TextView>(R.id.time)
    var active = itemView.findViewById<SwitchCompat>(R.id.changeActive)
}

interface AlarmListener{
    fun onCheckboxChange(item: Alarm, position: Int)
    fun onRemoveClick(item: Alarm, position: Int)
}