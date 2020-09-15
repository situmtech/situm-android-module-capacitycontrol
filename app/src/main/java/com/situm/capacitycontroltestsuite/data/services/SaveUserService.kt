package com.situm.capacitycontroltestsuite.data.services

import com.situm.capacitycontroltestsuite.data.PreferenceProvider
import com.situm.capacitycontroltestsuite.data.base.Result
import com.situm.capacitycontroltestsuite.model.User
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SaveUserService(val preferenceProvider: PreferenceProvider) {
    private var continuation: Continuation<Result<User>>? = null

    suspend fun runService(user: User): Result<User> {
        return suspendCoroutine { continuation ->
            this.continuation = continuation

            (preferenceProvider.saveUser(user))
                continuation.resume(Result.Response(user))
        }
    }
}