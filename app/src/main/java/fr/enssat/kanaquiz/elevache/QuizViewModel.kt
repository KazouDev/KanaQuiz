package fr.enssat.kanaquiz.elevache

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.enssat.kanaquiz.elevache.quiz.AnswerResult
import fr.enssat.kanaquiz.elevache.quiz.Question
import fr.enssat.kanaquiz.elevache.quiz.QuizGenerator
import fr.enssat.kanaquiz.elevache.quiz.QuizSettings
import fr.enssat.kanaquiz.elevache.stats.AppDatabase
import fr.enssat.kanaquiz.elevache.stats.GradeCalculator
import fr.enssat.kanaquiz.elevache.stats.QuizResult
import fr.enssat.kanaquiz.elevache.tts.TtsManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val ttsManager = TtsManager(application)
    private val dao = AppDatabase.getInstance(application).quizResultDao()
    private var generator: QuizGenerator? = null
    private var waitJob: Job? = null

    var settings by mutableStateOf(QuizSettings())
        private set
    var questions by mutableStateOf(emptyList<Question>())
        private set
    var results by mutableStateOf(emptyList<AnswerResult>())
        private set
    var index by mutableIntStateOf(0)
        private set
    var selectedAnswer by mutableStateOf<String?>(null)
        private set
    var isFinished by mutableStateOf(false)
        private set

    fun start(newSettings: QuizSettings, newGenerator: QuizGenerator) {
        settings = newSettings
        generator = newGenerator
        replay()
    }

    fun replay(newQuizGrade: Int) {
        if (settings.category == Category.KANJI || settings.isMix) {
            settings = settings.copy(grade = newQuizGrade)
        }
        replay()
    }

    private fun replay() {
        val currentGenerator = generator ?: return
        waitJob?.cancel()
        questions = currentGenerator.generate(settings)
        results = List(questions.size) { AnswerResult.NOT_ANSWERED }
        index = 0
        selectedAnswer = null
        isFinished = false
    }

    fun currentQuestion(): Question {
        return questions[index]
    }

    fun selectAnswer(answer: String) {
        if (selectedAnswer != null) {
            return
        }
        selectedAnswer = answer

        val question = currentQuestion()
        val newResults = results.toMutableList()
        if (answer == question.correctAnswer) {
            newResults[index] = AnswerResult.CORRECT
        } else {
            newResults[index] = AnswerResult.WRONG
        }
        results = newResults

        if (settings.tts) {
            ttsManager.speak(question.pronunciation, 1.0f)
        }

        waitJob = viewModelScope.launch {
            delay(1000)
            selectedAnswer = null
            if (index < questions.size - 1) {
                index++
            } else {
                isFinished = true
                saveResult()
            }
        }
    }

    private fun saveResult() {
        val result = QuizResult(
            date = System.currentTimeMillis(),
            title = settings.title(),
            correctCount = score(),
            questionCount = questions.size,
            score = GradeCalculator.quizScore(questions, results)
        )
        viewModelScope.launch {
            dao.insert(result)
        }
    }

    fun speakQuestion() {
        ttsManager.speak(currentQuestion().pronunciation, 1.0f)
    }

    fun score(): Int {
        return results.count { it == AnswerResult.CORRECT }
    }

    override fun onCleared() {
        ttsManager.release()
        super.onCleared()
    }
}
