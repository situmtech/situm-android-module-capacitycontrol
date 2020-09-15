package com.situm.capacitycontroltestsuite.data.services

import com.situm.capacitycontroltestsuite.data.PreferenceProvider
import com.situm.capacitycontroltestsuite.data.base.Result
import es.situm.sdk.SitumSdk
import es.situm.sdk.error.Error
import es.situm.sdk.utils.Handler
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LogoutService(val preferenceProvider: PreferenceProvider){
    private var continuation: Continuation<Result<Boolean>>? = null

    suspend fun runService(): Result<Boolean> {
        return suspendCoroutine { continuation ->
            this.continuation = continuation

            preferenceProvider.removeUser()

            SitumSdk.communicationManager().logout(object : Handler<Any> {
                override fun onSuccess(obtained: Any?) {
                    continuation.resume(Result.Response(true))
                }

                override fun onFailure(error: Error) {
                    continuation.resume(Result.Error(error.message))
                }
            })
        }
    }
}