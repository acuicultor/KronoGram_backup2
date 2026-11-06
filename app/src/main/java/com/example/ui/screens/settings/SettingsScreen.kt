package com.example.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.KronoSettingsState
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ChronoCyan
import com.example.ui.theme.ChronoCyanBright
import com.example.ui.theme.ChronoDestructRed
import com.example.ui.theme.KronoThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: KronoSettingsState,
    onBack: () -> Unit,
    onNavigateToKronoSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val isAmoled = settings.themeMode == KronoThemeMode.AMOLED_PURE
    val bg = if (isAmoled) AmoledBlack else Color(0xFF090E1A)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Ajustes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = if (isAmoled) AmoledBlack else Color(0xFF070B14)
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // User Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0C1322)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color(0xFF003852), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "RC",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChronoCyan
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = settings.accountDisplayName,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = settings.accountPhone,
                            fontSize = 13.sp,
                            color = Color.LightGray
                        )
                        Text(
                            text = "@${settings.accountUsername}",
                            fontSize = 12.sp,
                            color = ChronoCyan
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Prominent KronoGram Settings Card (The user's requested dedicated section!)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToKronoSettings() },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0A182E)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, ChronoCyan.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(ChronoCyan.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RocketLaunch,
                            contentDescription = "KronoGram Labs",
                            tint = ChronoCyanBright,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Ajustes de KronoGram",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ChronoCyan
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(ChronoCyan, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text("PRO", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AmoledBlack)
                            }
                        }
                        Text(
                            text = "Modo Fantasma, AMOLED Puro, Bloqueo PIN, Pestañas",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        tint = ChronoCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "AJUSTES DE TELEGRAM",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )

            // Telegram Standard Settings Items
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0C1322)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    SettingsRowItem(
                        icon = Icons.Default.Notifications,
                        title = "Notificaciones y sonidos",
                        subtitle = "Activadas para todos los chats"
                    )
                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    SettingsRowItem(
                        icon = Icons.Default.Security,
                        title = "Privacidad y seguridad",
                        subtitle = "Verificación 2FA, sesiones activas"
                    )
                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    SettingsRowItem(
                        icon = Icons.Default.DataUsage,
                        title = "Datos y almacenamiento",
                        subtitle = "Uso de red y limpieza de caché"
                    )
                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    SettingsRowItem(
                        icon = Icons.Default.ChatBubble,
                        title = "Ajustes de chats",
                        subtitle = "Temas, fuentes y fondos de pantalla"
                    )
                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    SettingsRowItem(
                        icon = Icons.Default.Devices,
                        title = "Dispositivos",
                        subtitle = "1 sesión activa en este teléfono"
                    )
                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    SettingsRowItem(
                        icon = Icons.Default.Language,
                        title = "Idioma",
                        subtitle = "Español (automático)"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Version info & logout
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0C1322)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = ChronoCyan, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text("KronoGram para Android", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                            Text("Versión 1.0.0 (Target SDK 36) • MTProto API ID: 37574376", fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLogout() }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription = null, tint = ChronoDestructRed, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(14.dp))
                        Text("Cerrar Sesión", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = ChronoDestructRed)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsRowItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = ChronoCyan,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.White)
            Text(subtitle, fontSize = 11.sp, color = Color.Gray)
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Color(0xFF475569),
            modifier = Modifier.size(14.dp)
        )
    }
}
