package com.situm.capacitycontroltestsuite.integration

import java.util.*

object DebugTool {
    var timedEventLogger: TimedEventLogger? = null

    fun registerTimedEvent(event: String) {
        synchronized(this) {
            timedEventLogger?.registerEventTimestamp(event, Date().time)
        }
    }

    interface TimedEventLogger {
        fun registerEventTimestamp(event: String, timestamp: Long)

        companion object EventType {
            const val timeMeasurement: String = "time_measurement"
            const val fetchInfo: String = "fetch_info"
        }
    }
}