package com.example.astroweather

import android.content.Context
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import service.JsonWeatherParser
import service.WeatherService
import java.text.SimpleDateFormat
import java.util.*


class Weather : Fragment() {
    lateinit var bundle: Bundle
    private var latitude: Double = 0.0
    private var altitude: Double = 0.0
    private var freq: Int = 0
    private lateinit var location:String
    private lateinit var unit:String
    private lateinit var resutl:String
    private lateinit var weatherService: WeatherService
    private lateinit var day1View:TextView
    private lateinit var image1view:ImageView
    private lateinit var day1Temp:TextView
    private lateinit var day2View:TextView
    private lateinit var image2view:ImageView
    private lateinit var day2Temp:TextView
    private lateinit var day3View:TextView
    private lateinit var image3view:ImageView
    private lateinit var day3Temp:TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bundle = this.arguments!!
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        freq = bundle.getInt("freq")
        latitude = bundle.getDouble("latitude")
        altitude = bundle.getDouble("altitude")
        unit = bundle.getString("unit").toString()
        location = bundle.getString("location").toString()
        day1View = view.findViewById(R.id.day1View)
        image1view = view.findViewById(R.id.image1View)
        day1Temp = view.findViewById(R.id.day1Temp)
        day2View = view.findViewById(R.id.day2View)
        image2view = view.findViewById(R.id.image2View)
        day2Temp = view.findViewById(R.id.day2Temp)
        day3View = view.findViewById(R.id.day3View)
        image3view = view.findViewById(R.id.image3View)
        day3Temp = view.findViewById(R.id.day3Temp)

        Thread {
            while(true) {

                activity?.runOnUiThread(java.lang.Runnable {
                    refresh()
                })
                Thread.sleep(((1000)*10*60).toLong())
            }
        }.start()

    }

    fun getJsonIndex(jsonObj:JSONObject,index:Int):JSONObject{
        var jsonArray = jsonObj.getJSONArray("list")
        var jsonindex = jsonArray.getJSONObject(index*8)
        return jsonindex
    }

    fun unixToDate(timeStamp: Long) : String? {
        val time = java.util.Date(timeStamp as Long * 1000)
        val sdf = SimpleDateFormat("yyyy-MM-dd hh-mm")
        return sdf.format(time)

    }

    fun verifyAvailableNetwork(activity: AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }

    private fun writeToFile(content: String) {
        val fileName = "test1.txt"

        context?.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
            if (output != null) {
                output.write(content.toByteArray())
            }
        }

    }

    private fun readFormFile():String{
        var str:String =""
        val fileName = "test1.txt"
        context?.openFileInput(fileName).use { stream ->
            val text = stream?.bufferedReader().use{
                it?.readText()
            }
            if (text != null) {
                str = text
            }
        }
        return str
    }

    fun refresh(){
        weatherService = WeatherService(unit)

        if(verifyAvailableNetwork(getActivity() as AppCompatActivity)){
            resutl = weatherService.getHourlyWeather(location)
            writeToFile(resutl)
        }else{
            resutl = readFormFile()
        }

        if(resutl!="404"){
            var jsonObj = JSONObject(resutl)
            var index1 = getJsonIndex(jsonObj,0)
            var index2 = getJsonIndex(jsonObj,1)
            var index3 = getJsonIndex(jsonObj,2)
            var index1image = index1.getJSONArray("weather")
            var index2image = index2.getJSONArray("weather")
            var index3image = index3.getJSONArray("weather")
            var index1imageindex = index1image.getJSONObject(0)
            var index2imageindex = index2image.getJSONObject(0)
            var index3imageindex = index3image.getJSONObject(0)
            var index1icon = index1imageindex.getString("icon")
            var index2icon = index2imageindex.getString("icon")
            var index3icon = index3imageindex.getString("icon")

            day1View.text = unixToDate(index1.getLong("dt"))

            day1Temp.text = index1.getJSONObject("main").getDouble("temp").toString()+" °C"
            day2View.text = unixToDate(index2.getLong("dt"))
            day2Temp.text = index2.getJSONObject("main").getDouble("temp").toString()+" °C"
            day3Temp.text = index3.getJSONObject("main").getDouble("temp").toString()+" °C"
            day3View.text = unixToDate(index3.getLong("dt"))


            if(verifyAvailableNetwork(getActivity() as AppCompatActivity)){
                var array:ByteArray= weatherService.getImage(index1icon)

                if(array!=null && array.isNotEmpty()){
                    var bitmap = BitmapFactory.decodeByteArray(array,0,array.size)
                    image1view.setImageBitmap(bitmap)
                }else{
                    Toast.makeText(activity, "Brak Grafiki",
                            Toast.LENGTH_LONG).show();
                }

                array = weatherService.getImage(index2icon)

                if(array!=null && array.isNotEmpty()){
                    var bitmap = BitmapFactory.decodeByteArray(array,0,array.size)
                    image2view.setImageBitmap(bitmap)
                }else{
                    Toast.makeText(activity, "Brak Grafiki",
                            Toast.LENGTH_LONG).show();
                }

                array = weatherService.getImage(index3icon)

                if(array!=null && array.isNotEmpty()){
                    var bitmap = BitmapFactory.decodeByteArray(array,0,array.size)
                    image3view.setImageBitmap(bitmap)
                }else{
                    Toast.makeText(activity, "Brak Grafiki",
                            Toast.LENGTH_LONG).show();
                }
            }

        }
    }

}
