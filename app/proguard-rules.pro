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

# 指定外部模糊字典
-obfuscationdictionary dictionary.txt
# 指定 class 模糊字典
-classobfuscationdictionary dictionary.txt
# 指定 package 模糊字典
-packageobfuscationdictionary dictionary.txt

-ignorewarning

-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

# android
-keep class android.support.**{*;}

# Suppresses third sdk proguard warnings
-keepattributes EnclosingMethod

# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface android.support.annotation.Keep

# Do not strip any method/class that is annotated with @Keep
-keep @android.support.annotation.Keep class *
-keepclassmembers class * {
    @android.support.annotation.Keep *;
}

# 过滤泛型，防止强制类型转换异常
-keepattributes Signature

# Annotation
-keepattributes *Annotation*

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# ARouter
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider
# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider

# EasyPermission
-keep,allowobfuscation @interface pub.devrel.easypermissions.AfterPermissionGranted
-keep class ** {
    @pub.devrel.easypermissions.AfterPermissionGranted <methods>;
}