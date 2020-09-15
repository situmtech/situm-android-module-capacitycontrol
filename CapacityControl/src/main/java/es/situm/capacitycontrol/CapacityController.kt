package es.situm.capacitycontrol

import es.situm.capacitycontrol.geofences.CCGeofenceTransformer
import es.situm.capacitycontrol.geofences.GeofenceProvider
import es.situm.capacitycontrol.geofences.SituationAwareGeofenceConstructor
import es.situm.capacitycontrol.model.CapacityControlGeofence
import es.situm.capacitycontrol.positioning.PositionProvider
import es.situm.capacitycontrol.positioning.RealtimeProvider

/**
 * This class' responsibility is to determine whether a geofence capacity has been breached.
 */
class CapacityController(private val geofenceProvider: GeofenceProvider, private val positionProvider: PositionProvider, val rtProvider: RealtimeProvider) {

    internal fun checkCapacity(callback: CapacityBreachHandler) {

        //Get Location data
        val lastLocation = positionProvider.getLastLocation() ?: run {
            callback.detectionUnavailable()
            return
        }

        //Get RealTime Data
        val lastRealtimeData = rtProvider.getRealTimeData() ?: run {
            callback.detectionUnavailable()
            return
        }

        //Check data integrity
        if (lastLocation.buildingIdentifier == "-1"
                || lastRealtimeData.locations.isNullOrEmpty()
                || lastLocation.buildingIdentifier != lastRealtimeData.locations[0].buildingIdentifier) {
            callback.detectionUnavailable()
            return
        }

        //Get all Geofences
        val geofenceWrapper = geofenceProvider.getGeofences(lastLocation)

        if (geofenceWrapper == null || geofenceWrapper.geofences.isNullOrEmpty()) {
            callback.detectionUnavailable()
            return
        }

        //Transform all Geofences to CapacityBreachGeofences.
        val capacityControlGeofences = CCGeofenceTransformer.extractGeofenceWrapper(geofenceWrapper, SituationAwareGeofenceConstructor(lastLocation, lastRealtimeData))

        //Detect user specific breaches
        val userSpecificBreaches = capacityControlGeofences.filter { it.breach != CapacityControlGeofence.CapacityBreachType.BREACH_NONE && it.userInside }

        //Notify calculation complete
        callback.detectionComplete(capacityControlGeofences, userSpecificBreaches)

    }

    interface CapacityBreachHandler {
        fun detectionComplete(geofences: List<CapacityControlGeofence>, breaches: List<CapacityControlGeofence>)
        fun detectionUnavailable()
    }
}