package com.situm.capacitycontroltestsuite.data.services

import com.situm.capacitycontroltestsuite.data.PreferenceProvider
import com.situm.capacitycontroltestsuite.data.base.Result
import com.situm.capacitycontroltestsuite.model.User
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FetchUserService(val preferenceProvider: PreferenceProvider) {

    private var continuation: Continuation<Result<User>>? = null

    suspend fun runService(): Result<User> {
        return suspendCoroutine { continuation ->
            this.continuation = continuation

            (preferenceProvider.getUser())?.let {
                continuation.resume(Result.Response(it))
            } ?: kotlin.run { continuation.resume(Result.Error("No user available")) }

        }
    }
}