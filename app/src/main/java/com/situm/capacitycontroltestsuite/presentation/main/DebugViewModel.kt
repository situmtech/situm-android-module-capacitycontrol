package com.situm.capacitycontroltestsuite.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.situm.capacitycontroltestsuite.data.SitumRealtimeManager
import com.situm.capacitycontroltestsuite.data.base.Event
import com.situm.capacitycontroltestsuite.integration.CapacityControlConfigurationImpl
import com.situm.capacitycontroltestsuite.integration.DebugTool
import com.situm.capacitycontroltestsuite.model.usecase.LogoutUseCase
import es.situm.integration.CapacityControlInput
import es.situm.integration.CapacityControlInput.updateRealtimeData
import es.situm.sdk.SitumSdk
import es.situm.sdk.configuration.network.NetworkOptions
import es.situm.sdk.error.Error
import es.situm.sdk.location.LocationListener
import es.situm.sdk.location.LocationRequest
import es.situm.sdk.location.LocationStatus
import es.situm.sdk.location.OutdoorLocationOptions
import es.situm.sdk.model.cartography.BuildingInfo
import es.situm.sdk.model.location.Location
import es.situm.sdk.model.realtime.RealTimeData
import es.situm.sdk.realtime.RealTimeListener
import es.situm.sdk.utils.Handler
import java.util.*

class DebugViewModel(val logoutUseCase: LogoutUseCase) : ViewModel() {

    private val selectedCacheStrategy = MutableLiveData<NetworkOptions.CacheStrategy>()
    private val selectedBuildingDetectorMode =
        MutableLiveData<OutdoorLocationOptions.BuildingDetector>()

    val buildingDetectorModes = MutableLiveData<Array<OutdoorLocationOptions.BuildingDetector>>()

    val useBLE = MutableLiveData<Boolean>()
    val useWifi = MutableLiveData<Boolean>()
    val useGPS = MutableLiveData<Boolean>()

    val isCapacityWarningActive = MutableLiveData<Boolean>()
    val notificationIntervalTime = MutableLiveData<String>()

    val log = MutableLiveData<StringBuilder>()
    val positioningStatus = MutableLiveData<String>()

    private var referenceTime: Long = 0
    private var firstPositiontimestamp = 0L

    // --- Events
    val checkPermissionsEvent = MutableLiveData<Event<Boolean>>()
    val capacityControlService = MutableLiveData<Event<Boolean>>()
    val completeLogoutEvent = MutableLiveData<Event<Boolean>>()


    init {
        log.value = java.lang.StringBuilder()
        positioningStatus.value = ""
        initDebuggerCallback()

        useBLE.value = true
        useWifi.value = true
        useGPS.value = false

        isCapacityWarningActive.value = CapacityControlConfigurationImpl.warningCapacityActive
        notificationIntervalTime.value =
            CapacityControlConfigurationImpl.breachUpdateInterval.toString()

        buildingDetectorModes.value = OutdoorLocationOptions.BuildingDetector.values()
            .also { it.sortBy { item -> item.toString() } }

        selectedCacheStrategy.value = NetworkOptions.CacheStrategy.TIMED_CACHE
    }

    private fun initDebuggerCallback() {
        DebugTool.timedEventLogger = object : DebugTool.TimedEventLogger {
            override fun registerEventTimestamp(event: String, timestamp: Long) {

                synchronized(this) {
                    if (referenceTime == 0L) referenceTime = timestamp
                    log.value?.append(event)
                        ?.append(": ")
                        ?.append("<b>${"%.2f".format((timestamp - referenceTime) / 1000f)}s</b>")
                        ?.append(" <br />")

                    log.postValue(log.value)
                }
            }
        }
    }

    fun checkPermissionsForPositioning() {
        checkPermissionsEvent.value = Event(true)
    }

    fun stopPositioning() {
        SitumSdk.locationManager().removeUpdates(locationListener)
        referenceTime = 0
        firstPositiontimestamp = 0
        capacityControlService.postValue(Event(false))
    }

    fun clearCache() {
        log.value = java.lang.StringBuilder()
        positioningStatus.value = ""
        SitumSdk.communicationManager().invalidateCache()
    }

    fun logout() {
        stopPositioning()
        logoutUseCase.invoke(LogoutUseCase.Params()) { result ->
            completeLogoutEvent.postValue(Event(true))
        }
    }

    fun updateSelectedBulidingDetector(position: Int) {
        selectedBuildingDetectorMode.value = buildingDetectorModes.value?.get(position)
            ?: OutdoorLocationOptions.BuildingDetector.GPS_PROXIMITY
    }

    fun startPositioning() {
        log.value = java.lang.StringBuilder()
        firstPositiontimestamp = 0

        val locationRequest = LocationRequest.Builder()
        locationRequest.useForegroundService(true)
        locationRequest.autoEnableBleDuringPositioning(false)

        //Set buildingDetetor mode
        val outdoorLocationOption = OutdoorLocationOptions.Builder()
        selectedBuildingDetectorMode.value?.let { outdoorLocationOption.buildingDetector(it) }
        outdoorLocationOption.scansBasedDetectorAlwaysOn(true)

        outdoorLocationOption.enableOutdoorPositions(true)
        outdoorLocationOption.averageSnrThreshold(40F)
        locationRequest.outdoorLocationOptions(outdoorLocationOption.build())

        //Set positioning mode
        locationRequest.useBle(useBLE.value ?: false)
        locationRequest.useGps(useGPS.value ?: false)
        locationRequest.useWifi(useWifi.value ?: false)
        locationRequest.useBatterySaver(false)

        isCapacityWarningActive.value?.let {
            CapacityControlConfigurationImpl.warningCapacityActive = it
        }

        notificationIntervalTime.value?.toLongOrNull()?.let {
            CapacityControlConfigurationImpl.breachUpdateInterval = it
        }

        DebugTool.registerTimedEvent("Warning notifications: ${CapacityControlConfigurationImpl.warningCapacityActive}")
        DebugTool.registerTimedEvent("Breach update interval: ${CapacityControlConfigurationImpl.breachUpdateInterval}")

        SitumSdk.locationManager().requestLocationUpdates(locationRequest.build(), locationListener)
        capacityControlService.postValue(Event(true))
    }

    private var oldLocation: Location? = null

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            if (firstPositiontimestamp == 0L && location.buildingIdentifier != "-1") {
                firstPositiontimestamp = Date().time
                DebugTool.registerTimedEvent("First Position - ${location.buildingIdentifier}")
            }
            positioningStatus.postValue("${location.buildingIdentifier} - ${location.cartesianCoordinate} , ${location.coordinate}, accuracy: ${location.accuracy} ")

            handleRealtime(location, oldLocation)

            oldLocation = location

            CapacityControlInput.updateLocation(location)

        }

        override fun onStatusChanged(status: LocationStatus) {
            positioningStatus.postValue(status.name)
            DebugTool.registerTimedEvent(status.name)
        }

        override fun onError(error: Error) {
            DebugTool.registerTimedEvent(error.toString())
            positioningStatus.postValue("ERROR")
            CapacityControlInput.updateLocation(null)


        }
    }

    private val rtManager = SitumRealtimeManager.getInstance()

    private fun handleRealtime(newLocation: Location, oldLocation: Location?) {
        if (newLocation.buildingIdentifier.equals("-1", ignoreCase = true)) {
            rtManager.stop()
        } else {
            if (isNewBuildingDifferent(oldLocation, newLocation)) {
                SitumSdk.communicationManager().fetchBuildingInfo(
                    newLocation.buildingIdentifier,
                    object : Handler<BuildingInfo> {
                        override fun onSuccess(buildingInfo: BuildingInfo) {
                            rtManager.stop()
                            rtManager.start(buildingInfo.building, object : RealTimeListener {
                                override fun onUserLocations(realTimeData: RealTimeData) {
                                    updateRealtimeData(realTimeData)
                                }

                                override fun onError(error: Error) {
                                    updateRealtimeData(null)
                                }
                            })
                        }

                        override fun onFailure(error: Error) {
                            updateRealtimeData(null)
                        }
                    })
            }
        }
    }

    private fun isNewBuildingDifferent(oldLocation: Location?, newLocation: Location): Boolean {
        return if (oldLocation == null || oldLocation.buildingIdentifier.equals(
                "-1",
                ignoreCase = true
            )
        ) {
            true
        } else {
            !oldLocation.buildingIdentifier.equals(
                newLocation.buildingIdentifier,
                ignoreCase = true
            )
        }
    }
}