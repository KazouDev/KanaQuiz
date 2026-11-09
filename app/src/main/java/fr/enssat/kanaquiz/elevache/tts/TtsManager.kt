package fr.enssat.kanaquiz.elevache.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {

    private val tts = TextToSpeech(context, this)

    var isReady = false
        private set

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.JAPANESE)
            isReady = result != TextToSpeech.LANG_MISSING_DATA &&
                result != TextToSpeech.LANG_NOT_SUPPORTED
        }
    }

    fun speak(text: String, rate: Float): Boolean {
        if (!isReady) {
            return false
        }
        tts.setSpeechRate(rate)
        val result = tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "kana")
        return result == TextToSpeech.SUCCESS
    }

    fun release() {
        tts.stop()
        tts.shutdown()
    }
}
