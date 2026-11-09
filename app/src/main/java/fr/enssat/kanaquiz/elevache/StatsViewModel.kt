package fr.enssat.kanaquiz.elevache

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.enssat.kanaquiz.elevache.stats.AppDatabase
import fr.enssat.kanaquiz.elevache.stats.QuizResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class StatsViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).quizResultDao()

    val results: StateFlow<List<QuizResult>> = dao.observeResults()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
