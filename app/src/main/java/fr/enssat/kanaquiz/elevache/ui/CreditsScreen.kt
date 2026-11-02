package fr.enssat.kanaquiz.elevache.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.enssat.kanaquiz.elevache.ui.theme.MyApplicationTheme

@Composable
fun CreditsScreen(onRetour: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(text = "Crédits", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "Données des kanji : kanji-data par David Luz Gouveia",
            textAlign = TextAlign.Center
        )
        Text(
            text = "https://github.com/davidluzgouveia/kanji-data",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Application réalisée dans le cadre du TP Android de l'ENSSAT",
            textAlign = TextAlign.Center
        )
        Button(onClick = onRetour) {
            Text("Retour")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreditsScreenPreview() {
    MyApplicationTheme {
        CreditsScreen(onRetour = {})
    }
}
