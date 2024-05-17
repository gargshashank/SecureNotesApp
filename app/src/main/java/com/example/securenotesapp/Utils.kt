package com.example.securenotesapp

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Utils {
    companion object{
        fun getDateFromTimeStamp(timeStamp:String): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val date = Date(timeStamp.toLong())
            return sdf.format(date)
        }
    }
}