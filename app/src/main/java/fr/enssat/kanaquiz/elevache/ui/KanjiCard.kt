package fr.enssat.kanaquiz.elevache.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.enssat.kanaquiz.elevache.data.Kanji
import fr.enssat.kanaquiz.elevache.data.KanjiInfo
import fr.enssat.kanaquiz.elevache.ui.theme.MyApplicationTheme

@Composable
fun KanjiCard(kanji: Kanji, modifier: Modifier = Modifier) {
    val info = kanji.info
    val traductions = info.meanings
        .filter { !it.contains("Radical") }
        .joinToString(", ")
    val lecturesOn = info.readingsOn.joinToString("、")
    val lecturesKun = info.readingsKun.joinToString("、")

    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = kanji.char, fontSize = 120.sp)
            Text(
                text = traductions,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "On : $lecturesOn", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Kun : $lecturesKun", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Grade ${info.grade}", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KanjiCardPreview() {
    val info = KanjiInfo(
        grade = 1,
        meanings = listOf("One"),
        readingsOn = listOf("いち", "いつ"),
        readingsKun = listOf("ひと-", "ひと.つ")
    )
    MyApplicationTheme {
        KanjiCard(Kanji("一", info))
    }
}
