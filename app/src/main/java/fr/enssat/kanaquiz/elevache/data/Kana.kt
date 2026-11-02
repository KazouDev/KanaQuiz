package fr.enssat.kanaquiz.elevache.data

import kotlinx.serialization.Serializable

@Serializable
data class Kana(
    val char: String,
    val romaji: String
)
