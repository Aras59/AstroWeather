package items

class Location {
    private var latitude:Double = 0.0
    private var altitude:Double = 0.0
    private lateinit var city:String
    private lateinit var country:String

    fun getAltitude(): Double {
        return altitude
    }

    fun setAltitude(longitude: Double) {
        altitude = longitude
    }

    fun getLatitude(): Double {
        return latitude
    }

    fun setLatitude(latitude: Double) {
        this.latitude = latitude
    }


    fun getCountry(): String {
        return country
    }

    fun setCountry(country: String) {
        this.country = country
    }

    fun getCity(): String {
        return city
    }

    fun setCity(city: String) {
        this.city = city
    }

}