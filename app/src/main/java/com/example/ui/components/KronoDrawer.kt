package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.repository.KronoSettingsState
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ChronoCyan
import com.example.ui.theme.ChronoCyanBright
import com.example.ui.theme.KronoThemeMode

@Composable
fun KronoDrawer(
    settings: KronoSettingsState,
    onNavigateToSettings: () -> Unit,
    onNavigateToKronoSettings: () -> Unit,
    onNewChatClick: (String) -> Unit,
    onSelectTheme: (KronoThemeMode) -> Unit,
    onToggleGhostMode: (Boolean) -> Unit
) {
    val isLight = settings.themeMode == KronoThemeMode.LIGHT_DAY
    val bg = if (isLight) MaterialTheme.colorScheme.surface else if (settings.themeMode == KronoThemeMode.AMOLED_PURE) AmoledBlack else Color(0xFF0A0F1D)
    val headerBg = if (isLight) MaterialTheme.colorScheme.primary else Color(0xFF0F1728)
    val textColor = if (isLight) Color(0xFF1F2326) else Color.White

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(310.dp)
            .background(bg)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()) // Ensures no item is ever cut off!
            .padding(bottom = 24.dp)
    ) {
        // User Profile Header with Official Brand Logo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerBg)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                    ) {
                        AsyncImage(
                            model = R.drawable.kronogram_brand_logo,
                            contentDescription = "Avatar KronoGram",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    if (settings.isGhostMode) {
                        Box(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Fantasma", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = settings.accountDisplayName,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "${settings.accountPhone} • @${settings.accountUsername}",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 3-Way Theme Switcher (Día, Noche, AMOLED) right in Drawer!
        Text(
            text = "TEMA VISUAL",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(start = 20.dp, top = 8.dp, bottom = 6.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            DrawerThemePill(
                label = "Día",
                isSelected = settings.themeMode == KronoThemeMode.LIGHT_DAY,
                onClick = { onSelectTheme(KronoThemeMode.LIGHT_DAY) },
                modifier = Modifier.weight(1f)
            )

            DrawerThemePill(
                label = "Noche",
                isSelected = settings.themeMode == KronoThemeMode.DARK_NIGHT,
                onClick = { onSelectTheme(KronoThemeMode.DARK_NIGHT) },
                modifier = Modifier.weight(1f)
            )

            DrawerThemePill(
                label = "AMOLED",
                isSelected = settings.themeMode == KronoThemeMode.AMOLED_PURE,
                onClick = { onSelectTheme(KronoThemeMode.AMOLED_PURE) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f), thickness = 0.5.dp)

        // Ghost Mode toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.VisibilityOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Modo Fantasma", fontSize = 14.sp, color = textColor, fontWeight = FontWeight.SemiBold)
                Text("Sin avisos de lectura ni online", fontSize = 11.sp, color = Color.Gray)
            }
            Switch(
                checked = settings.isGhostMode,
                onCheckedChange = onToggleGhostMode,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f), thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(4.dp))

        // Telegram standard actions
        DrawerItem(
            icon = Icons.Default.Group,
            title = "Nuevo Grupo",
            textColor = textColor,
            onClick = { onNewChatClick("GROUP") }
        )

        DrawerItem(
            icon = Icons.Default.Lock,
            title = "Nuevo Chat Secreto (Cifrado E2E)",
            textColor = textColor,
            iconTint = MaterialTheme.colorScheme.primary,
            onClick = { onNewChatClick("SECRET") }
        )

        DrawerItem(
            icon = Icons.Default.VolumeUp,
            title = "Nuevo Canal",
            textColor = textColor,
            onClick = { onNewChatClick("CHANNEL") }
        )

        DrawerItem(
            icon = Icons.Default.Person,
            title = "Contactos",
            textColor = textColor,
            onClick = { onNewChatClick("USER") }
        )

        DrawerItem(
            icon = Icons.Default.Call,
            title = "Llamadas",
            textColor = textColor,
            onClick = {}
        )

        DrawerItem(
            icon = Icons.Default.Bookmark,
            title = "Mensajes Guardados",
            textColor = textColor,
            onClick = {}
        )

        Spacer(modifier = Modifier.height(6.dp))
        HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f), thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(6.dp))

        // Telegram Settings
        DrawerItem(
            icon = Icons.Default.Settings,
            title = "Ajustes de Telegram",
            textColor = textColor,
            onClick = onNavigateToSettings
        )

        // KronoGram Labs (PRO)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .clickable { onNavigateToKronoSettings() }
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.RocketLaunch,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Ajustes de KronoGram",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Labs, pestañas, programador, PIN",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerThemePill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.15f)
            )
            .clickable { onClick() }
            .padding(vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else Color.Gray
        )
    }
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    title: String,
    textColor: Color,
    iconTint: Color = Color.Gray,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 13.dp),
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
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}
