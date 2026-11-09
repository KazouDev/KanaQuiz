package fr.enssat.kanaquiz.elevache

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fr.enssat.kanaquiz.elevache.stats.AppDatabase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CleanFakeResultsTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val dao = AppDatabase.getInstance(context).quizResultDao()

    @Test
    fun deleteOnlyFakeResults() = runBlocking {
        val realResultsBefore = dao.getResults().filter { !it.isFake }

        dao.deleteFakeResults()

        val resultsAfter = dao.getResults()
        assertEquals(0, resultsAfter.count { it.isFake })
        assertEquals(realResultsBefore, resultsAfter)
    }
}
