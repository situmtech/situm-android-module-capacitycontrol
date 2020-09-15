package es.situm.capacitycontrol.notifications

import es.situm.capacitycontrol.model.CapacityControlGeofence
import es.situm.integration.CapacityControlIntegrator
import java.util.*

object NotificationCache {
    private val handledRequests =
            mutableMapOf<Long, MutableList<CapacityControlGeofence>>()

    fun getHandledRequests(): Map<Long, MutableList<CapacityControlGeofence>> {
        val now = Date().time
        val keys = ArrayList(handledRequests.keys)

        keys.forEach { timestamp ->
            if (now - timestamp >= CapacityControlIntegrator.getConfiguration().breachUpdateInterval() * 1000) {
                handledRequests.remove(timestamp)
            }
        }
        return handledRequests
    }

    fun saveHandledRequests(capacityBreaches: MutableList<CapacityControlGeofence>) {
        handledRequests[Date().time] = capacityBreaches
    }
}