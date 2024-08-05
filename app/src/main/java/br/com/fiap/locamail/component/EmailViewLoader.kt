package br.com.fiap.locamail.component

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EmailViewLoader : ViewModel() {


    private val _isLoading = MutableStateFlow(false)
    private val _isRefreshing = MutableStateFlow(false)

    val isLoading = _isLoading.asStateFlow()

    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        loadStuff();
    }

    fun loadStuff() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(3000L)
            _isLoading.value = false
        }
    }

    fun refreshStuff() {
        viewModelScope.launch {
            _isRefreshing.value = true
            delay(3000L) // Simulação de atualização
            _isRefreshing.value = false
        }
    }
}