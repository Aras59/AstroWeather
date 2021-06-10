package items

class Weather  {
    private var iconData:ByteArray = ByteArray(1)
    private var condition = Condition()
    private var location = Location()
    private var temperature = Temperature()
    private var wind = Wind()
    private var rain = Rain()
    private var snow = Snow()
    private var clouds = Clouds()
    private var code:Int = 0
    private var visibilit:Int = 0

    fun getVisibility():Int{
        return visibilit
    }

    fun setVisibility(v:Int){
        this.visibilit = v
    }

    fun getMess():Int{
        return code;
    }

    fun setMess(c:Int){
        this.code = c
    }

    fun setIcon(img:ByteArray){
        this.iconData=img
    }

    fun getIcon():ByteArray{
        return this.iconData
    }

    fun setCondition(con:Condition){
        this.condition = con
    }

    fun getCondition():Condition{
        return this.condition
    }

    fun setLocation(loc:Location){
        this.location = loc
    }

    fun getLocation():Location{
        return this.location
    }

    fun setTemperature(temp:Temperature){
        this.temperature = temp
    }

    fun getTemperature():Temperature{
        return this.temperature
    }

    fun setWind(w:Wind){
        this.wind=w
    }

    fun getWind():Wind{
        return this.wind
    }

    fun setCloud(c:Clouds){
        this.clouds = c
    }

    fun getClound():Clouds{
        return this.clouds
    }


}