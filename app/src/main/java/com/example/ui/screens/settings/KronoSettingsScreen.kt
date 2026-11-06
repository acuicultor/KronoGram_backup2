package com.example.ui.screens.settings

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Forward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.ui.theme.ChronoDestructRed
import com.example.ui.theme.KronoThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KronoSettingsScreen(
    settings: KronoSettingsState,
    onBack: () -> Unit,
    onToggleGhostMode: (Boolean) -> Unit,
    onToggleHideOnline: (Boolean) -> Unit,
    onSetThemeMode: (KronoThemeMode) -> Unit,
    onToggleBiometric: (Boolean) -> Unit,
    onSetSecurityPin: (String?) -> Unit,
    onSetKronoFeature: (String, Any) -> Unit,
    onOpenFolderManager: () -> Unit
) {
    var showPinDialog by remember { mutableStateOf(false) }
    var inputPin by remember { mutableStateOf("") }

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
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = topBarBg
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 1. TRADUCCIÓN INTEGRADA
            SectionHeader(title = "TRADUCCIÓN INTEGRADA (KRONO TRANSLATE)")
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
                        Icon(imageVector = Icons.Default.Translate, contentDescription = null, tint = ChronoCyan)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Proveedor de Traducción", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                            Text(settings.translationProvider, fontSize = 12.sp, color = ChronoCyan)
                        }
                        DropdownMenu(expanded = providerMenu, onDismissRequest = { providerMenu = false }) {
                            listOf("Google Translate", "DeepL", "Motor Local Krono").forEach { prov ->
                                DropdownMenuItem(
                                    text = { Text(prov) },
                                    onClick = {
                                        onSetKronoFeature("trans_provider", prov)
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
                        Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = ChronoCyan)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Idioma Destino", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                            Text(settings.translationTargetLang, fontSize = 12.sp, color = ChronoCyan)
                        }
                        DropdownMenu(expanded = langMenu, onDismissRequest = { langMenu = false }) {
                            listOf("Español", "English", "Français", "Deutsch", "Italiano", "Português", "Русский").forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text(lang) },
                                    onClick = {
                                        onSetKronoFeature("trans_target_lang", lang)
                                        langMenu = false
                                    }
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSwitchRow(
                        icon = Icons.Default.Tune,
                        title = "Botón de traducir en mensajes",
                        subtitle = "Muestra icono de traducción instantánea en cada burbuja",
                        checked = settings.enableChatTranslation,
                        onCheckedChange = { onSetKronoFeature("trans_enable_chat", it) },
                        textColor = textColor
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSwitchRow(
                        icon = Icons.Default.Translate,
                        title = "Traducir antes de enviar",
                        subtitle = "Traduce el texto escrito al idioma seleccionado antes de mandarlo",
                        checked = settings.translateBeforeSend,
                        onCheckedChange = { onSetKronoFeature("trans_before_send", it) },
                        textColor = textColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. REENVÍO Y MENSAJERÍA AVANZADA
            SectionHeader(title = "REENVÍO Y MENSAJERÍA AVANZADA")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    KronoSwitchRow(
                        icon = Icons.AutoMirrored.Filled.Forward,
                        title = "Reenviar sin cita (Anónimo)",
                        subtitle = "Elimina automáticamente el autor y cabecera al reenviar mensajes",
                        checked = settings.forwardWithoutQuote,
                        onCheckedChange = { onSetKronoFeature("fwd_no_quote", it) },
                        textColor = textColor
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSwitchRow(
                        icon = Icons.Default.TouchApp,
                        title = "Reenvío con un solo toque",
                        subtitle = "Envía rápidamente a Mensajes Guardados con pulsación directa",
                        checked = settings.oneTapForward,
                        onCheckedChange = { onSetKronoFeature("fwd_one_tap", it) },
                        textColor = textColor
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    // Double Tap Action
                    var doubleTapMenu by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { doubleTapMenu = true }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.TouchApp, contentDescription = null, tint = ChronoCyan)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Acción de doble toque en mensajes", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                            Text(settings.doubleTapAction, fontSize = 12.sp, color = ChronoCyan)
                        }
                        DropdownMenu(expanded = doubleTapMenu, onDismissRequest = { doubleTapMenu = false }) {
                            listOf("Responder", "Traducir", "Copiar texto", "Reaccionar").forEach { act ->
                                DropdownMenuItem(
                                    text = { Text(act) },
                                    onClick = {
                                        onSetKronoFeature("double_tap_action", act)
                                        doubleTapMenu = false
                                    }
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSwitchRow(
                        icon = Icons.Default.Mic,
                        title = "Transcripción de voz a texto",
                        subtitle = "Permite transcribir notas de voz recibidas sin coste",
                        checked = settings.voiceToTextEnabled,
                        onCheckedChange = { onSetKronoFeature("voice_to_text", it) },
                        textColor = textColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. PERSONALIZACIÓN DE INTERFAZ & SISTEMA
            SectionHeader(title = "PERSONALIZACIÓN DE INTERFAZ & SISTEMA")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    KronoSwitchRow(
                        icon = Icons.Default.Mood,
                        title = "Usar emojis del sistema",
                        subtitle = "Muestra los emojis estándar de tu versión de Android",
                        checked = settings.useSystemEmojis,
                        onCheckedChange = { onSetKronoFeature("use_system_emojis", it) },
                        textColor = textColor
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSwitchRow(
                        icon = Icons.Default.FontDownload,
                        title = "Usar tipografía del sistema",
                        subtitle = "Aplica la fuente instalada en tu dispositivo",
                        checked = settings.useSystemFonts,
                        onCheckedChange = { onSetKronoFeature("use_system_fonts", it) },
                        textColor = textColor
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSwitchRow(
                        icon = Icons.Default.Keyboard,
                        title = "Ocultar teclado al hacer scroll",
                        subtitle = "Baja el teclado suavemente al desplazarte por la lista de mensajes",
                        checked = settings.hideKeyboardOnScroll,
                        onCheckedChange = { onSetKronoFeature("hide_keyboard_scroll", it) },
                        textColor = textColor
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSwitchRow(
                        icon = Icons.Default.Phone,
                        title = "Ocultar número en menú lateral",
                        subtitle = "Evita mostrar tu número de teléfono en la cabecera del drawer",
                        checked = settings.hidePhoneInDrawer,
                        onCheckedChange = { onSetKronoFeature("hide_phone_drawer", it) },
                        textColor = textColor
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSwitchRow(
                        icon = Icons.Default.Mood,
                        title = "Avatar como fondo de cabecera",
                        subtitle = "Usa tu foto de perfil difuminada en la parte superior del menú lateral",
                        checked = settings.avatarDrawerBackground,
                        onCheckedChange = { onSetKronoFeature("avatar_drawer_bg", it) },
                        textColor = textColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. CONTROL DE MULTIMEDIA Y STICKERS
            SectionHeader(title = "MULTIMEDIA Y STICKERS")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    // Sticker Size
                    var stickerMenu by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { stickerMenu = true }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Mood, contentDescription = null, tint = ChronoCyan)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Tamaño de Stickers en Chat", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                            Text(settings.stickerSize, fontSize = 12.sp, color = ChronoCyan)
                        }
                        DropdownMenu(expanded = stickerMenu, onDismissRequest = { stickerMenu = false }) {
                            listOf("Pequeño", "Normal", "Grande").forEach { size ->
                                DropdownMenuItem(
                                    text = { Text(size) },
                                    onClick = {
                                        onSetKronoFeature("sticker_size", size)
                                        stickerMenu = false
                                    }
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSwitchRow(
                        icon = Icons.Default.PauseCircle,
                        title = "Pausa automática de vídeos",
                        subtitle = "Pausa la reproducción al pasar la aplicación a segundo plano",
                        checked = settings.autoPauseVideos,
                        onCheckedChange = { onSetKronoFeature("auto_pause_videos", it) },
                        textColor = textColor
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSwitchRow(
                        icon = Icons.Default.Star,
                        title = "Stickers favoritos ilimitados",
                        subtitle = "Elimina el límite estándar de stickers destacados",
                        checked = settings.unlimitedFavoriteStickers,
                        onCheckedChange = { onSetKronoFeature("unlimited_fav_stickers", it) },
                        textColor = textColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 5. PRIVACIDAD AVANZADA Y ANTI-ACCIDENTES
            SectionHeader(title = "PRIVACIDAD AVANZADA Y ANTI-ACCIDENTES")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    KronoSwitchRow(
                        icon = Icons.Default.Call,
                        title = "Confirmación antes de llamar",
                        subtitle = "Evita realizar llamadas o videollamadas involuntarias por pulsación accidental",
                        checked = settings.confirmBeforeCall,
                        onCheckedChange = { onSetKronoFeature("confirm_call", it) },
                        textColor = textColor
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSwitchRow(
                        icon = Icons.Default.VisibilityOff,
                        title = "Ignorar usuarios bloqueados en grupos",
                        subtitle = "Oculta totalmente mensajes de contactos que tengas bloqueados en chats comunes",
                        checked = settings.ignoreBlockedUsersInGroups,
                        onCheckedChange = { onSetKronoFeature("ignore_blocked_groups", it) },
                        textColor = textColor
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSwitchRow(
                        icon = Icons.Default.History,
                        title = "Registro local de mensajes editados",
                        subtitle = "Guarda el contenido original de un mensaje antes de su edición para consulta",
                        checked = settings.localEditHistory,
                        onCheckedChange = { onSetKronoFeature("local_edit_history", it) },
                        textColor = textColor
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSwitchRow(
                        icon = Icons.Default.VisibilityOff,
                        title = "Modo Fantasma Completo",
                        subtitle = "Lee mensajes sin enviar marcas de lectura ni confirmar recibido",
                        checked = settings.isGhostMode,
                        onCheckedChange = onToggleGhostMode,
                        textColor = textColor
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    KronoSwitchRow(
                        icon = Icons.Default.Security,
                        title = "Ocultar Estado En Línea",
                        subtitle = "Nunca actualiza tu presencia ni última hora de conexión",
                        checked = settings.isHideOnlineStatus,
                        onCheckedChange = onToggleHideOnline,
                        textColor = textColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 6. BLOQUEO BIOMÉTRICO Y PIN
            SectionHeader(title = "SEGURIDAD & BLOQUEO")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    KronoSwitchRow(
                        icon = Icons.Default.Fingerprint,
                        title = "Bloqueo Biométrico",
                        subtitle = "Desbloquea con huella dactilar al abrir la app",
                        checked = settings.isBiometricLockEnabled,
                        onCheckedChange = onToggleBiometric,
                        textColor = textColor
                    )

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showPinDialog = true }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = ChronoCyan)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Código PIN de Seguridad", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                            Text(
                                if (settings.securityPin.isNullOrEmpty()) "Sin configurar" else "PIN activo (4 dígitos)",
                                fontSize = 12.sp,
                                color = if (settings.securityPin.isNullOrEmpty()) Color.Gray else ChronoCyan
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
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 7. GESTOR DE CARPETAS Y PESTAÑAS
            SectionHeader(title = "PESTAÑAS Y CARPETAS INTELIGENTES")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenFolderManager() },
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Folder, contentDescription = null, tint = ChronoCyan)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Gestor de Pestañas y Carpetas", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                        Text("Reordena pestañas, activa o crea carpetas personalizadas", fontSize = 12.sp, color = Color.Gray)
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        tint = ChronoCyan,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        // Configure PIN Dialog
        if (showPinDialog) {
            AlertDialog(
                onDismissRequest = { showPinDialog = false },
                title = { Text("Configurar PIN de 4 dígitos", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("Introduce un PIN para proteger el acceso a KronoGram:", fontSize = 13.sp, color = Color.LightGray)
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = inputPin,
                            onValueChange = { if (it.length <= 4) inputPin = it },
                            placeholder = { Text("Ej: 1234") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ChronoCyan,
                                unfocusedBorderColor = Color(0xFF1E293B),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (inputPin.length == 4) {
                                onSetSecurityPin(inputPin)
                                showPinDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ChronoCyan, contentColor = AmoledBlack)
                    ) {
                        Text("Guardar PIN", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = {
                        onSetSecurityPin(null)
                        showPinDialog = false
                    }) {
                        Text("Quitar PIN", color = ChronoDestructRed)
                    }
                },
                containerColor = Color(0xFF090E1A),
                shape = RoundedCornerShape(16.dp)
            )
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
        Icon(imageVector = icon, contentDescription = null, tint = ChronoCyan)
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = textColor)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = AmoledBlack, checkedTrackColor = ChronoCyan)
        )
    }
}
