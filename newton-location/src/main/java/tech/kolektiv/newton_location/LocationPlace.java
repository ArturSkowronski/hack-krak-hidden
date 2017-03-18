package tech.kolektiv.newton_location;

import android.location.Location;
import android.util.Log;

import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by arturskowronski on 24/06/16.
 */
public class LocationPlace {

    Location placeLocation;

    public Action1<Location> getPlaceDiscovered() {
        if (!active) return null;
        return placeDiscovered;
    }

    Action1<Location> placeDiscovered;
    float range = 5;

    public LocationPlace(double placeLat, double placeLng, float range) {
        placeLocation = new Location("");
        placeLocation.setLatitude(placeLat);
        placeLocation.setLongitude(placeLng);
        this.range = range;
    }

    boolean active;

    private float distanceFromLocation(Location newLocation) {
        return placeLocation.distanceTo(newLocation);
    }

    public void activate() {
        active = true;
    }

    public void disable() {
        active = false;
    }

    public void onPlaceDiscovered(final Action1<String> action1) {
        placeDiscovered = new Action1<Location>() {
            @Override
            public void call(Location location) {
                float v = distanceFromLocation(location);
                Log.d("SDM_Game", "DISTANCE FROM LOCATION:" + v + ":" + placeLocation.toString());
                if (v < range) action1.call("" + v);
            }
        };
    }
}
