package com.example.notesapp

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class DataHelper(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
    private val dateFormatter = SimpleDateFormat("MM:dd:yyyy HH:mm:ss", Locale.getDefault())

    private var timerCounting = false
    private var startTime: Date? = null
    private var stopTime: Date? = null

    init {
        timerCounting = sharedPreferences.getBoolean(COUNTING_KEY, false)

        val startString = sharedPreferences.getString(START_TIME_KEY, null)
        if (startString != null) {
            startTime = dateFormatter.parse(startString)
        }

        val stopString = sharedPreferences.getString(STOP_TIME_KEY, null)
        if (stopString != null) {
            stopTime = dateFormatter.parse(stopString)
        }
    }

    // Getter for startTime, renamed for clarity
    fun getStartTime(): Date? = startTime

    // Setter for startTime, persists to SharedPreferences
    fun setStartTime(startTime: Date?) {
        this.startTime = startTime
        with(sharedPreferences.edit()) {
            val stringDate = startTime?.let { dateFormatter.format(it) }
            putString(START_TIME_KEY, stringDate)
            apply()
        }
    }

    // Getter for stopTime, renamed for clarity
    fun getStopTime(): Date? = stopTime

    // Setter for stopTime, persists to SharedPreferences
    fun setStopTime(stopTime: Date?) {
        this.stopTime = stopTime
        with(sharedPreferences.edit()) {
            val stringDate = stopTime?.let { dateFormatter.format(it) }
            putString(STOP_TIME_KEY, stringDate)
            apply()
        }
    }

    // Getter for timerCounting
    fun isTimerCounting(): Boolean = timerCounting

    // Setter for timerCounting
    fun setTimerCounting(value: Boolean) {
        timerCounting = value
        with(sharedPreferences.edit()) {
            putBoolean(COUNTING_KEY, value)
            apply()
        }
    }

    companion object {
        const val PREFERENCE = "prefs"
        const val START_TIME_KEY = "startKey"
        const val STOP_TIME_KEY = "stopKey"
        const val COUNTING_KEY = "countingKey"
    }
}
