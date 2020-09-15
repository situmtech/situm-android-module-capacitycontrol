package es.situm.capacitycontrol.geofences

import es.situm.capacitycontrol.model.CapacityControlGeofence
import es.situm.integration.CapacityControlIntegrator
import es.situm.sdk.model.cartography.Geofence
import es.situm.sdk.model.location.Coordinate
import es.situm.sdk.model.location.Location
import es.situm.sdk.model.realtime.RealTimeData

class SituationAwareGeofenceConstructor(val location: Location, val realtime: RealTimeData) {

    fun buildCapacityControlGeofences(rawGeofences: List<CCGeofenceTransformer.RawCapacityControlGeofence>): List<CapacityControlGeofence> {

        val capacityControlGeofences = mutableListOf<CapacityControlGeofence>()

        for (geofence in rawGeofences) {
            val population = getNumberOfUserInGeofence(geofence.geofence)

            val capacityControlGeofence = CapacityControlGeofence(
                    geofence.geofence,
                    calculateBreach(geofence, population),
                    geofence.maxCapacity,
                    geofence.warningCapacity,
                    population,
                    getUserInsideInGeofence(geofence.geofence)
            )

            capacityControlGeofences.add(capacityControlGeofence)
        }

        return capacityControlGeofences;
    }

    //Returns number of people inside Geofence
    private fun getNumberOfUserInGeofence(geofence: Geofence): Int {
        var peopleInFence = 0
        for (rtLocation in realtime.locations) {
            if (rtLocation.floorIdentifier != geofence.floorIdentifier) {
                continue
            }
            val realtimeCoordinate = Coordinate(rtLocation.position.coordinate.latitude, rtLocation.position.coordinate.longitude)

            if (GeofenceUtils().checkIfCoordinateIsInsideGeofence(realtimeCoordinate, geofence)) {
                peopleInFence++
            }
        }
        return peopleInFence
    }

    //Returns whether the user is inside the current Geofence.
    private fun getUserInsideInGeofence(geofence: Geofence): Boolean {
        if (geofence.floorIdentifier != location.floorIdentifier) return false
        return GeofenceUtils().checkIfCoordinateIsInsideGeofence(location.coordinate, geofence)
    }

    private fun calculateBreach(geofence: CCGeofenceTransformer.RawCapacityControlGeofence, population: Int): CapacityControlGeofence.CapacityBreachType {
        return when {
            population > geofence.maxCapacity -> {
                CapacityControlGeofence.CapacityBreachType.BREACH_MAX
            }
            population > geofence.warningCapacity && CapacityControlIntegrator.getConfiguration().warningCapacityActive() -> {
                CapacityControlGeofence.CapacityBreachType.BREACH_WARNING
            }
            else -> {
                CapacityControlGeofence.CapacityBreachType.BREACH_NONE
            }
        }
    }
}