package es.situm.capacitycontrol.positioning

import es.situm.integration.CapacityControlInput
import es.situm.sdk.model.location.Location

/**
 * This class' responsibility is to provide the user's las known location.
 */
class PositionProvider {

    fun getLastLocation(): Location? {
        return CapacityControlInput.location
    }
}
