package org.babetech.borastock.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.babetech.borastock.data.models.*
import org.babetech.borastock.data.repository.StockRepository

/**
 * Use case for getting chart data from the repository
 */
class GetChartDataUseCase(
    private val repository: StockRepository
) {
    /**
     * Get stock distribution data for pie/donut charts
     */
    fun getStockDistribution(): Flow<List<ChartDataPoint>> {
        return repository.getAllStockItems().map { items ->
            items.groupBy { it.category }
                .map { (category, categoryItems) ->
                    ChartDataPoint(
                        label = category,
                        value = categoryItems.sumOf { it.totalValue },
                        count = categoryItems.size
                    )
                }
                .sortedByDescending { it.value }
        }
    }

    /**
     * Get stock status distribution for charts
     */
    fun getStockStatusDistribution(): Flow<List<ChartDataPoint>> {
        return repository.getAllStockItems().map { items ->
            items.groupBy { it.stockStatus }
                .map { (status, statusItems) ->
                    ChartDataPoint(
                        label = status.label,
                        value = statusItems.size.toDouble(),
                        count = statusItems.size,
                        color = status.color
                    )
                }
        }
    }

    /**
     * Get monthly movement trends for line charts
     */
    fun getMovementTrends(): Flow<List<MovementTrendPoint>> {
        return combine(
            repository.getAllStockEntries(),
            repository.getAllStockExits()
        ) { entries, exits ->
            val entryTrends = entries.groupBy { it.entryDate.take(7) } // YYYY-MM format
                .map { (month, monthEntries) ->
                    MovementTrendPoint(
                        period = month,
                        entries = monthEntries.sumOf { it.quantity },
                        exits = 0,
                        value = monthEntries.sumOf { it.totalValue }
                    )
                }

            val exitTrends = exits.groupBy { it.exitDate.take(7) }
                .map { (month, monthExits) ->
                    MovementTrendPoint(
                        period = month,
                        entries = 0,
                        exits = monthExits.sumOf { it.quantity },
                        value = monthExits.sumOf { it.totalValue }
                    )
                }

            // Combine and aggregate by month
            (entryTrends + exitTrends)
                .groupBy { it.period }
                .map { (month, trends) ->
                    MovementTrendPoint(
                        period = month,
                        entries = trends.sumOf { it.entries },
                        exits = trends.sumOf { it.exits },
                        value = trends.sumOf { it.value }
                    )
                }
                .sortedBy { it.period }
                .takeLast(12) // Last 12 months
        }
    }

    /**
     * Get supplier performance data for radar charts
     */
    fun getSupplierPerformance(): Flow<List<SupplierPerformancePoint>> {
        return repository.getAllSuppliers().map { suppliers ->
            suppliers.take(5).map { supplier ->
                SupplierPerformancePoint(
                    supplierName = supplier.name,
                    reliability = supplier.reliability.ordinal.toDouble() * 25, // Convert to 0-100 scale
                    rating = supplier.rating.toDouble() * 20, // Convert to 0-100 scale
                    deliveryTime = (4 - supplier.reliability.ordinal) * 25.0, // Inverse for delivery time
                    costEfficiency = supplier.rating.toDouble() * 18,
                    quality = supplier.rating.toDouble() * 22
                )
            }
        }
    }
}

/**
 * Data classes for chart data
 */
data class ChartDataPoint(
    val label: String,
    val value: Double,
    val count: Int = 0,
    val color: androidx.compose.ui.graphics.Color? = null
)

data class MovementTrendPoint(
    val period: String,
    val entries: Int,
    val exits: Int,
    val value: Double
)

data class SupplierPerformancePoint(
    val supplierName: String,
    val reliability: Double,
    val rating: Double,
    val deliveryTime: Double,
    val costEfficiency: Double,
    val quality: Double
)