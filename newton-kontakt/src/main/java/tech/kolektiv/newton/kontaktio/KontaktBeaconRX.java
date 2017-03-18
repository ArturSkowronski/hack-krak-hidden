package tech.kolektiv.newton.kontaktio;

import android.content.Context;
import android.util.Log;

import com.kontakt.sdk.android.ble.discovery.BluetoothDeviceEvent;
import com.kontakt.sdk.android.ble.discovery.eddystone.EddystoneDeviceEvent;
import com.kontakt.sdk.android.ble.manager.ProximityManager;

import tech.kolektiv.newton.ReactivePlace;
import tech.kolektiv.newton.kontaktio.internals.KontaktBeaconPlace;
import tech.kolektiv.newton.kontaktio.internals.KontaktManager;

/**
 * Created by arturskowronski on 09/06/16.
 */
public class KontaktBeaconRX extends ReactivePlace {

    private final String namespace;

    String INFO_PREFIX = null;
    String DEBUG_PREFIX = null;

    public KontaktBeaconRX(Context context, String namespace) {
        this.namespace = namespace;
        KontaktManager kontaktManager = new KontaktManager(context, namespace);
        kontaktManager.initialize(createListener());
    }

    private void setInfoPrefix(String tag){
        INFO_PREFIX = tag;
    }
    private void setDebugPrefix(String tag){
        DEBUG_PREFIX = tag;
    }

    public static class Builder {

        String infoPrefix = null;
        String debugPrefix = null;

        public KontaktBeaconRX.Builder withInfoPrefix(String tag){
            infoPrefix = tag;
            return this;
        }

        public KontaktBeaconRX.Builder withDebugPrefix(String tag){
            debugPrefix = tag;
            return this;
        }

        KontaktBeaconRX build(Context context, String namespace){
            KontaktBeaconRX kontaktBeaconRX = new KontaktBeaconRX(context, namespace);
            if(infoPrefix != null) kontaktBeaconRX.setInfoPrefix(infoPrefix);
            if(debugPrefix != null) kontaktBeaconRX.setDebugPrefix(debugPrefix);
            return kontaktBeaconRX;
        }
    }

    private ProximityManager.ProximityListener createListener(){

        return new ProximityManager.ProximityListener() {
            @Override
            public void onScanStart() {
                Log.d("SDM_Game", namespace + " onScanStart");

            }

            @Override
            public void onScanStop() {
                Log.d("SDM_Game", namespace + " onScanStop");
            }

            @Override
            public void onEvent(BluetoothDeviceEvent event) {
                if (!active) return;
                EddystoneDeviceEvent eddystoneEvent = (EddystoneDeviceEvent) event;

                switch (eddystoneEvent.getEventType()) {

                    case SPACE_ENTERED:
                        Log.d("SDM_Game", namespace + " SPACE_ENTERED");
                        placeDiscoveredBus.send(new KontaktBeaconPlace(namespace));
                        break;

                    case DEVICE_DISCOVERED:
                        Log.d("SDM_Game", namespace + " DEVICE_DISCOVERED");
                        deviceFoundBus.send(new KontaktBeaconPlace(namespace));
                        break;

                    case SPACE_ABANDONED:
                        Log.d("SDM_Game", namespace + " SPACE_ABANDONED");
                        placeLeftBus.send(new KontaktBeaconPlace(namespace));
                        break;

                    case DEVICES_UPDATE:
                        Log.d("SDM_Game", namespace + " DEVICES_UPDATE");
                        deviceFoundBus.send(new KontaktBeaconPlace(namespace));
                        break;

                    case DEVICE_LOST:
                        Log.d("SDM_Game", namespace + " DEVICE_LOST");
                        deviceLostBus.send(new KontaktBeaconPlace(namespace));
                        break;

                    default:
                        throw new IllegalStateException("This event should never occur because it is not specified in ScanContext: " + eddystoneEvent.getEventType().name());
                }
            }
        };
    }


}
