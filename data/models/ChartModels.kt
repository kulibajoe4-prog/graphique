package org.babetech.borastock.data.models

import androidx.compose.ui.graphics.Color

/**
 * Models specifically for chart data representation
 */
data class ChartDataPoint(
    val label: String,
    val value: Double,
    val count: Int = 0,
    val color: Color? = null
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

/**
 * Chart configuration models
 */
data class ChartConfig(
    val title: String,
    val type: ChartType,
    val period: String,
    val refreshInterval: Long = 30000L // 30 seconds
)

enum class ChartType {
    LINE, BAR, PIE, DONUT, RADAR
}