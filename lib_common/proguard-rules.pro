# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#############################################
#
# 暴露调用对象
#
#############################################
-keep public class com.danke.android.templete.common.* { *; }

-keep class com.danke.android.templete.common.**
-keepclassmembernames class com.danke.android.templete.common.** {
    public <fields>;
    public <methods>;
    protected <fields>;
    protected <methods>;
}

# event
-keep public interface com.danke.android.templete.common.EventAction{ *; }
-keep public interface com.danke.android.templete.common.EventAction$*{ *; }

# router
-keep public interface com.danke.android.templete.common.Router{ *; }
-keep public interface com.danke.android.templete.common.Router$*{ *; }
