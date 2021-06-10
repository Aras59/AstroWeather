package com.example.astroweather

import android.content.Context
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import items.Weather
import service.JsonWeatherParser
import service.WeatherService


class Adding : Fragment() {

    lateinit var bundle: Bundle
    private var latitude: Double = 0.0
    private var altitude: Double = 0.0
    private var freq: Int = 0
    private lateinit var location:String
    private lateinit var unit:String
    private var parser = JsonWeatherParser()
    private lateinit var resutl:String
    private lateinit var weather: Weather
    private lateinit var windSpeed: TextView
    private lateinit var directionWind: TextView
    private lateinit var waterlevel: TextView
    private lateinit var seeView: TextView
    private lateinit var errorView: TextView
    private lateinit var cloudView: TextView
    private lateinit var weatherService: WeatherService
    private lateinit var imageView: ImageView
    private lateinit var refreshButton:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bundle = this.arguments!!
        return inflater.inflate(R.layout.fragment_adding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        windSpeed = view.findViewById(R.id.windSpeed)
        directionWind = view.findViewById(R.id.windDirection)
        waterlevel = view.findViewById(R.id.waterView)
        seeView = view.findViewById(R.id.seeView)
        cloudView = view.findViewById(R.id.cloudView)
        imageView = view.findViewById(R.id.imageViewAdd)
        refreshButton = view.findViewById(R.id.refreshbutton)
        errorView = view.findViewById(R.id.errorView2)
        freq = bundle.getInt("freq")
        latitude = bundle.getDouble("latitude")
        altitude = bundle.getDouble("altitude")
        unit = bundle.getString("unit").toString()
        location = bundle.getString("location").toString()


        Thread {
            while(true) {

                activity?.runOnUiThread(java.lang.Runnable {
                    refresh()
                })
                Thread.sleep(((1000)*10*60).toLong())
            }
        }.start()

        refreshButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                refresh()
            }
        })


    }


    fun refresh(){
        weatherService = WeatherService(unit)
        if(verifyAvailableNetwork(getActivity() as AppCompatActivity)){
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
                windSpeed.text = weather.getWind().getSpeed().toString()+" km/h"
                directionWind.text = weather.getWind().getDeg().toString()+" °"
                waterlevel.text = weather.getCondition().getHumidity().toString()+" %"
                seeView.text = weather.getVisibility().toString() +" m"
                cloudView.text = weather.getClound().getPerc().toString()+" %"
            }else{
                errorView.text = "Nieprawidłowe dane"
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

    fun verifyAvailableNetwork(activity: AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
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
    
}