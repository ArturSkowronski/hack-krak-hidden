package tech.kolektiv.hiddengame.audio;

import android.content.Context;

public interface AudioInterface {

    void stop();
    void seekTo(int to);
    void start(Context aud, int id);
    void play();
    void resume();
    void pause();
    void pauseByDevice();
    long getCurrentPosition();
    long getDuration(String file);
}
