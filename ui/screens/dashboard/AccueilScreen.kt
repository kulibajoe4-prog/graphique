@@ .. @@
 import org.babetech.borastock.ui.screens.screennavigation.AccueilUiState
 import org.babetech.borastock.ui.screens.screennavigation.AccueilViewModel
+import org.babetech.borastock.ui.screens.dashboard.components.StockStatusChartPreview
+import org.babetech.borastock.ui.screens.dashboard.components.QuickStatsPreview
 import org.jetbrains.compose.resources.painterResource
 import org.koin.compose.viewmodel.koinViewModel
@@ .. @@
             item { DashboardMetricsGrid(statistics) }
+            item { 
+                StockStatusChartPreview(
+                    modifier = Modifier.padding(vertical = 8.dp)
+                )
+            }
+            item { 
+                QuickStatsPreview(
+                    modifier = Modifier.padding(vertical = 8.dp)
+                )
+            }
             item { RecentMovementsList(recentMovements) }
             if (criticalStockItems.isNotEmpty()) {
                 item { CriticalStockSection(criticalStockItems) }
             }
             item { QuickActionsSection() }