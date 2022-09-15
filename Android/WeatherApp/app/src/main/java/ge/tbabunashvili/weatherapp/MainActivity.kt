package ge.tbabunashvili.weatherapp

import android.graphics.drawable.GradientDrawable
import android.media.MediaCodec
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Date
import java.sql.Timestamp
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val weather = WeatherToday()
        val forecast = WeatherHourly()
        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = ViewPagerAdapter(weather, forecast, this)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        findViewById<ImageView>(R.id.daily).setOnClickListener { img ->
            viewPager.setCurrentItem(0, true)
        }
        findViewById<ImageView>(R.id.hourly).setOnClickListener { img ->
            viewPager.setCurrentItem(1, true)
        }
        // viewPager.setCurrentItem(0, true)
        //findViewById<ScrollView>(R.id.scroll_layout).setBackgroundColor(weather.getBackgroundColor())
      //  changeFragment(weather)
      //  bottomNavigation(weather,forecast)
    }

    private fun getBackgroundColor(weatherItem: WeatherItem) {
        val stamp = Timestamp(weatherItem.dt * 1000)
        val date = Date(stamp.time)
        val cal = Calendar.getInstance()
        cal.time = date
        val hours = cal.get(Calendar.HOUR_OF_DAY)
        var background : Int
        if (hours in 6..18) {
            background = ContextCompat.getColor(applicationContext,R.color.day)
        }   else {
            background = ContextCompat.getColor(applicationContext,R.color.night)
        }
      //  findViewById<LinearLayout>(R.id.main).setBackgroundColor(background)
    }

    /*private fun bottomNavigation(weather: WeatherToday, forecast: WeatherHourly) {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.today->{
                    changeFragment(weather)
                }
                R.id.hourly->{
                    changeFragment(forecast)
                }
            }
            true
        }
    }*/


}