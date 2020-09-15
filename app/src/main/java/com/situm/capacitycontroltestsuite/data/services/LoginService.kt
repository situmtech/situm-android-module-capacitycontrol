package com.situm.capacitycontroltestsuite.data.services

import com.situm.capacitycontroltestsuite.data.base.Result
import com.situm.capacitycontroltestsuite.model.User
import es.situm.sdk.SitumSdk
import es.situm.sdk.error.Error
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoginService() {
    private var continuation: Continuation<Result<Any>>? = null

    suspend fun runService(user: User): Result<Any> {
        return suspendCoroutine { continuation ->
            this.continuation = continuation

            SitumSdk.configuration().dashboardURL = user.dashboard
            SitumSdk.configuration().setUserPass(user.username, user.password)

            SitumSdk.communicationManager().validateUserCredentials(object : es.situm.sdk.utils.Handler<Any> {
                override fun onSuccess(obtained: Any) {
                    continuation.resume(Result.Response(obtained))
                }

                override fun onFailure(error: Error) {
                    continuation.resume(Result.Error(error.message))
                }
            })
        }
    }
}