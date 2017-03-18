package tech.kolektiv.newton.proximi;

import android.app.Activity;

import io.proximi.proximiiolibrary.Proximiio;
import io.proximi.proximiiolibrary.ProximiioBeacon;
import io.proximi.proximiiolibrary.ProximiioFactory;
import io.proximi.proximiiolibrary.ProximiioGeofence;
import io.proximi.proximiiolibrary.ProximiioListener;
import tech.kolektiv.newton.EventListenerSInterface;
import tech.kolektiv.newton.PlaceListener;
import tech.kolektiv.newton.rx.RxBus;

/**
 * Created by arturskowronski on 09/06/16.
 */
public class ProximoBeaconRX implements PlaceListener {

    public RxBus getRxBus() {
        return _rxBus;
    }

    private RxBus _rxBus;

    private final String namespace;

    private Proximiio proximiio;
    private ProximiioListener listener;

    String PROXIMI_LOGIN = "artur@kolektiv.tech";
    String PROXIMI_PASS = "escenic";

    public ProximoBeaconRX(Activity activity, String namespace) {
        this.namespace = namespace;
        proximiio = ProximiioFactory.getProximiio(activity, activity);
        proximiio.setLogin(PROXIMI_LOGIN, PROXIMI_PASS);
    }

    @Override
    public void onDeviceFound(final EventListenerSInterface action) {
        listener = new ProximiioListener() {
            public void foundBeacon(ProximiioBeacon beacon, boolean registered) {
                if(beacon.getNamespace().equals(namespace)){
                    ProximioBeaconPlace proximioBeaconPlace = new ProximioBeaconPlace();
                    action.doAction(proximioBeaconPlace);
                }
            }
        };
        proximiio.addListener(listener);
    }

    @Override
    public void onPlaceLeft(final EventListenerSInterface action) {
        listener = new ProximiioListener() {
            public void geofenceExit(ProximiioGeofence geofence) {
                if(geofence.getName().equals(namespace)){
                    ProximioBeaconPlace proximioBeaconPlace = new ProximioBeaconPlace();
                    action.doAction(proximioBeaconPlace);
                }
            }
        };
    }

    @Override
    public void onDeviceLost(final EventListenerSInterface action) {
        listener = new ProximiioListener() {
            public void lostBeacon(ProximiioBeacon beacon, boolean registered) {
                if(beacon.getNamespace().equals(namespace)){
                    ProximioBeaconPlace proximioBeaconPlace = new ProximioBeaconPlace();
                    action.doAction(proximioBeaconPlace);
                }
            }
        };
        proximiio.addListener(listener);
    }

    @Override
    public void onPlaceDiscovered(final EventListenerSInterface action) {
        listener = new ProximiioListener() {
            public void geofenceEnter(ProximiioGeofence geofence) {
                if(geofence.getName().equals(namespace)){
                    ProximioBeaconPlace proximioBeaconPlace = new ProximioBeaconPlace();
                    _rxBus.send(proximioBeaconPlace);
                    action.doAction(proximioBeaconPlace);
                }
            }
        };

        proximiio.addListener(listener);
    }
}
