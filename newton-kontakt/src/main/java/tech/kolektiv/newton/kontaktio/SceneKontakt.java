package tech.kolektiv.newton.kontaktio;

import tech.kolektiv.newton.EventListenerSInterface;
import tech.kolektiv.newton.Scene;
import tech.kolektiv.newton.devices.Place;

/**
 * Created by arturskowronski on 21/06/16.
 */
public class SceneKontakt implements Scene {

    private KontaktBeaconListener placeRx;
    private EventListenerSInterface placeDiscoveredAction;

    public SceneKontakt(KontaktBeaconListener placeRx){
        this.placeRx = placeRx;
    }

    public void activate(){
        placeRx.activate();
    }

    public void disable(){
        placeRx.disable();
    }

    @Override
    public void force() {
        placeDiscoveredAction.doAction(new Place() {
            @Override
            public String placeId() {
                return "";
            }
        });
    }

    public void setPlaceDiscoveredAction(EventListenerSInterface placeDiscoveredAction) {
        this.placeDiscoveredAction = placeDiscoveredAction;
    }

    public static class Builder {

        EventListenerSInterface placeLeftAction;
        EventListenerSInterface placeDiscoveredAction;
        EventListenerSInterface deviceLostAction;
        EventListenerSInterface deviceFoundAction;

        public Builder() {
        }

        private Builder deviceLostAction(EventListenerSInterface action0){
            deviceLostAction = action0;
            return this;
        }

        private Builder deviceFoundAction(EventListenerSInterface action0){
            deviceFoundAction = action0;
            return this;
        }

        public Builder placeDiscoveredAction(EventListenerSInterface action0){
            placeDiscoveredAction = action0;
            return this;
        }

        private Builder placeLeftAction(EventListenerSInterface action0){
            placeLeftAction = action0;
            return this;
        }

        public SceneKontakt build(KontaktSupervisor supervisor, String namespace){
            KontaktBeaconListener place = new KontaktBeaconListener(namespace);
            if(placeDiscoveredAction != null) place.onPlaceDiscovered(placeDiscoveredAction);
            supervisor.addPlace(place);
            SceneKontakt sceneKontakt = new SceneKontakt(place);
            sceneKontakt.setPlaceDiscoveredAction(placeDiscoveredAction);
            return sceneKontakt;
        }
    }

}
