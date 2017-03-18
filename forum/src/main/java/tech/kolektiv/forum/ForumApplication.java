package tech.kolektiv.forum;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialModule;
import javax.inject.Singleton;

import dagger.Component;
import tech.kolektiv.forum.module.GameModule;
import tech.kolektiv.forum.module.OnboardingModule;
import tech.kolektiv.forum.onboarding.OnboardingFragment;
import tech.kolektiv.hiddengame.modules.NewtonModule;

public class ForumApplication extends Application {
  
  @Singleton
  @Component(modules = {GameModule.class})
  public interface ApplicationComponent {
    void inject(ForumApplication application);
    void inject(GameActivity mainActivity);
    void inject(GameFragment gameFragment);
    void inject(OnboardingActivity onboardingActivity);
    void inject(OnboardingFragment onboardingFragment);
  }
  
  private ApplicationComponent component;

  @Override public void onCreate() {
    super.onCreate();
    component = DaggerForumApplication_ApplicationComponent.builder()
        .gameModule(new GameModule())
        .onboardingModule(new OnboardingModule())
        .newtonModule(new NewtonModule(this))
        .build();
    Iconify.with(new MaterialModule());

    component().inject(this); // As of now, LocationManager should be injected into this.
  }

  public ApplicationComponent component() {
    return component;
  }
}