package tech.kolektiv.forum.view;

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

    int story;
    int instruction;
    private String event;


    public SoundVM(int story, String event) {
        this.story = story;
        this.instruction = 0;
        this.event = event;
    }

    public SoundVM(int story, int instruction) {
        this.story = story;
        this.instruction = instruction;
        this.event = null;
    }

    public String getEvent() {
        return event;
    }
}
