package ge.tbabunashvili.weatherapp

data class WeatherItem(val weather: List<SingleWeatherItem>, val name: String,
                       val main: MainWeatherInfo, val dt: Long, val timezone: Long)

data class SingleWeatherItem(val description: String, val icon: String)

data class MainWeatherInfo(val temp: Float, val feels_like: Float, val humidity: Int,
                           val pressure: Int)

data class HourlyWeatherItem(val dt: Long, val dt_txt: String, val main: MainWeatherInfo, val weather: List<SingleWeatherItem>)

data class HourlyWeather(val list: List<HourlyWeatherItem>)