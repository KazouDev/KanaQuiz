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

enum class Categorie(val titre: String) {
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

    var chargement by mutableStateOf(true)
        private set
    var erreur by mutableStateOf<String?>(null)
        private set
    var erreurTts by mutableStateOf<String?>(null)
        private set

    var categorie by mutableStateOf(Categorie.HIRAGANA)
        private set

    var index by mutableIntStateOf(0)
        private set

    private var compteurClics = 0

    init {
        charger()
    }

    private fun charger() {
        viewModelScope.launch {
            try {
                val nouveauxHiraganas = withContext(Dispatchers.IO) { repository.getHiraganas() }
                val nouveauxKatakanas = withContext(Dispatchers.IO) { repository.getKatakanas() }
                val nouveauxKanjis = withContext(Dispatchers.IO) { repository.getKanjis() }
                hiraganas = nouveauxHiraganas
                katakanas = nouveauxKatakanas
                kanjis = nouveauxKanjis
            } catch (e: Exception) {
                erreur = "Impossible de charger les données : ${e.message}"
            }
            chargement = false
        }
    }

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
        val reussi = ttsManager.parler(caractereCourant(), vitesse)
        if (reussi) {
            erreurTts = null
            compteurClics++
        } else {
            erreurTts = "Synthèse vocale japonaise indisponible. Vérifiez les données vocales dans les paramètres."
        }
    }

    override fun onCleared() {
        ttsManager.liberer()
        super.onCleared()
    }
}
