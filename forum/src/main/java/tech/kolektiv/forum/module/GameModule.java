package tech.kolektiv.forum.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tech.kolektiv.hiddengame.audio.AudioHandler;
import tech.kolektiv.hiddengame.audio.MediaPlayerAudioHandler;
import tech.kolektiv.forum.game.ForumGame;
import tech.kolektiv.forum.view.PlayerVMPresenter;
import tech.kolektiv.forum.view.SoundVMProvider;
import tech.kolektiv.hiddengame.game.ScenesFactory;
import tech.kolektiv.hiddengame.modules.NewtonModule;

@Module(includes = {NewtonModule.class, OnboardingModule.class})
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
  ForumGame provideGame(ScenesFactory scenesFactory, PlayerVMPresenter presenter, SoundVMProvider provider) {
    return new ForumGame(scenesFactory, presenter, provider);
  }
}