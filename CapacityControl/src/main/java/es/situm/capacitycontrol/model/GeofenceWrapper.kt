package es.situm.capacitycontrol.model

import es.situm.sdk.model.cartography.Geofence

data class GeofenceWrapper(val geofences: List<Geofence>, val buildingId: String)