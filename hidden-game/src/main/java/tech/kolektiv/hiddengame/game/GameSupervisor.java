package tech.kolektiv.hiddengame.game;

import tech.kolektiv.newton.Scene;

/**
 * Created by arturskowronski on 21/06/16.
 */
public interface GameSupervisor {
    void addScene(String id, Scene scene);
    void start(String id);
    void end();
    void reset();
    void transit(String... id);
}
