package com.situm.capacitycontroltestsuite.presentation

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.situm.capacitycontroltestsuite.data.PreferenceProvider
import com.situm.capacitycontroltestsuite.model.User

class PreferenceProviderImpl(context: Context) : PreferenceProvider {

    private val APP_SHARED_PREFS = "CapacityControlTestSuite.Preferences"
    private val sharedPreferences = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
    private val editor = sharedPreferences.edit()

    private val PREF_USER = "PreferenceProviderImpl.USER"

    override fun saveUser(user: User) {
        editor.putString(PREF_USER, Gson().toJson(user))
        editor.commit()
    }

    override fun getUser(): User? {
        val rawUser = sharedPreferences.getString(PREF_USER, null)

        rawUser?.let {
            if (it.isEmpty().not()) {
                return Gson().fromJson(it, User::class.java)
            }
        }

        return null
    }

    override fun removeUser() {
        editor.remove(PREF_USER)
        editor.commit()
    }
}