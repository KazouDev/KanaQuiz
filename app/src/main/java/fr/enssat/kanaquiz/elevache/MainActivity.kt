package fr.enssat.kanaquiz.elevache

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.enssat.kanaquiz.elevache.data.KanaRepository
import fr.enssat.kanaquiz.elevache.ui.KanaCard
import fr.enssat.kanaquiz.elevache.ui.KanjiCard
import fr.enssat.kanaquiz.elevache.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = KanaRepository(this)
        val premierHiragana = repository.getHiraganas().first()
        val premierKanji = repository.getKanjis().first()

        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        KanaCard(premierHiragana)
                        KanjiCard(premierKanji)
                    }
                }
            }
        }
    }
}
