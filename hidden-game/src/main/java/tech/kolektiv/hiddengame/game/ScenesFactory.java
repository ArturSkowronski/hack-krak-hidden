package tech.kolektiv.hiddengame.game;

import javax.inject.Inject;

import rx.functions.Action1;
import tech.kolektiv.newton.Scene;
import tech.kolektiv.newton_location.LocationSupervisor;
import tech.kolektiv.newton_location.SceneLocation;

/**
 * Created by arturskowronski on 03/07/16.
 */
public class ScenesFactory {

    @Inject
    LocationSupervisor locationSupervisor;

    public ScenesFactory(LocationSupervisor locationSupervisor) {
        this.locationSupervisor = locationSupervisor;
    }

    public Scene locationScene(double lat, double lng, float range, Action1<String> action0){
        return new SceneLocation.Builder()
            .placeDiscoveredAction(action0)
            .build(locationSupervisor, range, lat, lng);
    }
}
