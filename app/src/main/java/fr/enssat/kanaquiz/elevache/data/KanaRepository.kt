package fr.enssat.kanaquiz.elevache.data

import android.content.Context
import androidx.annotation.RawRes
import fr.enssat.kanaquiz.elevache.R
import kotlinx.serialization.json.Json

class KanaRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    fun loadJsonFromRaw(@RawRes rawResId: Int): String {
        return context.resources.openRawResource(rawResId).bufferedReader().use {
            it.readText()
        }
    }

    fun getHiraganas(): List<Kana> {
        return getKanas("hiragana")
    }

    fun getKatakanas(): List<Kana> {
        return getKanas("katakana")
    }

    private fun getKanas(type: String): List<Kana> {
        val texte = loadJsonFromRaw(R.raw.kana_data)
        val donnees = json.decodeFromString<Map<String, Map<String, Map<String, List<Kana>>>>>(texte)

        val resultat = mutableListOf<Kana>()
        val categories = donnees.getValue(type)
        for (lignes in categories.values) {
            for (kanas in lignes.values) {
                resultat.addAll(kanas)
            }
        }
        return resultat
    }

    fun getKanjis(): List<Kanji> {
        val texte = loadJsonFromRaw(R.raw.kanji_jouyou)
        val donnees = json.decodeFromString<Map<String, KanjiInfo>>(texte)

        val resultat = mutableListOf<Kanji>()
        for ((caractere, info) in donnees) {
            resultat.add(Kanji(caractere, info))
        }
        return resultat
    }
}
