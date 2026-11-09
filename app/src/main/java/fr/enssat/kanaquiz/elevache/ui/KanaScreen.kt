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
import fr.enssat.kanaquiz.elevache.Category
import fr.enssat.kanaquiz.elevache.KanaViewModel

@Composable
fun KanaScreen(
    viewModel: KanaViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val error = viewModel.error
    if (viewModel.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (error != null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Content(viewModel, onBack, modifier)
    }
}

@Composable
private fun Content(
    viewModel: KanaViewModel,
    onBack: () -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        PrimaryTabRow(selectedTabIndex = viewModel.category.ordinal) {
            for (category in Category.entries) {
                Tab(
                    selected = viewModel.category == category,
                    onClick = { viewModel.selectCategory(category) },
                    text = { Text(category.title) }
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
            when (viewModel.category) {
                Category.HIRAGANA -> KanaCard(viewModel.hiraganas[viewModel.index])
                Category.KATAKANA -> KanaCard(viewModel.katakanas[viewModel.index])
                Category.KANJI -> KanjiCard(viewModel.kanjis[viewModel.index])
            }
            FilledTonalButton(onClick = { viewModel.speak() }) {
                Text("Écouter")
            }
            val ttsError = viewModel.ttsError
            if (ttsError != null) {
                Text(
                    text = ttsError,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = "${viewModel.index + 1} / ${viewModel.size()}",
                style = MaterialTheme.typography.labelLarge
            )
            TextButton(onClick = onBack) {
                Text("Retour à l'accueil")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { viewModel.previous() },
                enabled = viewModel.index > 0
            ) {
                Text("Précédent")
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { viewModel.next() },
                enabled = viewModel.index < viewModel.size() - 1
            ) {
                Text("Suivant")
            }
        }
    }
}
