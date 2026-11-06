package com.example.ui.screens.settings.telegram

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.KronoSettingsState
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ChronoCyan
import com.example.ui.theme.KronoThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsSettingsScreen(
    settings: KronoSettingsState,
    onBack: () -> Unit,
    onToggleSetting: (String, Boolean) -> Unit
) {
    val isLight = settings.themeMode == KronoThemeMode.LIGHT_DAY
    val bg = if (isLight) MaterialTheme.colorScheme.background else if (settings.themeMode == KronoThemeMode.AMOLED_PURE) AmoledBlack else Color(0xFF090E1A)
    val cardBg = if (isLight) MaterialTheme.colorScheme.surface else Color(0xFF0C1322)
    val textColor = if (isLight) MaterialTheme.colorScheme.onSurface else Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Notificaciones y sonidos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isLight) Color.White else Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "NOTIFICACIONES DE CHATS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    NotificationToggleRow(
                        title = "Chats privados",
                        subtitle = "Alertas para mensajes directos",
                        checked = settings.notificationsPrivateChats,
                        onCheckedChange = { onToggleSetting("private", it) },
                        textColor = textColor
                    )
                    HorizontalDivider(color = Color(0xFF1E2A3A).copy(alpha = 0.4f), thickness = 0.5.dp)

                    NotificationToggleRow(
                        title = "Grupos",
                        subtitle = "Alertas para comunidades y grupos",
                        checked = settings.notificationsGroups,
                        onCheckedChange = { onToggleSetting("groups", it) },
                        textColor = textColor
                    )
                    HorizontalDivider(color = Color(0xFF1E2A3A).copy(alpha = 0.4f), thickness = 0.5.dp)

                    NotificationToggleRow(
                        title = "Canales",
                        subtitle = "Alertas para difusiones públicas",
                        checked = settings.notificationsChannels,
                        onCheckedChange = { onToggleSetting("channels", it) },
                        textColor = textColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "AJUSTES EN LA APLICACIÓN",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    NotificationToggleRow(
                        title = "Sonidos en la app",
                        subtitle = "Reproducir sonidos al enviar y recibir",
                        checked = settings.inAppSounds,
                        onCheckedChange = { onToggleSetting("sounds", it) },
                        textColor = textColor
                    )
                    HorizontalDivider(color = Color(0xFF1E2A3A).copy(alpha = 0.4f), thickness = 0.5.dp)

                    NotificationToggleRow(
                        title = "Vibración",
                        subtitle = "Vibrar con nuevas notificaciones",
                        checked = settings.inAppVibrate,
                        onCheckedChange = { onToggleSetting("vibrate", it) },
                        textColor = textColor
                    )
                    HorizontalDivider(color = Color(0xFF1E2A3A).copy(alpha = 0.4f), thickness = 0.5.dp)

                    NotificationToggleRow(
                        title = "Vista previa en la app",
                        subtitle = "Mostrar mensaje emergente al recibir",
                        checked = settings.inAppPreview,
                        onCheckedChange = { onToggleSetting("preview", it) },
                        textColor = textColor
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    textColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = textColor)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}
