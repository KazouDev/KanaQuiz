package fr.enssat.kanaquiz.elevache

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.mikephil.charting.charts.LineChart
import fr.enssat.kanaquiz.elevache.stats.AppDatabase
import fr.enssat.kanaquiz.elevache.stats.GradeCalculator
import fr.enssat.kanaquiz.elevache.stats.QuizResult
import fr.enssat.kanaquiz.elevache.ui.PROGRESS_CHART_DESCRIPTION
import fr.enssat.kanaquiz.elevache.ui.StatsScreen
import fr.enssat.kanaquiz.elevache.ui.theme.MyApplicationTheme
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

const val ONE_DAY = 24 * 60 * 60 * 1000L

@RunWith(AndroidJUnit4::class)
class GenerateFakeResultsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val dao = AppDatabase.getInstance(context).quizResultDao()

    private val fakeScores = listOf(1.0, 1.5, 2.0, 2.2, 2.8, 3.0, 3.1, 3.5, 4.0, 4.2, 4.8, 5.0)

    @Before
    fun insertFakeResults() = runBlocking {
        dao.deleteFakeResults()
        val start = System.currentTimeMillis() - fakeScores.size * ONE_DAY
        for ((i, score) in fakeScores.withIndex()) {
            val result = QuizResult(
                date = start + i * ONE_DAY,
                title = "Quiz factice ${i + 1}",
                correctCount = 5 + i % 6,
                questionCount = 10,
                score = score,
                isFake = true
            )
            dao.insert(result)
        }
    }

    private fun getFakeResults(): List<QuizResult> = runBlocking {
        dao.getResults().filter { it.isFake }
    }

    @Test
    fun gradeIsCorrect() {
        val results = getFakeResults()
        val history = GradeCalculator.gradeHistory(results)

        assertEquals(12, results.size)
        assertEquals(1.0, history[0], 0.001)
        assertEquals(2.73, history[9], 0.001)
        assertEquals(3.46, GradeCalculator.grade(results), 0.001)
    }

    @Test
    fun progressChartIsDisplayed() {
        val results = getFakeResults()

        composeTestRule.setContent {
            MyApplicationTheme {
                StatsScreen(results = results, onBack = {})
            }
        }

        composeTestRule.onAllNodesWithText("3,5").onFirst().assertIsDisplayed()
        composeTestRule.onNodeWithText("Évolution du grade").assertIsDisplayed()

        onView(withContentDescription(PROGRESS_CHART_DESCRIPTION))
            .check(matches(isDisplayed()))
            .check { view, _ ->
                val chart = view as LineChart
                assertEquals(12, chart.data.entryCount)
                assertEquals(1.0f, chart.data.dataSets[0].getEntryForIndex(0).y, 0.001f)
                assertEquals(3.46f, chart.data.dataSets[0].getEntryForIndex(11).y, 0.001f)
            }
    }
}
