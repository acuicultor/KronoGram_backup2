package com.example.ui.screens.settings.krono

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.theme.KronoThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KronoTranslateSettingsScreen(
    settings: KronoSettingsState,
    onBack: () -> Unit,
    onSetFeature: (String, Any) -> Unit
) {
    val isLight = settings.themeMode == KronoThemeMode.LIGHT_DAY
    val isAmoled = settings.themeMode == KronoThemeMode.AMOLED_PURE
    val bg = if (isLight) MaterialTheme.colorScheme.background else if (isAmoled) AmoledBlack else Color(0xFF090E1A)
    val cardBg = if (isLight) MaterialTheme.colorScheme.surface else Color(0xFF0C1322)
    val textColor = if (isLight) MaterialTheme.colorScheme.onSurface else Color.White
    val topBarBg = if (isLight) MaterialTheme.colorScheme.primary else if (isAmoled) AmoledBlack else Color(0xFF070B14)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Traducción Integrada",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = topBarBg)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Explanation Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = ChronoCyan.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = ChronoCyan,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Traduce al instante cualquier mensaje recibido o traduce automáticamente antes de enviar sin salir del chat.",
                        fontSize = 13.sp,
                        color = if (isLight) MaterialTheme.colorScheme.onSurface else Color(0xFFCBD5E1),
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            SectionHeader(title = "CONFIGURACIÓN DEL MOTOR")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    // Translation Provider Selector
                    var providerMenu by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { providerMenu = true }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(ChronoCyan.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = null,
                                tint = ChronoCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Proveedor de Traducción",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textColor
                            )
                            Text(
                                text = settings.translationProvider,
                                fontSize = 13.sp,
                                color = ChronoCyan
                            )
                        }
                        DropdownMenu(
                            expanded = providerMenu,
                            onDismissRequest = { providerMenu = false }
                        ) {
                            listOf("Google Translate", "DeepL", "Motor Local Krono").forEach { prov ->
                                DropdownMenuItem(
                                    text = { Text(prov) },
                                    trailingIcon = if (settings.translationProvider == prov) {
                                        { Icon(Icons.Default.Check, contentDescription = null, tint = ChronoCyan) }
                                    } else null,
                                    onClick = {
                                        onSetFeature("trans_provider", prov)
                                        providerMenu = false
                                    }
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    // Target Language
                    var langMenu by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { langMenu = true }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(ChronoCyan.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = ChronoCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Idioma Destino Predeterminado",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textColor
                            )
                            Text(
                                text = settings.translationTargetLang,
                                fontSize = 13.sp,
                                color = ChronoCyan
                            )
                        }
                        DropdownMenu(
                            expanded = langMenu,
                            onDismissRequest = { langMenu = false }
                        ) {
                            listOf(
                                "Español", "English", "Français", "Deutsch",
                                "Italiano", "Português", "Русский", "日本語"
                            ).forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text(lang) },
                                    trailingIcon = if (settings.translationTargetLang == lang) {
                                        { Icon(Icons.Default.Check, contentDescription = null, tint = ChronoCyan) }
                                    } else null,
                                    onClick = {
                                        onSetFeature("trans_target_lang", lang)
                                        langMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            SectionHeader(title = "ACCIONES EN CHAT")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    KronoSwitchRow(
                        icon = Icons.Default.Tune,
                        title = "Botón de traducir en mensajes",
                        subtitle = "Muestra icono de traducción instantánea en cada burbuja de chat",
                        checked = settings.enableChatTranslation,
                        onCheckedChange = { onSetFeature("trans_enable_chat", it) },
                        textColor = textColor
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSwitchRow(
                        icon = Icons.Default.Translate,
                        title = "Traducir antes de enviar",
                        subtitle = "Convierte automáticamente tu texto al idioma de destino antes de emitir el mensaje",
                        checked = settings.translateBeforeSend,
                        onCheckedChange = { onSetFeature("trans_before_send", it) },
                        textColor = textColor
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = ChronoCyan,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@Composable
private fun KronoSwitchRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    textColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(ChronoCyan.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ChronoCyan,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = textColor)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray, lineHeight = 16.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = AmoledBlack, checkedTrackColor = ChronoCyan)
        )
    }
}
