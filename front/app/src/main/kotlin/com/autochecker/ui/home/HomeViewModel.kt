package com.autochecker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autochecker.core.network.NetworkResult
import com.autochecker.data.model.VehicleReport
import com.autochecker.data.repository.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SearchSource { LOCAL, GOVERNMENT, INTERNET }
enum class SearchType { PLATE, VIN }

data class HomeState(
    val searchQuery: String = "",
    val searchSource: SearchSource = SearchSource.LOCAL,
    val searchType: SearchType = SearchType.PLATE,
    val isSearching: Boolean = false,
    val searchError: String? = null,
    val searchResult: VehicleReport? = null,
    val sourceMessage: String? = null,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun updateSearchQuery(query: String) {
        _state.value = _state.value.copy(
            searchQuery = query.uppercase().take(20),
            searchError = null,
            sourceMessage = null,
        )
    }

    fun updateSearchSource(source: SearchSource) {
        _state.value = _state.value.copy(
            searchSource = source,
            searchError = null,
            sourceMessage = null,
        )
    }

    fun updateSearchType(type: SearchType) {
        _state.value = _state.value.copy(
            searchType = type,
            searchQuery = "",
            searchError = null,
        )
    }

    fun search() {
        val query = _state.value.searchQuery.trim()
        val emptyMsg = if (_state.value.searchType == SearchType.VIN)
            "VIN raqamini kiriting" else "Davlat raqamini kiriting"
        if (query.isEmpty()) {
            _state.value = _state.value.copy(searchError = emptyMsg)
            return
        }

        val source = when (_state.value.searchSource) {
            SearchSource.LOCAL -> "local"
            SearchSource.GOVERNMENT -> "government"
            SearchSource.INTERNET -> "internet"
        }

        val searchType = when (_state.value.searchType) {
            SearchType.PLATE -> "plate"
            SearchType.VIN -> "vin"
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isSearching = true, searchError = null, sourceMessage = null)

            when (val result = vehicleRepository.checkVehicle(
                query = query,
                searchType = searchType,
                source = source,
            )) {
                is NetworkResult.Success -> {
                    _state.value = _state.value.copy(
                        isSearching = false,
                        searchResult = result.data,
                    )
                }
                is NetworkResult.Error -> {
                    // Internet search returns its result as a NetworkResult.Error with the Gemini summary
                    if (_state.value.searchSource == SearchSource.INTERNET) {
                        _state.value = _state.value.copy(
                            isSearching = false,
                            sourceMessage = result.message,
                        )
                    } else {
                        _state.value = _state.value.copy(
                            isSearching = false,
                            searchError = result.message,
                        )
                    }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun clearResult() {
        _state.value = _state.value.copy(searchResult = null)
    }
}
