package fr.enssat.kanaquiz.elevache

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.enssat.kanaquiz.elevache.data.KanaRepository
import fr.enssat.kanaquiz.elevache.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = KanaRepository(this)
        val hiraganas = repository.getHiraganas()
        val katakanas = repository.getKatakanas()
        val kanjis = repository.getKanjis()
        Log.d("KanaQuiz", "Hiragana: ${hiraganas.size}, premier: ${hiraganas.first()}")
        Log.d("KanaQuiz", "Katakana: ${katakanas.size}, premier: ${katakanas.first()}")
        Log.d("KanaQuiz", "Kanji: ${kanjis.size}, premier: ${kanjis.first()}")

        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}