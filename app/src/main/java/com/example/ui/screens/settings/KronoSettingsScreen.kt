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
import androidx.compose.material.icons.automirrored.filled.Forward
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.ui.theme.KronoThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KronoSettingsScreen(
    settings: KronoSettingsState,
    onBack: () -> Unit,
    onNavigateToTranslate: () -> Unit,
    onNavigateToMessaging: () -> Unit,
    onNavigateToInterface: () -> Unit,
    onNavigateToMedia: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToFolders: () -> Unit
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
                    text = "Ajustes de KronoGram",
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
            // Hero Brand Banner Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isLight) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else Color(0xFF0A182E)
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ChronoCyan.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = R.drawable.kronogram_brand_logo,
                        contentDescription = "Krono Logo",
                        modifier = Modifier.size(52.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "KronoGram Labs",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Text(
                            text = "Ajustes avanzados organizados por categorías para optimizar tu experiencia.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // CATEGORÍA 1: MENSAJERÍA Y HERRAMIENTAS
            SectionHeader(title = "MENSAJERÍA Y HERRAMIENTAS")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    KronoSettingCategoryRow(
                        icon = Icons.Default.Translate,
                        title = "Traducción Integrada (Krono Translate)",
                        subtitle = "Motores de traducción, idioma de destino y traducir al enviar",
                        textColor = textColor,
                        onClick = onNavigateToTranslate
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSettingCategoryRow(
                        icon = Icons.AutoMirrored.Filled.Forward,
                        title = "Reenvío y Mensajería Avanzada",
                        subtitle = "Reenvío anónimo sin cita, doble toque y transcripción de voz",
                        textColor = textColor,
                        onClick = onNavigateToMessaging
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // CATEGORÍA 2: PERSONALIZACIÓN Y MULTIMEDIA
            SectionHeader(title = "PERSONALIZACIÓN Y MULTIMEDIA")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    KronoSettingCategoryRow(
                        icon = Icons.Default.Tune,
                        title = "Personalización e Interfaz",
                        subtitle = "Emojis del sistema, tipografía, menú lateral y teclado",
                        textColor = textColor,
                        onClick = onNavigateToInterface
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSettingCategoryRow(
                        icon = Icons.Default.Mood,
                        title = "Multimedia y Stickers",
                        subtitle = "Tamaño de stickers, stickers favoritos ilimitados y auto-pausa",
                        textColor = textColor,
                        onClick = onNavigateToMedia
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSettingCategoryRow(
                        icon = Icons.Default.Folder,
                        title = "Pestañas y Carpetas Inteligentes",
                        subtitle = "Reordenar, activar o crear carpetas de chats personalizadas",
                        textColor = textColor,
                        onClick = onNavigateToFolders
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // CATEGORÍA 3: SEGURIDAD Y PRIVACIDAD
            SectionHeader(title = "SEGURIDAD Y PRIVACIDAD")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    KronoSettingCategoryRow(
                        icon = Icons.Default.Security,
                        title = "Privacidad Avanzada y Seguridad",
                        subtitle = "Modo fantasma, ocultar estado en línea, confirmación de llamada, PIN y huella",
                        textColor = textColor,
                        onClick = onNavigateToPrivacy
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
private fun KronoSettingCategoryRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    textColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(ChronoCyan.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ChronoCyan,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.Gray,
                lineHeight = 16.sp
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(14.dp)
        )
    }
}
