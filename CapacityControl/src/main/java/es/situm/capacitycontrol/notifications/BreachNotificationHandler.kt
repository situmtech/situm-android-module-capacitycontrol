package es.situm.capacitycontrol.notifications

import es.situm.capacitycontrol.model.CapacityControlGeofence
import es.situm.integration.CapacityControlIntegrator

/**
 * This class' responsibility is to determine whether to notify the user about detected breaches.
 */
class BreachNotificationHandler {

    fun handleBreachRequest(breaches: List<CapacityControlGeofence>) {
        val breachesPendingHandling = mutableListOf<CapacityControlGeofence>()

        //check if request has been handled in the last 60 seconds
        breaches.forEach { newBreach ->
            if (checkIfBreachHandled(newBreach).not()) {
                //handle and add to handled breaches
                breachesPendingHandling.add(newBreach)
            }
        }

        if (breachesPendingHandling.isNotEmpty())
            handle(breachesPendingHandling)
    }

    private fun handle(breachesPendingHandling: MutableList<CapacityControlGeofence>) {
        //get warning breaches
        val warningBreaches = breachesPendingHandling.filter { it.breach == CapacityControlGeofence.CapacityBreachType.BREACH_WARNING }
        //get max breaches
        val maxBreaches = breachesPendingHandling.filter { it.breach == CapacityControlGeofence.CapacityBreachType.BREACH_MAX }

        CapacityControlIntegrator.getListener().newBreachesDetected(maxBreaches, warningBreaches)

        NotificationCache.saveHandledRequests(breachesPendingHandling)
    }

    private fun checkIfBreachHandled(newBreach: CapacityControlGeofence): Boolean {
        //Some cases may be left over (Same geofence different type)
        NotificationCache.getHandledRequests().values.forEach { breaches ->
            breaches.forEach { handledBreach ->
                //Iterate all handled breached to check if newBreach has been handled with different type
                if (newBreach.geofence.identifier == handledBreach.geofence.identifier) {

                    //Same geofence and same breach = assume handled
                    if (newBreach.breach == handledBreach.breach) return true

                    if (newBreach.breach == CapacityControlGeofence.CapacityBreachType.BREACH_WARNING
                            && handledBreach.breach == CapacityControlGeofence.CapacityBreachType.BREACH_MAX)
                        return true
                }
            }
        }

        return false
    }
}