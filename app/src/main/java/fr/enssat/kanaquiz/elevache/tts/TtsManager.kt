package fr.enssat.kanaquiz.elevache.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {

    private val tts = TextToSpeech(context, this)

    var pret = false
        private set

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val resultat = tts.setLanguage(Locale.JAPANESE)
            pret = resultat != TextToSpeech.LANG_MISSING_DATA &&
                resultat != TextToSpeech.LANG_NOT_SUPPORTED
        }
    }

    fun parler(texte: String) {
        if (pret) {
            tts.speak(texte, TextToSpeech.QUEUE_FLUSH, null, "kana")
        }
    }

    fun liberer() {
        tts.stop()
        tts.shutdown()
    }
}
