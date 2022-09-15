package ge.tbabunashvili.weatherapp

import android.app.Application
import android.media.Image
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import org.w3c.dom.Text
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherAdapter(var list: MutableList<HourlyWeatherItem>, var baseImg: String,
                     var endImg: String): RecyclerView.Adapter<WeatherItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherItemViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.hourly_weather_item,parent,false)
        return WeatherItemViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: WeatherItemViewHolder, position: Int) {
        holder.date.text = list[position].dt_txt
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val localDateTime = LocalDateTime.parse(list[position].dt_txt, pattern)
        val formatter = DateTimeFormatter.ofPattern("hh a dd MMMM")
        holder.date.text = localDateTime.format(formatter)
        Glide.with(holder.date.context).load(baseImg + list[position].weather[0].icon + endImg)
            .into(holder.weathericon)
        val temp = list[position].main.temp.toInt()
        holder.temp.text = temp.toString() + "°"
        holder.description.text = list[position].weather[0].description
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class WeatherItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var date = itemView.findViewById<TextView>(R.id.date)
    var weathericon = itemView.findViewById<ImageView>(R.id.hweathericon)
    var temp = itemView.findViewById<TextView>(R.id.htemp)
    var description = itemView.findViewById<TextView>(R.id.hweatherdesc)
}