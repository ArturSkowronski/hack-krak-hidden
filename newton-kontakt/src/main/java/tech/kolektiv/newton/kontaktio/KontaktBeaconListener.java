package tech.kolektiv.newton.kontaktio;

import com.kontakt.sdk.android.ble.discovery.EventType;

import java.util.HashMap;
import java.util.Map;

import tech.kolektiv.newton.EventListenerSInterface;
import tech.kolektiv.newton.PlaceListener;

/**
 * Created by arturskowronski on 09/06/16.
 */
public class KontaktBeaconListener implements PlaceListener {

    private String namespace;
    private boolean active;

    public Map<EventType, EventListenerSInterface> getEventListeners() {
        return eventListeners;
    }

    Map<EventType, EventListenerSInterface> eventListeners = new HashMap<>();

    public boolean isActive() {
        return active;
    }

    public void activate(){
        active = true;
    }

    public void disable(){
        active = false;
    }

    public KontaktBeaconListener(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public void onDeviceFound(EventListenerSInterface action) {
        eventListeners.put(EventType.DEVICE_DISCOVERED, action);
        eventListeners.put(EventType.DEVICES_UPDATE, action);
    }

    @Override
    public void onPlaceLeft(EventListenerSInterface action) {
        eventListeners.put(EventType.SPACE_ABANDONED, action);
    }

    @Override
    public void onDeviceLost(EventListenerSInterface action) {
        eventListeners.put(EventType.DEVICE_LOST, action);
    }

    @Override
    public void onPlaceDiscovered(EventListenerSInterface action) {
        eventListeners.put(EventType.SPACE_ENTERED, action);
    }

    public boolean isInPlace(String namespace){
        return namespace.equals(this.namespace);
    }

}
