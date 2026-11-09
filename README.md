# KronoGram (v13.1.1) — Cliente Avanzado de Telegram MTProto para Android

**KronoGram** es un cliente completo, oficial y de alto rendimiento de Telegram para Android (`com.rcuenca.kronogram`), basado en el motor nativo de **Nekogram v13.1.1** (Telegram MTProto v11.6.1). Incorpora la identidad visual **Cyber Chrono / AMOLED**, iconos adaptativos personalizados, configuración oficial de Firebase y soporte completo para la API nativa de Telegram.

---

## Características Principales

- **Motor Oficial Telegram MTProto (v11.6.1)**: Conexión nativa y cifrada a los datacenters globales de Telegram con aceleración de hardware C++ (`libtmessages.49.so`).
- **Identidad KronoGram**: Logotipo oficial de marca con esfera chrono de doble flecha y avión Telegram, iconos adaptativos para todas las densidades de pantalla (`mdpi` hasta `xxxhdpi`).
- **Tema Cyber Chrono & AMOLED Puro**:
  - Tema Día: Clásico Telegram (#527DA3 / #EFFDDE)
  - Tema Noche: Obsidiana Cyber
  - Tema AMOLED: Negro puro (#000000) de alto contraste con ahorro de batería en pantallas OLED.
- **Firebase Push & Analytics**: Totalmente integrado con el proyecto oficial `kronogram-64220` para notificaciones push en tiempo real a través de FCM (`firebase-messaging:25.1.3`).
- **Formatos Multimedia & Math**: Soporte integrado para LaTeX (`jlatexmath`), Markdown enriquecido (`markwon`), stickers Lottie, WebP y visor multimedia completo.

---

## Credenciales y Configuración

- **Application ID**: `com.rcuenca.kronogram`
- **Versión de App**: `13.1.1` (Build `7237`)
- **Versión Base Telegram**: `11.6.1` (Build `5573`)
- **Telegram API ID**: `37574376`
- **Telegram API Hash**: `51b61cb9574c106fd63b3f3c0f1e74fd`
- **Firebase Project ID**: `kronogram-64220` (`app/google-services.json` y `TMessagesProj/google-services.json`)

---

## Descarga del APK Compilado

El APK firmado listo para instalar se encuentra empaquetado en:
- **Ruta**: `assets/kronogram-arm64.apk`
- **Salida de Compilación**: `app/build/outputs/apk/debug/KronoGram-13.1.1-7237-arm64-v8a.apk`
- **Tamaño**: ~82.5 MB (optimizado para arquitectura `arm64-v8a` y compatible con los límites de exportación).

---

## Compilación del Proyecto

Para compilar el proyecto en cualquier entorno Android con Java 21 y Android SDK 36+:

```bash
# Compilar APK Debug arm64-v8a
gradle assembleDebug

# O mediante la herramienta del entorno
compile_applet
```
