package com.situm.capacitycontroltestsuite.integration

import es.situm.capacitycontrol.model.CapacityControlGeofence
import es.situm.integration.CapacityControlListener

object CapacityControlListenerImpl : CapacityControlListener {


    override fun newBreachesDetected(breaches: List<CapacityControlGeofence>, warnings: List<CapacityControlGeofence>) {
        val builder = StringBuilder()
        if (breaches.isNullOrEmpty().not()) {
            builder.append("·······BREACHES DETECTED: ${breaches.count()}·······").append(" <br />")
            for (breach in breaches) {
                builder.append(breach)
            }
            builder.append(" <br />")
        }

        if (warnings.isNullOrEmpty().not()) {
            builder.append("·······WARNINGS DETECTED: ${warnings.count()}·······").append(" <br />")
            for (warning in warnings) {
                builder.append(warning)
            }
            builder.append(" <br />")
        }
        DebugTool.registerTimedEvent(builder.toString())
    }

    override fun onUpdateCapacityControlGeofences(capacityBreaches: List<CapacityControlGeofence>) {
        val builder = StringBuilder()
        if (capacityBreaches.isNullOrEmpty().not()) {
            builder.append("·······GEOFENCES DETECTED: ${capacityBreaches.count()}·······").append(" <br />")
            for (geofence in capacityBreaches) {
                builder.append(geofence)
            }
            builder.append(" <br />")
        }

        DebugTool.registerTimedEvent(builder.toString())
    }
}