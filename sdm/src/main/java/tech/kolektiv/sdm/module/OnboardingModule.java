package tech.kolektiv.sdm.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tech.kolektiv.hiddengame.utils.ConnectorsProvider;

@Module
public class OnboardingModule {

  @Provides
  @Singleton
  ConnectorsProvider provideConnectivity() {
    return new ConnectorsProvider();
  }

}