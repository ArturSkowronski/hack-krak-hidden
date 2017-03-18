package tech.kolektiv.forum.view;

/**
 * Created by arturskowronski on 03/07/16.
 */
public class PlayerVM {
    String headerText;
    private String distance;


    public PlayerVM(String headerText) {
        this.headerText = headerText;
    }

    public PlayerVM(String headerText, String distance) {
        this.headerText = headerText;
        this.distance = distance;
    }

    public String getHeaderText() {
        return headerText;
    }

    public String getDistance() {
        return distance;
    }
}
