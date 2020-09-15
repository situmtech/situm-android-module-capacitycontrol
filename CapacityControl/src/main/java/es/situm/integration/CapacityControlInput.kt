package es.situm.integration

import es.situm.sdk.model.location.Location
import es.situm.sdk.model.realtime.RealTimeData

object CapacityControlInput {
    internal var location: Location? = null
    internal var realTimeData: RealTimeData? = null

    /**
     * The app must provide the positioning information obtained through the SDK.
     * It is recommended that this info is updated with the same response frequency as the SDK.
     */
    @JvmStatic
    fun updateLocation(location: Location?) {
        this.location = location
    }

    /**
     * The app must provide the RealTimeData obtained through the SDK.
     * It is recommended that this info is updated with the same response frequency as the SDK.
     */
    @JvmStatic
    fun updateRealtimeData(realTimeData: RealTimeData?) {
        this.realTimeData = realTimeData
    }
}