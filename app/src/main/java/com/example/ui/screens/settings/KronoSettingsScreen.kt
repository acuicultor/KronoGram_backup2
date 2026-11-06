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
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    onOpenFolderManager: () -> Unit
) {
    var showPinDialog by remember { mutableStateOf(false) }
    var inputPin by remember { mutableStateOf("") }

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
                containerColor = if (isAmoled) AmoledBlack else Color(0xFF070B14)
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 1. GHOST MODE & PRIVACY SECTION
            Text(
                text = "MODO FANTASMA & PRIVACIDAD",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = ChronoCyan,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )

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
                        Icon(imageVector = Icons.Default.VisibilityOff, contentDescription = null, tint = ChronoCyan)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Modo Fantasma Completo", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                            Text("Lee mensajes sin enviar marcas de lectura ni confirmar recibido", fontSize = 12.sp, color = Color.Gray)
                        }
                        Switch(
                            checked = settings.isGhostMode,
                            onCheckedChange = onToggleGhostMode,
                            colors = SwitchDefaults.colors(checkedThumbColor = AmoledBlack, checkedTrackColor = ChronoCyan)
                        )
                    }

                    HorizontalDivider(color = Color(0xFF162136), thickness = 0.5.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = ChronoCyan)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Ocultar Estado En Línea", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                            Text("Nunca actualiza tu presencia ni última hora de conexión", fontSize = 12.sp, color = Color.Gray)
                        }
                        Switch(
                            checked = settings.isHideOnlineStatus,
                            onCheckedChange = onToggleHideOnline,
                            colors = SwitchDefaults.colors(checkedThumbColor = AmoledBlack, checkedTrackColor = ChronoCyan)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. THEME & AMOLED MODE
            Text(
                text = "TEMA VISUAL & AMOLED PURO",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = ChronoCyan,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0C1322)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Selecciona el estilo de interfaz",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ThemeOptionBox(
                            title = "AMOLED Puro",
                            subtitle = "#000000",
                            isSelected = settings.themeMode == KronoThemeMode.AMOLED_PURE,
                            onClick = { onSetThemeMode(KronoThemeMode.AMOLED_PURE) },
                            modifier = Modifier.weight(1f)
                        )

                        ThemeOptionBox(
                            title = "Noche Cyber",
                            subtitle = "Obsidiana",
                            isSelected = settings.themeMode == KronoThemeMode.DARK_NIGHT,
                            onClick = { onSetThemeMode(KronoThemeMode.DARK_NIGHT) },
                            modifier = Modifier.weight(1f)
                        )

                        ThemeOptionBox(
                            title = "Modo Día",
                            subtitle = "Telegram Light",
                            isSelected = settings.themeMode == KronoThemeMode.LIGHT_DAY,
                            onClick = { onSetThemeMode(KronoThemeMode.LIGHT_DAY) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. BIOMETRIC LOCK & PIN
            Text(
                text = "SEGURIDAD & BLOQUEO DE LA APLICACIÓN",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = ChronoCyan,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )

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
                        Icon(imageVector = Icons.Default.Fingerprint, contentDescription = null, tint = ChronoCyan)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Bloqueo Biométrico", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                            Text("Desbloquea con huella dactilar al abrir la app", fontSize = 12.sp, color = Color.Gray)
                        }
                        Switch(
                            checked = settings.isBiometricLockEnabled,
                            onCheckedChange = onToggleBiometric,
                            colors = SwitchDefaults.colors(checkedThumbColor = AmoledBlack, checkedTrackColor = ChronoCyan)
                        )
                    }

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
                            Text("Código PIN de Seguridad", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
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

            // 4. SMART FOLDERS & CUSTOM TABS
            Text(
                text = "PESTAÑAS Y CARPETAS INTELIGENTES",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = ChronoCyan,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenFolderManager() },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0C1322)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Folder, contentDescription = null, tint = ChronoCyan)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Gestor de Pestañas y Carpetas", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
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
fun ThemeOptionBox(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) ChronoCyan else Color(0xFF1E2B42),
                shape = RoundedCornerShape(12.dp)
            )
            .background(if (isSelected) Color(0xFF002233) else Color(0xFF070B14), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isSelected) ChronoCyan else Color.White)
            Text(subtitle, fontSize = 10.sp, color = Color.Gray)
        }
    }
}
