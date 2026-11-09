package fr.enssat.kanaquiz.elevache.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.enssat.kanaquiz.elevache.Category
import fr.enssat.kanaquiz.elevache.QuizViewModel
import fr.enssat.kanaquiz.elevache.quiz.AnswerResult
import fr.enssat.kanaquiz.elevache.quiz.Question
import fr.enssat.kanaquiz.elevache.quiz.QuestionType
import fr.enssat.kanaquiz.elevache.quiz.QuizSettings
import fr.enssat.kanaquiz.elevache.ui.theme.CorrectGreen
import fr.enssat.kanaquiz.elevache.ui.theme.MyApplicationTheme
import fr.enssat.kanaquiz.elevache.ui.theme.UnansweredGrey
import fr.enssat.kanaquiz.elevache.ui.theme.WrongRed

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onReplay: () -> Unit,
    onSettings: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (viewModel.questions.isEmpty()) {
        return
    }
    if (viewModel.isFinished) {
        QuizResult(
            score = viewModel.score(),
            results = viewModel.results,
            onReplay = onReplay,
            onSettings = onSettings,
            onHome = onHome,
            modifier = modifier
        )
    } else {
        QuizQuestion(
            question = viewModel.currentQuestion(),
            number = viewModel.index + 1,
            settings = viewModel.settings,
            results = viewModel.results,
            selectedAnswer = viewModel.selectedAnswer,
            onSelect = { viewModel.selectAnswer(it) },
            onListen = { viewModel.speakQuestion() },
            onQuit = onSettings,
            modifier = modifier
        )
    }
}

@Composable
private fun QuizQuestion(
    question: Question,
    number: Int,
    settings: QuizSettings,
    results: List<AnswerResult>,
    selectedAnswer: String?,
    onSelect: (String) -> Unit,
    onListen: () -> Unit,
    onQuit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Question $number / ${results.size}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onQuit) {
                Text("Quitter")
            }
        }

        ResultBar(results = results, currentIndex = number - 1)

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = question.type.instruction,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = question.prompt,
                    fontSize = if (question.type.promptInJapanese) 96.sp else 36.sp,
                    textAlign = TextAlign.Center
                )
                if (question.hint != null) {
                    Text(
                        text = question.hint,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (settings.debug) {
                    Text(
                        text = "Debug : bonne réponse = ${question.correctAnswer}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                if (settings.tts && question.type.canListen) {
                    Spacer(modifier = Modifier.height(8.dp))
                    FilledTonalButton(onClick = onListen) {
                        Text("Écouter")
                    }
                }
            }
        }

        for (choice in question.choices) {
            AnswerButton(
                text = choice,
                inJapanese = question.type.answerInJapanese,
                isCorrect = choice == question.correctAnswer,
                selectedAnswer = selectedAnswer,
                onClick = { onSelect(choice) }
            )
        }
    }
}

@Composable
private fun AnswerButton(
    text: String,
    inJapanese: Boolean,
    isCorrect: Boolean,
    selectedAnswer: String?,
    onClick: () -> Unit
) {
    var backgroundColor = MaterialTheme.colorScheme.primary
    var textColor = MaterialTheme.colorScheme.onPrimary
    if (selectedAnswer != null) {
        if (isCorrect) {
            backgroundColor = CorrectGreen
            textColor = Color.White
        } else if (text == selectedAnswer) {
            backgroundColor = WrongRed
            textColor = Color.White
        }
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        )
    ) {
        Text(
            text = text,
            fontSize = if (inJapanese) 28.sp else 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ResultBar(results: List<AnswerResult>, currentIndex: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        for ((rowIndex, row) in results.chunked(10).withIndex()) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                for ((i, result) in row.withIndex()) {
                    val color = when (result) {
                        AnswerResult.CORRECT -> CorrectGreen
                        AnswerResult.WRONG -> WrongRed
                        AnswerResult.NOT_ANSWERED -> UnansweredGrey.copy(alpha = 0.4f)
                    }
                    var dot = Modifier
                        .size(24.dp)
                        .background(color, CircleShape)
                    if (rowIndex * 10 + i == currentIndex) {
                        dot = dot.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    }
                    Box(modifier = dot)
                }
            }
        }
    }
}

@Composable
private fun QuizResult(
    score: Int,
    results: List<AnswerResult>,
    onReplay: () -> Unit,
    onSettings: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val message = when {
        score == results.size -> "Parfait !"
        score >= results.size * 0.7 -> "Très bien !"
        score >= results.size * 0.4 -> "Pas mal, continue !"
        else -> "Courage, entraîne-toi encore !"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(text = "Quiz terminé", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "$score / ${results.size}",
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(text = message, style = MaterialTheme.typography.titleMedium)
        ResultBar(results = results, currentIndex = -1)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onReplay, modifier = Modifier.fillMaxWidth()) {
            Text("Rejouer")
        }
        OutlinedButton(onClick = onSettings, modifier = Modifier.fillMaxWidth()) {
            Text("Changer les paramètres")
        }
        TextButton(onClick = onHome) {
            Text("Accueil")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizQuestionPreview() {
    val question = Question(
        type = QuestionType.CHARACTER_TO_MEANING,
        prompt = "水",
        hint = "Lecture : すい",
        choices = listOf("Fire", "Water", "Tree", "Gold"),
        correctAnswer = "Water",
        pronunciation = "すい",
        difficulty = 2
    )
    MyApplicationTheme {
        QuizQuestion(
            question = question,
            number = 3,
            settings = QuizSettings(category = Category.KANJI),
            results = listOf(AnswerResult.CORRECT, AnswerResult.WRONG) + List(8) { AnswerResult.NOT_ANSWERED },
            selectedAnswer = null,
            onSelect = {},
            onListen = {},
            onQuit = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizResultPreview() {
    MyApplicationTheme {
        QuizResult(
            score = 7,
            results = List(7) { AnswerResult.CORRECT } + List(3) { AnswerResult.WRONG },
            onReplay = {},
            onSettings = {},
            onHome = {}
        )
    }
}
