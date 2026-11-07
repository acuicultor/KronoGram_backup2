# KronoGram — Plan de Corrección Integral, Imagen Oficial y Ajustes Nativos

Este plan detalla la reestructuración completa de la experiencia de usuario, diseño visual, flujo de autenticación y configuración nativa de **KronoGram** para solucionar todos los problemas reportados y cumplir con las decisiones confirmadas.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> Decisiones confirmadas por el usuario:
> 1. **Imagen de Marca Oficial**: Utilizar de manera exclusiva e idéntica la imagen enviada por el usuario (`IMG_6f2da96a-2535-427b-8892-12106e2c4884_3e53caf1.jpg` con el avión de Telegram y el anillo chrono de doble flecha neón) como icono de la aplicación en todas las densidades de pantalla y como logotipo principal en la interfaz.
> 2. **Ajustes Nativos de Telegram**: Implementar **pantallas completas dedicadas** para cada categoría nativa de Telegram (Notificaciones, Privacidad, Almacenamiento, Ajustes de Chat, Dispositivos e Idioma).
> 3. **Sistema de Temas de 3 Vías**: Selector directo e interactivo entre **Día (Telegram Clásico #527DA3 / #EFFDDE)**, **Noche (Obsidiana Cyber)** y **AMOLED Puro (#000000)**.
> 4. **Flujo de Autenticación Real**: Proceso de login interactivo en 2 pasos: primero número de teléfono con selector de prefijo internacional, seguido por la pantalla de ingreso del código de 5 dígitos (con temporizador de reenvío) y soporte para contraseña 2FA.
> 5. **Solución a Textos Cortados y Layout**: Rediseño de botones de bienvenida, eliminación del cuadro de API ID vertical invasivo en la cabecera, scroll completo en el menú lateral y padding inferior en la lista de chats para evitar solapamientos con el botón flotante (FAB).

---

### 1. Sustitución de Imagen de Marca e Iconos

```
┌─────────────────────────────────────────────────────────────┐
│                 Activo Oficial de Marca                     │
│  /public/assets/aistudio/IMG_6f2da96a-2535-427b-...jpg      │
│                              │                              │
│         ┌────────────────────┴────────────────────┐         │
│         ▼                                         ▼         │
│  [Recurso de Dibujable]                   [Mipmaps Android] │
│  app/.../kronogram_brand_logo.jpg         ic_launcher.png   │
│  (Auth, Onboarding, Drawer, About)        ic_launcher_round │
│                                           (mdpi a xxxhdpi)  │
└─────────────────────────────────────────────────────────────┘
```

- Copiar la imagen oficial a `app/src/main/res/drawable/kronogram_brand_logo.jpg`.
- Generar mediante ImageMagick los iconos rasterizados `ic_launcher.png` e `ic_launcher_round.png` directamente desde este archivo para las densidades `mdpi`, `hdpi`, `xhdpi`, `xxhdpi` y `xxxhdpi`.
- Reemplazar el icono genérico en la pantalla de bienvenida y de autenticación por la imagen oficial del logotipo con esquinas redondeadas o formato circular.

---

### 2. Flujo de Autenticación y Conexión de Mensajería

- **Paso 1: Teléfono & País:** Selector de prefijo de país con búsqueda rápida (+34 España, +52 México, +54 Argentina, +1 EE.UU., +57 Colombia, +56 Chile, etc.) y campo de teléfono validado.
- **Paso 2: Código de Confirmación:** Pantalla interactiva que solicita el código de 5 dígitos enviado por Telegram, con cuenta atrás de reenvío de 60 segundos y opción de reenvío por SMS.
- **Paso 3: 2FA & Sesión:** Entrada de contraseña en la nube si la cuenta está protegida. Al completar, se guarda la sesión persistente con el número y nombre del usuario.
- **Mensajería Reactiva:** Conexión completa con la base de datos Room para enviar, recibir, programar y borrar mensajes en tiempo real.

---

### 3. Pantallas Dedicadas de Ajustes Nativos de Telegram

Se añadirán los siguientes destinos de navegación y pantallas independientes:

1. **`NotificationsSettingsScreen`:**
   - Notificaciones para chats privados, grupos y canales (interruptor, sonido, vibración).
   - Vista previa de mensajes en la barra de estado.
   - Sonidos en la app y contador de mensajes no leídos (incluir/excluir chats silenciados).
2. **`PrivacySettingsScreen`:**
   - Quién puede ver mi número de teléfono (Todos, Mis contactos, Nadie).
   - Quién puede ver mi última hora de conexión y estado en línea.
   - Bloqueo con código de acceso / PIN de la aplicación.
   - Verificación en dos pasos (2FA).
   - Lista de usuarios bloqueados.
3. **`DataStorageSettingsScreen`:**
   - Desglose gráfico de uso de almacenamiento (Caché, Fotos, Vídeos, Archivos).
   - Botón interactivo "Limpiar Caché de KronoGram" con simulación de liberación de espacio.
   - Descarga automática de archivos multimedia (con datos móviles, con Wi-Fi, en itinerancia).
4. **`ChatSettingsScreen`:**
   - Selector visual de tema: **Día (Telegram Clásico)**, **Noche (Obsidiana)**, **AMOLED Puro**.
   - Barra deslizante de tamaño de texto del mensaje (12 sp a 24 sp) con burbuja de previsualización en vivo.
   - Selector de radio de esquinas de las burbujas (de cuadradas a redondeadas).
5. **`DevicesSettingsScreen`:**
   - Sesión actual en este teléfono (modelo, IP, versión de Android).
   - Sesiones activas en otros equipos (KronoGram Desktop, Telegram Web).
   - Botón rojo para "Cerrar todas las demás sesiones".
6. **`LanguageSettingsScreen`:**
   - Lista de idiomas (Español, English, Français, Deutsch, Italiano, Português, Русский) con cambio instantáneo de selección.

---

### 4. Perfeccionamiento del Tema Día Clásico de Telegram

- Cabecera en azul clásico Telegram `#527DA3` / `#5682A3`.
- Fondo de pantalla de chat en gris azulado suave `#E4E9EE`.
- Burbujas salientes en verde clásico `#EFFDDE` con borde sutil y marcas de verificación dobles en verde `#4FAE4E`.
- Burbujas entrantes en blanco puro `#FFFFFF`.
- Texto en alto contraste negro carbón `#1C242B`.

---

### 5. Corrección de Textos Cortados y Mejoras de Layout

1. **Barra Superior (`ChatListScreen`):**
   - Eliminar el bloque vertical invasivo `3757 / 4376`.
   - Colocar el título "KronoGram" limpio y elegante con un indicador sutil de estado de conexión.
2. **Pantalla de Bienvenida (`OnboardingWizardScreen`):**
   - Rediseñar los botones rápidos con texto completo en formato de tarjetas o chips horizontales ("Todo Activo", "Esencial", "Minimalista").
   - Ajustar las descripciones de los ítems para que admitan salto de línea sin recortarse.
3. **Menú Lateral (`KronoDrawer`):**
   - Añadir `.verticalScroll(rememberScrollState())` y `.navigationBarsPadding()` para garantizar que todas las opciones (incluyendo los ajustes de Telegram y KronoGram) se puedan ver y tocar en cualquier tamaño de pantalla.
4. **Lista de Conversaciones:**
   - Agregar `PaddingValues(bottom = 88.dp)` para que el botón flotante de redacción no oculte nunca el último chat de la lista.
   - Ajustar el ancho del título del chat y de la última hora de mensaje para que no se trunquen prematuramente.

---

### 7. Reestructuración de Ajustes de KronoGram en Categorías Dedicadas (Completado)

Para eliminar la pantalla única con scroll infinito y ofrecer la misma experiencia pulida que los ajustes de Telegram, se ha reorganizado `KronoSettingsScreen` como un panel principal de categorías con subpantallas independientes:

1. **`KronoTranslateSettingsScreen` (Traducción Integrada):**
   - Selección de motor (Google Translate, DeepL, Motor Local Krono).
   - Selector de idioma de destino (Español, English, Français, Deutsch, etc.).
   - Interruptor para botón de traducción directa en cada burbuja.
   - Interruptor para traducir texto antes de enviar.
2. **`KronoMessagingSettingsScreen` (Reenvío y Mensajería):**
   - Reenvío sin cita / anónimo (elimina autor y cabecera).
   - Reenvío con un solo toque hacia Mensajes Guardados.
   - Selector de acción de doble toque (Responder, Traducir, Copiar, Reaccionar).
   - Transcripción instantánea de notas de voz.
3. **`KronoInterfaceSettingsScreen` (Personalización e Interfaz):**
   - Emojis del sistema Android.
   - Tipografía estándar del dispositivo.
   - Ocultar teclado automáticamente al hacer scroll.
   - Ocultar número en menú lateral (drawer) y avatar difuminado de cabecera.
4. **`KronoMediaSettingsScreen` (Multimedia y Stickers):**
   - Selector de tamaño de stickers (Pequeño, Normal, Grande).
   - Pausa automática de vídeos al salir de pantalla.
   - Stickers favoritos ilimitados.
5. **`KronoPrivacySettingsScreen` (Privacidad y Seguridad):**
   - Modo fantasma completo (sin confirmación de lectura ni doble tick azul).
   - Ocultar estado en línea y última hora de conexión.
   - Confirmación explícita antes de llamar para evitar accidentes.
   - Ignorar contactos bloqueados en chats grupales.
   - Historial local de mensajes editados.
   - Bloqueo biométrico y configuración de PIN de 4 dígitos con diálogo interactivo.
6. **`FolderManagerScreen` (Pestañas y Carpetas Inteligentes):**
   - Reordenación, activación y creación de filtros personalizados de chats.

---

### 8. Soporte de Color Dinámico Monet (Material You) (Completado)

Se ha implementado el soporte dinámico Monet en todos los temas de KronoGram:
- **`Theme.kt`**: Extracción del esquema de color dinámico del sistema con `dynamicDarkColorScheme` y `dynamicLightColorScheme` (Android 12+).
  - En **AMOLED Puro**, conserva el fondo negro absoluto `#000000` pero colorea todos los acentos con los tonos del fondo de pantalla.
  - En **Noche Cyber** y **Día Clásico**, adapta los acentos, botones y burbujas al color del sistema.
- **`KronoInterfaceSettingsScreen.kt`**: Sección interactiva "COLOR DINÁMICO MONET (MATERIAL YOU)" con interruptor y badge de estado en tiempo real.
- **`ChatSettingsScreen.kt`**: Interruptor integrado junto al selector de temas para alternar rápidamente entre la paleta tradicional y la paleta dinámica Monet.
- **Persistencia**: Estado guardado en SharedPreferences y propagado reactivamente por `MainViewModel`.

---

### 10. Restauración de Imagen Oficial de KronoGram y Conexión TDLib (Completado)

1. **Restauración y Adaptación del Emblema Oficial (`IMG_6f2da96a-2535-427b-8892-12106e2c4884.jpg`):**
   - Se recortó y aisló el emblema oficial auténtico: el disco 3D con el anillo chrono neón cian y el avión de Telegram en su interior.
   - Generado en 512x512 sRGB TrueColor sobre fondo negro absoluto `#000000` en `kronogram_brand_logo.png`.
   - Regenerados todos los mipmaps (`ic_launcher.png` e `ic_launcher_round.png`) para `mdpi`, `hdpi`, `xhdpi`, `xxhdpi` y `xxxhdpi`.
   - Icono adaptativo limpio en `ic_launcher_foreground.xml` (72dp centrado) e `ic_launcher_background.xml` (`#000000`).

2. **Reparación de la Conexión de Cuentas de Telegram (TDLib MTProto):**
   - **Corrección de librería nativa**: Se eliminó la llamada manual fallida a `tdjson`; el cliente TDLib carga `libtdjni.so` automáticamente mediante su inicializador estático, inicializando `isTdLibInitialized = true`.
   - **Parámetros de TDLib**: Se incluyó `databaseEncryptionKey = ByteArray(0)`, `useFileDatabase = true`, `useChatInfoDatabase = true`, `useMessageDatabase = true` y `useSecretChats = true`, desbloqueando la inicialización de la base de datos de Telegram.
   - **Flujo de autenticación completo**:
     - `SetAuthenticationPhoneNumber`: Envío del número a los servidores de Telegram.
     - `CheckAuthenticationCode`: Verificación del código de 5 dígitos enviado por Telegram.
     - `CheckAuthenticationPassword`: Soporte nativo para verificación en dos pasos (2FA).
     - `RequestQrCodeAuthentication`: Soporte para vincular cuenta mediante código QR.
     - `GetMe` y `LoadChats`: Sincronización automática de perfil real (nombre, username, teléfono) y conversaciones de la nube hacia la base de datos Room.

---

### 11. Plan de Verificación

- Compilación verificada exitosamente con `compile_applet`.
- Verificación de imagen oficial sRGB y mipmaps para todas las densidades.
- Verificación del flujo de conexión de cuentas de Telegram y control de errores.
