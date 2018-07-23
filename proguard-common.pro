# 指定外部模糊字典
-obfuscationdictionary ./dictionary-pro.txt
# 指定 class 模糊字典
-classobfuscationdictionary ./dictionary-pro.txt
# 指定 package 模糊字典
-packageobfuscationdictionary ./dictionary-pro.txt

#############################################
#
# 对于一些基本指令的添加
#
#############################################
-ignorewarnings

# 不缩减代码
-dontshrink

# 不优化代码
# -dontoptimize

# 不混淆输入的类文件
# -dontobfuscate

# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5

# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames

# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses

# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers

# 使项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose

# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify

# 保留Annotation不混淆，避免混淆泛型，抛出异常时保留代码行号，等
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod,LocalVariableTable,LocalVariableTypeTable

# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
# -optimizations !code/simplification/*,!field/*,!class/merging/*,!method/removal/parameter,!method/propagation/*,!method/marking/static,!class/unboxing/enum,!code/removal/advanced,!code/allocation/variable

# 把代码以及所使用到的各种第三方库代码统统移动到同一个包下
# -repackageclasses 'com.danke.android.internal'

# 优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification

-useuniqueclassmembernames
-keeppackagenames doNotKeepAThing

# 竭力合并接口
# -mergeinterfacesaggressively


#############################################
#
# Android开发中一些需要保留的公共部分
#
#############################################

# 保留四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用
#-keep class * extends android.app.*{ *; }
#-keep public class * extends android.os.Binder
#-keep public class * extends android.app.Fragment
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Appliction
#-keep public class * extends android.app.Service
#-keep public class * extends android.os.IInterface
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.appwidget.AppWidgetProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class * extends android.webkit.*{ *; }
#-keep public class * extends android.widget.*{ *; }
#-keep public class * extends android.view.View{ *; }

# 保留support下的所有类及其内部类
-keep class android.support.** { *; }

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

-keep interface android.support.v7.**{ *; }
-keep public class android.support.v7.** { *; }
#-keep public class * extends android.support.**{ *; }
-keep public class * extends android.support.annotation.**
-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

# 保留R下面的资源
-keep class **.R$* { *; }

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留在Activity中的方法参数是view的方法，
# 这样在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    **[] $VALUES;
    public *;
}

# 保留自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# json
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep class org.json.**{ *; }

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}

# 确保没有开启 --dontoptimize选项的前提下，添加Proguard优化日志配置，移除日志
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static *** d(...);
    public static *** e(...);
    public static *** i(...);
    public static *** v(...);
    public static *** w(...);
    public static *** wtf(...);
    public static *** println(...);
}

# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface android.support.annotation.Keep

# Do not strip any method/class that is annotated with @Keep
-keep @android.support.annotation.Keep class *
-keepclassmembers class * {
    @android.support.annotation.Keep *;
}

#############################################
#
# 暴露调用对象
#
#############################################
-keepclassmembers, allowobfuscation class * { *; }
-keep, allowobfuscation class com.danke.android.**

# 属性信息
-keep public class com.danke.android.**.BuildConfig {
    public <fields>;
    public <methods>;
}

# arouter
-keep class com.alibaba.android.arouter.routes.**
-keepclassmembernames class com.alibaba.android.arouter.routes.** {
    public <fields>;
    public <methods>;
    protected <fields>;
    protected <methods>;
}