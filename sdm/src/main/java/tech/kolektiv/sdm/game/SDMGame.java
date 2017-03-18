package tech.kolektiv.sdm.game;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.functions.Action1;
import tech.kolektiv.sdm.R;
import tech.kolektiv.sdm.view.PlayerVM;
import tech.kolektiv.sdm.view.PlayerVMPresenter;
import tech.kolektiv.sdm.view.SoundVM;
import tech.kolektiv.sdm.view.SoundVMProvider;
import tech.kolektiv.hiddengame.game.Events;
import tech.kolektiv.hiddengame.game.GameEngine;
import tech.kolektiv.hiddengame.game.ScenesFactory;

/**
 * Created by arturskowronski on 21/06/16.
 */
public class SDMGame extends GameEngine {

    private final ArrayList<String> sceneList;
    String TAG = "SDM_GAME";

    @Inject
    ScenesFactory scenesFactory;

    @Inject
    PlayerVMPresenter presenter;

    @Inject
    SoundVMProvider provider;

    public SDMGame(ScenesFactory scenesFactory, PlayerVMPresenter presenter, SoundVMProvider provider) {
        this.scenesFactory = scenesFactory;
        this.presenter = presenter;
        this.provider = provider;
        this.sceneList = new ArrayList<>();
        this.sceneList.add(Scenes.SDM_1);
        this.sceneList.add(Scenes.SDM_2);
        this.sceneList.add(Scenes.SDM_3);
        this.sceneList.add(Scenes.SDM_4);
        this.sceneList.add(Scenes.SDM_5);
        this.sceneList.add(Scenes.SDM_6);
        this.sceneList.add(Scenes.SDM_7);
        this.sceneList.add(Scenes.SDM_8);
        this.sceneList.add(Scenes.SDM_9);
        this.sceneList.add(Scenes.SDM_10);
    }

    @Override
    public void start(String id){
        super.start(id);
        Log.d(TAG, id);
        trigger(id);
    }

    public void trigger(int id){
        try {
            String s = sceneList.get(id);
            transit(s);
            sceneMap.get(s).force();
        } catch (Exception ignored){

        }
    }


    public void trigger(String id){
        try {
            transit(id);
            sceneMap.get(id).force();
        } catch (Exception ignored){

        }
    }

    public void initializeGame() {
        addScene(Scenes.SDM_1, scenesFactory.locationScene(50.05494, 19.93823, 10,
                pointCreation(1, 50.05539, 19.93654, R.raw.p1, Scenes.SDM_1, Scenes.SDM_2)));

        addScene(Scenes.SDM_2, scenesFactory.locationScene(50.05539, 19.93654, 20,
                pointCreation(2, 50.054683, 19.934850, R.raw.p2, Scenes.SDM_2, Scenes.SDM_2_PLUS)));

        addScene(Scenes.SDM_2_PLUS, scenesFactory.locationScene(50.054683, 19.934850, 15,
                pointCreation(3, 50.054513,19.935499, R.raw.p2p, Scenes.SDM_2_PLUS, Scenes.SDM_3)));

        addScene(Scenes.SDM_3, scenesFactory.locationScene(50.054513,19.935499, 12,
                pointCreation(4, 50.053392, 19.933819, R.raw.p3, Scenes.SDM_3, Scenes.SDM_4)));

        addScene(Scenes.SDM_4, scenesFactory.locationScene(50.053392, 19.933819, 12,
                pointCreation(5, 50.052939, 19.934286, R.raw.p4, Scenes.SDM_4, Scenes.SDM_5)));

        addScene(Scenes.SDM_5, scenesFactory.locationScene(50.052939, 19.934286, 10,
                pointCreation(6, 50.053883,19.937566, R.raw.p5, Scenes.SDM_5, Scenes.SDM_6)));

        addScene(Scenes.SDM_6, scenesFactory.locationScene(50.053883,19.937566, 15,
                pointCreation(7, 50.054738, 19.938366, R.raw.p6, Scenes.SDM_6, Scenes.SDM_7)));

        addScene(Scenes.SDM_7, scenesFactory.locationScene(50.054738, 19.938366, 25,
                pointCreation(8, 50.057675, 19.938102, R.raw.p7, Scenes.SDM_7, Scenes.SDM_8)));

        addScene(Scenes.SDM_8, scenesFactory.locationScene(50.057675, 19.938102, 20,
                pointCreation(9, 50.057974, 19.936716, R.raw.p8, Scenes.SDM_8, Scenes.SDM_9)));

        addScene(Scenes.SDM_9, scenesFactory.locationScene(50.057974, 19.936716, 10,
                pointCreation(10, 50.059087, 19.934640, R.raw.p9, Scenes.SDM_9, Scenes.SDM_10)));

        addScene(Scenes.SDM_10, scenesFactory.locationScene(50.059087, 19.934640, 25,
                pointCreation(11, 50.059087, 19.934640, R.raw.p10, Scenes.SDM_10)));


    }

    @NonNull
    private Action1<String> pointCreation(int point, double lat, double lng, int sound, String currentScene, String nextScene) {
        return (distance) -> {
            Log.d(TAG, "Device " + point + " Found");
            presenter.send(new PlayerVM("Place " + point, distance, new LatLng(lat, lng)));
            provider.send(new SoundVM(currentScene, sound, 0));
            transit(nextScene);
        };
    }


    @NonNull
    private Action1<String> pointCreation(int point, double lat, double lng, int sound, String currentScene) {
        return (distance) -> {
            Log.d(TAG, "Device " + point + " Found");
            presenter.send(new PlayerVM("Place " + point, distance, new LatLng(lat, lng)));
            provider.send(new SoundVM(currentScene, sound, Events.END));
            end();
        };
    }

    @Override
    public void end(){
        super.end();
    }

}


