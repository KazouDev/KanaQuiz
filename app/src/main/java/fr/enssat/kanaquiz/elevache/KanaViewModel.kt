package fr.enssat.kanaquiz.elevache

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import fr.enssat.kanaquiz.elevache.data.Kana
import fr.enssat.kanaquiz.elevache.data.KanaRepository
import fr.enssat.kanaquiz.elevache.data.Kanji
import fr.enssat.kanaquiz.elevache.tts.TtsManager

enum class Categorie(val titre: String) {
    HIRAGANA("Hiragana"),
    KATAKANA("Katakana"),
    KANJI("Kanji")
}

class KanaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = KanaRepository(application)
    private val ttsManager = TtsManager(application)

    val hiraganas: List<Kana> = repository.getHiraganas()
    val katakanas: List<Kana> = repository.getKatakanas()
    val kanjis: List<Kanji> = repository.getKanjis()

    var categorie by mutableStateOf(Categorie.HIRAGANA)
        private set

    var index by mutableIntStateOf(0)
        private set

    private var compteurClics = 0

    fun taille(): Int {
        return when (categorie) {
            Categorie.HIRAGANA -> hiraganas.size
            Categorie.KATAKANA -> katakanas.size
            Categorie.KANJI -> kanjis.size
        }
    }

    fun changerCategorie(nouvelle: Categorie) {
        categorie = nouvelle
        index = 0
        compteurClics = 0
    }

    fun precedent() {
        if (index > 0) {
            index--
            compteurClics = 0
        }
    }

    fun suivant() {
        if (index < taille() - 1) {
            index++
            compteurClics = 0
        }
    }

    fun caractereCourant(): String {
        return when (categorie) {
            Categorie.HIRAGANA -> hiraganas[index].char
            Categorie.KATAKANA -> katakanas[index].char
            Categorie.KANJI -> kanjis[index].char
        }
    }

    fun prononcer() {
        val vitesse = if (compteurClics % 2 == 0) 1.0f else 0.5f
        ttsManager.parler(caractereCourant(), vitesse)
        compteurClics++
    }

    override fun onCleared() {
        ttsManager.liberer()
        super.onCleared()
    }
}
