package tech.kolektiv.newton;

/**
 * Created by arturskowronski on 19/06/16.
 */
public interface PlaceListener {
    void onDeviceFound(final EventListenerSInterface action);
    void onPlaceLeft(final EventListenerSInterface action);
    void onDeviceLost(final EventListenerSInterface action);
    void onPlaceDiscovered(final EventListenerSInterface action);
}
