package com.example.ui.screens.chatlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatEntity
import com.example.data.model.FolderTabEntity
import com.example.data.repository.KronoSettingsState
import com.example.ui.components.NewChatDialog
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ChronoCyan
import com.example.ui.theme.ChronoCyanBright
import com.example.ui.theme.ChronoOnlineGreen
import com.example.ui.theme.KronoThemeMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    chats: List<ChatEntity>,
    folders: List<FolderTabEntity>,
    selectedFolderId: String,
    searchQuery: String,
    settings: KronoSettingsState,
    onSelectFolder: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onChatClick: (Long) -> Unit,
    onOpenDrawer: () -> Unit,
    onOpenFolderManager: () -> Unit,
    onOpenKronoSettings: () -> Unit,
    onLockApp: () -> Unit,
    onCreateChat: (String, String, String) -> Unit
) {
    var isSearchActive by remember { mutableStateOf(false) }
    var showNewChatDialog by remember { mutableStateOf(false) }
    var dialogChatType by remember { mutableStateOf("USER") }

    val isAmoled = settings.themeMode == KronoThemeMode.AMOLED_PURE
    val bg = if (isAmoled) AmoledBlack else Color(0xFF0A0F1D)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top App Bar
            TopAppBar(
                title = {
                    if (isSearchActive) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            placeholder = { Text("Buscar mensajes, canales...", color = Color.Gray, fontSize = 14.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "KronoGram",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(ChronoCyan.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "37574376",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ChronoCyan
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    if (isSearchActive) {
                        IconButton(onClick = {
                            isSearchActive = false
                            onSearchQueryChange("")
                        }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar búsqueda", tint = Color.White)
                        }
                    } else {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
                        }

                        // App lock button if security enabled
                        if (settings.isBiometricLockEnabled || !settings.securityPin.isNullOrEmpty()) {
                            IconButton(onClick = onLockApp) {
                                Icon(imageVector = Icons.Default.Lock, contentDescription = "Bloquear", tint = ChronoCyan)
                            }
                        }

                        IconButton(onClick = onOpenKronoSettings) {
                            Icon(imageVector = Icons.Default.RocketLaunch, contentDescription = "Ajustes KronoGram", tint = ChronoCyanBright)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isAmoled) AmoledBlack else Color(0xFF070B14)
                )
            )

            // Scrollable Tab Row with customizable smart folders
            if (folders.isNotEmpty()) {
                val selectedIndex = folders.indexOfFirst { it.id == selectedFolderId }.coerceAtLeast(0)

                ScrollableTabRow(
                    selectedTabIndex = selectedIndex,
                    containerColor = if (isAmoled) AmoledBlack else Color(0xFF090E1A),
                    contentColor = ChronoCyan,
                    edgePadding = 12.dp,
                    indicator = { tabPositions ->
                        if (selectedIndex < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                                color = ChronoCyan,
                                height = 3.dp
                            )
                        }
                    },
                    divider = {
                        HorizontalDivider(color = Color(0xFF162035), thickness = 0.8.dp)
                    }
                ) {
                    folders.forEachIndexed { index, folder ->
                        val isSelected = index == selectedIndex
                        // Calculate unread count for this tab
                        val unreadCount = when (folder.filterType) {
                            "ALL" -> chats.sumOf { it.unreadCount }
                            "UNREAD" -> chats.filter { it.unreadCount > 0 }.sumOf { it.unreadCount }
                            "PERSONAL" -> chats.filter { it.type == "USER" || it.type == "SECRET" }.sumOf { it.unreadCount }
                            "GROUPS" -> chats.filter { it.type == "GROUP" }.sumOf { it.unreadCount }
                            "CHANNELS" -> chats.filter { it.type == "CHANNEL" }.sumOf { it.unreadCount }
                            "BOTS" -> chats.filter { it.type == "BOT" }.sumOf { it.unreadCount }
                            else -> 0
                        }

                        Tab(
                            selected = isSelected,
                            onClick = { onSelectFolder(folder.id) },
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = folder.title,
                                        fontSize = 14.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) ChronoCyan else Color(0xFF8B9BB4)
                                    )

                                    if (unreadCount > 0) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    if (isSelected) ChronoCyan else Color(0xFF1E2D4A),
                                                    CircleShape
                                                )
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = unreadCount.toString(),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) AmoledBlack else Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }

                    // Plus icon to customize folders
                    Tab(
                        selected = false,
                        onClick = onOpenFolderManager,
                        text = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Personalizar Pestañas",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                }
            }

            // Chat List or Empty State
            if (chats.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color(0xFF0F172A), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = ChronoCyan,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No hay conversaciones en esta pestaña",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = "Pulsa el botón flotante para iniciar un nuevo chat o cambiar de carpeta",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp, start = 32.dp, end = 32.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    items(chats, key = { it.id }) { chat ->
                        ChatItemRow(
                            chat = chat,
                            onClick = { onChatClick(chat.id) }
                        )
                        HorizontalDivider(
                            color = Color(0xFF131D2E),
                            thickness = 0.5.dp,
                            modifier = Modifier.padding(start = 76.dp)
                        )
                    }
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = {
                dialogChatType = "USER"
                showNewChatDialog = true
            },
            containerColor = ChronoCyan,
            contentColor = AmoledBlack,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Nuevo Chat", modifier = Modifier.size(24.dp))
        }

        if (showNewChatDialog) {
            NewChatDialog(
                initialType = dialogChatType,
                onDismiss = { showNewChatDialog = false },
                onConfirm = { title, username, type ->
                    showNewChatDialog = false
                    onCreateChat(title, username, type)
                }
            )
        }
    }
}

@Composable
fun ChatItemRow(
    chat: ChatEntity,
    onClick: () -> Unit
) {
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val formattedTime = remember(chat.lastMessageTimestamp) {
        timeFormat.format(Date(chat.lastMessageTimestamp))
    }

    val avatarColor = remember(chat.id) {
        when (chat.type) {
            "SECRET" -> Color(0xFF00B0FF)
            "CHANNEL" -> Color(0xFF7C4DFF)
            "GROUP" -> Color(0xFF00E676)
            "BOT" -> Color(0xFFFFB300)
            else -> Color(0xFF00BCD4)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar with status indicator
        Box(modifier = Modifier.size(52.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(avatarColor.copy(alpha = 0.25f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (chat.isSecretChat) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Secreto",
                        tint = ChronoCyan,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = chat.title.take(2).uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = avatarColor
                    )
                }
            }

            if (chat.isOnline) {
                Box(
                    modifier = Modifier
                        .size(13.dp)
                        .background(ChronoOnlineGreen, CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Content
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (chat.isSecretChat) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = ChronoCyan,
                        modifier = Modifier
                            .size(14.dp)
                            .padding(end = 4.dp)
                    )
                }

                Text(
                    text = chat.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (chat.isMuted) {
                    Icon(
                        imageVector = Icons.Default.VolumeOff,
                        contentDescription = "Silenciado",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(14.dp)
                            .padding(end = 4.dp)
                    )
                }

                Text(
                    text = formattedTime,
                    fontSize = 12.sp,
                    color = if (chat.unreadCount > 0) ChronoCyan else Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Outgoing checkmarks
                Text(
                    text = chat.lastMessage,
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (chat.isPinned) {
                    Icon(
                        imageVector = Icons.Default.PushPin,
                        contentDescription = "Fijado",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(end = 4.dp)
                    )
                }

                if (chat.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .background(ChronoCyan, CircleShape)
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = chat.unreadCount.toString(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmoledBlack
                        )
                    }
                }
            }
        }
    }
}
