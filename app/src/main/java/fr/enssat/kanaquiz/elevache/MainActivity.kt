package fr.enssat.kanaquiz.elevache

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.enssat.kanaquiz.elevache.ui.KanaScreen
import fr.enssat.kanaquiz.elevache.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val kanaViewModel: KanaViewModel = viewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    KanaScreen(
                        viewModel = kanaViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
