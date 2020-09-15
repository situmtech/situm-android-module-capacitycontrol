package es.situm.capacitycontrol

import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class ForegroundServiceLauncher(private val serviceClass: Class<out Service>) {
    //Reference: https://proandroiddev.com/pitfalls-of-a-foreground-service-lifecycle-59f014c6a125

    private var isStarting = false
    private var shouldStop = false

    @Synchronized
    fun startForegroundService(context: Context, intent: Intent) {
        isStarting = true
        shouldStop = false
        ContextCompat.startForegroundService(context, intent)
    }

    @Synchronized
    fun stopService(context: Context) {
        if (isStarting) {
            shouldStop = true
        } else {
            context.stopService(Intent(context, serviceClass))
        }
    }

    @Synchronized
    fun onServiceStarted(service: Service): Boolean {
        isStarting = false
        if (shouldStop) {
            shouldStop = false
            service.stopSelf()
            return true
        }
        return false
    }
}