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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Termes et Conditions",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface // Utiliser la couleur du thème
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Retour",
                            tint = MaterialTheme.colorScheme.onSurface // Utiliser la couleur du thème
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant // Un fond subtil pour la barre supérieure
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background) // S'assurer d'une couleur d'arrière-plan
            ) {
                // Section de titre principale
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
                        .padding(horizontal = 24.dp, vertical = 32.dp), // Plus d'espacement vertical pour cette section
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Conditions générales d'utilisation",
                        fontSize = 32.sp, // Plus grand pour un impact fort
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "En utilisant l’application Elimu, vous acceptez les présentes conditions d'utilisation. Veuillez les lire attentivement.",
                        fontSize = 18.sp, // Texte du corps légèrement plus grand pour la lisibilité
                        lineHeight = 26.sp, // Espacement de ligne amélioré
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f), // Couleur de texte plus douce
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp) // Ajout de rembourrage horizontal au texte lui-même
                    )
                }

                Spacer(modifier = Modifier.height(24.dp)) // Espacement après la section de bienvenue

                // Colonne de contenu principal avec un rembourrage horizontal cohérent
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp) // Appliquer un rembourrage horizontal cohérent ici
                ) {
                    // Section: Utilisation des services
                    TermsSection(
                        title = "1. Utilisation des services",
                        content = "L'accès à la plateforme est réservé aux étudiants et mentors inscrits. Toute utilisation abusive ou frauduleuse entraînera une suspension de compte."
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Section: Données personnelles
                    TermsSection(
                        title = "2. Données personnelles",
                        content = "Les données personnelles collectées (nom, email, photo, track, etc.) sont utilisées uniquement pour le bon fonctionnement de la plateforme. Elles sont sécurisées et ne sont pas partagées sans votre consentement."
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Section: Contenus vidéo
                    TermsSection(
                        title = "3. Contenus vidéo",
                        content = "Les vidéos sont à usage pédagogique uniquement. Toute reproduction ou distribution sans autorisation est interdite."
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Section: Interactions avec les mentors et l'IA
                    TermsSection(
                        title = "4. Interactions avec les mentors et l'IA",
                        content = "Les échanges doivent rester respectueux et professionnels. Tout comportement inapproprié pourra entraîner un bannissement."
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Section: Modifications
                    TermsSection(
                        title = "5. Modifications",
                        content = "Les termes peuvent être mis à jour à tout moment. Vous serez notifié en cas de changement majeur."
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Section: Résiliation
                    TermsSection(
                        title = "6. Résiliation",
                        content = "Vous pouvez supprimer votre compte à tout moment. L’équipe Elimu se réserve le droit de résilier tout compte en cas de non-respect des conditions."
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // Pied de page
                    Text(
                        text = "Dernière mise à jour : juillet 2025",
                        fontSize = 14.sp, // Texte du pied de page légèrement plus grand
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f), // Gris plus clair, subtil
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center // Centrer le pied de page
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "© 2025 – Elimu | GDG on Campus UCB. Tous droits réservés.",
                        fontSize = 14.sp, // Texte du pied de page légèrement plus grand
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f), // Encore plus clair, gris subtil
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp), // Rembourrage tout en bas
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )
}

@Composable
fun TermsSection(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)) // Coins arrondis pour la boîte de section
            .background(MaterialTheme.colorScheme.surface) // Utiliser la couleur de surface pour un effet de carte
            .padding(16.dp) // Rembourrage à l'intérieur de la boîte de section
    ) {
        Text(
            text = title,
            fontSize = 20.sp, // Taille de police pour le titre de la section
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface // Couleur du thème
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            fontSize = 16.sp, // Taille de police pour le contenu de la section
            lineHeight = 24.sp, // Espacement de ligne pour la lisibilité
            color = MaterialTheme.colorScheme.onSurfaceVariant // Couleur de texte plus douce
        )
    }
}
