package es.situm.integration

import android.app.Notification

interface CapacityControlConfiguration {

    /**
     * The interval in seconds a breach should be notified. If a breach is notified, it will not be
     * notified again for the duration of this interval. New cases will be notified immediately.
     */
    fun breachUpdateInterval(): Long

    /**
     * CapacityWarnings will be notified when the current capactiy is reaching it's limit (ex: 80% of max capacity)
     * If false, BREACH_WARNING will never be calculated or notified (it will be treated as a BREACH_NONE). If true they will be calculated and notified.
     */
    fun warningCapacityActive(): Boolean

    /**
     * Notification that will be shown while the service is running, this is shown because the service needs to run in background.
     * Right now there will be two notifications, the Situm SDK positioning notification and this one, this may be a problem so the interface may change
     * to allow us to remove the notification. To do this this module needs to stop using a service.
     * If not specified (null), the module will produce it's own default notification.
     */
    fun capacityControlForegroundNotification(): Notification?
}