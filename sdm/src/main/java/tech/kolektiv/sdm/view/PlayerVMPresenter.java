package tech.kolektiv.sdm.view;

import android.util.Log;

import rx.Observable;
import tech.kolektiv.hiddengame.utils.RxBus;

/**
 * Created by arturskowronski on 03/07/16.
 */
public class PlayerVMPresenter {

    protected RxBus<PlayerVM> presenter = new RxBus<>();

    public void send(PlayerVM object){
        Log.d("SDM_DEBUG", object.toString());
        presenter.send(object);
    }

    public Observable<PlayerVM> views(){
        return presenter.toObserverable();
    }
}
