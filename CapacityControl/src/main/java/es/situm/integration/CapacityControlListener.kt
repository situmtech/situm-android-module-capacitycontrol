package es.situm.integration

import es.situm.capacitycontrol.model.CapacityControlGeofence

interface CapacityControlListener {

    /**
     * The module will notify when a user populated geofence's capacity has exceeded the warning threshold or the max capacity.
     * You can specify the max capacity for each geofence by adding "max_capacity" as a custom field.
     * By default, the warning threshold is 80% of the max capacity. You can specify the warning threshold for each geofence in the
     * dashboard by adding "warning_capacity_threshold" as a custom field.
     * The interval to notify this is defined in CapacityControlConfiguration.breachUpdateInterval.
     */
    fun newBreachesDetected(breaches: List<CapacityControlGeofence>, warnings: List<CapacityControlGeofence>)

    /**
     * Returns all the capacity control geofences with their capacity breach state. (BREACH_MAX, BREACH_WARNING, BREACH_NONE)
     * This includes all the geofences whether they are populated by the user or not.
     * This is called every 10 seconds.
     */
    fun onUpdateCapacityControlGeofences(capacityBreaches: List<CapacityControlGeofence>)

}