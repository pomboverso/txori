package com.rama.txori.activities

import android.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rama.txori.R
import com.rama.txori.widgets.WdButton

class StopwatchFragment : Fragment() {

    private lateinit var counterView: android.widget.TextView
    private lateinit var counterStartButton: WdButton
    private lateinit var counterResetButton: WdButton

    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false
    private var startTime = 0L
    private var pausedElapsed = 0L

    private val ticker = object : Runnable {
        override fun run() {
            if (isRunning) {
                val elapsed = SystemClock.elapsedRealtime() - startTime
                counterView.text = formatTime(elapsed)
                handler.postDelayed(this, 100)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.view_stopwatch, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        counterView        = view.findViewById(R.id.counter)
        counterStartButton = view.findViewById(R.id.start_stopwatch)
        counterResetButton = view.findViewById(R.id.reset_counter)

        counterView.setOnClickListener { toggleStopwatch() }
        counterView.setOnLongClickListener { resetStopwatch(); true }
        counterStartButton.setOnClickListener { toggleStopwatch() }
        counterResetButton.setOnClickListener { resetStopwatch() }
    }

    private fun toggleStopwatch() {
        if (isRunning) pauseStopwatch() else startStopwatch()
    }

    private fun startStopwatch() {
        // Hide navbar while stopwatch is running
        (activity as? MainActivity)?.setNavbarVisible(false)
        startTime = SystemClock.elapsedRealtime() - pausedElapsed
        isRunning = true
        counterStartButton.setText("Pause stopwatch")
        handler.post(ticker)
    }

    private fun pauseStopwatch() {
        (activity as? MainActivity)?.setNavbarVisible(true)
        pausedElapsed = SystemClock.elapsedRealtime() - startTime
        isRunning = false
        counterStartButton.setText("Start stopwatch")
        handler.removeCallbacks(ticker)
    }

    private fun resetStopwatch() {
        (activity as? MainActivity)?.setNavbarVisible(true)
        isRunning = false
        pausedElapsed = 0L
        counterStartButton.setText("Start stopwatch")
        handler.removeCallbacks(ticker)
        counterView.text = "0"
    }

    private fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val hours   = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        val tenths  = (ms % 1000) / 100

        return when {
            hours > 0   -> "$hours:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}.$tenths"
            minutes > 0 -> "$minutes:${seconds.toString().padStart(2, '0')}.$tenths"
            else        -> "$seconds.$tenths"
        }
    }

    override fun onDestroyView() {
        handler.removeCallbacks(ticker)
        super.onDestroyView()
    }
}
