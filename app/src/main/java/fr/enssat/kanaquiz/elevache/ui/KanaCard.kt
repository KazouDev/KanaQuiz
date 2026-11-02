package fr.enssat.kanaquiz.elevache.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.enssat.kanaquiz.elevache.data.Kana
import fr.enssat.kanaquiz.elevache.ui.theme.MyApplicationTheme

@Composable
fun KanaCard(kana: Kana, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = kana.char, fontSize = 120.sp)
            Text(text = kana.romaji, style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KanaCardPreview() {
    MyApplicationTheme {
        KanaCard(Kana("あ", "a"))
    }
}
