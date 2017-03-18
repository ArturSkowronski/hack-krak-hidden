package tech.kolektiv.hiddengame.game;

import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action0;
import tech.kolektiv.newton.Scene;

/**
 * Created by arturskowronski on 21/06/16.
 */
public abstract class GameEngine implements GameSupervisor {

    protected Map<String, Scene> sceneMap = new HashMap<>();

    private Action0 onEnd;

    public void onEnd(Action0 onEnd) {
        this.onEnd = onEnd;
    }

    public void disableAll(){
        for (Map.Entry<String, Scene> entry : sceneMap.entrySet())
        {
            entry.getValue().disable();
        }
    }

    @Override
    public void addScene(String id, Scene scene) {
        sceneMap.put(id, scene);
    }

    @Override
    public void start(String id) {
        disableAll();
        Scene currentScene = sceneMap.get(id);
        currentScene.activate();
    }

    @Override
    public void end() {
        disableAll();
        onEnd.call();

    }

    @Override
    public void reset() {
        disableAll();
    }

    @Override
    public void transit(String... scenesToActive) {
        disableAll();

        List<String> scenes = Arrays.asList(scenesToActive);
        for (String sceneId : scenes) {
            Scene currentScene = sceneMap.get(sceneId);
            if(currentScene == null) return;
            Log.d("SDM_Game", "transit to:" + sceneId);
            currentScene.activate();
        }
    }


    public void onDestroy() {

    }
}
