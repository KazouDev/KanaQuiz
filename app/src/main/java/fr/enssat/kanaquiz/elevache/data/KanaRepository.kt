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
        val text = loadJsonFromRaw(R.raw.kana_data)
        val data = json.decodeFromString<Map<String, Map<String, Map<String, List<Kana>>>>>(text)

        val result = mutableListOf<Kana>()
        val groups = data.getValue(type)
        for (lines in groups.values) {
            for (kanas in lines.values) {
                result.addAll(kanas)
            }
        }
        return result
    }

    fun getKanjis(): List<Kanji> {
        val text = loadJsonFromRaw(R.raw.kanji_jouyou)
        val data = json.decodeFromString<Map<String, KanjiInfo>>(text)

        val result = mutableListOf<Kanji>()
        for ((character, info) in data) {
            result.add(Kanji(character, info))
        }
        return result
    }
}
