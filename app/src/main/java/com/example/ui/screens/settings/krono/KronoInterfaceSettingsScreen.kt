package com.example.ui.screens.settings.krono

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Phone
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
fun KronoInterfaceSettingsScreen(
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
                    text = "Personalización e Interfaz",
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
                        text = "Adapta la tipografía, comportamiento del teclado, visualización del perfil en el menú lateral y compatibilidad con el sistema operativo.",
                        fontSize = 13.sp,
                        color = if (isLight) MaterialTheme.colorScheme.onSurface else Color(0xFFCBD5E1),
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            SectionHeader(title = "COLOR DINÁMICO MONET (MATERIAL YOU)")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    KronoSwitchRow(
                        icon = Icons.Default.Palette,
                        title = "Acento dinámico Monet",
                        subtitle = "Sigue la paleta de colores de tu fondo de pantalla del sistema en cualquiera de los temas (Día, Noche o AMOLED)",
                        checked = settings.useMonetColors,
                        onCheckedChange = { onSetFeature("use_monet_colors", it) },
                        textColor = textColor
                    )

                    if (settings.useMonetColors) {
                        HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Acento Monet activo en tiempo real con el fondo del sistema",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            SectionHeader(title = "SISTEMA Y TIPOGRAFÍA")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    KronoSwitchRow(
                        icon = Icons.Default.Mood,
                        title = "Usar emojis del sistema",
                        subtitle = "Muestra los emojis estándar de tu versión de Android en lugar de los de Telegram",
                        checked = settings.useSystemEmojis,
                        onCheckedChange = { onSetFeature("use_system_emojis", it) },
                        textColor = textColor
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSwitchRow(
                        icon = Icons.Default.FontDownload,
                        title = "Usar tipografía del sistema",
                        subtitle = "Aplica la fuente predeterminada de tu dispositivo en toda la aplicación",
                        checked = settings.useSystemFonts,
                        onCheckedChange = { onSetFeature("use_system_fonts", it) },
                        textColor = textColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            SectionHeader(title = "COMPORTAMIENTO DEL TECLADO")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    KronoSwitchRow(
                        icon = Icons.Default.Keyboard,
                        title = "Ocultar teclado al hacer scroll",
                        subtitle = "Baja el teclado suavemente cuando te desplaces verticalmente por los mensajes",
                        checked = settings.hideKeyboardOnScroll,
                        onCheckedChange = { onSetFeature("hide_keyboard_scroll", it) },
                        textColor = textColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            SectionHeader(title = "MENÚ LATERAL (DRAWER)")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    KronoSwitchRow(
                        icon = Icons.Default.Phone,
                        title = "Ocultar número en menú lateral",
                        subtitle = "Oculta tu número telefónico para mayor discreción visual al abrir el menú",
                        checked = settings.hidePhoneInDrawer,
                        onCheckedChange = { onSetFeature("hide_phone_drawer", it) },
                        textColor = textColor
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSwitchRow(
                        icon = Icons.Default.Mood,
                        title = "Avatar como fondo de cabecera",
                        subtitle = "Usa tu foto de perfil difuminada en la parte superior del panel lateral",
                        checked = settings.avatarDrawerBackground,
                        onCheckedChange = { onSetFeature("avatar_drawer_bg", it) },
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
