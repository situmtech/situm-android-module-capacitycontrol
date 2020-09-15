package es.situm.capacitycontrol.positioning

import es.situm.sdk.model.realtime.RealTimeData

interface RealtimeProvider {
    fun getRealTimeData(): RealTimeData?
}