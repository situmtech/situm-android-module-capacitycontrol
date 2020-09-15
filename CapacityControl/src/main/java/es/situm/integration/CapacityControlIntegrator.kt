package es.situm.integration

object CapacityControlIntegrator {

    private var configuration: CapacityControlConfiguration? = null
    private var listener: CapacityControlListener? = null

    /**
     * Use this method to set the desired configuration.
     * It is required to set it before starting the service.
     */
    @JvmStatic
    fun setCapacityControlConfiguration(configuration: CapacityControlConfiguration) {
        this.configuration = configuration
    }

    internal fun getConfiguration(): CapacityControlConfiguration {
        configuration?.let {
            return it
        }
                ?: throw CapacityControlIntegrationException("Missing CapacityControlConfiguration dependency")
    }

    /**
     * Use this method to set the desired listener to receive capacity control updates.
     * It is required to set it before starting the service.
     */
    @JvmStatic
    fun setCapacityControlListener(listener: CapacityControlListener) {
        this.listener = listener
    }

    internal fun getListener(): CapacityControlListener {
        listener?.let {
            return it
        } ?: throw CapacityControlIntegrationException("Missing CapacityControlListener dependency")
    }
}