
package tech.kolektiv.forum.view;

import android.util.Log;

import rx.Observable;
import tech.kolektiv.hiddengame.utils.RxBus;

/**
 * Created by arturskowronski on 03/07/16.
 */
public class SoundVMProvider {

    protected RxBus<SoundVM> presenter = new RxBus<>();

    public void send(SoundVM object){
        Log.d("SDM_DEBUG", object.toString());
        presenter.send(object);
    }

    public Observable<SoundVM> sounds(){
        return presenter.toObserverable();
    }
}
