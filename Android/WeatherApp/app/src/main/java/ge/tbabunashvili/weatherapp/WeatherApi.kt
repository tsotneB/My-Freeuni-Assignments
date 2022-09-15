package ge.tbabunashvili.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather?")
    fun getCurrentWeather(@Query("q") city: String, @Query("appid") apiKey: String,
                          @Query("units") units: String): Call<WeatherItem>

    @GET("data/2.5/forecast?")
    fun getHourlyWeather(@Query("q") city: String, @Query("appid") apiKey: String,
                        @Query("units") unit:String): Call<HourlyWeather>

}