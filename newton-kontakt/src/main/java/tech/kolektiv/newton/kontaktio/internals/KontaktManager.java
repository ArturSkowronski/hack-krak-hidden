package tech.kolektiv.newton.kontaktio.internals;

import android.content.Context;
import android.support.annotation.NonNull;

import com.kontakt.sdk.android.ble.configuration.ActivityCheckConfiguration;
import com.kontakt.sdk.android.ble.configuration.ForceScanConfiguration;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.configuration.scan.EddystoneScanContext;
import com.kontakt.sdk.android.ble.configuration.scan.ScanContext;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.filter.eddystone.EddystoneFilters;
import com.kontakt.sdk.android.ble.filter.eddystone.UIDFilter;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerContract;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.manager.KontaktProximityManager;

import java.util.Collections;
import java.util.List;

/**
 * Created by arturskowronski on 20/06/16.
 */
public class KontaktManager {


    public ProximityManagerContract getProximityManager() {
        return proximityManager;
    }

    ProximityManagerContract proximityManager;
    ScanContext scanContext;


    public void initialize(final ProximityManager.ProximityListener listener) {
        proximityManager.initializeScan(scanContext, new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.attachListener(listener);
            }

            @Override
            public void onConnectionFailure() {

            }
        });
    }

    public KontaktManager(Context context, String namespace) {
        KontaktSDK.initialize(context);
        this.proximityManager = new KontaktProximityManager(context);
        List<UIDFilter> filterList = Collections.singletonList(EddystoneFilters.newNamespaceIdFilter(namespace));
        scanContext = createScanContext(new EddystoneScanContext.Builder()
                .setUIDFilters(filterList)
                .build());
    }

    public KontaktManager(Context context) {
        KontaktSDK.initialize(context);
        this.proximityManager = new KontaktProximityManager(context);
        List<UIDFilter> filterList = Collections.emptyList();
        scanContext = createScanContext(new EddystoneScanContext.Builder()
                .setUIDFilters(filterList)
                .build());
    }

    @NonNull
    private ScanContext createScanContext(EddystoneScanContext eddystoneScanContext) {
        return new ScanContext.Builder()
                .setScanPeriod(ScanPeriod.RANGING) // or for monitoring for 15 seconds scan and 10 seconds waiting:
                .setScanMode(ProximityManager.SCAN_MODE_BALANCED)
                .setActivityCheckConfiguration(ActivityCheckConfiguration.MINIMAL)
                .setForceScanConfiguration(ForceScanConfiguration.MINIMAL)
                .setEddystoneScanContext(eddystoneScanContext)
                .setForceScanConfiguration(ForceScanConfiguration.MINIMAL)
                .build();
    }

}
