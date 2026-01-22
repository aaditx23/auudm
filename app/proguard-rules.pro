# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Optimization flags
-optimizationpasses 5
-dontusemixedcaseclassnames
-verbose

# Keep line numbers for debugging stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Keep data classes used with Firestore
-keepclassmembers class com.aaditx23.auudm.data.remote.model.** {
    *;
}
-keepclassmembers class com.aaditx23.auudm.domain.model.** {
    *;
}

# Keep Room entities
-keep class com.aaditx23.auudm.data.local.entity.** { *; }

# Firebase/Firestore rules
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# Keep Koin - more specific rules
-keep class org.koin.core.** { *; }
-keep class org.koin.android.** { *; }
-keep class * extends org.koin.core.module.Module

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# Keep Composable functions
-keepclassmembers class ** {
    @androidx.compose.runtime.Composable <methods>;
}
-keep interface androidx.compose.runtime.Composer

