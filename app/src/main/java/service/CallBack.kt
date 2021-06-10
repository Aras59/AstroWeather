package service

import java.lang.Exception

interface CallBack {
    fun serviceSuccess()
    fun serviceFail(exception: Exception)
}