package tech.kolektiv.newton_location;

import rx.functions.Action0;
import rx.functions.Action1;
import tech.kolektiv.newton.Scene;

/**
 * Created by arturskowronski on 21/06/16.
 */
public class SceneLocation implements Scene {
    protected LocationPlace place;

    Action1<String> placeDiscoveredAction;
    private SceneLocation(LocationPlace place) {
        this.place = place;
    }

    public void activate(){
        place.activate();
    }

    public void disable(){
        place.disable();
    }

    public void setPlaceDiscoveredAction(Action1<String> placeDiscoveredAction) {
        this.placeDiscoveredAction = placeDiscoveredAction;
    }

    @Override
    public void force() {
        activate();
        placeDiscoveredAction.call("");
    }

    public static class Builder {

        Action0 placeLeftAction;
        Action1<String> placeDiscoveredAction;
        Action0 deviceLostAction;
        Action0 deviceFoundAction;

        public Builder() {
        }

        private Builder deviceLostAction(Action0 action0){
            deviceLostAction = action0;
            return this;
        }

        private Builder deviceFoundAction(Action0 action0){
            deviceFoundAction = action0;
            return this;
        }

        public Builder placeDiscoveredAction(Action1<String> action0){
            placeDiscoveredAction = action0;
            return this;
        }

        private Builder placeLeftAction(Action0 action0){
            placeLeftAction = action0;
            return this;
        }

        public SceneLocation build(LocationSupervisor supervisor, float range, double lat, double lng){
            LocationPlace place = new LocationPlace(lat, lng, range);
            if(placeDiscoveredAction!=null) place.onPlaceDiscovered(placeDiscoveredAction);
            supervisor.addPlace(place);
            SceneLocation sceneLocation = new SceneLocation(place);
            sceneLocation.setPlaceDiscoveredAction(placeDiscoveredAction);
            return sceneLocation;
        }
    }
}
