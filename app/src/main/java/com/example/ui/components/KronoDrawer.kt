package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.KronoSettingsState
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.AmoledSurface
import com.example.ui.theme.ChronoCyan
import com.example.ui.theme.ChronoCyanBright
import com.example.ui.theme.KronoThemeMode

@Composable
fun KronoDrawer(
    settings: KronoSettingsState,
    onNavigateToSettings: () -> Unit,
    onNavigateToKronoSettings: () -> Unit,
    onNewChatClick: (String) -> Unit,
    onToggleAmoled: (Boolean) -> Unit,
    onToggleGhostMode: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(310.dp)
            .background(if (settings.themeMode == KronoThemeMode.AMOLED_PURE) AmoledBlack else AmoledSurface)
            .padding(vertical = 24.dp)
    ) {
        // Profile Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFF0F2644), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "RC",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChronoCyan
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Ghost Mode status badge
                    if (settings.isGhostMode) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF0F2644), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = ChronoCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Fantasma", fontSize = 11.sp, color = ChronoCyan)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Ramon Cuenca",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "+34 612 345 678 • @rcuenca",
                    fontSize = 13.sp,
                    color = Color.LightGray
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(color = Color(0xFF1B2A44), thickness = 0.8.dp)
        Spacer(modifier = Modifier.height(8.dp))

        // Quick Toggles in Drawer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.VisibilityOff,
                contentDescription = null,
                tint = ChronoCyan,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Modo Fantasma", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
                Text("Sin avisos de lectura ni online", fontSize = 11.sp, color = Color.Gray)
            }
            Switch(
                checked = settings.isGhostMode,
                onCheckedChange = onToggleGhostMode,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = AmoledBlack,
                    checkedTrackColor = ChronoCyan
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DarkMode,
                contentDescription = null,
                tint = ChronoCyan,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("AMOLED Puro", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
                Text("Negro absoluto para OLED", fontSize = 11.sp, color = Color.Gray)
            }
            Switch(
                checked = settings.themeMode == KronoThemeMode.AMOLED_PURE,
                onCheckedChange = onToggleAmoled,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = AmoledBlack,
                    checkedTrackColor = ChronoCyan
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color(0xFF1B2A44), thickness = 0.8.dp)
        Spacer(modifier = Modifier.height(8.dp))

        // Actions
        DrawerItem(
            icon = Icons.Default.Group,
            title = "Nuevo Grupo",
            onClick = { onNewChatClick("GROUP") }
        )

        DrawerItem(
            icon = Icons.Default.Lock,
            title = "Nuevo Chat Secreto (Cifrado E2E)",
            iconTint = ChronoCyan,
            onClick = { onNewChatClick("SECRET") }
        )

        DrawerItem(
            icon = Icons.Default.VolumeUp,
            title = "Nuevo Canal",
            onClick = { onNewChatClick("CHANNEL") }
        )

        DrawerItem(
            icon = Icons.Default.Person,
            title = "Contactos",
            onClick = { onNewChatClick("USER") }
        )

        DrawerItem(
            icon = Icons.Default.Call,
            title = "Llamadas Cifradas",
            onClick = {}
        )

        DrawerItem(
            icon = Icons.Default.Bookmark,
            title = "Mensajes Guardados",
            onClick = {}
        )

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color(0xFF1B2A44), thickness = 0.8.dp)
        Spacer(modifier = Modifier.height(8.dp))

        // Telegram Settings
        DrawerItem(
            icon = Icons.Default.Settings,
            title = "Ajustes de Telegram",
            onClick = onNavigateToSettings
        )

        // KronoGram Labs (Highlight)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .background(Color(0xFF0C192E), RoundedCornerShape(12.dp))
                .clickable { onNavigateToKronoSettings() }
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.RocketLaunch,
                    contentDescription = null,
                    tint = ChronoCyanBright,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Ajustes de KronoGram",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChronoCyan
                    )
                    Text(
                        text = "Labs, pestañas, programador, PIN",
                        fontSize = 11.sp,
                        color = Color(0xFF8BA2BF)
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    title: String,
    iconTint: Color = Color.LightGray,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconTint,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(18.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}
