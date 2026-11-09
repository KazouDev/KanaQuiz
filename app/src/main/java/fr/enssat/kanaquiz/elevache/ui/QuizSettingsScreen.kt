package fr.enssat.kanaquiz.elevache.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.enssat.kanaquiz.elevache.Category
import fr.enssat.kanaquiz.elevache.quiz.QuestionType
import fr.enssat.kanaquiz.elevache.quiz.QuizSettings
import fr.enssat.kanaquiz.elevache.ui.theme.MyApplicationTheme

val QUESTION_COUNTS = listOf(5, 10, 15, 20)

@Composable
fun QuizSettingsScreen(
    initialSettings: QuizSettings,
    quizGrade: Int,
    onStart: (QuizSettings) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var category by rememberSaveable { mutableStateOf(initialSettings.category) }
    var questionType by rememberSaveable { mutableStateOf(initialSettings.questionType) }
    var hint by rememberSaveable { mutableStateOf(initialSettings.hint) }
    var tts by rememberSaveable { mutableStateOf(initialSettings.tts) }
    var isMix by rememberSaveable { mutableStateOf(initialSettings.isMix) }
    var questionCount by rememberSaveable { mutableIntStateOf(initialSettings.questionCount) }
    var debug by rememberSaveable { mutableStateOf(initialSettings.debug) }

    val isKanji = category == Category.KANJI
    val usesKanji = isKanji || isMix
    val availableTypes = QuestionType.entries.filter { it.isKanji == isKanji }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Nouveau quiz", style = MaterialTheme.typography.headlineMedium)

        SectionTitle("Caractères")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (c in Category.entries) {
                FilterChip(
                    selected = !isMix && category == c,
                    onClick = {
                        isMix = false
                        category = c
                        if (questionType.isKanji != (c == Category.KANJI)) {
                            questionType = QuestionType.entries.first { it.isKanji == (c == Category.KANJI) }
                        }
                    },
                    label = { Text(c.title) }
                )
            }
            FilterChip(
                selected = isMix,
                onClick = { isMix = true },
                label = { Text("Mix") }
            )
        }

        if (usesKanji) {
            SectionTitle("Grade du quiz : $quizGrade")
            Text(
                text = "Les kanji sont tirés parmi ceux de grade <= $quizGrade. Ce grade dépend de ton grade de joueur : améliore-le pour débloquer de nouveaux kanji !",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        SectionTitle("Type de question")
        if (isMix) {
            Text(
                text = "Tous les caractères et tous les types de questions sont mélangés !",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            for (type in availableTypes) {
                RadioRow(
                    text = type.label,
                    selected = questionType == type,
                    onClick = { questionType = type }
                )
            }
        }

        SectionTitle("Nombre de questions")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (count in QUESTION_COUNTS) {
                FilterChip(
                    selected = questionCount == count,
                    onClick = { questionCount = count },
                    label = { Text("$count") }
                )
            }
        }

        SectionTitle("Options")
        SwitchRow(
            text = if (usesKanji) "Aide" else "Aide (kanji uniquement)",
            checked = hint && usesKanji,
            enabled = usesKanji,
            onChange = { hint = it }
        )
        SwitchRow(
            text = "Synthèse vocale",
            checked = tts,
            enabled = true,
            onChange = { tts = it }
        )
        SwitchRow(
            text = "Mode debug (voir la bonne réponse)",
            checked = debug,
            enabled = true,
            onChange = { debug = it }
        )

        Button(
            onClick = {
                val settings = QuizSettings(
                    category = category,
                    questionType = questionType,
                    grade = if (usesKanji) quizGrade else 1,
                    hint = hint && usesKanji,
                    tts = tts,
                    isMix = isMix,
                    questionCount = questionCount,
                    debug = debug
                )
                onStart(settings)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(56.dp)
        ) {
            Text("Lancer le quiz", style = MaterialTheme.typography.titleMedium)
        }
        TextButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Retour à l'accueil")
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 16.dp)
    )
}

@Composable
private fun RadioRow(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(text = text, modifier = Modifier.padding(start = 12.dp))
    }
}

@Composable
private fun SwitchRow(
    text: String,
    checked: Boolean,
    enabled: Boolean,
    onChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onChange, enabled = enabled)
    }
}

@Preview(showBackground = true)
@Composable
fun QuizSettingsScreenPreview() {
    MyApplicationTheme {
        QuizSettingsScreen(
            initialSettings = QuizSettings(category = Category.KANJI, questionType = QuestionType.CHARACTER_TO_MEANING),
            quizGrade = 3,
            onStart = {},
            onBack = {}
        )
    }
}
