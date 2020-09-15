package es.situm.capacitycontrol.positioning

import es.situm.integration.CapacityControlInput
import es.situm.sdk.model.realtime.RealTimeData

class RealtimeProviderImpl : RealtimeProvider {
    override fun getRealTimeData() =
            CapacityControlInput.realTimeData;
}