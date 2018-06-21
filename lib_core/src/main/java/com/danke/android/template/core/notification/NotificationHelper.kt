package com.danke.android.template.core.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.Settings
import android.support.annotation.DrawableRes
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat

/**
 * @author danke
 * @date 2018/6/21
 */
object NotificationHelper {

    class Builder(private val context: Context) {

        private val P = Params()

        private inner class Params {

            internal var groupId: String = "default_group"

            internal var groupName: CharSequence = "default_group"

            internal var channelId: String = "default_channel"

            internal var channelName: CharSequence = "default_channel"

            internal var autoCancel: Boolean = false

            internal var pendingIntent: PendingIntent? = null

            internal var largeIcon: Bitmap? = null

            @DrawableRes
            internal var largeIconRes: Int = -1

            @DrawableRes
            internal var smallIconRes: Int = -1

            internal var title: CharSequence = ""

            internal var text: CharSequence = ""

            @SuppressLint("InlinedApi")
            internal var importance: Int = NotificationManager.IMPORTANCE_MIN

            @SuppressLint("InlinedApi")
            internal var lockScreenVisibility: Int = Notification.VISIBILITY_PRIVATE

            internal var showBadge: Boolean = false

            internal var lights: Boolean = false
        }

        fun setGroupId(groupId: String): Builder {
            P.groupId = groupId
            return this
        }

        fun setGroupName(groupName: String): Builder {
            P.groupName = groupName
            return this
        }

        fun setChannelId(channelId: String): Builder {
            P.channelId = channelId
            return this
        }

        fun setChannelName(channelName: String): Builder {
            P.channelName = channelName
            return this
        }

        fun setAutoCancel(autoCancel: Boolean): Builder {
            P.autoCancel = autoCancel
            return this
        }

        fun setPendingIntent(pendingIntent: PendingIntent?): Builder {
            P.pendingIntent = pendingIntent
            return this
        }

        fun setLargeIcon(largeIcon: Bitmap?): Builder {
            P.largeIcon = largeIcon
            return this
        }

        fun setLargeIconRes(@DrawableRes largeIconRes: Int): Builder {
            P.largeIconRes = largeIconRes
            return this
        }

        fun setSmallIconRes(@DrawableRes smallIconRes: Int): Builder {
            P.smallIconRes = smallIconRes
            return this
        }

        fun setTitle(title: CharSequence): Builder {
            P.title = title
            return this
        }

        fun setText(text: CharSequence): Builder {
            P.text = text
            return this
        }

        fun setImportance(importance: Int): Builder {
            P.importance = importance
            return this
        }

        fun setLockScreenVisibility(lockScreenVisibility: Int): Builder {
            P.lockScreenVisibility = lockScreenVisibility
            return this
        }

        fun setShowBadge(showBadge: Boolean): Builder {
            P.showBadge = showBadge
            return this
        }

        fun setLights(lights: Boolean): Builder {
            P.lights = lights
            return this
        }

        /**
         * https://medium.com/exploring-android/exploring-android-o-notification-channels-94cd274f604c
         *
         * @return
         */
        fun build(): Notification {
            val notification: Notification

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val manager = context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                val group = NotificationChannelGroup(P.groupId, P.groupName)
                manager.createNotificationChannelGroup(group)

                val channel = NotificationChannel(P.channelId, P.channelName, P.importance)
                channel.setShowBadge(P.showBadge)
                channel.enableLights(P.lights)
                channel.group = P.groupId
                channel.lockscreenVisibility = P.lockScreenVisibility
                manager.createNotificationChannel(channel)

                val builder = Notification.Builder(context.applicationContext, P.channelId)
                        .setContentIntent(P.pendingIntent)
                        .setContentTitle(P.title)
                        .setContentText(P.text)
                        .setAutoCancel(P.autoCancel)
                        .setWhen(System.currentTimeMillis())

                if (P.smallIconRes != -1) {
                    builder.setSmallIcon(P.smallIconRes)
                }

                if (P.largeIcon != null) {
                    builder.setLargeIcon(P.largeIcon)
                } else if (P.largeIconRes != -1) {
                    builder.setLargeIcon(BitmapFactory.decodeResource(context.resources, P.largeIconRes))
                }

                notification = builder.build()
            } else {
                val builder = NotificationCompat.Builder(context.applicationContext, P.channelId)
                        .setContentIntent(P.pendingIntent)
                        .setContentTitle(P.title)
                        .setContentText(P.text)
                        .setAutoCancel(P.autoCancel)
                        .setWhen(System.currentTimeMillis())

                var priority = NotificationCompat.PRIORITY_MIN
                when (P.importance) {
                    NotificationManager.IMPORTANCE_MIN -> priority = NotificationCompat.PRIORITY_MIN
                    NotificationManager.IMPORTANCE_LOW -> priority = NotificationCompat.PRIORITY_LOW
                    NotificationManager.IMPORTANCE_DEFAULT -> priority = NotificationCompat.PRIORITY_DEFAULT
                    NotificationManager.IMPORTANCE_HIGH -> priority = NotificationCompat.PRIORITY_HIGH
                }
                builder.priority = priority

                if (P.smallIconRes != -1) {
                    builder.setSmallIcon(P.smallIconRes)
                }

                if (P.largeIcon != null) {
                    builder.setLargeIcon(P.largeIcon)
                } else if (P.largeIconRes != -1) {
                    builder.setLargeIcon(BitmapFactory.decodeResource(context.resources, P.largeIconRes))
                }

                notification = builder.build()
            }

            return notification
        }
    }

    /**
     * You can't programmatically modify the behavior of a notification channel
     * after it's created and submitted to the notification manager;
     * the user is in charge of those settings after creation.
     * Or uninstall the app.
     *
     * @param context
     * @param channelId
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun gotoNotificationChannelSettings(context: Context, channelId: String) {
        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        context.startActivity(intent)
    }

}
