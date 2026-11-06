package com.example.ui.screens.chatlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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

    val isLight = settings.themeMode == KronoThemeMode.LIGHT_DAY
    val bg = if (isLight) MaterialTheme.colorScheme.background else if (settings.themeMode == KronoThemeMode.AMOLED_PURE) AmoledBlack else Color(0xFF0A0F1D)
    val tabBg = if (isLight) MaterialTheme.colorScheme.surface else if (settings.themeMode == KronoThemeMode.AMOLED_PURE) AmoledBlack else Color(0xFF090E1A)
    val dividerColor = if (isLight) Color(0xFFE2E8F0) else Color(0xFF131D2E)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Clean Elegant Top App Bar (Without the ugly stacked badge!)
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
                            if (settings.isGhostMode) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("Fantasma", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
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
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                        }
                    } else {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
                        }

                        if (settings.isBiometricLockEnabled || !settings.securityPin.isNullOrEmpty()) {
                            IconButton(onClick = onLockApp) {
                                Icon(imageVector = Icons.Default.Lock, contentDescription = "Bloquear", tint = Color.White)
                            }
                        }

                        IconButton(onClick = onOpenKronoSettings) {
                            Icon(imageVector = Icons.Default.RocketLaunch, contentDescription = "Ajustes KronoGram", tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )

            // Scrollable Tab Row with customizable smart folders
            if (folders.isNotEmpty()) {
                val selectedIndex = folders.indexOfFirst { it.id == selectedFolderId }.coerceAtLeast(0)

                ScrollableTabRow(
                    selectedTabIndex = selectedIndex,
                    containerColor = tabBg,
                    contentColor = MaterialTheme.colorScheme.primary,
                    edgePadding = 12.dp,
                    indicator = { tabPositions ->
                        if (selectedIndex < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                                color = MaterialTheme.colorScheme.primary,
                                height = 3.dp
                            )
                        }
                    },
                    divider = {
                        HorizontalDivider(color = dividerColor, thickness = 0.8.dp)
                    }
                ) {
                    folders.forEachIndexed { index, folder ->
                        val isSelected = index == selectedIndex
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
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                                    )

                                    if (unreadCount > 0) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.4f),
                                                    CircleShape
                                                )
                                                .padding(horizontal = 7.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = unreadCount.toString(),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }

                    Tab(
                        selected = false,
                        onClick = onOpenFolderManager,
                        text = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Personalizar Pestañas",
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                }
            }

            // Chat List with generous bottom padding so FAB never covers the last item!
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
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No hay conversaciones en esta pestaña",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isLight) MaterialTheme.colorScheme.onBackground else Color.White
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = 96.dp) // Ensures FAB never overlaps the last chat!
                ) {
                    items(chats, key = { it.id }) { chat ->
                        ChatItemRow(
                            chat = chat,
                            isLight = isLight,
                            onClick = { onChatClick(chat.id) }
                        )
                        HorizontalDivider(
                            color = dividerColor,
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
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
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
    isLight: Boolean,
    onClick: () -> Unit
) {
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val formattedTime = remember(chat.lastMessageTimestamp) {
        timeFormat.format(Date(chat.lastMessageTimestamp))
    }

    val titleColor = if (isLight) Color(0xFF1F2326) else Color.White
    val lastMsgColor = if (isLight) Color(0xFF707579) else Color(0xFF94A3B8)

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
        // Avatar with online dot
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
                        tint = MaterialTheme.colorScheme.primary,
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

        // Content - Balanced row with ample width for title without truncation!
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (chat.isSecretChat) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp).padding(end = 4.dp)
                    )
                }

                Text(
                    text = chat.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Spacer(modifier = Modifier.width(8.dp))

                if (chat.isMuted) {
                    Icon(
                        imageVector = Icons.Default.VolumeOff,
                        contentDescription = "Silenciado",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp).padding(end = 4.dp)
                    )
                }

                Text(
                    text = formattedTime,
                    fontSize = 12.sp,
                    color = if (chat.unreadCount > 0) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.lastMessage,
                    fontSize = 13.sp,
                    color = lastMsgColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (chat.isPinned) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.PushPin,
                        contentDescription = "Fijado",
                        tint = Color.Gray,
                        modifier = Modifier.size(15.dp)
                    )
                }

                if (chat.unreadCount > 0) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = chat.unreadCount.toString(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
