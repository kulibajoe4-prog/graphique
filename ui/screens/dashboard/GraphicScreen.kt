package org.babetech.borastock.ui.screens.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import borastock.composeapp.generated.resources.*
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.donutChart.DonutChart
import com.aay.compose.donutChart.PieChart
import com.aay.compose.donutChart.model.PieChartData
import com.aay.compose.lineChart.LineChart
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType
import com.aay.compose.radarChart.RadarChart
import com.aay.compose.radarChart.model.NetLinesStyle
import com.aay.compose.radarChart.model.Polygon
import com.aay.compose.radarChart.model.PolygonStyle
import org.babetech.borastock.domain.usecase.GetChartDataUseCase
import org.babetech.borastock.ui.screens.dashboard.viewmodel.GraphicsViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

data class ChartType(
    val key: String,
    val title: String,
    val icon: Painter,
    val description: String
)

@Composable
fun GraphicSwitcherScreen(
    viewModel: GraphicsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedChart by remember { mutableStateOf("Line") }

    val chartTypes = listOf(
        ChartType("Line", "Évolution", painterResource(Res.drawable.analytics), "Tendances temporelles"),
        ChartType("Bar", "Comparaisons", painterResource(Res.drawable.barchart), "Données par catégorie"),
        ChartType("Pie", "Répartition", painterResource(Res.drawable.piechart), "Distribution globale"),
        ChartType("Donut", "Statuts", painterResource(Res.drawable.donutlarge), "États du stock"),
        ChartType("Radar", "Performance", painterResource(Res.drawable.analytics), "Analyse fournisseurs")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Chart type selector
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(chartTypes) { chartType ->
                ElevatedCard(
                    onClick = { selectedChart = chartType.key },
                    modifier = Modifier.width(160.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = if (selectedChart == chartType.key) 
                            MaterialTheme.colorScheme.primaryContainer 
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = chartType.icon,
                            contentDescription = chartType.title,
                            tint = if (selectedChart == chartType.key) 
                                MaterialTheme.colorScheme.primary 
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            chartType.title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = if (selectedChart == chartType.key) 
                                MaterialTheme.colorScheme.primary 
                            else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            chartType.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Dynamic chart display
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (uiState) {
                is GraphicsUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is GraphicsUiState.Error -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Erreur de chargement",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                uiState.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Button(
                                onClick = { viewModel.loadChartData() },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Réessayer")
                            }
                        }
                    }
                }
                is GraphicsUiState.Success -> {
                    when (selectedChart) {
                        "Line" -> DynamicLineChart(uiState.movementTrends)
                        "Bar" -> DynamicBarChart(uiState.stockDistribution)
                        "Pie" -> DynamicPieChart(uiState.stockDistribution)
                        "Donut" -> DynamicDonutChart(uiState.stockStatusDistribution)
                        "Radar" -> DynamicRadarChart(uiState.supplierPerformance)
                    }
                }
            }
        }
    }
}

@Composable
fun DynamicLineChart(trends: List<org.babetech.borastock.domain.usecase.MovementTrendPoint>) {
    if (trends.isEmpty()) {
        EmptyChartState("Aucune donnée de tendance disponible")
        return
    }

    val lineParameters = listOf(
        LineParameters(
            label = "Entrées",
            data = trends.map { it.entries.toDouble() },
            lineColor = Color(0xFF22c55e),
            lineType = LineType.CURVED_LINE,
            lineShadow = true
        ),
        LineParameters(
            label = "Sorties",
            data = trends.map { it.exits.toDouble() },
            lineColor = Color(0xFFef4444),
            lineType = LineType.CURVED_LINE,
            lineShadow = true
        )
    )

    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Évolution des mouvements de stock",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LineChart(
                modifier = Modifier.fillMaxSize(),
                linesParameters = lineParameters,
                isGrid = true,
                gridColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                xAxisData = trends.map { it.period },
                animateChart = true,
                showGridWithSpacer = true,
                yAxisStyle = TextStyle(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                xAxisStyle = TextStyle(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.W400
                ),
                yAxisRange = 10,
                oneLineChart = false,
                gridOrientation = GridOrientation.VERTICAL
            )
        }
    }
}

@Composable
fun DynamicBarChart(distribution: List<org.babetech.borastock.domain.usecase.ChartDataPoint>) {
    if (distribution.isEmpty()) {
        EmptyChartState("Aucune donnée de distribution disponible")
        return
    }

    val barParameters = listOf(
        BarParameters(
            dataName = "Valeur du stock",
            data = distribution.map { it.value },
            barColor = Color(0xFF3b82f6)
        )
    )

    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Distribution du stock par catégorie",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            BarChart(
                chartParameters = barParameters,
                gridColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                xAxisData = distribution.map { it.label },
                isShowGrid = true,
                animateChart = true,
                showGridWithSpacer = true,
                yAxisStyle = TextStyle(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                xAxisStyle = TextStyle(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.W400
                ),
                yAxisRange = 10,
                barWidth = 30.dp
            )
        }
    }
}

@Composable
fun DynamicPieChart(distribution: List<org.babetech.borastock.domain.usecase.ChartDataPoint>) {
    if (distribution.isEmpty()) {
        EmptyChartState("Aucune donnée de répartition disponible")
        return
    }

    val pieChartData = distribution.mapIndexed { index, point ->
        PieChartData(
            partName = point.label,
            data = point.value,
            color = point.color ?: getColorForIndex(index)
        )
    }

    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Répartition de la valeur du stock",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            PieChart(
                modifier = Modifier.fillMaxSize(),
                pieChartData = pieChartData,
                ratioLineColor = MaterialTheme.colorScheme.outline,
                textRatioStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            )
        }
    }
}

@Composable
fun DynamicDonutChart(statusDistribution: List<org.babetech.borastock.domain.usecase.ChartDataPoint>) {
    if (statusDistribution.isEmpty()) {
        EmptyChartState("Aucune donnée de statut disponible")
        return
    }

    val donutChartData = statusDistribution.map { point ->
        PieChartData(
            partName = point.label,
            data = point.value,
            color = point.color ?: Color(0xFF6b7280)
        )
    }

    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "État du stock",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            DonutChart(
                modifier = Modifier.fillMaxSize(),
                pieChartData = donutChartData,
                centerTitle = "Stock",
                centerTitleStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                outerCircularColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                innerCircularColor = MaterialTheme.colorScheme.surface,
                ratioLineColor = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun DynamicRadarChart(performance: List<org.babetech.borastock.domain.usecase.SupplierPerformancePoint>) {
    if (performance.isEmpty()) {
        EmptyChartState("Aucune donnée de performance disponible")
        return
    }

    val radarLabels = listOf(
        "Fiabilité", "Note", "Livraison", "Coût", "Qualité"
    )

    val polygons = performance.take(3).mapIndexed { index, supplier ->
        Polygon(
            values = listOf(
                supplier.reliability,
                supplier.rating,
                supplier.deliveryTime,
                supplier.costEfficiency,
                supplier.quality
            ),
            unit = "%",
            style = PolygonStyle(
                fillColor = getColorForIndex(index).copy(alpha = 0.3f),
                fillColorAlpha = 0.3f,
                borderColor = getColorForIndex(index),
                borderColorAlpha = 0.8f,
                borderStrokeWidth = 2f,
                borderStrokeCap = StrokeCap.Round
            )
        )
    }

    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Performance des fournisseurs",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            RadarChart(
                modifier = Modifier.fillMaxSize(),
                radarLabels = radarLabels,
                labelsStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                ),
                netLinesStyle = NetLinesStyle(
                    netLineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    netLinesStrokeWidth = 1f,
                    netLinesStrokeCap = StrokeCap.Round
                ),
                scalarSteps = 4,
                scalarValue = 100.0,
                scalarValuesStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp
                ),
                polygons = polygons
            )
        }
    }
}

@Composable
private fun EmptyChartState(message: String) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.analytics),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun getColorForIndex(index: Int): Color {
    val colors = listOf(
        Color(0xFF3b82f6), // Blue
        Color(0xFF22c55e), // Green
        Color(0xFFf59e0b), // Yellow
        Color(0xFFef4444), // Red
        Color(0xFF8b5cf6), // Purple
        Color(0xFF06b6d4), // Cyan
        Color(0xFFf97316), // Orange
        Color(0xFFec4899)  // Pink
    )
    return colors[index % colors.size]
}