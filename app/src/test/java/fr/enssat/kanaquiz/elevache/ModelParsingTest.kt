package fr.enssat.kanaquiz.elevache

import fr.enssat.kanaquiz.elevache.data.Kana
import fr.enssat.kanaquiz.elevache.data.KanjiInfo
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class ModelParsingTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun lireUnKana() {
        val texte = """{ "char": "あ", "romaji": "a" }"""

        val kana = json.decodeFromString<Kana>(texte)

        assertEquals("あ", kana.char)
        assertEquals("a", kana.romaji)
    }

    @Test
    fun lireUnKanji() {
        val texte = """
            {
              "一": {
                "strokes": 1,
                "grade": 1,
                "meanings": ["One", "One Radical (no.1)"],
                "readings_on": ["いち", "いつ"],
                "readings_kun": ["ひと-", "ひと.つ"]
              }
            }
        """

        val kanjis = json.decodeFromString<Map<String, KanjiInfo>>(texte)
        val info = kanjis.getValue("一")

        assertEquals(1, info.grade)
        assertEquals("One", info.meanings[0])
        assertEquals("いち", info.readingsOn[0])
        assertEquals("ひと.つ", info.readingsKun[1])
    }
}
