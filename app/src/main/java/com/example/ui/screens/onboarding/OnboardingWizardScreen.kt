package com.example.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.MarkEmailUnread
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ChronoCyan
import com.example.ui.theme.ChronoCyanBright

data class TabOption(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector
)

@Composable
fun OnboardingWizardScreen(
    onFinished: (List<String>) -> Unit
) {
    val availableTabs = remember {
        listOf(
            TabOption("all", "Todos los chats", "Bandeja general sin filtrar", Icons.Default.Chat),
            TabOption("unread", "No leídos", "Acceso directo a mensajes pendientes", Icons.Default.MarkEmailUnread),
            TabOption("personal", "Personales", "Conversaciones directas y chats secretos", Icons.Default.Person),
            TabOption("groups", "Grupos", "Comunidades y chats grupales", Icons.Default.Forum),
            TabOption("channels", "Canales", "Canales de noticias y difusión informativa", Icons.Default.VolumeUp),
            TabOption("bots", "Bots", "Servicios automatizados y utilidades", Icons.Default.SmartToy)
        )
    }

    val selectedTabs = remember {
        mutableStateListOf("all", "unread", "personal", "groups", "channels", "bots")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        // Header with glowing emblem
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(ChronoCyan.copy(alpha = 0.35f), Color.Transparent)
                            ),
                            shape = CircleShape
                        )
                        .border(1.5.dp, ChronoCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RocketLaunch,
                        contentDescription = "KronoGram",
                        tint = ChronoCyanBright,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Bienvenido a KronoGram",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Configura tus pestañas y carpetas inteligentes desde cero",
                    fontSize = 14.sp,
                    color = Color(0xFF94A3B8),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Preset Quick Selectors
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    selectedTabs.clear()
                    selectedTabs.addAll(listOf("all", "unread", "personal", "groups", "channels", "bots"))
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = ChronoCyan
                )
            ) {
                Text("Todo Activo", fontSize = 11.sp, maxLines = 1)
            }

            OutlinedButton(
                onClick = {
                    selectedTabs.clear()
                    selectedTabs.addAll(listOf("all", "personal", "groups"))
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = ChronoCyan
                )
            ) {
                Text("Esencial", fontSize = 11.sp, maxLines = 1)
            }

            OutlinedButton(
                onClick = {
                    selectedTabs.clear()
                    selectedTabs.addAll(listOf("all", "unread"))
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = ChronoCyan
                )
            ) {
                Text("Minimalista", fontSize = 11.sp, maxLines = 1)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "SELECCIONA TUS PESTAÑAS ACTIVAS",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = ChronoCyan,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // List of custom folder tabs
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(availableTabs) { tab ->
                val isSelected = selectedTabs.contains(tab.id)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable {
                            if (isSelected) {
                                if (selectedTabs.size > 1) { // Keep at least one tab
                                    selectedTabs.remove(tab.id)
                                }
                            } else {
                                selectedTabs.add(tab.id)
                            }
                        }
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) ChronoCyan else Color(0xFF1E293B),
                            shape = RoundedCornerShape(14.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF0C1424) else Color(0xFF070B12)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    if (isSelected) ChronoCyan.copy(alpha = 0.2f) else Color(0xFF131D2E),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                tint = if (isSelected) ChronoCyan else Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = tab.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                text = tab.description,
                                fontSize = 12.sp,
                                color = Color(0xFF7A8B9E)
                            )
                        }

                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { checked ->
                                if (checked) {
                                    if (!selectedTabs.contains(tab.id)) selectedTabs.add(tab.id)
                                } else {
                                    if (selectedTabs.size > 1) selectedTabs.remove(tab.id)
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = ChronoCyan,
                                checkmarkColor = AmoledBlack,
                                uncheckedColor = Color.DarkGray
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Start KronoGram Button
        Button(
            onClick = {
                onFinished(selectedTabs.toList())
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ChronoCyan,
                contentColor = AmoledBlack
            )
        ) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = AmoledBlack)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Iniciar KronoGram",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
