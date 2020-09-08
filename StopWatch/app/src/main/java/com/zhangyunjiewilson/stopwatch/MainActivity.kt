package com.zhangyunjiewilson.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {

    private var seconds: Int = 0
    private var running: Boolean = false
    private var wasRunning: Boolean = false
    private val handler = Handler(Looper.getMainLooper())

    private val runnable = Runnable {
        updateDisplay()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds")
            running = savedInstanceState.getBoolean("running")
            wasRunning = savedInstanceState.getBoolean("wasRunning")
        }

        runTimer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("seconds", seconds)
        outState.putBoolean("running", running)
        outState.putBoolean("wasRunning", wasRunning)
    }

    override fun onPause() {
        super.onPause()
        wasRunning = running
        running = false
    }

    override fun onResume() {
        super.onResume()
        if (wasRunning) {
            running = true
        }
    }

    fun onClickStart(view: View) {
        running = true
    }

    fun onClickStop(view: View) {
        running = false
    }

    fun onClickReset(view: View) {
        running = false
        seconds = 0
    }

    private fun runTimer() {
        handler.post(runnable)
    }

    private fun updateDisplay() {
        var hours = seconds / 3600
        var mins = seconds % 3600 / 60
        var secs = seconds % 60
        val format: String = "%2d:%2d:%2d"
        val time: String = format.format(Locale.getDefault(), hours, mins, secs)
        val timeText: TextView = findViewById(R.id.time_view)
        timeText.text = time
        if (running) {
            seconds++
        }
        handler.postDelayed(runnable, 1000)
    }
}