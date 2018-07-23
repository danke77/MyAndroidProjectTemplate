# EasyPermission
-keep,allowobfuscation @interface pub.devrel.easypermissions.AfterPermissionGranted
-keep class ** {
    @pub.devrel.easypermissions.AfterPermissionGranted <methods>;
}