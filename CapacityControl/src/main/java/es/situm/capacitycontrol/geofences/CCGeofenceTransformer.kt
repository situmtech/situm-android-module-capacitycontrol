package es.situm.capacitycontrol.geofences

import es.situm.capacitycontrol.model.CapacityControlGeofence
import es.situm.capacitycontrol.model.GeofenceWrapper
import es.situm.sdk.model.cartography.Geofence

object CCGeofenceTransformer {

    fun extractGeofenceWrapper(geofenceWrapper: GeofenceWrapper, sagc: SituationAwareGeofenceConstructor): List<CapacityControlGeofence> {

        //Get preliminary rawGeofence object to be processed with SituationAwareConstructor
        val rawGeofences: MutableList<RawCapacityControlGeofence> = mutableListOf()

        for (geofence in geofenceWrapper.geofences) {
            val maxCapacity = getGeofenceMaxCapacity(geofence)
            maxCapacity?.let { maximumCapacity ->
                rawGeofences.add(RawCapacityControlGeofence(geofence, maximumCapacity, getGeofenceWarningCapacity(geofence, maximumCapacity)))
            }
        }

        //Use SituationAwareConstrutor to buildCapacityControlGeofences
        return sagc.buildCapacityControlGeofences(rawGeofences)
    }

    private fun getGeofenceMaxCapacity(geofence: Geofence): Int? {
        return if (geofence.customFields.isNotEmpty()) {
            geofence.customFields[GeofenceUtils.CUSTOM_FIELD_MAX_CAPACITY]?.toIntOrNull()?.let {
                it
            }
        } else null
    }

    private fun getGeofenceWarningCapacity(geofence: Geofence, maxCapacity: Int): Int {
        var warningCapacity: Int = (maxCapacity * GeofenceUtils.WARNING_CAPACITY_PERCENTAGE_DEFAULT).toInt()
        if (geofence.customFields.isNotEmpty()) {
            geofence.customFields[GeofenceUtils.CUSTOM_FIELD_WARNING_CAPACITY_THRESHOLD]?.toIntOrNull()?.let {
                if (it in 1 until maxCapacity) warningCapacity = it
            }
        }
        return warningCapacity
    }

    data class RawCapacityControlGeofence(val geofence: Geofence, val maxCapacity: Int, val warningCapacity: Int)
}