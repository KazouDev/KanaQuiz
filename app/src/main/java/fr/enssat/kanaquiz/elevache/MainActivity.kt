package fr.enssat.kanaquiz.elevache

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.enssat.kanaquiz.elevache.quiz.QuizGenerator
import fr.enssat.kanaquiz.elevache.stats.GradeCalculator
import fr.enssat.kanaquiz.elevache.ui.CreditsScreen
import fr.enssat.kanaquiz.elevache.ui.HomeScreen
import fr.enssat.kanaquiz.elevache.ui.KanaScreen
import fr.enssat.kanaquiz.elevache.ui.QuizScreen
import fr.enssat.kanaquiz.elevache.ui.QuizSettingsScreen
import fr.enssat.kanaquiz.elevache.ui.StatsScreen
import fr.enssat.kanaquiz.elevache.ui.theme.MyApplicationTheme

enum class Screen {
    HOME,
    EXPLORE,
    QUIZ_SETTINGS,
    QUIZ,
    STATS,
    CREDITS
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val kanaViewModel: KanaViewModel = viewModel()
                val quizViewModel: QuizViewModel = viewModel()
                val statsViewModel: StatsViewModel = viewModel()
                val results by statsViewModel.results.collectAsState()
                val quizGrade = GradeCalculator.quizGrade(results)
                var screen by rememberSaveable { mutableStateOf(Screen.HOME) }

                BackHandler(enabled = screen != Screen.HOME) {
                    if (screen == Screen.QUIZ) {
                        screen = Screen.QUIZ_SETTINGS
                    } else {
                        screen = Screen.HOME
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val modifier = Modifier.padding(innerPadding)
                    when (screen) {
                        Screen.HOME -> HomeScreen(
                            isLoading = kanaViewModel.isLoading,
                            error = kanaViewModel.error,
                            grade = GradeCalculator.grade(results),
                            onStats = { screen = Screen.STATS },
                            onQuiz = { screen = Screen.QUIZ_SETTINGS },
                            onExplore = { screen = Screen.EXPLORE },
                            onCredits = { screen = Screen.CREDITS },
                            modifier = modifier
                        )

                        Screen.EXPLORE -> KanaScreen(
                            viewModel = kanaViewModel,
                            onBack = { screen = Screen.HOME },
                            modifier = modifier
                        )

                        Screen.QUIZ_SETTINGS -> QuizSettingsScreen(
                            initialSettings = quizViewModel.settings,
                            quizGrade = quizGrade,
                            onStart = { settings ->
                                val generator = QuizGenerator(
                                    kanaViewModel.hiraganas,
                                    kanaViewModel.katakanas,
                                    kanaViewModel.kanjis
                                )
                                quizViewModel.start(settings, generator)
                                screen = Screen.QUIZ
                            },
                            onBack = { screen = Screen.HOME },
                            modifier = modifier
                        )

                        Screen.QUIZ -> QuizScreen(
                            viewModel = quizViewModel,
                            onReplay = { quizViewModel.replay(quizGrade) },
                            onSettings = { screen = Screen.QUIZ_SETTINGS },
                            onHome = { screen = Screen.HOME },
                            modifier = modifier
                        )

                        Screen.STATS -> StatsScreen(
                            results = results,
                            onBack = { screen = Screen.HOME },
                            modifier = modifier
                        )

                        Screen.CREDITS -> CreditsScreen(
                            onBack = { screen = Screen.HOME },
                            modifier = modifier
                        )
                    }
                }
            }
        }
    }
}
