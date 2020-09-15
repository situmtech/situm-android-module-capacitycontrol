package es.situm.capacitycontrol.data.services

import es.situm.capacitycontrol.data.base.Result
import es.situm.sdk.SitumSdk
import es.situm.sdk.error.Error
import es.situm.sdk.model.cartography.Building
import es.situm.sdk.model.cartography.Geofence
import es.situm.sdk.utils.Handler
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetGeofencesService(private val building: Building) {
    private var continuation: Continuation<Result<List<Geofence>>>? = null

    suspend fun runService(): Result<List<Geofence>> {
        return suspendCoroutine { continuation ->
            this.continuation = continuation

            SitumSdk.communicationManager().fetchGeofencesFromBuilding(building, object :
                    Handler<List<Geofence>> {

                override fun onSuccess(p0: List<Geofence>) {
                    continuation.resume(Result.Response(ArrayList(p0)))
                }

                override fun onFailure(p0: Error) {
                    continuation.resume(Result.Error())
                }
            })
        }
    }
}