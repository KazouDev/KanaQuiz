package fr.enssat.kanaquiz.elevache.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.enssat.kanaquiz.elevache.Categorie
import fr.enssat.kanaquiz.elevache.KanaViewModel

@Composable
fun KanaScreen(viewModel: KanaViewModel, modifier: Modifier = Modifier) {
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
            Text(
                text = "${viewModel.index + 1} / ${viewModel.taille()}",
                style = MaterialTheme.typography.labelLarge
            )
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
