# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/xingyaoma/Software/android-sdk-linux/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-ignorewarnings

-libraryjars libs/Android_Map_V3.1.0.20151116.jar
-libraryjars libs/AMap_Location_v1.4.1_20150917.jar
-libraryjars libs/AMap_ Navi_v1.5.0_20151117.jar
-libraryjars libs/Msc.jar




-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# As described in tools/proguard/examples/android.pro - ignore all warnings.
-dontwarn android.support.v4.**
#如果有其它包有warning，在报出warning的包加入下面类似的-dontwarn 报名
-dontwarn com.amap.api.**
-dontwarn com.aps.**
#高德相关混淆文件
#3D 地图
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.amap.mapcore.*{*;}
-keep   class com.autonavi.amap.mapcore.*{*;}
#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
#Location
-keep   class com.amap.api.location.**{*;}
-keep   class com.aps.**{*;}
-keep   class com.autonavi.aps.amapapi.model.**{*;}
#Service
-keep class com.amap.api.services.**{*;}
#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}
#讯飞语音
-keep class com.iflytek.**{*;}