package tech.kolektiv.sdm.view;

/**
 * Created by arturskowronski on 03/07/16.
 */
public class SoundVM {
    public int getStory() {
        return story;
    }

    public int getInstruction() {
        return instruction;
    }

    private String state;
    int story;
    int instruction;
    private String event;


    public SoundVM(String state, int story, String event) {
        this.state = state;
        this.story = story;
        this.instruction = 0;
        this.event = event;
    }

    public SoundVM(String state, int story, int instruction) {
        this.state = state;
        this.story = story;
        this.instruction = instruction;
        this.event = null;
    }

    public String getEvent() {
        return event;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
