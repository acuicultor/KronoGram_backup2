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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import com.example.ui.theme.KronoThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacySettingsScreen(
    settings: KronoSettingsState,
    onBack: () -> Unit,
    onSetPrivacy: (String, String) -> Unit
) {
    val isLight = settings.themeMode == KronoThemeMode.LIGHT_DAY
    val bg = if (isLight) MaterialTheme.colorScheme.background else if (settings.themeMode == KronoThemeMode.AMOLED_PURE) AmoledBlack else Color(0xFF090E1A)
    val cardBg = if (isLight) MaterialTheme.colorScheme.surface else Color(0xFF0C1322)
    val textColor = if (isLight) MaterialTheme.colorScheme.onSurface else Color.White

    var showDialogFor by remember { mutableStateOf<String?>(null) } // "lastSeen" or "phone"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Privacidad y seguridad",
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
                text = "PRIVACIDAD",
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
                    PrivacyOptionRow(
                        title = "Número de teléfono",
                        value = settings.phoneNumberPrivacy,
                        onClick = { showDialogFor = "phone" },
                        textColor = textColor
                    )
                    HorizontalDivider(color = Color(0xFF1E2A3A).copy(alpha = 0.4f), thickness = 0.5.dp)

                    PrivacyOptionRow(
                        title = "Última vez y en línea",
                        value = settings.lastSeenPrivacy,
                        onClick = { showDialogFor = "lastSeen" },
                        textColor = textColor
                    )
                    HorizontalDivider(color = Color(0xFF1E2A3A).copy(alpha = 0.4f), thickness = 0.5.dp)

                    PrivacyOptionRow(
                        title = "Fotos de perfil",
                        value = "Todos",
                        onClick = {},
                        textColor = textColor
                    )
                    HorizontalDivider(color = Color(0xFF1E2A3A).copy(alpha = 0.4f), thickness = 0.5.dp)

                    PrivacyOptionRow(
                        title = "Mensajes reenviados",
                        value = "Todos",
                        onClick = {},
                        textColor = textColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "SEGURIDAD",
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
                    PrivacyOptionRow(
                        title = "Código de bloqueo",
                        value = if (settings.securityPin != null) "Activado" else "Desactivado",
                        onClick = {},
                        textColor = textColor
                    )
                    HorizontalDivider(color = Color(0xFF1E2A3A).copy(alpha = 0.4f), thickness = 0.5.dp)

                    PrivacyOptionRow(
                        title = "Verificación en dos pasos (2FA)",
                        value = "Activada (Contraseña en la nube)",
                        onClick = {},
                        textColor = textColor
                    )
                    HorizontalDivider(color = Color(0xFF1E2A3A).copy(alpha = 0.4f), thickness = 0.5.dp)

                    PrivacyOptionRow(
                        title = "Usuarios bloqueados",
                        value = "0 usuarios",
                        onClick = {},
                        textColor = textColor
                    )
                }
            }
        }

        // Privacy selection dialog
        if (showDialogFor != null) {
            val key = showDialogFor!!
            val title = if (key == "phone") "¿Quién puede ver mi número?" else "¿Quién puede ver mi última vez?"
            val currentValue = if (key == "phone") settings.phoneNumberPrivacy else settings.lastSeenPrivacy
            val options = listOf("Todos", "Mis contactos", "Nadie")

            AlertDialog(
                onDismissRequest = { showDialogFor = null },
                title = { Text(title, fontWeight = FontWeight.Bold, color = textColor) },
                text = {
                    Column {
                        options.forEach { option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSetPrivacy(key, option)
                                        showDialogFor = null
                                    }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = option == currentValue,
                                    onClick = {
                                        onSetPrivacy(key, option)
                                        showDialogFor = null
                                    },
                                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(option, fontSize = 15.sp, color = textColor)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { showDialogFor = null }) {
                        Text("Cerrar")
                    }
                },
                containerColor = cardBg,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
private fun PrivacyOptionRow(
    title: String,
    value: String,
    onClick: () -> Unit,
    textColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = textColor)
            Text(value, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(14.dp)
        )
    }
}
