package tech.kolektiv.newton.kontaktio.internals;

import tech.kolektiv.newton.devices.Place;

/**
 * Created by arturskowronski on 19/06/16.
 */
public class KontaktBeaconPlace implements Place {

    private String placeId;

    public KontaktBeaconPlace(String placeId) {
        this.placeId = placeId;
    }

    @Override
    public String placeId() {
        return placeId;
    }
}
