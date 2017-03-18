package tech.kolektiv.newton;

import rx.Observable;
import tech.kolektiv.newton.devices.Place;
import tech.kolektiv.newton.rx.RxBus;

public abstract class ReactivePlace {

    protected RxBus deviceFoundBus = new RxBus();
    protected RxBus placeLeftBus = new RxBus();
    protected RxBus deviceLostBus = new RxBus();
    protected RxBus placeDiscoveredBus = new RxBus();

    protected boolean active = false;

    public Observable<Place> deviceFoundEvents() {
        return deviceFoundBus.toObserverable();
    }

    public Observable<Place> placeLeftEvents() {
        return placeLeftBus.toObserverable();
    }

    public Observable<Place> deviceLostEvents() {
        return deviceLostBus.toObserverable();
    }

    public Observable<Place> placeDiscoveredEvents() {
        return placeDiscoveredBus.toObserverable();
    }

    public void activate(){
        active = true;
    }

    public void disable(){
        active = false;
    }

    public void observeAll(StateExecutor observator) {
        observator.onDeviceFound(deviceFoundBus.toObserverable());
        observator.onDeviceLost(deviceLostBus.toObserverable());
        observator.onPlaceDiscovered(placeDiscoveredBus.toObserverable());
        observator.onPlaceLeft(placeLeftBus.toObserverable());
    }
}