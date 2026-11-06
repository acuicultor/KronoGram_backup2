# KronoGram — Cliente Avanzado de Telegram para Android

**KronoGram** es un fork moderno y de alto rendimiento de Telegram para Android (`com.rcuenca.kronogram`), construido con **Jetpack Compose**, **Material 3** y arquitectura reactiva. Incorpora una estética **Cyber Chrono** con soporte para **Modo Oscuro AMOLED Puro (#000000)**, pestañas y carpetas inteligentes configurables desde cero, **Modo Fantasma**, programador cronológico de mensajes con temporizadores de autodestrucción y compatibilidad desde **Android 11 (API 30)** hasta **Android 16 (Target SDK 36)**.

---

## 🚀 Especificaciones del Proyecto

| Parámetro | Valor |
| :--- | :--- |
| **Nombre de la Aplicación** | KronoGram |
| **Nombre del Paquete (Application ID)** | `com.rcuenca.kronogram` |
| **Telegram API ID** | `37574376` |
| **Telegram API Hash** | `51b61cb9574c106fd63b3f3c0f1e74fd` |
| **Minimum SDK** | `30` (Android 11) |
| **Target SDK / Compile SDK** | `36` (Android 16) |
| **Lenguaje de Programación** | Kotlin 2.x |
| **Framework de Interfaz** | Jetpack Compose + Material Design 3 |
| **Persistencia Local** | Room Database (con KSP) |

---

## ⚡ Características Exclusivas

1. **Estética Cyber Chrono & AMOLED Puro:**
   - Modo negro absoluto (`#000000`) para máxima eficiencia energética en pantallas OLED/AMOLED.
   - Acentos en Cian Eléctrico (`#00E5FF`) y Azul Neón (`#0091FF`).
   - Icono adaptativo futurista con el símbolo de cronómetro y avión metálico.

2. **Pestañas y Carpetas Inteligentes desde Cero:**
   - Asistente de bienvenida interactivo para activar únicamente las pestañas deseadas (*Todos, No leídos, Personales, Grupos, Canales, Bots*).
   - Gestor dedicado de carpetas con creación de filtros inteligentes personalizados y reordenación.

3. **Ajustes de KronoGram (Labs):**
   - **Modo Fantasma:** Previsualiza y lee mensajes sin emitir confirmación de lectura ni marcas de visto al servidor.
   - **Ocultar Estado En Línea:** Mantente invisible en la red sin actualizar tu presencia.
   - **Bloqueo Biométrico y PIN:** Pantalla de seguridad con teclado numérico y autenticación por huella dactilar.

4. **Programador Chrono & Mensajería Cifrada:**
   - Envío programado de mensajes con fecha y hora precisa.
   - Temporizadores de autodestrucción configurables (10s, 30s, 60s tras lectura).
   - Soporte para chats secretos con cifrado de extremo a extremo (E2E) y verificación visual de huella digital MTProto.

---

## 🛠️ Requisitos del Entorno de Desarrollo

Para compilar y trabajar en el código de KronoGram localmente en tu equipo:

1. **Java Development Kit (JDK):**
   - **JDK 17** o **JDK 21** (recomendado Azul Zulu u OpenJDK).
   - Configura la variable de entorno `JAVA_HOME` apuntando a tu JDK.

2. **Android Studio:**
   - Se recomienda **Android Studio Ladybug (2024.2+)** o superior.
   - Android SDK Platform **API 36** instalado a través del SDK Manager.
   - Android SDK Build-Tools **36.0.0** o superior.

3. **Variables de Entorno (Opcional):**
   - Si deseas compilar desde la terminal sin Android Studio, añade el Android SDK a tu ruta:
     ```bash
     export ANDROID_HOME=$HOME/Android/Sdk
     export PATH=$PATH:$ANDROID_HOME/platform-tools
     ```

---

## 📦 Instrucciones Paso a Paso para Compilar

### 1. Clonar o Descargar el Proyecto
Abre una terminal en el directorio raíz del proyecto:
```bash
cd /ruta/hacia/kronogram
```

### 2. Verificar las Credenciales de Telegram API
Las credenciales ya se encuentran integradas en la configuración de `app/build.gradle.kts`:
```kotlin
buildConfigField("int", "TELEGRAM_API_ID", "37574376")
buildConfigField("String", "TELEGRAM_API_HASH", "\"51b61cb9574c106fd63b3f3c0f1e74fd\"")
```

### 3. Compilar el APK en Modo Debug
Ejecuta la tarea Gradle correspondiente:
```bash
gradle :app:assembleDebug
```
El APK compilado se generará en:
`app/build/outputs/apk/debug/app-debug.apk`

### 4. Compilar el APK en Modo Release
Para generar el paquete optimizado de producción:
```bash
gradle :app:assembleRelease
```
El archivo resultante se ubicará en:
`app/build/outputs/apk/release/app-release-unsigned.apk` (o firmado con tu keystore de lanzamiento).

### 5. Ejecutar las Pruebas Locales (Unit / Robolectric)
Para verificar la integridad de la base de datos Room, los repositorios y las vistas:
```bash
gradle :app:testDebugUnitTest
```

---

## 📱 Instalación en un Dispositivo Físico o Emulador

Conecta tu teléfono Android mediante depuración USB (o inicia un emulador Android con API 30 a 36):
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## 🏛️ Estructura del Código Fuente

```
app/src/main/
├── AndroidManifest.xml                  # Permisos, launcher icon y declaración de componentes
├── java/com/example/
│   ├── MainActivity.kt                  # Punto de entrada, Scaffold edge-to-edge y navegación
│   ├── data/
│   │   ├── local/                       # Room Database, ChatDao, MessageDao, FolderDao
│   │   ├── model/                       # ChatEntity, MessageEntity, FolderTabEntity
│   │   └── repository/                  # ChatRepository, FolderRepository, KronoPreferences
│   ├── engine/                          # TelegramEngine (Auth state, MTProto gateway, API 37574376)
│   └── ui/
│       ├── MainViewModel.kt             # Lógica de presentación, filtros de chats y reactividad
│       ├── components/                  # Drawer, LockScreenOverlay, NewChatDialog
│       ├── screens/
│       │   ├── auth/                    # AuthScreen (Login con Teléfono, SMS/App code, 2FA, QR)
│       │   ├── chatdetail/              # ChatDetailScreen (Burbujas Chrono, Programador)
│       │   ├── chatlist/                # ChatListScreen (Pestañas dinámicas, búsqueda, badges)
│       │   ├── onboarding/              # OnboardingWizardScreen (Personalizar pestañas de 0)
│       │   └── settings/                # SettingsScreen & KronoSettingsScreen
│       └── theme/                       # Color.kt, Theme.kt (AMOLED #000000, Cyber Chrono)
└── res/
    ├── drawable/                        # Iconos vectoriales y recursos gráficos
    ├── mipmap-*/                        # Iconos adaptativos y raster fallbacks
    └── values/                          # strings.xml (KronoGram), themes.xml
```

---

## 🔒 Privacidad y Seguridad

- **Chats Secretos:** Protocolo de extremo a extremo sin registro en servidores centrales.
- **Biometría:** Huella dactilar integrada mediante APIs nativas de Android.
- **Protección de Datos:** La base de datos local y las preferencias residen exclusivamente en el almacenamiento seguro de la aplicación.
