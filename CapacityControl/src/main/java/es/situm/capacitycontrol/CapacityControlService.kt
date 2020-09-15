package es.situm.capacitycontrol

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import es.situm.capacitycontrol.geofences.GeofenceProvider
import es.situm.capacitycontrol.model.CapacityControlGeofence
import es.situm.capacitycontrol.notifications.BreachNotificationHandler
import es.situm.capacitycontrol.notifications.NotificationService
import es.situm.capacitycontrol.positioning.PositionProvider
import es.situm.capacitycontrol.positioning.RealtimeProviderImpl
import es.situm.integration.CapacityControlIntegrator
import java.text.SimpleDateFormat
import java.util.*

class CapacityControlService : Service() {
    private val TAG: String = CapacityControlService::class.java.simpleName
    private val FOREGROUND_NOTIFICATION_ID = SimpleDateFormat("HHmmssSSS", Locale.US).format(Date()).toInt()


    //Dependencies
    private val notificationService = NotificationService(this)

    //Non-injected dependencies
    private var context: CapacityControlService
    private var handler: Handler = Handler()
    private var runnable: Runnable? = null

    //pollingTime is default 10s. Separate from notification times.
    private val scanningFrecuencyMs = 10 * 1000L

    private var isServiceAlreadyRunning = false
    private var isServiceBeingDestroyed = false

    companion object {

        private val LAUNCHER = ForegroundServiceLauncher(CapacityControlService::class.java)

        @JvmStatic
        fun startSocialDistanceService(context: Context?) {
            if (context == null) {
                return
            }
            val intent = Intent(context, CapacityControlService::class.java)
            LAUNCHER.startForegroundService(context, intent)
        }

        @JvmStatic
        fun stopSocialDistanceService(context: Context?) {
            if (context == null) {
                return
            }
            LAUNCHER.stopService(context)
        }
    }

    init {
        isServiceAlreadyRunning = false
        context = this
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        initService()

        isServiceBeingDestroyed = LAUNCHER.onServiceStarted(this)
        if (isServiceBeingDestroyed || isServiceAlreadyRunning) {
            return START_STICKY
        }
        isServiceAlreadyRunning = true

        return START_NOT_STICKY
    }

    private fun initService() {
        if (notificationService.createServiceNotificationChannel()) {
            launchServiceWithForegroundNotification(notificationService.createServiceNotification())
            val capacityController = CapacityController(GeofenceProvider(), PositionProvider(), RealtimeProviderImpl())
            startNotificationLoop(capacityController, BreachNotificationHandler())
        } else {
            Log.e(TAG, "Error while creating notification channel for social distance checking.")
        }
    }

    private fun startNotificationLoop(capacityController: CapacityController, notificationHandler: BreachNotificationHandler) {
        runnable = object : Runnable {
            override fun run() {
                if (isServiceBeingDestroyed) return

                capacityController.checkCapacity(object : CapacityController.CapacityBreachHandler {
                    override fun detectionComplete(geofences: List<CapacityControlGeofence>, breaches: List<CapacityControlGeofence>) {
                        CapacityControlIntegrator.getListener().onUpdateCapacityControlGeofences(geofences)
                        notificationHandler.handleBreachRequest(breaches)
                    }

                    override fun detectionUnavailable() {
                        Log.d(TAG, "Unable to detect breaches. Not enough data.")
                    }

                })
                handler.postDelayed(this, scanningFrecuencyMs)
            }
        }

        runnable?.let {
            handler.postDelayed(it, scanningFrecuencyMs)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        isServiceBeingDestroyed = true

        runnable?.let {
            handler.removeCallbacks(it)
        }

        notificationService.removeServiceNotificationChannel()
        isServiceAlreadyRunning = false

        super.onDestroy()
    }

    private fun launchServiceWithForegroundNotification(notification: Notification) {
        this.startForeground(FOREGROUND_NOTIFICATION_ID, notification)
    }
}