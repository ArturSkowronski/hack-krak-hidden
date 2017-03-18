package tech.kolektiv.newton.kontaktio;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.kontakt.sdk.android.ble.device.EddystoneDevice;
import com.kontakt.sdk.android.ble.discovery.BluetoothDeviceEvent;
import com.kontakt.sdk.android.ble.discovery.EventType;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.common.profile.RemoteBluetoothDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tech.kolektiv.newton.EventListenerSInterface;
import tech.kolektiv.newton.devices.Place;
import tech.kolektiv.newton.kontaktio.internals.KontaktBeaconPlace;
import tech.kolektiv.newton.kontaktio.internals.KontaktManager;

/**
 * Created by arturskowronski on 03/07/16.
 */
public class KontaktSupervisor {

    List<KontaktBeaconListener> sceneListeners = new ArrayList<>();

    public KontaktSupervisor(Context context) {
        KontaktManager kontaktManager = new KontaktManager(context);
        kontaktManager.initialize(createListener());
    }

    public void addPlace(KontaktBeaconListener listener){
        sceneListeners.add(listener);
    }

    public void doAction(String namespace, BluetoothDeviceEvent event, Map<EventType, EventListenerSInterface> eventListeners) {
        Place place = new KontaktBeaconPlace(namespace);
        EventListenerSInterface eventListenerSInterface = eventListeners.get(event.getEventType());
        if (eventListenerSInterface != null) eventListenerSInterface.doAction(place);
    }

    private ProximityManager.ProximityListener createListener() {

        return new ProximityManager.ProximityListener() {
            @Override
            public void onScanStart() {
                Log.d("SDM_DEBUG", "onScanStart");

            }

            @Override
            public void onScanStop() {
                Log.d("SDM_DEBUG", "onScanStop");

            }

            @Override
            public void onEvent(BluetoothDeviceEvent event) {
                List<? extends RemoteBluetoothDevice> deviceList = event.getDeviceList();

                Set<String> activeNamespaces = getActiveNamespaces(deviceList);

                for (String namespace : activeNamespaces) {

                    for (KontaktBeaconListener key : sceneListeners)
                    {
                        if(key.isInPlace(namespace) && key.isActive()){
                            doAction(namespace, event, key.getEventListeners());
                        }
                    }
                }
            }
        };
    }

    @NonNull
    private Set<String> getActiveNamespaces(List<? extends RemoteBluetoothDevice> deviceList) {
        Set<String> activeNamespaces = new HashSet<>();

        for (RemoteBluetoothDevice remoteBluetoothDevice : deviceList) {
            if(remoteBluetoothDevice instanceof EddystoneDevice){
                EddystoneDevice eddystoneDevice = (EddystoneDevice) remoteBluetoothDevice;
                activeNamespaces.add(eddystoneDevice.getNamespaceId());
            }
        }
        return activeNamespaces;
    }


}
