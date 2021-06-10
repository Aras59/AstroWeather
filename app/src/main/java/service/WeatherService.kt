package service

import android.net.Uri
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class WeatherService{
    private var temperatureUnit = "C"
    lateinit var urlW: URL

    constructor(tempUnit:String) {
        this.temperatureUnit = tempUnit
    }

    fun getTemperatureUnit(): String {
        return this.getTemperatureUnit()
    }

    @Throws(FileNotFoundException::class)
    fun getWeatherString(location: String): String {
        var string:String ="Brak Danych"
        val t = Thread(Runnable {

            try {
                if(temperatureUnit=="C"){
                    urlW= URL("https://api.openweathermap.org/data/2.5/weather?q="+location+"&units=metric&APPID=68ebf3cd42007df3b453de1f33a79e49")
                }
                if(temperatureUnit=="F"){
                    urlW= URL("https://api.openweathermap.org/data/2.5/weather?q="+location+"&units=imperial&APPID=68ebf3cd42007df3b453de1f33a79e49")
                }
                if(temperatureUnit=="K"){
                    urlW= URL("https://api.openweathermap.org/data/2.5/weather?q="+location+"&units=standard&APPID=68ebf3cd42007df3b453de1f33a79e49")
                }

                var connection: HttpURLConnection = urlW.openConnection() as HttpURLConnection
                connection.connect()

                var input: InputStream = connection.getInputStream()
                var reader: BufferedReader = BufferedReader(InputStreamReader(input))
                var result: StringBuilder = java.lang.StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    result.append(line)
                }
                string = result.toString()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }catch (e:FileNotFoundException){
                string = "404"
            }})

        t.start()
        t.join()
        return string
    }

    fun getImage(code: String): ByteArray {
        val buffer = ByteArray(4096)
        val t = Thread(Runnable {

            try {
                var url: URL = URL("https://openweathermap.org/img/w/"+code+".png")

                var connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.connect()

                var input: InputStream = connection.getInputStream()
                val baos = ByteArrayOutputStream()
                while (input.read(buffer) !== -1) baos.write(buffer)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }})

        t.start()
        t.join()
        return buffer
    }

    @Throws(FileNotFoundException::class)
    fun getHourlyWeather(location:String):String{
        var string:String ="Brak Danych"
        val t = Thread(Runnable {

            try {
                if(temperatureUnit=="C"){
                    urlW= URL("https://api.openweathermap.org/data/2.5/forecast?q="+location+"&units=metric&APPID=68ebf3cd42007df3b453de1f33a79e49")
                }
                if(temperatureUnit=="F"){
                    urlW= URL("https://api.openweathermap.org/data/2.5/forecast?q="+location+"&units=imperial&APPID=68ebf3cd42007df3b453de1f33a79e49")
                }
                if(temperatureUnit=="K"){
                    urlW= URL("https://api.openweathermap.org/data/2.5/forecast?q="+location+"&units=standart&APPID=68ebf3cd42007df3b453de1f33a79e49")
                }

                var connection: HttpURLConnection = urlW.openConnection() as HttpURLConnection
                connection.connect()

                var input: InputStream = connection.getInputStream()
                var reader: BufferedReader = BufferedReader(InputStreamReader(input))
                var result: StringBuilder = java.lang.StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    result.append(line)
                }
                string = result.toString()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }catch (e:FileNotFoundException){
                string = "404"
            }})

        t.start()
        t.join()
        return string

    }
}