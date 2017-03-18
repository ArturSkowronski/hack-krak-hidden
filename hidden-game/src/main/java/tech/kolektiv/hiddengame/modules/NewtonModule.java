package tech.kolektiv.hiddengame.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tech.kolektiv.hiddengame.game.ScenesFactory;
import tech.kolektiv.newton_location.LocationSupervisor;

@Module
public class NewtonModule {

    private Context context;

    public NewtonModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    ScenesFactory provideScenes(LocationSupervisor locationSupervisor) {
        return new ScenesFactory(locationSupervisor);
    }
//
//    @Provides
//    @Singleton
//    KontaktSupervisor provideKontakt() {
//        return new KontaktSupervisor(context);
//    }

    @Provides
    @Singleton
    LocationSupervisor provideLocation() {
        return new LocationSupervisor(context);
    }
}