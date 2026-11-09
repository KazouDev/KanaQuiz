package fr.enssat.kanaquiz.elevache.stats

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Long,
    val title: String,
    val correctCount: Int,
    val questionCount: Int,
    val score: Double,
    val isFake: Boolean = false
)
