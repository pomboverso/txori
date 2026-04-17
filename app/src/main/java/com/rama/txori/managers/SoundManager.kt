package com.rama.txori.managers

import android.media.AudioManager
import android.media.ToneGenerator

object SoundManager {

    private var toneGen: ToneGenerator? = null

    fun init() {
        if (toneGen == null) {
            toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        }
    }

    fun beepTick() {
        toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
    }

    fun beepFinish() {
        toneGen?.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 150)
    }

    fun release() {
        toneGen?.release()
        toneGen = null
    }
}