@@ .. @@
 import org.babetech.borastock.data.models.ChartPeriod
 import org.babetech.borastock.data.models.StatisticCard
 import org.babetech.borastock.data.models.TopProduct
+import org.babetech.borastock.ui.screens.dashboard.GraphiquesDetailsScreen
 import org.jetbrains.compose.resources.painterResource
@@ .. @@
 @OptIn(ExperimentalMaterial3AdaptiveApi::class)
 @Composable
-fun StatistiqueScreen() {
+fun StatistiqueScreen(
+    onNavigateToDetailedCharts: () -> Unit = {}
+) {
     val navigator = rememberSupportingPaneScaffoldNavigator()
     val scope = rememberCoroutineScope()
+    var showDetailedCharts by remember { mutableStateOf(false) }
 
     var selectedPeriod by remember { mutableStateOf("7j") }
     var selectedChart by remember { mutableStateOf("Ventes") }
@@ .. @@
                     // Bouton pour afficher les graphiques détaillés
                     item {
                         Button(
                             onClick = {
-                                scope.launch {
-                                    navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
-                                }
+                                showDetailedCharts = true
                             },
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .height(48.dp),
                             shape = RoundedCornerShape(12.dp),
                             colors = ButtonDefaults.buttonColors(
                                 containerColor = MaterialTheme.colorScheme.primaryContainer
                             )
                         ) {
                             Row(
                                 horizontalArrangement = Arrangement.spacedBy(8.dp),
                                 verticalAlignment = Alignment.CenterVertically
                             ) {
                                 Icon(
                                     painterResource(Res.drawable.analytics),
                                     contentDescription = null,
                                     modifier = Modifier.size(18.dp)
                                 )
                                 Text(
-                                    "Voir les graphiques détaillés",
+                                    "Analyses avancées",
                                     style = MaterialTheme.typography.labelLarge.copy(
                                         fontWeight = FontWeight.Medium
                                     )
                                 )
                             }
                         }
                     }
+
+                    // Quick charts preview
+                    item {
+                        Button(
+                            onClick = {
+                                scope.launch {
+                                    navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
+                                }
+                            },
+                            modifier = Modifier
+                                .fillMaxWidth()
+                                .height(48.dp),
+                            shape = RoundedCornerShape(12.dp),
+                            colors = ButtonDefaults.buttonColors(
+                                containerColor = MaterialTheme.colorScheme.secondaryContainer
+                            )
+                        ) {
+                            Row(
+                                horizontalArrangement = Arrangement.spacedBy(8.dp),
+                                verticalAlignment = Alignment.CenterVertically
+                            ) {
+                                Icon(
+                                    painterResource(Res.drawable.barchart),
+                                    contentDescription = null,
+                                    modifier = Modifier.size(18.dp)
+                                )
+                                Text(
+                                    "Graphiques interactifs",
+                                    style = MaterialTheme.typography.labelLarge.copy(
+                                        fontWeight = FontWeight.Medium
+                                    )
+                                )
+                            }
+                        }
+                    }
@@ .. @@
         },
         supportingPane = {
             AnimatedPane {
-                DetailedAnalyticsPane(
-                    selectedChart = selectedChart,
-                    onChartSelected = { selectedChart = it },
-                    selectedPeriod = selectedPeriod,
-                    onBackClick = {
-                        scope.launch {
-                            navigator.navigateBack()
-                        }
-                    },
-                    showBackButton = navigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] != PaneAdaptedValue.Expanded
-                )
+                GraphicSwitcherScreen()
             }
         }
     )
+
+    // Full-screen detailed charts
+    if (showDetailedCharts) {
+        GraphiquesDetailsScreen(
+            onBackClick = { showDetailedCharts = false }
+        )
+    }
 }