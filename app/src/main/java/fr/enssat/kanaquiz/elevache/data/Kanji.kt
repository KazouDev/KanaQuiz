package fr.enssat.kanaquiz.elevache.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KanjiInfo(
    val grade: Int,
    val meanings: List<String>,
    @SerialName("readings_on")
    val readingsOn: List<String>,
    @SerialName("readings_kun")
    val readingsKun: List<String>
)

data class Kanji(
    val char: String,
    val info: KanjiInfo
) {
    fun meaning(): String {
        return info.meanings.firstOrNull { !it.contains("Radical") } ?: info.meanings.first()
    }

    fun reading(): String {
        val kunReadings = info.readingsKun.map { it.replace(".", "").replace("-", "") }
        return (info.readingsOn + kunReadings).first()
    }
}
