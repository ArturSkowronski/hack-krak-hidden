package tech.kolektiv.sdm.view;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by arturskowronski on 03/07/16.
 */
public class PlayerVM {
    String headerText;
    private String distance;

    public LatLng getLatLng() {
        return latLng;
    }

    private LatLng latLng;


    public PlayerVM(String headerText) {
        this.headerText = headerText;
    }

    public PlayerVM(String headerText, String distance, LatLng latLng) {
        this.headerText = headerText;
        this.distance = distance;
        this.latLng = latLng;
    }

    public String getHeaderText() {
        return headerText;
    }

    public String getDistance() {
        return distance;
    }
}
