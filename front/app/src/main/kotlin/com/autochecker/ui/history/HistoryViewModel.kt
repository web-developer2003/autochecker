package com.autochecker.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autochecker.core.network.NetworkResult
import com.autochecker.data.model.SearchHistoryItem
import com.autochecker.data.repository.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryState(
    val items: List<SearchHistoryItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = vehicleRepository.getSearchHistory()) {
                is NetworkResult.Success -> {
                    _state.value = HistoryState(items = result.data)
                }
                is NetworkResult.Error -> {
                    _state.value = HistoryState(error = result.message)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }
}
