package org.babetech.borastock.ui.screens.dashboard

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import borastock.composeapp.generated.resources.*
import org.babetech.borastock.data.models.ChartPeriod
import org.babetech.borastock.ui.screens.dashboard.viewmodel.GraphicsViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphiquesDetailsScreen(
    onBackClick: () -> Unit,
    viewModel: GraphicsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedPeriod by remember { mutableStateOf("30j") }
    var selectedMetric by remember { mutableStateOf("stock") }

    val periods = listOf(
        ChartPeriod("7 jours", "7j"),
        ChartPeriod("30 jours", "30j"),
        ChartPeriod("3 mois", "3m"),
        ChartPeriod("1 an", "1a")
    )

    val metrics = listOf(
        "stock" to "État du stock",
        "movements" to "Mouvements",
        "categories" to "Catégories",
        "suppliers" to "Fournisseurs"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Analyses détaillées",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.3f)
                        )
                    )
                )
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Period selector
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Période d'analyse",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(periods) { period ->
                                FilterChip(
                                    onClick = { selectedPeriod = period.value },
                                    label = { Text(period.label) },
                                    selected = selectedPeriod == period.value,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.primary
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Metric selector
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Type d'analyse",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(metrics) { (key, label) ->
                                FilterChip(
                                    onClick = { selectedMetric = key },
                                    label = { Text(label) },
                                    selected = selectedMetric == key,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.primary
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Chart display based on state
            item {
                when (uiState) {
                    is GraphicsUiState.Loading -> {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
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
                                    CircularProgressIndicator()
                                    Text(
                                        "Chargement des données...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
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
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.error),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onErrorContainer
                                )
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
                                    onClick = { viewModel.loadChartData() }
                                ) {
                                    Text("Réessayer")
                                }
                            }
                        }
                    }
                    is GraphicsUiState.Success -> {
                        DetailedChartView(
                            selectedMetric = selectedMetric,
                            selectedPeriod = selectedPeriod,
                            uiState = uiState
                        )
                    }
                }
            }

            // Additional insights
            item {
                when (uiState) {
                    is GraphicsUiState.Success -> {
                        InsightsSection(uiState)
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun DetailedChartView(
    selectedMetric: String,
    selectedPeriod: String,
    uiState: GraphicsUiState.Success
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        when (selectedMetric) {
            "stock" -> DynamicDonutChart(uiState.stockStatusDistribution)
            "movements" -> DynamicLineChart(uiState.movementTrends)
            "categories" -> DynamicBarChart(uiState.stockDistribution)
            "suppliers" -> DynamicRadarChart(uiState.supplierPerformance)
        }
    }
}

@Composable
private fun InsightsSection(uiState: GraphicsUiState.Success) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Insights automatiques",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Generate insights based on data
            val insights = generateInsights(uiState)
            
            insights.forEach { insight ->
                InsightItem(insight)
            }
        }
    }
}

@Composable
private fun InsightItem(insight: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(MaterialTheme.colorScheme.primary)
        )
        Text(
            insight,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun generateInsights(uiState: GraphicsUiState.Success): List<String> {
    val insights = mutableListOf<String>()
    
    // Stock status insights
    val lowStockItems = uiState.stockStatusDistribution.find { it.label.contains("faible", ignoreCase = true) }
    if (lowStockItems != null && lowStockItems.count > 0) {
        insights.add("${lowStockItems.count} produits ont un stock faible et nécessitent un réapprovisionnement.")
    }
    
    // Category insights
    val topCategory = uiState.stockDistribution.maxByOrNull { it.value }
    if (topCategory != null) {
        insights.add("La catégorie '${topCategory.label}' représente la plus grande valeur de stock (€${String.format("%.0f", topCategory.value)}).")
    }
    
    // Movement trends insights
    if (uiState.movementTrends.isNotEmpty()) {
        val latestTrend = uiState.movementTrends.last()
        if (latestTrend.exits > latestTrend.entries) {
            insights.add("Les sorties dépassent les entrées ce mois-ci. Surveillez les niveaux de stock.")
        } else {
            insights.add("Les entrées sont supérieures aux sorties, indiquant une croissance du stock.")
        }
    }
    
    return insights.ifEmpty { listOf("Aucun insight disponible pour le moment.") }
}