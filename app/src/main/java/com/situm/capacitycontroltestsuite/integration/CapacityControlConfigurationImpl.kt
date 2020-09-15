package com.situm.capacitycontroltestsuite.integration

import android.app.Notification
import es.situm.integration.CapacityControlConfiguration

object CapacityControlConfigurationImpl: CapacityControlConfiguration {
    var breachUpdateInterval: Long = 60
    var warningCapacityActive = true

    override fun breachUpdateInterval(): Long {
        return breachUpdateInterval
    }

    override fun warningCapacityActive(): Boolean {
        return warningCapacityActive
    }

    override fun capacityControlForegroundNotification(): Notification? {
        return null
    }
}