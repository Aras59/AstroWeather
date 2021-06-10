package service

import items.*
import org.json.JSONException
import org.json.JSONObject

class JsonWeatherParser {

    @Throws(JSONException::class)
    public fun getWeather(data:String): Weather {
        var weather = Weather()
        var location = Location()

        var jsonObject = JSONObject(data)

        var code = jsonObject.getInt("cod")
        if(code == 200 ){
            var coordObj = jsonObject.getJSONObject("coord")
            location.setAltitude(coordObj.getDouble("lon"))
            location.setLatitude(coordObj.getDouble("lat"))

            var sysObj = jsonObject.getJSONObject("sys")
            location.setCountry(sysObj.getString("country"))
            location.setCity(jsonObject.getString("name"))

            weather.setLocation(location)

            weather.setVisibility(jsonObject.getInt("visibility"))

            var jarr = jsonObject.getJSONArray("weather")
            var jsonWeather = jarr.getJSONObject(0)
            var condition = Condition()
            var temperature = Temperature()
            condition.setWeatherId(jsonWeather.getInt("id"))
            condition.setCondition(jsonWeather.getString("main"))
            condition.setIcon(jsonWeather.getString("icon"))
            condition.setDescr(jsonWeather.getString("description"))

            var mainObj = jsonObject.getJSONObject("main")
            condition.setHumidity(mainObj.getInt("humidity"))
            condition.setPressure(mainObj.getInt("pressure"))
            weather.setCondition(condition)
            temperature.setMaxTemp(mainObj.getDouble("temp_max"))
            temperature.setMinTemp(mainObj.getDouble("temp_min"))
            temperature.setTemp(mainObj.getDouble("temp"))
            weather.setTemperature(temperature)

            var wind = Wind()
            var wObj = jsonObject.getJSONObject("wind")
            wind.setSpeed(wObj.getDouble("speed"))
            wind.setDeg(wObj.getDouble("deg"))
            weather.setWind(wind)

            var cloud = Clouds()
            var cObj = jsonObject.getJSONObject("clouds")
            cloud.setPerc(cObj.getInt("all"))
            weather.setCloud(cloud)
            weather.setMess(200)
        }else{
            weather.setMess(404)
        }
        return weather;
    }
}