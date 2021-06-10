package items

class Temperature {
    private var temp = 0.0
    private var minTemp = 0.0
    private var maxTemp = 0.0

    fun getTemp(): Double {
        return temp
    }

    fun setTemp(temp: Double) {
        this.temp = temp
    }

    fun getMinTemp(): Double {
        return minTemp
    }

    fun setMinTemp(minTemp: Double) {
        this.minTemp = minTemp
    }

    fun getMaxTemp(): Double {
        return maxTemp
    }

    fun setMaxTemp(maxTemp: Double) {
        this.maxTemp = maxTemp
    }

}