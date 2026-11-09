package fr.enssat.kanaquiz.elevache.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.enssat.kanaquiz.elevache.stats.GradeCalculator
import fr.enssat.kanaquiz.elevache.stats.MAX_GRADE
import fr.enssat.kanaquiz.elevache.stats.QuizResult
import fr.enssat.kanaquiz.elevache.ui.theme.MyApplicationTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.floor

@Composable
fun StatsScreen(
    results: List<QuizResult>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val grade = GradeCalculator.grade(results)
    val history = GradeCalculator.gradeHistory(results)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Ma progression", style = MaterialTheme.typography.headlineMedium)

        Text(text = "Grade actuel", style = MaterialTheme.typography.titleMedium)
        Text(
            text = GradeCalculator.formatGrade(grade),
            fontSize = 80.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = motivationMessage(results, grade),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        if (results.isNotEmpty()) {
            KeyFigures(results = results, bestGrade = history.max())

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Évolution du grade", style = MaterialTheme.typography.titleMedium)
                    ProgressChart(
                        grades = history,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(top = 8.dp)
                    )
                }
            }

            LastQuizzes(results = results.takeLast(3).reversed())
        }

        TextButton(onClick = onBack) {
            Text("Retour à l'accueil")
        }
    }
}

fun motivationMessage(results: List<QuizResult>, grade: Double): String {
    if (results.isEmpty()) {
        return "Fais ton premier quiz pour obtenir un grade !"
    }
    if (grade >= MAX_GRADE) {
        return "Grade maximum atteint, bravo !"
    }
    val nextGrade = floor(grade).toInt() + 1
    val missing = nextGrade - grade
    return "Encore ${GradeCalculator.formatGrade(missing)} pour atteindre le grade $nextGrade !"
}

@Composable
private fun KeyFigures(results: List<QuizResult>, bestGrade: Double) {
    val totalCorrect = results.sumOf { it.correctCount }
    val totalQuestions = results.sumOf { it.questionCount }
    val successRate = totalCorrect * 100 / totalQuestions

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        KeyFigure(value = "${results.size}", label = "Quiz joués", modifier = Modifier.weight(1f))
        KeyFigure(value = "$successRate %", label = "Réussite", modifier = Modifier.weight(1f))
        KeyFigure(
            value = GradeCalculator.formatGrade(bestGrade),
            label = "Meilleur grade",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun KeyFigure(value: String, label: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = label, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun LastQuizzes(results: List<QuizResult>) {
    val dateFormat = SimpleDateFormat("dd/MM HH:mm", Locale.FRANCE)

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Derniers quiz", style = MaterialTheme.typography.titleMedium)
            for (result in results) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = result.title, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = dateFormat.format(Date(result.date)),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "${result.correctCount} / ${result.questionCount}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatsScreenPreview() {
    val results = List(5) { i ->
        QuizResult(
            date = System.currentTimeMillis() - (5 - i) * 86_400_000L,
            title = "Kanji <= 2 - Kanji -> signification",
            correctCount = 5 + i,
            questionCount = 10,
            score = 1.0 + i * 0.5
        )
    }
    MyApplicationTheme {
        StatsScreen(results = results, onBack = {})
    }
}
