package com.example.astroweather

class TimeConverter {

    private var hours:Int = 0
    private var minutes:Int = 0
    private var second:Int = 0
    private var time = String()


    constructor(h:Int,m:Int,s:Int){
        this.hours = h
        this.minutes = m
        this.second = s

    }
    public fun convert():String{
        if(hours<10){
            time = time+0
        }
        time = time+hours.toString()+":"
        if(minutes<10){
            time = time+0
        }
        time = time+minutes.toString()+":"
        if(second<10){
            time = time+0
        }
        time = time+second.toString()
        return time;
    }




}