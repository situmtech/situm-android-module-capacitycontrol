package es.situm.capacitycontrol.geofences

import es.situm.capacitycontrol.data.base.Result
import es.situm.capacitycontrol.data.usecase.GetGeofenceUsecase
import es.situm.capacitycontrol.model.GeofenceWrapper
import es.situm.sdk.model.location.Location

/**
 * This class' responsibility is to obtain all the geofences inside the building corresponding to a specific location.
 */
class GeofenceProvider {

    private var geofenceWrapper: GeofenceWrapper? = null

    fun getGeofences(location: Location?): GeofenceWrapper? {
        if (location == null || location.buildingIdentifier == "-1") {
            return null
        }

        geofenceWrapper?.let { geofences ->
            return if (geofences.buildingId== location.buildingIdentifier) geofences
            else {
                updateGeofences(location.buildingIdentifier)
                null
            }
        } ?: run {
            updateGeofences(location.buildingIdentifier)
            return null
        }
    }

    private fun updateGeofences(buildingId: String) {
        GetGeofenceUsecase().invoke(GetGeofenceUsecase.Params(buildingId)) { result ->
            geofenceWrapper = when (result) {
                is Result.Error -> {
                    null
                }
                is Result.Response<GeofenceWrapper> -> {
                    result.data
                }
            }
        }
    }
}
