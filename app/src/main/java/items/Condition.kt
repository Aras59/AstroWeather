package items

class Condition {
    private var weatherId = 0
    private var condition: String? = null
    private var descr: String = ""
    private var icon: String = ""
    private var pressure = 0
    private var humidity = 0

    fun getWeatherId(): Int {
        return weatherId
    }

    fun setWeatherId(weatherId: Int) {
        this.weatherId = weatherId
    }

    fun getCondition(): String? {
        return condition
    }

    fun setCondition(condition: String?) {
        this.condition = condition
    }

    fun getDescr(): String {
        return descr
    }

    fun setDescr(descr: String) {
        this.descr = descr
    }

    fun getIcon(): String {
        return icon
    }

    fun setIcon(icon: String) {
        this.icon = icon
    }

    fun getPressure(): Int {
        return pressure
    }

    fun setPressure(pressure: Int ) {
        this.pressure = pressure
    }

    fun getHumidity(): Int  {
        return humidity
    }

    fun setHumidity(humidity: Int ) {
        this.humidity = humidity
    }

}