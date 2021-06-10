package com.example.astroweather

class DataConverter {

    private var years:Int = 0
    private var mouths:Int = 0
    private var days:Int = 0
    private var data = String()


    constructor(y:Int,m:Int,d:Int){
        this.years = y
        this.mouths = m
        this.days = d

    }
    public fun convert():String{
        if(years<10){
            data = data+0
        }
        data = data+years.toString()+"-"
        if(mouths<10){
            data = data+0
        }
        data = data+mouths.toString()+"-"
        if(days<10){
            data = data+0
        }
        data = data+days.toString()
        return data;
    }




}