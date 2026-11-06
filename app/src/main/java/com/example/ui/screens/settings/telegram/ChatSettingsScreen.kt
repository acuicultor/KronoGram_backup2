package com.example.ui.screens.settings.telegram

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun ChatSettingsScreen(
    settings: KronoSettingsState,
    onBack: () -> Unit,
    onSetThemeMode: (KronoThemeMode) -> Unit,
    onSetAppearance: (fontSize: Int, cornerRadius: Int) -> Unit,
    onSetChatSetting: (key: String, value: Any) -> Unit = { _, _ -> }
) {
    val isLight = settings.themeMode == KronoThemeMode.LIGHT_DAY
    val bg = if (isLight) MaterialTheme.colorScheme.background else if (settings.themeMode == KronoThemeMode.AMOLED_PURE) AmoledBlack else Color(0xFF090E1A)
    val cardBg = if (isLight) MaterialTheme.colorScheme.surface else Color(0xFF0C1322)
    val textColor = if (isLight) MaterialTheme.colorScheme.onSurface else Color.White

    var fontSizeSlider by remember { mutableFloatStateOf(settings.chatFontSize.toFloat()) }
    var cornerRadiusSlider by remember { mutableFloatStateOf(settings.bubbleCornerRadius.toFloat()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Ajustes de chats",
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
                containerColor = MaterialTheme.colorScheme.primary
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Live Preview Card
            Text(
                text = "VISTA PREVIA DE MENSAJE",
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
                Column(modifier = Modifier.padding(16.dp)) {
                    // Incoming preview
                    Box(
                        modifier = Modifier
                            .align(Alignment.Start)
                            .clip(RoundedCornerShape(cornerRadiusSlider.dp))
                            .background(if (isLight) Color.White else Color(0xFF131D2E))
                            .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(cornerRadiusSlider.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "¿Cómo se ve el texto de KronoGram?",
                            fontSize = fontSizeSlider.sp,
                            color = textColor
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Outgoing preview
                    Box(
                        modifier = Modifier
                            .align(Alignment.End)
                            .clip(RoundedCornerShape(cornerRadiusSlider.dp))
                            .background(if (isLight) Color(0xFFEFFDDE) else Color(0xFF003852))
                            .border(
                                1.dp,
                                if (isLight) Color(0xFFC7EBB0) else ChronoCyan.copy(alpha = 0.4f),
                                RoundedCornerShape(cornerRadiusSlider.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "¡Se ve perfecto y super nítido!",
                                fontSize = fontSizeSlider.sp,
                                color = if (isLight) Color(0xFF1F2326) else Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.DoneAll,
                                contentDescription = null,
                                tint = if (isLight) Color(0xFF4FAE4E) else ChronoCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Theme selector (3-way)
            Text(
                text = "TEMA VISUAL",
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ThemeSelectCard(
                        title = "Día Clásico",
                        subtitle = "Telegram Blue",
                        isSelected = settings.themeMode == KronoThemeMode.LIGHT_DAY,
                        onClick = { onSetThemeMode(KronoThemeMode.LIGHT_DAY) },
                        modifier = Modifier.weight(1f)
                    )

                    ThemeSelectCard(
                        title = "Noche Cyber",
                        subtitle = "Obsidiana",
                        isSelected = settings.themeMode == KronoThemeMode.DARK_NIGHT,
                        onClick = { onSetThemeMode(KronoThemeMode.DARK_NIGHT) },
                        modifier = Modifier.weight(1f)
                    )

                    ThemeSelectCard(
                        title = "AMOLED",
                        subtitle = "#000000 Puro",
                        isSelected = settings.themeMode == KronoThemeMode.AMOLED_PURE,
                        onClick = { onSetThemeMode(KronoThemeMode.AMOLED_PURE) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Size slider
            Text(
                text = "TAMAÑO DEL TEXTO EN MENSAJES",
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
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Tamaño de letra", fontSize = 14.sp, color = textColor)
                        Text("${fontSizeSlider.toInt()} sp", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }

                    Slider(
                        value = fontSizeSlider,
                        onValueChange = {
                            fontSizeSlider = it
                            onSetAppearance(it.toInt(), cornerRadiusSlider.toInt())
                        },
                        valueRange = 12f..24f,
                        steps = 5,
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Curvatura de burbujas", fontSize = 14.sp, color = textColor)
                        Text("${cornerRadiusSlider.toInt()} dp", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }

                    Slider(
                        value = cornerRadiusSlider,
                        onValueChange = {
                            cornerRadiusSlider = it
                            onSetAppearance(fontSizeSlider.toInt(), it.toInt())
                        },
                        valueRange = 4f..28f,
                        steps = 5,
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Telegram Standard Chat Settings
            Text(
                text = "CONFIGURACIÓN GENERAL DE CHATS",
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Navegador en la app", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                            Text("Abre enlaces dentro de KronoGram", fontSize = 12.sp, color = Color.Gray)
                        }
                        Switch(
                            checked = settings.inAppBrowser,
                            onCheckedChange = { onSetChatSetting("in_app_browser", it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = AmoledBlack, checkedTrackColor = MaterialTheme.colorScheme.primary)
                        )
                    }

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Animation, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Stickers animados en bucle", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                            Text("Reproduce animaciones continuamente", fontSize = 12.sp, color = Color.Gray)
                        }
                        Switch(
                            checked = settings.loopAnimatedStickers,
                            onCheckedChange = { onSetChatSetting("loop_animated_stickers", it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = AmoledBlack, checkedTrackColor = MaterialTheme.colorScheme.primary)
                        )
                    }

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    // Auto night mode
                    var nightMenu by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { nightMenu = true }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.BrightnessAuto, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Modo noche automático", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                            Text(settings.autoNightMode, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                        }
                        DropdownMenu(expanded = nightMenu, onDismissRequest = { nightMenu = false }) {
                            listOf("Desactivado", "Programado", "Adaptativo").forEach { mode ->
                                DropdownMenuItem(
                                    text = { Text(mode) },
                                    onClick = {
                                        onSetChatSetting("auto_night_mode", mode)
                                        nightMenu = false
                                    }
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Guardar en galería", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                            Text("Guarda fotos de chats privados automáticamente", fontSize = 12.sp, color = Color.Gray)
                        }
                        Switch(
                            checked = settings.saveToGalleryPrivate,
                            onCheckedChange = { onSetChatSetting("save_gallery_private", it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = AmoledBlack, checkedTrackColor = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeSelectCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                else Color(0xFF1E2A3A).copy(alpha = 0.3f)
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}
