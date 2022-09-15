package ge.tbabunashvili.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherHourly : Fragment() {
    private lateinit var chosenCity : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        chosenCity=resources.getString(R.string.t)
        return inflater.inflate(R.layout.fragment_weather_hourly, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.setFragmentResultListener("requestbackground", this) { requestKey, bundle ->
            val result = bundle.getInt("background")
            view.setBackgroundColor(result)
        }
        parentFragmentManager.setFragmentResultListener("requestcity", this) { requestKey, bundle ->
            val city = bundle.getString("city")
            if (city != null) {
                changeCity(city, view, false)
            }
        }
        view.findViewById<ImageView>(R.id.georgianFlag1).setOnClickListener { img ->
            changeCity(resources.getString(R.string.t), view, true)
        }
        view.findViewById<ImageView>(R.id.ukFlag1).setOnClickListener { img ->
            changeCity(resources.getString(R.string.l), view, true)
        }
        view.findViewById<ImageView>(R.id.jamaicanFlag1).setOnClickListener { img ->
            changeCity(resources.getString(R.string.k), view, true)
        }
        getData(view)
    }

    private fun changeCity(newChosenCity: String, view: View, pass: Boolean) {
        if (newChosenCity != chosenCity) {
            chosenCity = newChosenCity
            if (pass) parentFragmentManager.setFragmentResult("requestcity1",
                bundleOf("city1" to chosenCity))
            getData(view)
        }
    }

    private fun getData(view: View) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.baseurl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var weathers = mutableListOf<HourlyWeatherItem>()
        var weatherApi = retrofit.create(WeatherApi::class.java)

        var weatherCall = weatherApi.getHourlyWeather(chosenCity, resources.getString(R.string.api),
            resources.getString(R.string.unit))
        var rvWeather = view.findViewById<RecyclerView>(R.id.rvWeather)

        weatherCall.enqueue(object: Callback<HourlyWeather> {
            override fun onFailure(call: Call<HourlyWeather>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show();
            }

            override fun onResponse(call: Call<HourlyWeather>, response: Response<HourlyWeather>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        for (weather in it.list) {
                            weathers.add(weather)
                        }
                        var adapter = WeatherAdapter(weathers, resources.getString(R.string.baseimg),
                                                        resources.getString(R.string.endimg))
                        rvWeather.adapter = adapter
                    }
                }   else {
                    Toast.makeText(context, response.message(),Toast.LENGTH_SHORT).show();
                }
            }
        })
    }

}