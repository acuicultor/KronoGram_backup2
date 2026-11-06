# KronoGram — Cliente Avanzado de Telegram (Android)

KronoGram es un fork de Telegram de alto rendimiento para Android (`com.rcuenca.kronogram`, Target SDK 36, Android 11+), diseñado con una estética Cyber Chrono de alto impacto, modo AMOLED Puro, gestión de sesión y mensajería cifrada mediante las credenciales de Telegram API (API ID: `37574376`, API Hash: `51b61cb9574c106fd63b3f3c0f1e74fd`), asistente de bienvenida para configuración de pestañas desde cero, carpetas inteligentes y ajustes avanzados de privacidad y tiempo.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> A continuación se resumen las decisiones acordadas a través de la fase de clarificación interactiva:
> - **Identidad Visual y Paleta**: **Cyber Chrono** (Cian Eléctrico `#00E5FF`, Azul Neón `#00A3FF`, Fondo Negro AMOLED Puro `#000000`, Superficies Obsidiana `#0A0E17` / `#131B2E`).
> - **Pestañas y Carpetas**: **Personalizable desde cero** con un Asistente de Bienvenida interactivo que permite al usuario escoger exactamente qué pestañas (Todos, No leídos, Personales, Grupos, Canales, Bots, o creadas a medida) desea activar en su espacio de trabajo.
> - **Ajustes Exclusivos de KronoGram**: **Modo Fantasma** (ocultar lectura y estado en línea), **Programador Horario Avanzado de Mensajes** (con destrucción programada / temporizadores de autodestrucción), **Bloqueo Biométrico / PIN de app** y **Interruptor AMOLED Puro**.
> - **Arquitectura del Motor**: Capa desacoplada compatible con protocolo Telegram MTProto y TDLib con persistencia de sesión cifrada, clave de sesión local segura y base de datos reactiva Room para almacenamiento en caché offline de mensajes, diálogos y carpetas.

---

### 1. Overview & Core Concept

- **Qué es KronoGram**: Un cliente de mensajería rápido, moderno y seguro para la red de Telegram, que respeta la compatibilidad con el ecosistema oficial al tiempo que brinda personalización extrema del flujo de chats, control de tiempo de lectura y estética futurista de bajo consumo energético para pantallas OLED.
- **Público Objetivo**: Usuarios que demandan productividad extrema en Telegram, filtrado impecable de canales y grupos, mayor privacidad (modo fantasma y protección biométrica) y una interfaz negra absoluta sin halos grises.
- **Valor Principal**:
  1. Organización total: Sin desorden, con pestañas dinámicas y filtros instantáneos.
  2. Privacidad y tiempo: Envío programado inteligente y lectura discreta sin enviar confirmaciones de lectura no deseadas.
  3. Rendimiento en Android 11 a Android 16: Con soporte para las APIs modernas de Android 36 (WindowInsets edge-to-edge, Predictive Back, Notificaciones con canales dedicados).

---

### 2. User Experience & Visual Design

#### 2.1 Aesthetic Direction & Theme Tokens
- **Tema Cyber Chrono & AMOLED Puro:**
  - `Background`: `#000000` (Negro absoluto, 0% brillo de píxeles OLED)
  - `Surface`: `#0A0F1D` (Obsidiana profunda con tintes cian)
  - `SurfaceVariant`: `#131C31` (Contenedores elevados y tarjetas de mensajes)
  - `Primary / Accent`: `#00E5FF` (Cian eléctrico chrono)
  - `Secondary Accent`: `#0091FF` (Azul Telegram Neón)
  - `Accent Alert / Destruct`: `#FF3366` (Rojo neón para cronómetros de autodestrucción)
  - `Text Primary`: `#F0F6FC` (Blanco luminoso de alto contraste)
  - `Text Secondary`: `#8B9BB4` (Gris azulado frío)
  - `Bubble Incoming`: `#101726` con borde sutil `#1E2C44`
  - `Bubble Outgoing`: `#003B5C` con acento en `#00E5FF`

#### 2.2 Pantallas Principales y Flujos UX

```
┌─────────────────────────────────────────────────────────────┐
│                      Flujo Principal                        │
│                                                             │
│  [Asistente de Bienvenida] ──► [Pantalla de Inicio de Sesión]│
│  (Configura Pestañas y Temas)  (Teléfono / Código / QR)      │
│                                             │               │
│                                             ▼               │
│                                    [Lista Principal Chats]  │
│                                    ├─ Pestañas Dinámicas    │
│                                    ├─ Carpetas Inteligentes │
│                                    └─ Búsqueda Rápida       │
│                                             │               │
│                  ┌──────────────────────────┴──────────────┐│
│                  ▼                                         ▼│
│           [Ventana de Chat]                       [Ajustes] │
│           ├─ Mensajes Cifrados           ├─ Telegram Estándar│
│           ├─ Programador Chrono          └─ Ajustes KronoGram│
│           └─ Envío Multimedia                                │
└─────────────────────────────────────────────────────────────┘
```

1. **Onboarding / Asistente de Pestañas & Carpetas:**
   - Presentación interactiva con opción de elegir paquetes de pestañas predefinidas (Productividad, Social, Minimalista) o seleccionar manualmente qué carpetas mostrar.
2. **Autenticación Telegram Fluida:**
   - Entrada de número de teléfono con detección internacional.
   - Envío y validación de código de verificación (SMS o app oficial).
   - Soporte para contraseña de segundo factor (2FA Cloud Password).
   - Alternativa de inicio de sesión rápido por Código QR.
3. **Bandeja de Chats:**
   - Pestañas horizontales con scroll suave y badge numérico de mensajes no leídos.
   - Pinned chats con indicador chrono.
   - Menú lateral deslizante (Drawer) con acceso a perfil, ajustes, llamadas, contactos y KronoGram Labs.
4. **Conversación & Programador Chrono:**
   - Burbujas de chat con marcas de verificación dobles (doble check cian).
   - Botón de programar mensaje al mantener presionado el botón de enviar ("Enviar a una hora precisa", "Enviar sin sonido", "Autodestrucción en X segundos").
5. **Ajustes de KronoGram:**
   - Interruptor de Modo Fantasma (No enviar `readHistory`, no enviar `setTyping`).
   - Gestión de pestañas (reordenar por drag-and-drop, añadir carpetas inteligentes personalizadas).
   - Bloqueo por Huella Dactilar / Biometría y PIN de 4 dígitos.
   - Configuración de temas: AMOLED Puro, Noche Cyber, Modo Claro y personalización de color de acento.

---

### 3. Key Product Decisions & Trade-Offs

- **Decisión 1: Arquitectura del Motor Telegram (TDLib Protocol & Gateway)**
  - *Enfoque*: Estructurar la capa de red con la especificación de Telegram MTProto y TDLib, inyectando directamente las credenciales `API_ID = 37574376` y `API_HASH = "51b61cb9574c106fd63b3f3c0f1e74fd"`. Se implementa un administrador de sesión seguro con almacenamiento en Room y DataStore/SharedPreferences cifradas.
  - *Ventaja*: Garantiza la interoperabilidad con las APIs de Telegram, soporte de 2FA y cifrado de extremo a extremo en chats secretos.
- **Decisión 2: Asistente de Inicio Personalizable para Pestañas**
  - *Enfoque*: En lugar de imponer pestañas fijas, el usuario experimenta un wizard en el primer inicio (o reconfigurable desde Ajustes de KronoGram) para activar/desactivar y crear pestañas inteligentes como "Trabajo", "Canales de Noticias", "No Leídos", etc.
- **Decisión 3: Modo Fantasma y Privacidad del Lado del Cliente**
  - *Enfoque*: Interceptar los eventos de lectura saliente y presencia para que el usuario pueda previsualizar mensajes sin enviar confirmación de lectura al servidor ni actualizar la última hora de conexión si el modo fantasma está activo.
- **Decisión 4: Compatibilidad de Android 11 a 16 (API 30 - 36)**
  - *Enfoque*: `minSdk = 30` (Android 11) y `targetSdk = 36` (Android 16), utilizando insets modernos `WindowInsets.safeDrawing`, Edge-to-Edge habilitado sin barras grises y Material 3 con tokens Cyber Chrono.

---

### 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                        Arquitectura KronoGram                          │
├────────────────────────────────────────────────────────────────────────┤
│                                UI LAYER                                │
│   [MainActivity] ──► [KronoNavGraph] ──► [Compose Screens]            │
│   ├── OnboardingWizardScreen (Carpetas & Pestañas)                     │
│   ├── AuthScreen (Phone / Code / 2FA / QR)                            │
│   ├── ChatListScreen (Dynamic Tabs + Smart Folders)                    │
│   ├── ChatDetailScreen (Encrypted Messaging + Chrono Scheduler)        │
│   └── SettingsScreen (Telegram Settings + KronoGram Lab Settings)      │
├────────────────────────────────────────────────────────────────────────┤
│                             VIEWMODEL LAYER                            │
│   [AuthViewModel]  [ChatListViewModel]  [ChatDetailViewModel]         │
│   [KronoSettingsViewModel]  [FolderManagerViewModel]                  │
├────────────────────────────────────────────────────────────────────────┤
│                            REPOSITORY LAYER                            │
│   [TelegramSessionRepository]      [ChatRepository]                    │
│   [KronoSettingsRepository]        [FolderRepository]                  │
├────────────────────────────────────────────────────────────────────────┤
│                       DATA & PERSISTENCE LAYER                         │
│   ├── Room Database (AppDatabase):                                     │
│   │   ├── ChatEntity & MessageEntity (Cache local cifrable)           │
│   │   └── FolderTabEntity (Configuración dinámica de pestañas)         │
│   ├── Secure Session Storage (Tokens, AuthKey, User Credentials)       │
│   └── Telegram MTProto / TDLib Client (ApiId: 37574376, ApiHash: ...) │
└────────────────────────────────────────────────────────────────────────┘
```

#### 4.1 Entidades y Modelos de Datos en Room
1. `ChatEntity`: `chatId`, `title`, `type` (USER, GROUP, CHANNEL, BOT), `avatarUrl`, `lastMessage`, `lastMessageTimestamp`, `unreadCount`, `isPinned`, `folderId`, `isMuted`.
2. `MessageEntity`: `messageId`, `chatId`, `senderId`, `senderName`, `text`, `timestamp`, `isOutgoing`, `deliveryStatus` (SENDING, SENT, READ), `isEncrypted`, `scheduledTime`, `autoDestructSeconds`.
3. `FolderTabEntity`: `id`, `name`, `iconName`, `orderIndex`, `includedTypes`, `isCustom`, `isEnabled`.
4. `KronoSettings`: `isGhostModeEnabled`, `isHideOnlineStatus`, `isAmoledTheme`, `isBiometricLockEnabled`, `accentColorHex`, `autoCleanCacheDays`.

---

### 5. Plan de Ejecución Paso a Paso

1. **Configuración de Gradle, Manifiesto y Metadatos:**
   - Configurar `applicationId = "com.rcuenca.kronogram"`, `minSdk = 30`, `targetSdk = 36`.
   - Inyectar `TELEGRAM_API_ID = 37574376` y `TELEGRAM_API_HASH = "51b61cb9574c106fd63b3f3c0f1e74fd"` en `BuildConfig` vía `build.gradle.kts` y `.env.example`.
   - Declarar permisos en `AndroidManifest.xml` (`INTERNET`, `ACCESS_NETWORK_STATE`, `VIBRATE`, `USE_BIOMETRIC`, `POST_NOTIFICATIONS`).
   - Sincronizar `metadata.json` con el nombre `KronoGram`.

2. **Diseño de Marca, Icono Adaptativo y Tipografía:**
   - Generación del icono vectorial adaptativo y de alta resolución `ic_launcher` representando el cronómetro futurista con el avión de papel Cyber Chrono sobre fondo `#000000`.
   - Configuración de temas en `Color.kt` y `Theme.kt` con soporte AMOLED puro `#000000` y modo noche Cyber.

3. **Capa de Datos y Persistencia Local (Room):**
   - Implementar `AppDatabase`, `ChatDao`, `MessageDao`, `FolderDao` y repositorios asociados con flujos reactivos `Flow`.

4. **Motor de Protocolo Telegram MTProto / TDLib:**
   - Implementar `TelegramClientEngine` con manejo de estados de sesión (`Unauthenticated`, `WaitPhoneNumber`, `WaitCode`, `WaitPassword`, `Ready`).
   - Soporte para envío de mensajes en tiempo real, programación de mensajes (Chrono) y recepción reactiva.

5. **Asistente de Bienvenida y Pantallas de Autenticación:**
   - Crear `OnboardingWizardScreen` para permitir personalizar las pestañas y carpetas desde cero.
   - Crear `AuthScreen` completo (Phone input con prefijo, pantalla de código de 5 dígitos, pantalla de contraseña en la nube 2FA).

6. **Interfaz de Lista de Chats con Pestañas y Carpetas:**
   - `ChatListScreen` con `ScrollableTabRow` dinámico, Drawer de navegación M3, barra superior con búsqueda y botón flotante para nuevo mensaje/grupo.
   - Visualización de avatares con gradientes cian, estados de mensaje y conteo de no leídos.

7. **Ventana de Chat y Mensajería Cifrada:**
   - `ChatDetailScreen` con burbujas de mensaje estilizadas, indicadores de entrega, panel de adjuntos y diálogo de programación de mensajes (Chrono Scheduler).

8. **Ajustes Estándar de Telegram y Sección Exclusiva KronoGram:**
   - `SettingsScreen` con navegación a ajustes de cuenta, notificaciones, privacidad y almacenamiento.
   - `KronoGramSettingsScreen` con Modo Fantasma, Bloqueo Biométrico/PIN, personalizador de pestañas y selector de temas AMOLED.

9. **Documentación Técnica (`README.md`):**
   - Guía exhaustiva de compilación con Gradle, configuración de Android SDK 36, variables de entorno, y arquitectura de desarrollo.

10. **Compilación y Verificación:**
    - Ejecución de `compile_applet` para garantizar una compilación limpia sin errores.
