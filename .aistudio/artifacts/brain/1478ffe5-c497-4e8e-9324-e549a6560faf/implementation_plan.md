# KronoGram — Plan de Optimización Ultra-Agresiva con ProGuard / R8

Este plan define la configuración de optimización, reducción de código (*shrinking*), ofuscación y reducción de recursos (*resource shrinking*) de **KronoGram** tanto para compilaciones **Debug** como **Release**, alcanzando el mínimo tamaño de APK y máxima velocidad de ejecución.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> Decisiones confirmadas por el usuario:
> - **Entornos de Compilación**: Habilitar optimización R8/ProGuard en **ambos modos: Debug y Release** para validar las reglas y el rendimiento directamente en desarrollo.
> - **Nivel de Agresividad**: **Ultra agresiva**, incluyendo 5 pasadas de optimización de bytecode (`-optimizationpasses 5`), modificación de accesos de clases/métodos, fusión de interfaces (`-mergeinterfacesaggressively`) y eliminación completa de llamadas a `android.util.Log`.
> - **Preservación Segura**: Mantener intactas las entidades de Room, DAOs, base de datos SQLite, Compose runtime, Coroutines y campos de `BuildConfig` para evitar fallos de tiempo de ejecución.

---

### 1. Cambios en la Configuración de Gradle (`app/build.gradle.kts`)

1. **Habilitación de Minificación y Reducción de Recursos:**
   - En `release`:
     - `isMinifyEnabled = true`
     - `isShrinkResources = true`
     - `proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")`
   - En `debug`:
     - `isMinifyEnabled = true`
     - `isShrinkResources = true`
     - `proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")`
2. **Conservación de Signing Configs:**
   - No alterar `signingConfig` existente en `release` ni en `debugConfig`.

---

### 2. Reglas Específicas de ProGuard / R8 (`app/proguard-rules.pro`)

```
┌─────────────────────────────────────────────────────────────┐
│                 Estructura de Reglas R8                     │
│                                                             │
│  [Reglas Generales de Optimización]                         │
│  ├── -optimizationpasses 5                                 │
│  ├── -allowaccessmodification                              │
│  ├── -mergeinterfacesaggressively                          │
│  └── -repackageclasses ''                                   │
│                                                             │
│  [Eliminación de Bytecode / Overhead]                       │
│  └── -assumenosideeffects android.util.Log { *; }          │
│                                                             │
│  [Preservación de Componentes Críticos]                     │
│  ├── Room Entities & DAOs (com.example.data.**)             │
│  ├── Jetpack Compose Runtime & State                        │
│  ├── Kotlin Coroutines & Flow (kotlinx.coroutines.**)       │
│  ├── Telegram API Credentials (com.example.BuildConfig)     │
│  └── Modelos y Clases de UI                                 │
└─────────────────────────────────────────────────────────────┘
```

#### Detalle de Reglas Clave:
1. **Reglas de Optimización Global:**
   - `-optimizationpasses 5`: Ejecuta 5 ciclos de análisis para optimizar bucles, propagar constantes y eliminar código muerto en profundidad.
   - `-allowaccessmodification`: Permite a R8 ampliar la visibilidad de clases y miembros privados para facilitar el *method inlining*.
   - `-overloadaggressively`: Sobrecarga nombres de métodos para reducir la tabla de cadenas del DEX.

2. **Eliminación de Logs en Bytecode:**
   - `-assumenosideeffects class android.util.Log`: Elimina llamadas a `Log.v()`, `Log.d()`, `Log.i()` y `Log.println()` sin dejar rastro en el binario.

3. **Protección de Room Database:**
   - `-keep class * extends androidx.room.RoomDatabase`: Mantiene las clases de base de datos.
   - `-keep @androidx.room.Entity class * { *; }`: Protege los campos de las tablas SQLite (`ChatEntity`, `MessageEntity`, `FolderTabEntity`).
   - `-keep @androidx.room.Dao interface * { *; }`: Protege las interfaces DAO (`ChatDao`, `MessageDao`, `FolderDao`).

4. **Protección de Jetpack Compose:**
   - Conservación de los anotadores `@Composable` y de los mutadores de estado `State<T>`, `MutableState<T>`.

5. **Protección de Credenciales de Telegram en `BuildConfig`:**
   - `-keep class com.example.BuildConfig { public static final int TELEGRAM_API_ID; public static final java.lang.String TELEGRAM_API_HASH; }`

---

### 3. Plan de Verificación

1. Aplicar las reglas en `app/proguard-rules.pro`.
2. Actualizar el bloque `buildTypes` en `app/build.gradle.kts`.
3. Ejecutar `compile_applet` para validar que R8 procesa y empaqueta el APK con éxito sin conflictos de símbolos ni advertencias fatales.
4. Documentar los cambios en `README.md`.
