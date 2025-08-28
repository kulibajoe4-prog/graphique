package org.babetech.borastock.ui.screens.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.babetech.borastock.domain.usecase.*

sealed class GraphicsUiState {
    object Loading : GraphicsUiState()
    data class Success(
        val stockDistribution: List<ChartDataPoint>,
        val stockStatusDistribution: List<ChartDataPoint>,
        val movementTrends: List<MovementTrendPoint>,
        val supplierPerformance: List<SupplierPerformancePoint>
    ) : GraphicsUiState()
    data class Error(val message: String) : GraphicsUiState()
}

class GraphicsViewModel(
    private val getChartDataUseCase: GetChartDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GraphicsUiState>(GraphicsUiState.Loading)
    val uiState: StateFlow<GraphicsUiState> = _uiState.asStateFlow()

    init {
        loadChartData()
    }

    fun loadChartData() {
        viewModelScope.launch {
            _uiState.value = GraphicsUiState.Loading
            
            try {
                combine(
                    getChartDataUseCase.getStockDistribution(),
                    getChartDataUseCase.getStockStatusDistribution(),
                    getChartDataUseCase.getMovementTrends(),
                    getChartDataUseCase.getSupplierPerformance()
                ) { distribution, statusDistribution, trends, performance ->
                    GraphicsUiState.Success(
                        stockDistribution = distribution,
                        stockStatusDistribution = statusDistribution,
                        movementTrends = trends,
                        supplierPerformance = performance
                    )
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                _uiState.value = GraphicsUiState.Error("Erreur lors du chargement des graphiques: ${e.message}")
            }
        }
    }
}