package ge.tbabunashvili.weatherapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import kotlin.time.hours
import androidx.fragment.app.FragmentManager as FragmentManager

class WeatherToday : Fragment() {
    private var color = 0
    private lateinit var chosenCity : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        chosenCity = resources.getString(R.string.t)
        return inflater.inflate(R.layout.fragment_weather_today, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragmentManager.setFragmentResultListener("requestcity1", this) { requestKey, bundle ->
            val city = bundle.getString("city1")
            if (city != null) {
                changeCity(city, view, false)
            }
        }
        view.findViewById<ImageView>(R.id.georgianFlag).setOnClickListener { img ->
            changeCity(resources.getString(R.string.t), view, true)
        }
        view.findViewById<ImageView>(R.id.ukFlag).setOnClickListener { img ->
            changeCity(resources.getString(R.string.l), view, true)
        }
        view.findViewById<ImageView>(R.id.jamaicanFlag).setOnClickListener { img ->
            changeCity(resources.getString(R.string.k), view, true)
        }
        getData(view)
    }

    private fun changeCity(newChosenCity: String, view: View, pass: Boolean) {
        if (newChosenCity != chosenCity) {
            chosenCity = newChosenCity
            if (pass) parentFragmentManager.setFragmentResult("requestcity",
                bundleOf("city" to chosenCity))
            getData(view)
        }
    }

    private fun getData(view: View) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.baseurl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var weatherApi = retrofit.create(WeatherApi::class.java)
        var weatherCall = weatherApi.getCurrentWeather(chosenCity, resources.getString(R.string.api),
                                                        resources.getString(R.string.unit))
        weatherCall.enqueue(object: Callback<WeatherItem> {
            override fun onFailure(call: Call<WeatherItem>, t: Throwable) {
                Toast.makeText(context, t.message,Toast.LENGTH_SHORT).show();
            }

            override fun onResponse(call: Call<WeatherItem>, response: Response<WeatherItem>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        view.findViewById<TextView>(R.id.capitalLabel).text = it.name
                        val temp = it.main.temp.toInt()
                        val feels_like = it.main.feels_like.toInt()
                        view.findViewById<TextView>(R.id.tempLabel).text = temp.toString() +
                                resources.getString(R.string.deg)
                        view.findViewById<TextView>(R.id.weatherDescriptionLabel).text = it.weather[0].description.uppercase()
                        view.findViewById<TextView>(R.id.tempDataLabel).text = temp.toString() +
                                resources.getString(R.string.deg)
                        view.findViewById<TextView>(R.id.feelDataLabel).text = feels_like.toString() +
                                resources.getString(R.string.deg)
                        view.findViewById<TextView>(R.id.humDataLabel).text = it.main.humidity.toString() +
                                resources.getString(R.string.per)
                        view.findViewById<TextView>(R.id.presDataLabel).text = it.main.pressure.toString()
                        Glide.with(this@WeatherToday).load(resources.getString(R.string.baseimg) +
                                it.weather[0].icon + resources.getString(R.string.endimg)).
                        into(view.findViewById<ImageView>(R.id.weatherIcon))
                        val stamp = Timestamp(it.dt * 1000)
                        val date = Date(stamp.time)
                        val cal = Calendar.getInstance()
                        cal.time = date
                        var hours = cal.get(Calendar.HOUR_OF_DAY)
                        var background : Int
                        var offset = cal.timeZone.rawOffset
                        val milisec_to_hours = 3600000
                        val second_to_hours = milisec_to_hours / 1000
                        hours -= offset / milisec_to_hours // Get to UTC time
                        hours = (hours + it.timezone / second_to_hours).toInt() // Get to local time
                        if (hours in 6..17) {
                            background = ContextCompat.getColor(context!!,R.color.day)
                        }   else {
                            background = ContextCompat.getColor(context!!,R.color.night)
                        }
                        color = background
                        view.setBackgroundColor(background)
                        parentFragmentManager.setFragmentResult("requestbackground",
                            bundleOf("background" to background))
                    }
                }   else {
                    Toast.makeText(context, response.message(),Toast.LENGTH_SHORT).show();
                }
            }
        })
    }

}