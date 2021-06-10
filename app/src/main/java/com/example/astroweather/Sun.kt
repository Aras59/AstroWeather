package com.example.astroweather

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.astrocalculator.AstroCalculator
import com.astrocalculator.AstroDateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Sun : Fragment() {

    lateinit var sunRiseView: TextView
    lateinit var sunSetView: TextView
    lateinit var sunRiseAzimutView: TextView
    lateinit var sunSetAzimutView: TextView
    lateinit var zmierzView: TextView
    lateinit var switView: TextView
    private var latitude: Double = 51.75
    private var altitude: Double = 19.45
    private var freq: Int = 1
    lateinit var bundle: Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bundle = this.arguments!!

        return inflater.inflate(R.layout.fragment_sun, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sunRiseView = view.findViewById(R.id.moonRiseView)
        sunSetView = view.findViewById(R.id.moonSetView)
        sunRiseAzimutView = view.findViewById(R.id.newMoonView)
        sunSetAzimutView = view.findViewById(R.id.fullMoonView)
        zmierzView = view.findViewById(R.id.phaseView)
        switView = view.findViewById(R.id.synDayView)
        freq = bundle.getInt("freq")
        latitude = bundle.getDouble("latitude")
        altitude = bundle.getDouble("altitude")


        Thread {
            while(true) {

                activity?.runOnUiThread(java.lang.Runnable {
                    var current = LocalDateTime.now()
                    var astro = AstroCalculator(AstroDateTime(current.year,current.monthValue,current.dayOfMonth,current.hour,current.minute,
                        current.second,2,true),AstroCalculator.Location(latitude,altitude))

                    var dataTimeConverter = TimeConverter(astro.sunInfo.sunrise.hour,astro.sunInfo.sunrise.minute,astro.sunInfo.sunrise.second)
                    sunRiseView.text = dataTimeConverter.convert();
                    dataTimeConverter = TimeConverter(astro.sunInfo.sunset.hour,astro.sunInfo.sunset.minute,astro.sunInfo.sunset.second)
                    sunSetView.text = dataTimeConverter.convert()
                    sunRiseAzimutView.text = astro.sunInfo.azimuthRise.toString()
                    sunSetAzimutView.text = astro.sunInfo.azimuthSet.toString()
                    dataTimeConverter = TimeConverter(astro.sunInfo.twilightEvening.hour,astro.sunInfo.twilightEvening.minute,astro.sunInfo.twilightEvening.second)
                    zmierzView.text = dataTimeConverter.convert()
                    dataTimeConverter = TimeConverter(astro.sunInfo.twilightMorning.hour,astro.sunInfo.twilightMorning.minute,astro.sunInfo.twilightMorning.second)
                    switView.text = dataTimeConverter.convert()
                })
                Thread.sleep(((1000)*freq*60).toLong())
            }
        }.start()

    }
}