package es.situm.capacitycontrol.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import es.situm.capacitycontrol.R
import java.text.SimpleDateFormat
import java.util.*

class NotificationService(val context: Context) {
    private val CHANNEL_SERVICE_NOTIFICATION = "NotificationService.CHANNEL_SERVICE_NOTIFICATION"
    private val CHANNEL_NAME_SERVICE_NOTIFICATION: CharSequence = "SocialDistanceNotificationChannel"
    private val CHANNEL_DESCRIPTION_SERVICE_NOTIFICATION = "This channel is used to notify when the Social Distance detector is working."


    fun createServiceNotificationChannel(): Boolean {
        var channelSuccessfullyCreated = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_SERVICE_NOTIFICATION, CHANNEL_NAME_SERVICE_NOTIFICATION, NotificationManager.IMPORTANCE_LOW)
            channel.description = CHANNEL_DESCRIPTION_SERVICE_NOTIFICATION
            val notificationManager: NotificationManager? = context.getSystemService(NotificationManager::class.java)
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel)
            } else {
                channelSuccessfullyCreated = false
            }
        }

        return channelSuccessfullyCreated
    }

    fun removeServiceNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager? = context.getSystemService(NotificationManager::class.java)
            notificationManager?.deleteNotificationChannel(CHANNEL_SERVICE_NOTIFICATION)
        }
    }

    fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(context, CHANNEL_SERVICE_NOTIFICATION)
                .setSmallIcon(R.drawable.ic_distance_24dp)
                .setContentTitle(context.resources.getString(R.string.situm_tray_notification_foreground_title))
                .setContentText(context.resources.getString(R.string.situm_tray_notification_foreground_description))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()
    }

//    fun sendMaxBreachNotification() {
//        val builder = NotificationCompat.Builder(context, CHANNEL_SOCIAL_DISTANCE)
//                .setSmallIcon(R.drawable.ic_alert_24dp)
//                .setContentTitle(context.resources.getString(R.string.situm_tray_notification_crowded_geofence_title))
//                .setContentText(context.getResources().getString(R.string.situm_tray_notification_crowded_geofence_description))
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//
//                CapacityControlIntegrator.crowdControlDependency().capacityBreachNotificationIntent()?.let {
//                    builder.setContentIntent(it)
//                }
//
//        val notificationManager = NotificationManagerCompat.from(context)
//
//        notificationManager.notify(NOTIFICATION_ID_CAPACITY_BREACH_MAX, builder.build())
//    }
//
//    fun sendBreachWarningNotification() {
//        val builder = NotificationCompat.Builder(context, CHANNEL_SOCIAL_DISTANCE)
//                .setSmallIcon(R.drawable.ic_situm_warning)
//                .setContentTitle(context.resources.getString(R.string.situm_tray_notification_warning_geofence_title))
//                .setContentText(context.resources.getString(R.string.situm_tray_notification_warning_geofence_description))
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//        val notificationManager = NotificationManagerCompat.from(context)
//
//        CapacityControlIntegrator.crowdControlDependency().capacityWarningNotificationIntent()?.let {
//            builder.setContentIntent(it)
//        }
//
//        notificationManager.notify(NOTIFICATION_ID_CAPACITY_BREACH_WARNING, builder.build())
//    }
}