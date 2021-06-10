package com.example.astroweather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class Settings : AppCompatActivity() {
    private var latitude: Double = 0.0
    private var altitude: Double = 0.0
    private var freq: Int = 0
    private lateinit var spinner:Spinner
    private lateinit var spinnerUnits:Spinner
    lateinit var altitudePlain:EditText
    lateinit var latitudePlain:EditText
    lateinit var editCity: EditText
    lateinit var unit:String
    lateinit var location:String

    fun send(view: View){
        val intent = Intent(this,MainActivity::class.java).apply{}
        latitude = latitudePlain.text.toString().toDouble()
        altitude = altitudePlain.text.toString().toDouble()
        location = editCity.text.toString()
        intent.putExtra("latitude",latitude.toString())
        intent.putExtra("altitude",altitude.toString())
        intent.putExtra("freq",freq.toString())
        intent.putExtra("location",location)
        intent.putExtra("unit",unit)
        startActivity(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        altitudePlain = findViewById(R.id.altitudePlain)
        latitudePlain = findViewById(R.id.latitudePlain)
        spinner = findViewById(R.id.spinner)
        spinnerUnits = findViewById(R.id.spinnerUnits)
        editCity = findViewById(R.id.editCity)
        val minutes = resources.getStringArray(R.array.Minutes)
        val units = resources.getStringArray(R.array.Units)

        altitudePlain.setText(getIntent().getStringExtra("altitude"))
        latitudePlain.setText(getIntent().getStringExtra("latitude"))
        editCity.setText(getIntent().getStringExtra("location"))
        freq = (getIntent().getStringExtra("freq"))?.toInt() ?:  1

        if(spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, minutes
            )
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    freq = minutes[position].toInt()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some actionq
                }
            }
        }

        if(spinnerUnits != null) {
            val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item, units
            )
            spinnerUnits.adapter = adapter
            spinnerUnits.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View, position: Int, id: Long
                ) {
                    unit = units[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some actionq
                }
            }
        }
    }


}