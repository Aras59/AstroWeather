package com.example.astroweather

import android.content.Context
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.astrocalculator.AstroCalculator
import com.astrocalculator.AstroDateTime
import items.Weather
import service.JsonWeatherParser
import service.WeatherService
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.time.LocalDateTime

class Basic : Fragment() {
    private lateinit var weatherService: WeatherService
    private lateinit var cityView: TextView
    private lateinit var tempView: TextView
    private lateinit var tempOdczView: TextView
    private lateinit var opisView: TextView
    private lateinit var cisnienieView: TextView
    private lateinit var locationView: TextView
    private lateinit var tempMinView: TextView
    private lateinit var errorView: TextView
    private lateinit var imageView: ImageView
    private lateinit var resutl:String
    private lateinit var weather:Weather
    private lateinit var button:Button
    private var latitude: Double = 0.0
    private var altitude: Double = 0.0
    private var freq: Int = 0
    private lateinit var location:String
    private lateinit var unit:String
    private var parser = JsonWeatherParser()
    lateinit var bundle: Bundle


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bundle = this.arguments!!

        return inflater.inflate(R.layout.fragment_basic, container, false)
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        freq = bundle.getInt("freq")
        latitude = bundle.getDouble("latitude")
        altitude = bundle.getDouble("altitude")
        unit = bundle.getString("unit").toString()
        location = bundle.getString("location").toString()

        cityView = view.findViewById(R.id.cityView)
        imageView = view.findViewById(R.id.imageViewAdd)
        locationView = view.findViewById(R.id.locationView)
        opisView = view.findViewById(R.id.descView)
        cisnienieView = view.findViewById(R.id.cisnienieView)
        tempView = view.findViewById(R.id.tempView)
        tempOdczView = view.findViewById(R.id.tempMaxView)
        tempMinView = view.findViewById(R.id.tempMinView)
        errorView = view.findViewById(R.id.errorView)
        button = view.findViewById(R.id.RefreshAdd)

        Thread {
            while(true) {

                activity?.runOnUiThread(java.lang.Runnable {
                    refresh()
                })
                Thread.sleep(((1000)*1*60).toLong())
            }
        }.start()


        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
               refresh()
            }
        })


    }

    fun refresh(){
        weatherService = WeatherService(unit)
        if(verifyAvailableNetwork(activity as AppCompatActivity)){
            resutl = weatherService.getWeatherString(location)
            writeToFile(resutl)
        }else{
            resutl = readFormFile()
        }
        if(resutl!="404"){

            weather = parser.getWeather(resutl)
            if(weather.getMess()==200){

                errorView.text = ""
                if(verifyAvailableNetwork(activity as AppCompatActivity))
                weather.setIcon(weatherService.getImage(weather.getCondition().getIcon()))
                cityView.text = weather.getLocation().getCountry()+" "+weather.getLocation().getCity()
                locationView.text = weather.getLocation().getLatitude().toString()+" , "+weather.getLocation().getAltitude()
                cisnienieView.text = weather.getCondition().getPressure().toString()+" hPa"
                opisView.text = weather.getCondition().getDescr()
                tempView.text = weather.getTemperature().getTemp().toString()+" °"+unit
                tempOdczView.text = weather.getTemperature().getMaxTemp().toString()+" °"+unit
                tempMinView.text = weather.getTemperature().getMinTemp().toString()+" °"+unit
            }else{
                errorView.text = "Nieprawidłowe dane!"
            }

            if(weather.getIcon()!=null && weather.getIcon().isNotEmpty()){
                var bitmap = BitmapFactory.decodeByteArray(weather.getIcon(),0,weather.getIcon().size)
                imageView.setImageBitmap(bitmap)
            }else{
                Toast.makeText(activity, "Brak Grafiki",
                        Toast.LENGTH_LONG).show();
            }
        }else{
            errorView.text = "Nieprawidłowe dane!"
        }
    }


    private fun writeToFile(content: String) {
        val fileName = "test.txt"
        val fileBody = "test"

        context?.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
            if (output != null) {
                output.write(content.toByteArray())
            }
        }

    }

    private fun readFormFile():String{
        var str:String =""
        val fileName = "test.txt"
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

    fun verifyAvailableNetwork(activity: AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }

}