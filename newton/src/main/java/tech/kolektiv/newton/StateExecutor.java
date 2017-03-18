package tech.kolektiv.newton;
import rx.Observable;
import tech.kolektiv.newton.devices.Place;

/**
 * Created by arturskowronski on 09/06/16.
 */
public interface StateExecutor {
    void onDeviceFound(Observable<Place> observable);
    void onPlaceLeft(Observable<Place> observable);
    void onDeviceLost(Observable<Place> observable);
    void onPlaceDiscovered(Observable<Place> observable);
}
