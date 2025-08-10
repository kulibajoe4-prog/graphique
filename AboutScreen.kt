package com.kotlingdgocucb.elimuApp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Add this import for Icons.Default.Check
import androidx.compose.material.icons.filled.Check

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "À propos d'Elimu",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Retour",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background) // Ensure background color
            ) {
                // Welcome Section - Enhanced with a subtle gradient background
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.background
                                )
                            )
                        )
                        .padding(horizontal = 24.dp, vertical = 32.dp), // More vertical padding for this section
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bienvenue sur Elimu",
                        fontSize = 36.sp, // Even larger for a strong impact
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Elimu est une plateforme éducative interactive développée dans le cadre du programme #RoadToMentor – GDG on Campus UCB. " +
                                "Elle offre un accompagnement personnalisé à travers des vidéos, des mentors et une intelligence artificielle dédiée.",
                        fontSize = 18.sp, // Slightly larger body text for readability
                        lineHeight = 26.sp, // Improved line spacing
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f), // Softer text color
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp) // Add some horizontal padding to the text itself
                    )
                }

                Spacer(modifier = Modifier.height(24.dp)) // Space after the welcome section

                // Main content column with consistent horizontal padding
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp) // Apply consistent horizontal padding here
                ) {
                    // Section: Our Mission
                    Text(
                        text = "Notre mission",
                        fontSize = 28.sp, // Consistent heading size
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)) // Rounded corners for the mission box
                            .background(MaterialTheme.colorScheme.surface) // Use surface color for a card-like effect
                            .padding(16.dp) // Padding inside the mission box
                    ) {
                        MissionPoint(text = "Offrir un accès illimité à des vidéos éducatives de qualité.")
                        MissionPoint(text = "Connecter les étudiants à leurs mentors pour un suivi adapté.")
                        MissionPoint(text = "Utiliser l’IA pour enrichir la compréhension et l’apprentissage.")
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Section: Key Features
                    Text(
                        text = "Fonctionnalités clés",
                        fontSize = 28.sp, // Consistent heading size
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)) // Rounded corners for the features box
                            .background(MaterialTheme.colorScheme.surface) // Card-like effect
                            .padding(16.dp) // Padding inside the features box
                    ) {
                        val features = listOf(
                            "Onboarding interactif en 3 étapes.",
                            "Connexion rapide via Google.",
                            "Choix du parcours (Web, Kotlin, Flutter, Python) et mentor dédié.",
                            "Accueil personnalisé avec vidéos populaires et recommandées.",
                            "Détail complet de chaque vidéo, avec interaction IA.",
                            "Chat avec mentor et IA.",
                            "Notifications claires et utiles.",
                            "Profil utilisateur avec historique, favoris, et paramètres.",
                            "Interface multilingue et adaptable.",
                            "Sécurité des données et respect de la vie privée."
                        )
                        features.forEach {
                            FeatureBulletPoint(text = it)
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // Signature - Elevated with a card appearance
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp), // Slightly less horizontal padding for the card itself
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer // A distinct background for the signature
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Add a subtle shadow
                    ) {
                        Text(
                            text = "Une plateforme conçue par la Team de GDG on Campus UCB pour révolutionner l'apprentissage numérique.",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer, // Text color contrasting with secondaryContainer
                            modifier = Modifier
                                .padding(20.dp) // More padding inside the card
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    // Footer
                    Text(
                        text = "© 2025 – Elimu | GDG on Campus UCB. Tous droits réservés.",
                        fontSize = 14.sp, // Slightly larger footer text
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f), // Even lighter, subtle grey
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp), // Padding at the very bottom
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )
}

@Composable
fun MissionPoint(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp), // More vertical padding for each point
        verticalAlignment = Alignment.CenterVertically // Align bullet and text vertically
    ) {
        Text(
            text = "•",
            fontSize = 20.sp, // Slightly larger bullet
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 12.dp) // More space after bullet
        )
        Text(
            text = text,
            fontSize = 17.sp,
            lineHeight = 26.sp, // Consistent line height
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun FeatureBulletPoint(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp), // More vertical padding for each point
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Check, // Using a proper checkmark icon for features
            contentDescription = null, // Decorative icon
            tint = MaterialTheme.colorScheme.tertiary, // Accent color for checkmark
            modifier = Modifier
                .size(20.dp) // Size the icon
                .padding(end = 12.dp) // More space after icon
        )
        Text(
            text = text,
            fontSize = 17.sp,
            lineHeight = 26.sp, // Consistent line height
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


