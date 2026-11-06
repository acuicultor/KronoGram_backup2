package com.example.ui.screens.chatdetail

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatEntity
import com.example.data.model.MessageEntity
import com.example.data.repository.KronoSettingsState
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.BubbleInAmoled
import com.example.ui.theme.BubbleOutAmoled
import com.example.ui.theme.ChronoCyan
import com.example.ui.theme.ChronoDestructRed
import com.example.ui.theme.ChronoOnlineGreen
import com.example.ui.theme.KronoThemeMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    chat: ChatEntity?,
    messages: List<MessageEntity>,
    settings: KronoSettingsState,
    onBack: () -> Unit,
    onSendMessage: (text: String, isEncrypted: Boolean, scheduledTimestamp: Long?, autoDestructSeconds: Int?) -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    var showSchedulerDialog by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val isLight = settings.themeMode == KronoThemeMode.LIGHT_DAY
    val bg = if (isLight) MaterialTheme.colorScheme.background else if (settings.themeMode == KronoThemeMode.AMOLED_PURE) AmoledBlack else Color(0xFF090E1A)
    val inputBarBg = if (isLight) MaterialTheme.colorScheme.surface else if (settings.themeMode == KronoThemeMode.AMOLED_PURE) AmoledBlack else Color(0xFF0C1322)

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Chat TopAppBar
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    if (chat?.isSecretChat == true) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.2f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (chat?.isSecretChat == true) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Cifrado E2E",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text(
                                    text = chat?.title?.take(2)?.uppercase() ?: "TG",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (chat?.isSecretChat == true) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp).padding(end = 3.dp)
                                    )
                                }
                                Text(
                                    text = chat?.title ?: "Chat",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = if (chat?.isSecretChat == true) "Cifrado MTProto • Huella: ⏱️🔒⚡"
                                       else if (chat?.isOnline == true) "en línea"
                                       else "visto recientemente",
                                fontSize = 11.sp,
                                color = if (chat?.isOnline == true) Color(0xFFB9F6CA) else Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { showSchedulerDialog = true }) {
                        Icon(imageVector = Icons.Default.Schedule, contentDescription = "Programar Mensaje", tint = Color.White)
                    }
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = "Llamar", tint = Color.White)
                    }
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Opciones", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )

            // Secret Chat Banner if applicable
            if (chat?.isSecretChat == true) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isLight) Color(0xFFDCEAF5) else Color(0xFF0A1828))
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🔒 Chat secreto de extremo a extremo: nada queda en servidores",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages, key = { it.id }) { message ->
                    MessageBubble(
                        message = message,
                        isLight = isLight,
                        fontSize = settings.chatFontSize,
                        cornerRadius = settings.bubbleCornerRadius,
                        settings = settings
                    )
                }
            }

            // Input Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(inputBarBg)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.SentimentSatisfiedAlt, contentDescription = "Stickers", tint = Color.Gray)
                }

                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.AttachFile, contentDescription = "Adjuntar", tint = Color.Gray)
                }

                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Escribe un mensaje...", color = Color.Gray, fontSize = 14.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = if (isLight) Color(0xFFF1F5F9) else Color(0xFF131D2E),
                        unfocusedContainerColor = if (isLight) Color(0xFFF1F5F9) else Color(0xFF131D2E),
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = if (isLight) Color(0xFF1F2326) else Color.White,
                        unfocusedTextColor = if (isLight) Color(0xFF1F2326) else Color.White
                    ),
                    singleLine = false,
                    maxLines = 4
                )

                Spacer(modifier = Modifier.width(6.dp))

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .clickable {
                            if (messageText.isNotBlank()) {
                                onSendMessage(
                                    messageText,
                                    chat?.isSecretChat == true,
                                    null,
                                    null
                                )
                                messageText = ""
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Chrono Scheduler & Auto-Destruct Dialog
        if (showSchedulerDialog) {
            ChronoScheduleDialog(
                onDismiss = { showSchedulerDialog = false },
                onScheduleConfirmed = { delaySeconds, destructSeconds ->
                    showSchedulerDialog = false
                    if (messageText.isNotBlank()) {
                        val scheduledTime = if (delaySeconds > 0) System.currentTimeMillis() + (delaySeconds * 1000L) else null
                        val autoDestruct = if (destructSeconds > 0) destructSeconds else null

                        onSendMessage(
                            messageText,
                            chat?.isSecretChat == true,
                            scheduledTime,
                            autoDestruct
                        )
                        messageText = ""
                    }
                }
            )
        }
    }
}

@Composable
fun MessageBubble(
    message: MessageEntity,
    isLight: Boolean,
    fontSize: Int,
    cornerRadius: Int,
    settings: KronoSettingsState
) {
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val formattedTime = remember(message.timestamp) {
        timeFormat.format(Date(message.timestamp))
    }

    val isOut = message.isOutgoing
    val alignment = if (isOut) Alignment.End else Alignment.Start

    val bubbleColor = if (isOut) {
        if (isLight) Color(0xFFEFFDDE) else BubbleOutAmoled
    } else {
        if (isLight) Color.White else BubbleInAmoled
    }

    val textColor = if (isLight) Color(0xFF1F2326) else Color.White
    val bubbleBorder = if (isOut) {
        if (isLight) Color(0xFFC7EBB0) else ChronoCyan.copy(alpha = 0.3f)
    } else {
        if (isLight) Color(0xFFD6DCE2) else Color(0xFF1E2C44)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = cornerRadius.dp,
                        topEnd = cornerRadius.dp,
                        bottomStart = if (isOut) cornerRadius.dp else 2.dp,
                        bottomEnd = if (isOut) 2.dp else cornerRadius.dp
                    )
                )
                .background(bubbleColor)
                .border(
                    width = 1.dp,
                    color = bubbleBorder,
                    shape = RoundedCornerShape(
                        topStart = cornerRadius.dp,
                        topEnd = cornerRadius.dp,
                        bottomStart = if (isOut) cornerRadius.dp else 2.dp,
                        bottomEnd = if (isOut) 2.dp else cornerRadius.dp
                    )
                )
                .padding(horizontal = 14.dp, vertical = 9.dp)
        ) {
            var isTranslated by remember { mutableStateOf(false) }

            Column {
                if (!isOut) {
                    Text(
                        text = message.senderName,
                        fontSize = (fontSize - 3).coerceAtLeast(11).sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                Text(
                    text = message.text,
                    fontSize = fontSize.sp,
                    color = textColor
                )

                if (isTranslated) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Traducido (${settings.translationTargetLang} • ${settings.translationProvider}):\n${message.text}",
                        fontSize = (fontSize - 2).coerceAtLeast(11).sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (settings.enableChatTranslation) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Traducir",
                            tint = if (isTranslated) MaterialTheme.colorScheme.primary else Color.Gray,
                            modifier = Modifier
                                .size(14.dp)
                                .clickable { isTranslated = !isTranslated }
                                .padding(end = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    if (message.autoDestructSeconds != null && message.autoDestructSeconds > 0) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Autodestrucción",
                            tint = ChronoDestructRed,
                            modifier = Modifier.size(12.dp).padding(end = 3.dp)
                        )
                        Text(
                            text = "${message.autoDestructSeconds}s",
                            fontSize = 10.sp,
                            color = ChronoDestructRed,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }

                    if (message.scheduledTimestamp != null && message.scheduledTimestamp > System.currentTimeMillis()) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Programado",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(12.dp).padding(end = 3.dp)
                        )
                    }

                    Text(
                        text = formattedTime,
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    if (isOut) {
                        Spacer(modifier = Modifier.width(4.dp))
                        when (message.deliveryStatus) {
                            "READ" -> Icon(
                                imageVector = Icons.Default.DoneAll,
                                contentDescription = "Leído",
                                tint = if (isLight) Color(0xFF4FAE4E) else ChronoCyan,
                                modifier = Modifier.size(15.dp)
                            )
                            "SENT" -> Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "Enviado",
                                tint = Color.Gray,
                                modifier = Modifier.size(15.dp)
                            )
                            else -> Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = "Pendiente",
                                tint = Color.Gray,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChronoScheduleDialog(
    onDismiss: () -> Unit,
    onScheduleConfirmed: (delaySeconds: Int, destructSeconds: Int) -> Unit
) {
    var selectedDelayOption by remember { mutableIntStateOf(0) }
    var selectedDestructOption by remember { mutableIntStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Programador Chrono", fontSize = 17.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "CUÁNDO ENVIAR EL MENSAJE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                val delays = listOf(
                    Pair(0, "Enviar inmediatamente"),
                    Pair(60, "En 1 minuto"),
                    Pair(300, "En 5 minutos"),
                    Pair(3600, "En 1 hora")
                )

                delays.forEach { (sec, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedDelayOption = sec }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedDelayOption == sec,
                            onClick = { selectedDelayOption = sec },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(label, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "TEMPORIZADOR DE AUTODESTRUCCIÓN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ChronoDestructRed
                )

                val destructs = listOf(
                    Pair(0, "Desactivado"),
                    Pair(10, "10 segundos tras lectura"),
                    Pair(30, "30 segundos tras lectura"),
                    Pair(60, "1 minuto tras lectura")
                )

                destructs.forEach { (sec, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedDestructOption = sec }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedDestructOption == sec,
                            onClick = { selectedDestructOption = sec },
                            colors = RadioButtonDefaults.colors(selectedColor = ChronoDestructRed)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(label, fontSize = 13.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onScheduleConfirmed(selectedDelayOption, selectedDestructOption) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Confirmar Envío", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
