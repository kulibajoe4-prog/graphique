package org.babetech.borastock.ui.screens.dashboard.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import borastock.composeapp.generated.resources.*
import com.aay.compose.donutChart.DonutChart
import com.aay.compose.donutChart.model.PieChartData
import org.babetech.borastock.domain.usecase.GetChartDataUseCase
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun StockStatusChartPreview(
    modifier: Modifier = Modifier,
    getChartDataUseCase: GetChartDataUseCase = koinInject()
) {
    val statusDistribution by getChartDataUseCase.getStockStatusDistribution()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn()
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(280.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "État du stock",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "Répartition par statut",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Icon(
                        painter = painterResource(Res.drawable.donutlarge),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (statusDistribution.isNotEmpty()) {
                    val pieChartData = statusDistribution.map { point ->
                        PieChartData(
                            partName = point.label,
                            data = point.value,
                            color = point.color ?: Color(0xFF6b7280)
                        )
                    }

                    DonutChart(
                        modifier = Modifier.fillMaxSize(),
                        pieChartData = pieChartData,
                        centerTitle = "${statusDistribution.sumOf { it.count }}",
                        centerTitleStyle = androidx.compose.ui.text.TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = androidx.compose.ui.unit.sp(18),
                            fontWeight = FontWeight.Bold
                        ),
                        outerCircularColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        innerCircularColor = MaterialTheme.colorScheme.surface,
                        ratioLineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Aucune donnée disponible",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickStatsPreview(
    modifier: Modifier = Modifier,
    getChartDataUseCase: GetChartDataUseCase = koinInject()
) {
    val stockDistribution by getChartDataUseCase.getStockDistribution()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Aperçu rapide",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            if (stockDistribution.isNotEmpty()) {
                val topCategories = stockDistribution.take(3)
                topCategories.forEach { category ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(category.color ?: Color(0xFF3b82f6))
                            )
                            Text(
                                category.label,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            "€${String.format("%.0f", category.value)}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            } else {
                Text(
                    "Aucune donnée disponible",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}