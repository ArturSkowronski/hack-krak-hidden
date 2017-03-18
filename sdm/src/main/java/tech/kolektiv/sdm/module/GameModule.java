package tech.kolektiv.sdm.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tech.kolektiv.hiddengame.audio.AudioHandler;
import tech.kolektiv.hiddengame.audio.MediaPlayerAudioHandler;
import tech.kolektiv.hiddengame.modules.NewtonModule;
import tech.kolektiv.hiddenstory.client.module.ClientModule;
import tech.kolektiv.sdm.view.PlayerVMPresenter;
import tech.kolektiv.sdm.view.SoundVMProvider;
import tech.kolektiv.hiddengame.game.ScenesFactory;

@Module(includes = {NewtonModule.class, OnboardingModule.class, ClientModule.class})
public class GameModule {

  @Provides
  @Singleton
  PlayerVMPresenter provideVMPresenter() {
    return new PlayerVMPresenter();
  }

  @Provides
  @Singleton
  AudioHandler provideAudioHandler() {
    return new MediaPlayerAudioHandler();
  }

  @Provides
  @Singleton
  SoundVMProvider provideSoundVM() {
    return new SoundVMProvider();
  }

  @Provides
  @Singleton
  SDMGame provideGame(ScenesFactory scenesFactory, PlayerVMPresenter presenter, SoundVMProvider provider) {
    return new SDMGame(scenesFactory, presenter, provider);
  }
}