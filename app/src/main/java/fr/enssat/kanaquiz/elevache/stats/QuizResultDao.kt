package fr.enssat.kanaquiz.elevache.stats

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {

    @Insert
    suspend fun insert(result: QuizResult)

    @Query("SELECT * FROM quiz_results ORDER BY date, id")
    fun observeResults(): Flow<List<QuizResult>>

    @Query("SELECT * FROM quiz_results ORDER BY date, id")
    suspend fun getResults(): List<QuizResult>

    @Query("DELETE FROM quiz_results WHERE isFake = 1")
    suspend fun deleteFakeResults()
}
