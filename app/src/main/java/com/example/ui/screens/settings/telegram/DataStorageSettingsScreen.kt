package com.example.ui.screens.settings.telegram

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.KronoThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataStorageSettingsScreen(
    settings: KronoSettingsState,
    onBack: () -> Unit,
    onToggleAutoDownload: (String, Boolean) -> Unit,
    onSetStorageOptions: (String, String) -> Unit = { _, _ -> },
    onClearCache: () -> Unit
) {
    val isLight = settings.themeMode == KronoThemeMode.LIGHT_DAY
    val bg = if (isLight) MaterialTheme.colorScheme.background else if (settings.themeMode == KronoThemeMode.AMOLED_PURE) AmoledBlack else Color(0xFF090E1A)
    val cardBg = if (isLight) MaterialTheme.colorScheme.surface else Color(0xFF0C1322)
    val textColor = if (isLight) MaterialTheme.colorScheme.onSurface else Color.White

    var cleanedCacheNotice by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Datos y almacenamiento",
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
            Text(
                text = "USO DE ALMACENAMIENTO",
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Storage, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Caché de KronoGram: ${settings.cachedStorageMb} MB",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = { (settings.cachedStorageMb.toFloat() / 500f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = Color(0xFF1E2A3A)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            onClearCache()
                            cleanedCacheNotice = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CleaningServices, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (cleanedCacheNotice) "¡Caché liberada con éxito!" else "Limpiar Caché (${settings.cachedStorageMb} MB)")
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = Color(0xFF1E2A3A).copy(alpha = 0.4f), thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Keep Media
                    var durationMenu by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { durationMenu = true }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Conservar archivos multimedia", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                            Text(settings.keepMediaDuration, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                        }
                        DropdownMenu(expanded = durationMenu, onDismissRequest = { durationMenu = false }) {
                            listOf("3 días", "1 semana", "1 mes", "Para siempre").forEach { dur ->
                                DropdownMenuItem(
                                    text = { Text(dur) },
                                    onClick = {
                                        onSetStorageOptions(dur, settings.maxCacheSize)
                                        durationMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Max Cache
                    var maxCacheMenu by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { maxCacheMenu = true }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Tamaño máximo de caché", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                            Text(settings.maxCacheSize, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                        }
                        DropdownMenu(expanded = maxCacheMenu, onDismissRequest = { maxCacheMenu = false }) {
                            listOf("5 GB", "10 GB", "20 GB", "Sin límite").forEach { max ->
                                DropdownMenuItem(
                                    text = { Text(max) },
                                    onClick = {
                                        onSetStorageOptions(settings.keepMediaDuration, max)
                                        maxCacheMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "AUTODESCARGA DE ARCHIVOS MULTIMEDIA",
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
                    StorageToggleRow(
                        title = "Con datos móviles",
                        subtitle = "Solo fotos y notas de voz",
                        checked = settings.autoDownloadMobile,
                        onCheckedChange = { onToggleAutoDownload("mobile", it) },
                        textColor = textColor
                    )
                    HorizontalDivider(color = Color(0xFF1E2A3A).copy(alpha = 0.4f), thickness = 0.5.dp)

                    StorageToggleRow(
                        title = "Con Wi-Fi",
                        subtitle = "Fotos, vídeos y archivos grandes",
                        checked = settings.autoDownloadWifi,
                        onCheckedChange = { onToggleAutoDownload("wifi", it) },
                        textColor = textColor
                    )
                    HorizontalDivider(color = Color(0xFF1E2A3A).copy(alpha = 0.4f), thickness = 0.5.dp)

                    StorageToggleRow(
                        title = "En itinerancia (Roaming)",
                        subtitle = "Desactivado para ahorrar datos",
                        checked = settings.autoDownloadRoaming,
                        onCheckedChange = { onToggleAutoDownload("roaming", it) },
                        textColor = textColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "REPRODUCCIÓN AUTOMÁTICA",
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
                    StorageToggleRow(
                        title = "GIFs",
                        subtitle = "Reproducir GIFs automáticamente",
                        checked = settings.autoPlayGifs,
                        onCheckedChange = { onToggleAutoDownload("gifs", it) },
                        textColor = textColor
                    )
                    HorizontalDivider(color = Color(0xFF1E2A3A).copy(alpha = 0.4f), thickness = 0.5.dp)

                    StorageToggleRow(
                        title = "Vídeos",
                        subtitle = "Reproducir vídeos en segundo plano",
                        checked = settings.autoPlayVideos,
                        onCheckedChange = { onToggleAutoDownload("videos", it) },
                        textColor = textColor
                    )
                }
            }
        }
    }
}

@Composable
private fun StorageToggleRow(
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
