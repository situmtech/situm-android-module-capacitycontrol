package com.situm.capacitycontroltestsuite.application

import android.app.Application
import com.situm.capacitycontroltestsuite.data.SitumRealtimeManager
import com.situm.capacitycontroltestsuite.integration.CapacityControlConfigurationImpl
import com.situm.capacitycontroltestsuite.integration.CapacityControlListenerImpl
import com.situm.capacitycontroltestsuite.presentation.presentationModule
import es.situm.integration.CapacityControlIntegrator
import es.situm.sdk.SitumSdk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class CapacityControlTestSuiteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CapacityControlTestSuiteApplication)
            modules(presentationModule)
        }
        SitumSdk.init(this)
        SitumSdk.configuration().allowInvalidSSLCertificate(true)
        SitumSdk.configuration().setCacheMaxAge(1, TimeUnit.DAYS)

        CapacityControlIntegrator.setCapacityControlConfiguration(CapacityControlConfigurationImpl)
        CapacityControlIntegrator.setCapacityControlListener(CapacityControlListenerImpl)

        SitumRealtimeManager.init()
    }
}