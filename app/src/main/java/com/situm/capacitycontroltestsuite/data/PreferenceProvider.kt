package com.situm.capacitycontroltestsuite.data

import com.situm.capacitycontroltestsuite.model.User
import org.koin.core.KoinComponent

interface PreferenceProvider: KoinComponent {
    fun saveUser(user: User)

    fun getUser(): User?

    fun removeUser()
}