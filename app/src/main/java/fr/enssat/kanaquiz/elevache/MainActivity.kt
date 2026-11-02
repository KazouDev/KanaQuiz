package fr.enssat.kanaquiz.elevache

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.enssat.kanaquiz.elevache.ui.CreditsScreen
import fr.enssat.kanaquiz.elevache.ui.KanaScreen
import fr.enssat.kanaquiz.elevache.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val kanaViewModel: KanaViewModel = viewModel()
                var afficherCredits by rememberSaveable { mutableStateOf(false) }

                BackHandler(enabled = afficherCredits) {
                    afficherCredits = false
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (afficherCredits) {
                        CreditsScreen(
                            onRetour = { afficherCredits = false },
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        KanaScreen(
                            viewModel = kanaViewModel,
                            onCredits = { afficherCredits = true },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
