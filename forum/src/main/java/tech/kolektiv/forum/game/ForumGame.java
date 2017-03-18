package tech.kolektiv.forum.game;

import android.util.Log;

import java.util.Arrays;

import javax.inject.Inject;

import tech.kolektiv.forum.R;
import tech.kolektiv.forum.view.PlayerVM;
import tech.kolektiv.forum.view.PlayerVMPresenter;
import tech.kolektiv.forum.view.SoundVM;
import tech.kolektiv.forum.view.SoundVMProvider;
import tech.kolektiv.hiddengame.game.Events;
import tech.kolektiv.hiddengame.game.GameEngine;
import tech.kolektiv.hiddengame.game.ScenesFactory;

import static tech.kolektiv.forum.game.Scenes.FORUM_1;
import static tech.kolektiv.forum.game.Scenes.FORUM_2;
import static tech.kolektiv.forum.game.Scenes.FORUM_3;
import static tech.kolektiv.forum.game.Scenes.FORUM_4;
import static tech.kolektiv.forum.game.Scenes.FORUM_5;

/**
 * Created by arturskowronski on 21/06/16.
 */
public class ForumGame extends GameEngine {

    @Inject
    ScenesFactory scenesFactory;

    @Inject
    PlayerVMPresenter presenter;

    @Inject
    SoundVMProvider provider;

    public ForumGame(ScenesFactory scenesFactory, PlayerVMPresenter presenter, SoundVMProvider provider) {
        this.scenesFactory = scenesFactory;
        this.presenter = presenter;
        this.provider = provider;
    }

    @Override
    public void start(String id){
        super.start(id);
        Log.d("SDM_Game", id);
        trigger(0);
    }

    public void trigger(int id){
        try {
            Scenes scenes = Arrays.asList(Scenes.values()).get(id);
            if (scenes != null) sceneMap.get(scenes.name()).force();
        } catch (Exception ignored){}
    }

    public void initializeGame() {
        addScene(FORUM_1.name(), scenesFactory.kontaktScene("c7826da6bc5b71e0893e", place -> {
            Log.d("SDM_Game", "Device 1 Found");
            presenter.send(new PlayerVM("Stacja 1"));
            provider.send(new SoundVM(R.raw.sound1, R.raw.sound1i));
            transit(FORUM_2.name());
        }));

        addScene(FORUM_2.name(), scenesFactory.locationScene(50.045389, 19.935958, 30, (distance) -> {
            Log.d("SDM_Game", "Device 2 Found");
            presenter.send(new PlayerVM("Stacja 2", distance));
            provider.send(new SoundVM(R.raw.sound2, R.raw.sound2i));
            transit(FORUM_3.name());
        }));

        addScene(FORUM_3.name(), scenesFactory.locationScene(50.045613, 19.936081, 10, (distance) -> {
            Log.d("SDM_Game", "Device 3 Found");
            presenter.send(new PlayerVM("Stacja 3", distance));
            provider.send(new SoundVM(R.raw.sound3, R.raw.sound3i));
            transit(FORUM_4.name());
        }));

        addScene(FORUM_4.name(), scenesFactory.locationScene(50.0457309, 19.9351548,  10, (distance) -> {
            Log.d("SDM_Game", "Device 4 Found");
            presenter.send(new PlayerVM("Stacja 4", distance));
            provider.send(new SoundVM(R.raw.sound4,  R.raw.sound4i));
            transit(FORUM_5.name());
        }));

        addScene(FORUM_5.name(), scenesFactory.locationScene(50.0447799, 19.9347368, 10, (distance) -> {
            Log.d("SDM_Game", "Device 5 Found");
            presenter.send(new PlayerVM("Stacja 5", distance));
            provider.send(new SoundVM(R.raw.sound5, Events.END));
            end();
        }));
    }

    @Override
    public void end(){
        super.end();
    }

}


