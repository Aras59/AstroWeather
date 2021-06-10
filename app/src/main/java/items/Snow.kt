package items

class Snow {
    private var time: String? = null
    private var ammount = 0f

    fun getTime(): String? {
        return time
    }

    fun setTime(time: String?) {
        this.time = time
    }

    fun getAmmount(): Float {
        return ammount
    }

    fun setAmmount(ammount: Float) {
        this.ammount = ammount
    }

}