package dz.plombidz.app.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlombiViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = PlombiDatabase.get(app).dao()

    val clients = dao.clients()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val jobs = dao.interventions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveClient(c: Client) = viewModelScope.launch {
        if (c.id == 0L) dao.insertClient(c) else dao.updateClient(c)
    }

    fun removeClient(c: Client) = viewModelScope.launch { dao.deleteClient(c) }

    fun saveJob(j: Intervention) = viewModelScope.launch {
        if (j.id == 0L) dao.insertJob(j) else dao.updateJob(j)
    }

    fun removeJob(j: Intervention) = viewModelScope.launch { dao.deleteJob(j) }
}
