package es.situm.capacitycontrol.data.services

import es.situm.capacitycontrol.data.base.Result
import es.situm.sdk.SitumSdk
import es.situm.sdk.error.Error
import es.situm.sdk.model.cartography.Building
import es.situm.sdk.utils.Handler
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetBuildingService {
    private var continuation: Continuation<Result<Collection<Building>>>? = null

    suspend fun runService(): Result<Collection<Building>> {
        return suspendCoroutine { continuation ->
            this.continuation = continuation

            SitumSdk.communicationManager().fetchBuildings(object : Handler<Collection<Building>> {
                override fun onSuccess(p0: Collection<Building>) {
                    continuation.resume(Result.Response(p0))
                }

                override fun onFailure(p0: Error) {
                    continuation.resume(Result.Error())
                }
            })
        }
    }
}