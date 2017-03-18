package tech.kolektiv.sdm;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialModule;

import javax.inject.Singleton;

import dagger.Component;
import tech.kolektiv.sdm.module.GameModule;
import tech.kolektiv.hiddengame.modules.NewtonModule;
import tech.kolektiv.sdm.module.OnboardingModule;
import tech.kolektiv.sdm.onboarding.OnboardingFragment;

public class SDMApplication extends Application {

  public static final String GAME_NAME = "sdm";
  public static final String GAME_VERSION = "0.1";
  public static final String ENGINE_VERSION = "0.1";

  @Singleton
  @Component(modules = {GameModule.class})
  public interface ApplicationComponent {
    void inject(SDMApplication application);
    void inject(GameActivity mainActivity);
    void inject(GameFragment gameFragment);
    void inject(OnboardingActivity onboardingActivity);
    void inject(OnboardingFragment onboardingFragment);
    void inject(SplashScreenActivity splashScreenActivity);
  }
  
  private ApplicationComponent component;

  @Override public void onCreate() {
    super.onCreate();
    FacebookSdk.sdkInitialize(getApplicationContext());
    AppEventsLogger.activateApp(this);
    Iconify.with(new MaterialModule());

    component = DaggerSDMApplication_ApplicationComponent.builder()
        .gameModule(new GameModule())
        .onboardingModule(new OnboardingModule())
        .newtonModule(new NewtonModule(this))
        .build();


    component().inject(this); // As of now, LocationManager should be injected into this.
  }

  public ApplicationComponent component() {
    return component;
  }
}