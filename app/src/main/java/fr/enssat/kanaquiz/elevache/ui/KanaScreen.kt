package fr.enssat.kanaquiz.elevache.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.enssat.kanaquiz.elevache.Categorie
import fr.enssat.kanaquiz.elevache.KanaViewModel

@Composable
fun KanaScreen(
    viewModel: KanaViewModel,
    onCredits: () -> Unit,
    modifier: Modifier = Modifier
) {
    val erreur = viewModel.erreur
    if (viewModel.chargement) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (erreur != null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = erreur,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Contenu(viewModel, onCredits, modifier)
    }
}

@Composable
private fun Contenu(
    viewModel: KanaViewModel,
    onCredits: () -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        PrimaryTabRow(selectedTabIndex = viewModel.categorie.ordinal) {
            for (categorie in Categorie.entries) {
                Tab(
                    selected = viewModel.categorie == categorie,
                    onClick = { viewModel.changerCategorie(categorie) },
                    text = { Text(categorie.titre) }
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (viewModel.categorie) {
                Categorie.HIRAGANA -> KanaCard(viewModel.hiraganas[viewModel.index])
                Categorie.KATAKANA -> KanaCard(viewModel.katakanas[viewModel.index])
                Categorie.KANJI -> KanjiCard(viewModel.kanjis[viewModel.index])
            }
            FilledTonalButton(onClick = { viewModel.prononcer() }) {
                Text("🔊 Écouter")
            }
            val erreurTts = viewModel.erreurTts
            if (erreurTts != null) {
                Text(
                    text = erreurTts,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = "${viewModel.index + 1} / ${viewModel.taille()}",
                style = MaterialTheme.typography.labelLarge
            )
            TextButton(onClick = onCredits) {
                Text("Crédits")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { viewModel.precedent() },
                enabled = viewModel.index > 0
            ) {
                Text("◀ Précédent")
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { viewModel.suivant() },
                enabled = viewModel.index < viewModel.taille() - 1
            ) {
                Text("Suivant ▶")
            }
        }
    }
}
