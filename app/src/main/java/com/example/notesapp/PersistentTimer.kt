package com.example.notesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.databinding.ActivityPersistentTimerBinding
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class PersistentTimer : AppCompatActivity() {

    private lateinit var binding: ActivityPersistentTimerBinding
    private lateinit var dataHelper: DataHelper

    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Proper initialization of the binding
        binding = ActivityPersistentTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataHelper = DataHelper(applicationContext)

        binding.startButton.setOnClickListener { startStopAction() }
        binding.resetButton.setOnClickListener { resetAction() }

        if (dataHelper.isTimerCounting()) {
            startTimer()
        } else {
            stopTimer()
            if (dataHelper.getStartTime() != null && dataHelper.getStopTime() != null) {
                val time = Date().time - calcRestartTime().time
                binding.timeTV.text = timeStringFromLong(time)
            }
        }

        // Use `schedule` instead of `scheduleAtFixedRate`
        timer.schedule(TimeTask(), 0, 1000)
    }

    private inner class TimeTask : TimerTask() {
        override fun run() {
            if (dataHelper.isTimerCounting()) {
                val time = Date().time - (dataHelper.getStartTime()?.time ?: 0)
                runOnUiThread {
                    binding.timeTV.text = timeStringFromLong(time)
                }
            }
        }
    }

    private fun startStopAction() {
        if (dataHelper.isTimerCounting()) {
            dataHelper.setStopTime(Date())
            stopTimer()
        } else {
            if (dataHelper.getStopTime() != null) {
                dataHelper.setStartTime(calcRestartTime())
                dataHelper.setStopTime(null)
            } else {
                dataHelper.setStartTime(Date())
            }
            startTimer()
        }
    }

    private fun stopTimer() {
        dataHelper.setTimerCounting(false)
        binding.startButton.text = getString(R.string.start)
    }

    private fun startTimer() {
        dataHelper.setTimerCounting(true)
        binding.startButton.text = getString(R.string.stop)
    }

    private fun calcRestartTime(): Date {
        val diff = dataHelper.getStartTime()!!.time - dataHelper.getStopTime()!!.time
        return Date(System.currentTimeMillis() - diff)
    }

    private fun resetAction() {
        stopTimer()
        binding.timeTV.text = timeStringFromLong(0)
        dataHelper.setStartTime(null)
        dataHelper.setStopTime(null)
    }

    private fun timeStringFromLong(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60) % 60)
        val hours = (ms / (1000 * 60 * 60)) % 24

        // Include locale in String.format
        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String {
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }

    override fun onPause() {
        super.onPause()
        timer.cancel() // Stop the timer when the activity is paused
    }
}


//package com.example.notesapp
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import com.example.notesapp.databinding.ActivityPersistentTimerBinding
//import java.util.Date
//import java.util.Timer
//import java.util.TimerTask
//
//class PersistentTimer : AppCompatActivity() {
//
//    lateinit var binding: ActivityPersistentTimerBinding
//    lateinit var dataHelper: DataHelper
//
//    private val timer = Timer()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_persistent_timer)
//        dataHelper = DataHelper(applicationContext)
//
//        binding.startButton.setOnClickListener { startStopAction() }
//        binding.resetButton.setOnClickListener { resetAction() }
//
//        if (dataHelper.isTimerCounting()) {
//            startTimer()
//        } else {
//            stopTimer()
//            if (dataHelper.getStartTime() != null && dataHelper.getStopTime() != null) {
//                val time = Date().time - calcRestartTime().time
//                binding.timeTV.text = timeStringFromLong(time)
//            }
//        }
//
//        timer.scheduleAtFixedRate(TimeTask(), 0, 1000)
//    }
//
//    private inner class TimeTask : TimerTask() {
//        override fun run() {
//            if (dataHelper.isTimerCounting()) {
//                val time = Date().time - (dataHelper.getStartTime()?.time ?: 0)
//                runOnUiThread {
//                    binding.timeTV.text = timeStringFromLong(time)
//                }
//            }
//        }
//    }
//
//    private fun startStopAction() {
//        if (dataHelper.isTimerCounting()) {
//            dataHelper.setStopTime(Date())
//            stopTimer()
//        } else {
//            if (dataHelper.getStopTime() != null) {
//                dataHelper.setStartTime(calcRestartTime())
//                dataHelper.setStopTime(null)
//            } else {
//                dataHelper.setStartTime(Date())
//            }
//            startTimer()
//        }
//    }
//
//    private fun stopTimer() {
//        dataHelper.setTimerCounting(false)
//        binding.startButton.text = getString(R.string.start)
//    }
//
//    private fun startTimer() {
//        dataHelper.setTimerCounting(true)
//        binding.startButton.text = getString(R.string.stop)
//    }
//
//    private fun calcRestartTime(): Date {
//        val diff = dataHelper.getStartTime()!!.time - dataHelper.getStopTime()!!.time
//        return Date(System.currentTimeMillis() - diff)
//    }
//
//    private fun resetAction() {
//        stopTimer()
//        binding.timeTV.text = timeStringFromLong(0)
//        dataHelper.setStartTime(null)
//        dataHelper.setStopTime(null)
//    }
//
//    private fun timeStringFromLong(ms: Long): String {
//        val seconds = (ms / 1000) % 60
//        val minutes = (ms / (1000 * 60) % 60)
//        val hours = (ms / (1000 * 60 * 60)) % 24
//
//        return makeTimeString(hours, minutes, seconds)
//    }
//
//    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String {
//        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        timer.cancel() // Stop the timer when the activity is paused
//    }
//
//    companion object {
//        fun setOnClickListener(function: () -> Unit) {
//
//        }
//    }
//}
