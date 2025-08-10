package com.kotlingdgocucb.elimuApp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextFieldDefaults // Explicitly import OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import com.kotlingdgocucb.elimuApp.domain.model.User // Import the User data class
import com.airbnb.lottie.compose.* // Import Lottie Compose components
import kotlinx.coroutines.delay // Import for delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(
    onBack: () -> Unit = {},
    user: State<User?> // Add user as a State parameter
) {
    // State to control the visibility of the Lottie animation
    var showLottieAnimation by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Envoyer un Feedback", // Titre plus descriptif
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
            Box(modifier = Modifier.fillMaxSize()) { // Use Box to layer content and Lottie
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    // Section de titre principale avec dégradé
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
                            .padding(horizontal = 24.dp, vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Vos retours sont précieux !",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Aidez-nous à améliorer Elimu en partageant vos suggestions, questions ou problèmes.",
                            fontSize = 18.sp,
                            lineHeight = 26.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Formulaire de feedback dans une carte
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp) // Rembourrage horizontal pour la carte
                            .clip(RoundedCornerShape(16.dp)), // Coins arrondis pour la carte
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface // Couleur de surface pour la carte
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Ombre subtile
                    ) {
                        // Remove name and email state variables, as they will come from the user object
                        var message by remember { mutableStateOf("") }

                        Column(
                            modifier = Modifier
                                .padding(20.dp) // Rembourrage à l'intérieur de la carte
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp) // Espacement entre les champs
                        ) {

                            OutlinedTextField(
                                value = message,
                                onValueChange = { message = it },
                                label = { Text("Votre Message") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 120.dp), // Hauteur minimale pour le message
                                maxLines = 10,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    val userName = user.value?.name ?: "N/A"
                                    val userEmail = user.value?.email ?: "N/A"

                                    if (message.isNotBlank()) {
                                        println("Feedback envoyé: Nom=$userName, Email=$userEmail, Message=$message")
                                        message = ""
                                        showLottieAnimation = true // Show Lottie animation
                                    } else {
                                        println("Veuillez saisir votre message.")
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Text(
                                    text = "Envoyer le Feedback",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // Pied de page
                    Text(
                        text = "Nous vous remercions de votre contribution !",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "© 2025 – Elimu | GDG on Campus UCB. Tous droits réservés.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }

                // Lottie Animation Overlay
                if (showLottieAnimation) {
                    // This LaunchedEffect will hide the animation after 3 seconds
                    LaunchedEffect(Unit) {
                        delay(5000L)
                        showLottieAnimation = false // Hide the animation
                    }

                    // Load the Lottie animation composition
                    // IMPORTANT: Replace "https://lottie.host/a6119567-0c7f-4b03-a26a-666324d45d3c/7Tj0V3GjLp.json"
                    // with the actual URL of your Lottie JSON file.
                    // You might need to add the Lottie Compose dependency to your build.gradle:
                    // implementation "com.airbnb.android:lottie-compose:6.0.0" (or the latest version)
                    val composition by rememberLottieComposition(
                        LottieCompositionSpec.Url("https://lottie.host/8a491d89-5a49-4a6f-9869-e1561c1be774/cJ7zjBpbSi.lottie")
                    )

                    // Control the animation playback
                    val progress by animateLottieCompositionAsState(
                        composition,
                        iterations = 1, // Play once
                        isPlaying = showLottieAnimation,
                        speed = 1f,
                        restartOnPlay = true
                    )

                    // Display the Lottie animation centered on the screen
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier
                            .size(200.dp) // Adjust size as needed
                            .align(Alignment.Center) // Center the animation
                    )
                }
            }
        }
    )
}
