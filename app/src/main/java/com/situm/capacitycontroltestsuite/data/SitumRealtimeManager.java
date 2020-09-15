package com.situm.capacitycontroltestsuite.data;

import android.util.Log;

import androidx.annotation.VisibleForTesting;

import es.situm.sdk.SitumSdk;
import es.situm.sdk.model.cartography.Building;
import es.situm.sdk.realtime.RealTimeListener;
import es.situm.sdk.realtime.RealTimeManager;
import es.situm.sdk.realtime.RealTimeRequest;

public class SitumRealtimeManager {
    private static final String TAG = SitumRealtimeManager.class.getSimpleName();

    private final RealTimeManager realtimeManager;

    private static SitumRealtimeManager INSTANCE;

    public static void init() {
        INSTANCE = new SitumRealtimeManager(
                SitumSdk.realtimeManager()
        );
    }

    @VisibleForTesting
    public SitumRealtimeManager(RealTimeManager realtimeManager) {
        this.realtimeManager = realtimeManager;
    }

    public static SitumRealtimeManager getInstance() {
        return INSTANCE;
    }

    public void start(Building building, RealTimeListener listener) {
        RealTimeRequest.Builder builder = new RealTimeRequest.Builder();
        builder.building(building);
        builder.pollTimeMs(10000);

        Log.v(TAG, "start: ");
        realtimeManager.requestRealTimeUpdates(builder.build(), listener);
    }

    public void stop() {
        Log.v(TAG, "stop: ");
        realtimeManager.removeRealTimeUpdates();
    }
}
