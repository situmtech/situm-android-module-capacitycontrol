package es.situm.capacitycontrol.model

import es.situm.sdk.model.cartography.Geofence

/**
 * This class contains the following values:
 *  - geofence: An instance of the class Geofence, defined in SitumSDK.
 *  - breach: A value indicating if the capacity (or warning) limit has been reached.
 *  - maxCapacity: Max number of people allowed inside this geofence.
 *                 This value is retrieved from the custom fields of the geofence.
 *  - warningCapacity: Number of people allowed inside this geofence before warning the user.
 *                     This value is retrieved from the custom fields of the geofence.
 *  - numberOfPeople: Amount of people currently inside the geofence.
 *  - userInside: Boolean value indicating if the user is currently inside the geofence.
 */
data class CapacityControlGeofence(
        val geofence: Geofence,
        val breach: CapacityBreachType,
        val maxCapacity: Int,
        val warningCapacity: Int,
        val numberOfPeople: Int,
        val userInside: Boolean) {

    enum class CapacityBreachType {
        BREACH_WARNING,
        BREACH_MAX,
        BREACH_NONE
    }

    override fun equals(other: Any?): Boolean {
        if (other !is CapacityControlGeofence) return false
        return other.geofence.identifier == this.geofence.identifier && other.breach == breach
    }

    override fun hashCode(): Int {
        var result = geofence.hashCode()
        result = 31 * result + breach.hashCode()
        result = 31 * result + maxCapacity
        result = 31 * result + warningCapacity
        result = 31 * result + numberOfPeople
        result = 31 * result + userInside.hashCode()
        return result
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("{")
        builder.append("geofence: ${geofence.identifier}").append(" <br />")
        builder.append("breach: $breach").append(" <br />")
        builder.append("maxCapacity: $maxCapacity").append(" <br />")
        builder.append("warningCapacity: $warningCapacity").append(" <br />")
        builder.append("numberOfPeople: $numberOfPeople").append(" <br />")
        builder.append("userInside: $userInside").append(" <br />")
        builder.append("}")
        return builder.toString()
    }
}