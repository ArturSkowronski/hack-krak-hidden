package tech.kolektiv.newton_location;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;
import java.util.List;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.functions.Action1;


/**
 * Created by arturskowronski on 03/07/16.
 */
public class LocationSupervisor {

    Observable<Location> updatedLocation;
    List<LocationPlace> locationPlaces = new ArrayList<>();

    LocationRequest request;

    public LocationSupervisor(Context context) {
        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(context);

        request = LocationRequest.create() //standard GMS LocationRequest
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(100);

        updatedLocation = locationProvider.getUpdatedLocation(request);
        updatedLocation.subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
                Log.d("SDM_DEBUG", "LOCATION_update:" + location.getLatitude() + "," + location.getLongitude());
                for (LocationPlace locationPlace : locationPlaces) {
                    Action1<Location> placeDiscovered = locationPlace.getPlaceDiscovered();
                    if(placeDiscovered != null) placeDiscovered.call(location);
                }
            }
        });
    }

    public void addPlace(LocationPlace listener){
        locationPlaces.add(listener);
    }


}
