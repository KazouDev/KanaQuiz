package fr.enssat.kanaquiz.elevache

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.enssat.kanaquiz.elevache.data.Kana
import fr.enssat.kanaquiz.elevache.data.KanaRepository
import fr.enssat.kanaquiz.elevache.data.Kanji
import fr.enssat.kanaquiz.elevache.tts.TtsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class Category(val title: String) {
    HIRAGANA("Hiragana"),
    KATAKANA("Katakana"),
    KANJI("Kanji")
}

class KanaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = KanaRepository(application)
    private val ttsManager = TtsManager(application)

    var hiraganas by mutableStateOf(emptyList<Kana>())
        private set
    var katakanas by mutableStateOf(emptyList<Kana>())
        private set
    var kanjis by mutableStateOf(emptyList<Kanji>())
        private set

    var isLoading by mutableStateOf(true)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var ttsError by mutableStateOf<String?>(null)
        private set

    var category by mutableStateOf(Category.HIRAGANA)
        private set

    var index by mutableIntStateOf(0)
        private set

    private var clickCount = 0

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            try {
                val newHiraganas = withContext(Dispatchers.IO) { repository.getHiraganas() }
                val newKatakanas = withContext(Dispatchers.IO) { repository.getKatakanas() }
                val newKanjis = withContext(Dispatchers.IO) { repository.getKanjis() }
                hiraganas = newHiraganas
                katakanas = newKatakanas
                kanjis = newKanjis
            } catch (e: Exception) {
                error = "Impossible de charger les données : ${e.message}"
            }
            isLoading = false
        }
    }

    fun size(): Int {
        return when (category) {
            Category.HIRAGANA -> hiraganas.size
            Category.KATAKANA -> katakanas.size
            Category.KANJI -> kanjis.size
        }
    }

    fun selectCategory(newCategory: Category) {
        category = newCategory
        index = 0
        clickCount = 0
    }

    fun previous() {
        if (index > 0) {
            index--
            clickCount = 0
        }
    }

    fun next() {
        if (index < size() - 1) {
            index++
            clickCount = 0
        }
    }

    fun currentCharacter(): String {
        return when (category) {
            Category.HIRAGANA -> hiraganas[index].char
            Category.KATAKANA -> katakanas[index].char
            Category.KANJI -> kanjis[index].char
        }
    }

    fun speak() {
        val rate = if (clickCount % 2 == 0) 1.0f else 0.5f
        val success = ttsManager.speak(currentCharacter(), rate)
        if (success) {
            ttsError = null
            clickCount++
        } else {
            ttsError = "Synthèse vocale japonaise indisponible. Vérifiez les données vocales dans les paramètres."
        }
    }

    override fun onCleared() {
        ttsManager.release()
        super.onCleared()
    }
}
