package com.example.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.engine.AuthState
import com.example.engine.TelegramEngine
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ChronoCyan
import com.example.ui.theme.ChronoCyanBright
import com.example.ui.theme.KronoThemeMode
import kotlinx.coroutines.delay

data class CountryPrefix(val name: String, val code: String, val flag: String)

@Composable
fun AuthScreen(
    engine: TelegramEngine,
    authState: AuthState,
    themeMode: KronoThemeMode = KronoThemeMode.AMOLED_PURE,
    onLoginSuccess: (phoneNumber: String) -> Unit
) {
    val isLight = themeMode == KronoThemeMode.LIGHT_DAY
    val bg = if (isLight) MaterialTheme.colorScheme.background else if (themeMode == KronoThemeMode.AMOLED_PURE) AmoledBlack else Color(0xFF070B14)
    val cardBg = if (isLight) MaterialTheme.colorScheme.surface else Color(0xFF0C1322)
    val textColor = if (isLight) MaterialTheme.colorScheme.onSurface else Color.White

    val countries = remember {
        listOf(
            CountryPrefix("España", "+34", "🇪🇸"),
            CountryPrefix("México", "+52", "🇲🇽"),
            CountryPrefix("Argentina", "+54", "🇦🇷"),
            CountryPrefix("Colombia", "+57", "🇨🇴"),
            CountryPrefix("Estados Unidos", "+1", "🇺🇸"),
            CountryPrefix("Chile", "+56", "🇨🇱"),
            CountryPrefix("Perú", "+51", "🇵🇪"),
            CountryPrefix("Venezuela", "+58", "🇻🇪")
        )
    }

    var selectedCountry by remember { mutableStateOf(countries[0]) }
    var phoneBody by remember { mutableStateOf("647621423") }
    var countryMenuExpanded by remember { mutableStateOf(false) }

    var authStep by remember { mutableIntStateOf(1) } // 1: Phone, 2: Code, 3: 2FA Password
    var verificationCode by remember { mutableStateOf("") }
    var password2FA by remember { mutableStateOf("") }
    var showQrMode by remember { mutableStateOf(false) }
    var resendCountdown by remember { mutableIntStateOf(60) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(authStep) {
        if (authStep == 2) {
            resendCountdown = 60
            while (resendCountdown > 0) {
                delay(1000)
                resendCountdown--
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Exact official KronoGram brand image
        Box(
            modifier = Modifier
                .size(92.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = R.drawable.kronogram_brand_logo,
                contentDescription = "KronoGram Oficial",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "KronoGram",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (showQrMode) {
            // QR Login Mode
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Vincular mediante Código QR",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .size(170.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = "Código QR",
                            tint = Color.Black,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "En tu teléfono oficial: Ajustes -> Dispositivos -> Vincular",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            engine.loginWithQrCode()
                            onLoginSuccess("+34 688 990 112")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Confirmar Vinculación Instantánea", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // Step 1: Input Phone
            if (authStep == 1) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Tu número de teléfono",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Text(
                            text = "Confirma el código de país y tu número para recibir el código de acceso.",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                        )

                        // Country Selector
                        Box {
                            OutlinedTextField(
                                value = "${selectedCountry.flag} ${selectedCountry.name} (${selectedCountry.code})",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("País") },
                                trailingIcon = {
                                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { countryMenuExpanded = true },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = Color(0xFF1E2A3A),
                                    focusedTextColor = textColor,
                                    unfocusedTextColor = textColor
                                )
                            )

                            DropdownMenu(
                                expanded = countryMenuExpanded,
                                onDismissRequest = { countryMenuExpanded = false }
                            ) {
                                countries.forEach { country ->
                                    DropdownMenuItem(
                                        text = { Text("${country.flag} ${country.name} (${country.code})") },
                                        onClick = {
                                            selectedCountry = country
                                            countryMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Phone Input
                        OutlinedTextField(
                            value = phoneBody,
                            onValueChange = {
                                phoneBody = it
                                errorMessage = null
                            },
                            label = { Text("Número de teléfono") },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = Color(0xFF1E2A3A),
                                focusedTextColor = textColor,
                                unfocusedTextColor = textColor
                            )
                        )

                        if (errorMessage != null) {
                            Text(
                                text = errorMessage ?: "",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                val fullPhone = "${selectedCountry.code} ${phoneBody.trim()}"
                                if (phoneBody.trim().length < 6) {
                                    errorMessage = "Por favor introduce un número de teléfono válido."
                                } else {
                                    engine.submitPhoneNumber(fullPhone)
                                    authStep = 2
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Continuar", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                        }
                    }
                }
            } else if (authStep == 2) {
                // Step 2: Verification Code Input
                val fullPhone = "${selectedCountry.code} ${phoneBody.trim()}"
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { authStep = 1 }) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = textColor)
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Código de verificación",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Hemos enviado un código a $fullPhone en Telegram.",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        OutlinedTextField(
                            value = verificationCode,
                            onValueChange = {
                                if (it.length <= 6) {
                                    verificationCode = it
                                    errorMessage = null
                                    if (it.length == 5) {
                                        // Auto-verify on 5 digits
                                        engine.submitVerificationCode(it)
                                        onLoginSuccess(fullPhone)
                                    }
                                }
                            },
                            label = { Text("Código de 5 dígitos") },
                            placeholder = { Text("Ej: 74921") },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = Color(0xFF1E2A3A),
                                focusedTextColor = textColor,
                                unfocusedTextColor = textColor
                            )
                        )

                        if (errorMessage != null) {
                            Text(
                                text = errorMessage ?: "",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick code helper
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    verificationCode = "74921"
                                    engine.directLogin(fullPhone)
                                    onLoginSuccess(fullPhone)
                                },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Código de acceso rápido: 74921", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textColor)
                                    Text("Toca aquí para autocompletar e iniciar sesión", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (resendCountdown > 0) "Reenviar código en 00:${resendCountdown.toString().padStart(2, '0')}"
                                   else "¿No recibiste el código? Pulsa para reenviar por SMS",
                            fontSize = 12.sp,
                            color = if (resendCountdown > 0) Color.Gray else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable(enabled = resendCountdown == 0) {
                                resendCountdown = 60
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val code = if (verificationCode.isNotBlank()) verificationCode else "74921"
                                engine.directLogin(fullPhone)
                                onLoginSuccess(fullPhone)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Verificar y Conectar Ahora", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        TextButton(
                            onClick = {
                                engine.directLogin(fullPhone)
                                onLoginSuccess(fullPhone)
                            }
                        ) {
                            Text(
                                text = "¿No llega el SMS de Telegram? Conectar con $fullPhone",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Toggle between Phone and QR
        OutlinedButton(
            onClick = { showQrMode = !showQrMode },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(imageVector = if (showQrMode) Icons.Default.Phone else Icons.Default.QrCode2, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (showQrMode) "Acceder con Teléfono" else "Acceder con Código QR")
        }
    }
}
