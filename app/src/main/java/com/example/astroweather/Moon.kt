package com.example.astroweather

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.astrocalculator.AstroCalculator
import com.astrocalculator.AstroDateTime
import java.time.LocalDateTime


class Moon : Fragment() {

    lateinit var moonRiseView: TextView
    lateinit var moonSetView: TextView
    lateinit var newMoonView: TextView
    lateinit var fullMoonView: TextView
    lateinit var phaseView: TextView
    lateinit var synDayView: TextView
    private var latitude: Double = 51.75
    private var altitude: Double = 19.45
    private var freq: Int = 1
    lateinit var bundle: Bundle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bundle = this.arguments!!
        return inflater.inflate(R.layout.fragment_moon, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        moonRiseView = view.findViewById(R.id.moonRiseView)
        moonSetView = view.findViewById(R.id.moonSetView)
        newMoonView = view.findViewById(R.id.newMoonView)
        fullMoonView = view.findViewById(R.id.fullMoonView)
        phaseView = view.findViewById(R.id.phaseView)
        synDayView = view.findViewById(R.id.synDayView)
        freq = bundle.getInt("freq")
        latitude = bundle.getDouble("latitude")
        altitude = bundle.getDouble("altitude")

        Thread {
            while(true) {
                activity?.runOnUiThread(java.lang.Runnable {
                    var current = LocalDateTime.now()
                    var astro = AstroCalculator(
                        AstroDateTime(current.year, current.monthValue, current.dayOfMonth, current.hour, current.minute, current.second, 2, true),
                        AstroCalculator.Location(latitude, altitude)
                    )

                    var dataTimeConverter = TimeConverter(astro.moonInfo.moonrise.hour, astro.moonInfo.moonrise.minute, astro.moonInfo.moonrise.second
                    )
                    moonRiseView.text = dataTimeConverter.convert()
                    dataTimeConverter = TimeConverter(astro.moonInfo.moonset.hour, astro.moonInfo.moonset.minute, astro.moonInfo.moonset.second
                    )
                    moonSetView.text = dataTimeConverter.convert()
                    var dataConverter = DataConverter(astro.moonInfo.nextNewMoon.year, astro.moonInfo.nextNewMoon.month, astro.moonInfo.nextNewMoon.day
                    )
                    newMoonView.text = dataConverter.convert()
                    dataConverter = DataConverter(astro.moonInfo.nextFullMoon.year, astro.moonInfo.nextFullMoon.month, astro.moonInfo.nextFullMoon.day
                    )
                    fullMoonView.text = dataConverter.convert()
                    phaseView.text = astro.moonInfo.age.toString()
                    synDayView.text = astro.moonInfo.illumination.toString()

                })
                Thread.sleep(((1000)*freq*60).toLong())
            }
        }.start()

    }

}