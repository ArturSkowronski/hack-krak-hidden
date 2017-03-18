package tech.kolektiv.hiddengame.audio;

import rx.functions.Action0;

public abstract class AudioHandler implements AudioInterface {

    protected AudioCallback callback;
    protected Action0 onEnd;

    public void setCallback(AudioCallback callback) {
        this.callback = callback;
    }

    public void setOnEndAction(Action0 onEnd) {
        this.onEnd = onEnd;
    }
}
