package com.example.astroweather

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2;
    private lateinit var tabs: TabLayout;
    private lateinit var timeView:TextView;
    private lateinit var latitudeView:TextView;
    private lateinit var altitudeView:TextView;
    private var latitude: Double = 51.75
    private var altitude: Double = 19.45
    private var freq: Int = 1
    private var unit:String = "C"
    private var location:String = "Lodz,pl"


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager=findViewById(R.id.ViewPager2)
        tabs = findViewById(R.id.tabs)
        timeView = findViewById(R.id.timeView)
        altitude = (getIntent().getStringExtra("altitude"))?.toDouble() ?:altitude
        latitude = (getIntent().getStringExtra("latitude"))?.toDouble() ?: latitude
        freq = (getIntent().getStringExtra("freq"))?.toInt() ?:  1

        if((getIntent().getStringExtra("unit")) != null)
        unit = (getIntent().getStringExtra("unit").toString())

        if((getIntent().getStringExtra("location"))!=null)
        location = (getIntent().getStringExtra("location")).toString()
        latitudeView = findViewById(R.id.latitudeTextView)
        altitudeView = findViewById(R.id.altitudeTextView)
        latitudeView.text = "Szerokość:"+" "+latitude.toString()
        altitudeView.text = "Długość:"+" "+ altitude.toString()

        System.out.println(verifyAvailableNetwork(this).toString())

        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        Thread {
            while(true){
                var currentDateTime = LocalDateTime.now()
                val formatted = currentDateTime.format(formatter)
                timeView.text = formatted.toString()
                Thread.sleep(1000)
            }
        }.start()

        val fragments:ArrayList<Fragment> = arrayListOf(Sun(),Moon(),Basic(),Adding(),Weather())

        for(f in fragments){
            val bundle = Bundle()
            bundle.putInt("freq",freq)
            bundle.putDouble("latitude",latitude)
            bundle.putDouble("altitude",altitude)
            bundle.putString("unit",unit)
            bundle.putString("location",location)
            f.arguments = bundle
        }

        val adapter = MyViewPagerAdapter(fragments,this)
        viewPager.adapter = adapter
        TabLayoutMediator(tabs,viewPager){
            tab, position -> if(position==0) tab.text="Sun"
            if(position==1) tab.text="Moon"
            if(position==2) tab.text="Basic"
            if(position==3) tab.text="Adding"
            if(position==4) tab.text="Info"
        }.attach()
    }

    override fun onBackPressed() {
        if(viewPager.currentItem == 0 ){
            super.onBackPressed()
        }else{
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var menuInflater:MenuInflater = menuInflater;
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings->{
                val intent = Intent(this,Settings::class.java).apply{}
                intent.putExtra("latitude",latitude.toString())
                intent.putExtra("altitude",altitude.toString())
                intent.putExtra("freq",freq.toString())
                intent.putExtra("unit",unit)
                intent.putExtra("location",location)
                startActivity(intent)
            }
            R.id.author->{
                val intent = Intent(this,Author::class.java).apply{}
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item);
    }

    fun verifyAvailableNetwork(activity:AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }

}


