package es.situm.capacitycontrol.data.usecase

import es.situm.capacitycontrol.data.base.Result
import es.situm.capacitycontrol.data.base.UseCase
import es.situm.capacitycontrol.data.services.GetBuildingService
import es.situm.capacitycontrol.data.services.GetGeofencesService
import es.situm.capacitycontrol.geofences.GeofenceUtils
import es.situm.capacitycontrol.model.GeofenceWrapper
import es.situm.sdk.model.cartography.Geofence
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class GetGeofenceUsecase : UseCase<GeofenceWrapper, GetGeofenceUsecase.Params>() {

    override suspend fun run(params: Params): Result<GeofenceWrapper> {

        val geofences = mutableListOf<Geofence>()

        when (val response = GetBuildingService().runService()) {

            is Result.Response -> {
                if (response.data.any { building -> building.identifier == params.buildingid }) {
                    val building = response.data.find { building -> building.identifier == params.buildingid }
                    building?.let {

                        val geofenceJob = GlobalScope.async {
                            GetGeofencesService(it).runService()
                        }

                        geofenceJob.await().also { geofenceResponse ->
                            if (geofenceResponse is Result.Response) {
                                geofences.addAll(geofenceResponse.data.filter { geofence ->
                                    geofence.customFields.containsKey(GeofenceUtils.CUSTOM_FIELD_MAX_CAPACITY)
                                            && geofence.customFields[GeofenceUtils.CUSTOM_FIELD_MAX_CAPACITY]?.toIntOrNull()?.let { it > 0 } ?: false
                                })
                            }
                        }

                        return Result.Response(GeofenceWrapper(geofences, building.identifier))
                    } ?: return Result.Error()
                } else {
                    return Result.Error()
                }
            }
            is Result.Error -> {
                return response
            }
        }
    }

    class Params(internal val buildingid: String)

}
