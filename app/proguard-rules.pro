# ==============================================================================
# KronoGram — Ultra-Aggressive ProGuard & R8 Optimization Rules
# ==============================================================================

# 1. Bytecode Optimization Passes & Inlining
-optimizationpasses 5
-allowaccessmodification
-mergeinterfacesaggressively
-overloadaggressively

# 2. Strip Logging & Debug Overhead
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int println(...);
}

# 3. Preserve Line Numbers for Exception Traces (compact format)
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# 4. BuildConfig & Telegram API Credentials
-keep class com.example.BuildConfig {
    public static final int TELEGRAM_API_ID;
    public static final java.lang.String TELEGRAM_API_HASH;
}

# 5. Room Database & SQLite Entity Mapping
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase {
    *;
}
-keep class com.example.data.model.** { *; }
-keep class com.example.data.local.** { *; }
-dontwarn androidx.room.paging.**

# 6. Kotlin Coroutines & Flow
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-keep class kotlinx.coroutines.android.** { *; }
-keep class kotlinx.coroutines.internal.MainDispatcherLoader { *; }

# 7. Jetpack Compose
-keepclassmembers class androidx.compose.runtime.Recomposer {
    *;
}
-keep class * implements androidx.compose.runtime.State { *; }
-keep class * implements androidx.compose.runtime.MutableState { *; }
-dontwarn androidx.compose.**

# 8. Coil Image Loading & Networking
-dontwarn coil.**
-dontwarn okhttp3.**
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# 9. App Components & Security
-keep class com.example.engine.** { *; }
-keep class com.example.ui.MainViewModel { *; }
-keep class com.example.data.repository.** { *; }

# 10. TDLib (Telegram Database Library) JNI & Data Types
-keep class org.drinkless.tdlib.** { *; }
-dontwarn org.drinkless.tdlib.**
