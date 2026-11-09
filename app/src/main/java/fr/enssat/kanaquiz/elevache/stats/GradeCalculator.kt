package fr.enssat.kanaquiz.elevache.stats

import fr.enssat.kanaquiz.elevache.quiz.AnswerResult
import fr.enssat.kanaquiz.elevache.quiz.Question
import java.util.Locale

const val WINDOW_SIZE = 10
const val MAX_GRADE = 9
const val MAX_KANJI_GRADE = 8

object GradeCalculator {

    fun quizScore(questions: List<Question>, results: List<AnswerResult>): Double {
        if (questions.isEmpty()) {
            return 0.0
        }
        var points = 0
        for (i in questions.indices) {
            if (results[i] == AnswerResult.CORRECT) {
                points += questions[i].difficulty
            }
        }
        return points.toDouble() / questions.size
    }

    fun grade(results: List<QuizResult>): Double {
        val window = results.takeLast(WINDOW_SIZE)
        if (window.isEmpty()) {
            return 0.0
        }
        return window.map { it.score }.average()
    }

    fun gradeHistory(results: List<QuizResult>): List<Double> {
        val history = mutableListOf<Double>()
        for (i in results.indices) {
            history.add(grade(results.subList(0, i + 1)))
        }
        return history
    }

    fun quizGrade(results: List<QuizResult>): Int {
        val userGrade = grade(results).toInt()
        return userGrade.coerceIn(1, MAX_KANJI_GRADE)
    }

    fun formatGrade(grade: Double): String {
        return String.format(Locale.FRANCE, "%.1f", grade)
    }
}
