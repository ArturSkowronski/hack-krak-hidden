package tech.kolektiv.sdm;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tech.kolektiv.hiddengame.utils.ConnectorsProvider;
import tech.kolektiv.hiddenstory.client.version.domain.GameVersions;
import tech.kolektiv.hiddenstory.client.version.domain.Info;
import tech.kolektiv.hiddenstory.client.version.rest.VersionService;

import static tech.kolektiv.hiddengame.utils.FirebaseUtils.createBundle;
import static tech.kolektiv.hiddengame.utils.FirebaseUtils.createCustomBundle;
import static tech.kolektiv.sdm.SDMApplication.GAME_NAME;
import static tech.kolektiv.sdm.SDMApplication.GAME_VERSION;

public class SplashScreenActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    @Inject
    VersionService versionService;

    @Inject
    ConnectorsProvider connectorsProvider;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ((SDMApplication) getApplication()).component().inject(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("SEEN_SPLASH_SCREEN"));
        mFirebaseAnalytics.logEvent("SEEN_SPLASH_SCREEN", createCustomBundle(true));

        new Handler().postDelayed(() -> {
            if(checkPlayServices()) {
                try {
                    versionService.getVersions()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnError(this::goToOnboarding)
                            .subscribe(new Subscriber<GameVersions>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("ONBOARDING_CALL_ERROR"));
                                    mFirebaseAnalytics.logEvent("ONBOARDING_CALL_ERROR", createCustomBundle(true));

                                    goToOnboarding(e);
                                }

                                @Override
                                public void onNext(GameVersions gameVersions) {
                                    checkGameVersion(gameVersions);
                                }
                            });
                } catch (Exception e) {
                    goToOnboarding(e);
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void checkGameVersion(GameVersions globalVersionModel) {

        if (!connectorsProvider.isOnline(SplashScreenActivity.this)) {
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("ONBOARDING_OFFLINE"));
            mFirebaseAnalytics.logEvent("ONBOARDING_OFFLINE", createCustomBundle(true));

            goToOnboarding();
        } else {
            Info story = globalVersionModel.findStory(GAME_NAME);

            if (GAME_VERSION.equals(story.getStoryVersion())) {
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("ONBOARDING_CURRENT_VERSION"));
                mFirebaseAnalytics.logEvent("ONBOARDING_CURRENT_VERSION", createCustomBundle(true));

                goToOnboarding();
            } else {
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("ONBOARDING_UPDATE_VERSION"));
                mFirebaseAnalytics.logEvent("ONBOARDING_UPDATE_VERSION", createCustomBundle(true));

                goToUpdate();
            }
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }


    private void goToOnboarding(Throwable throwable) {
        Toast.makeText(SplashScreenActivity.this, R.string.no_check_version, Toast.LENGTH_LONG).show();
        Intent i = new Intent(SplashScreenActivity.this, OnboardingActivity.class);
        startActivity(i);
    }

    private void goToOnboarding() {
        Intent i = new Intent(SplashScreenActivity.this, OnboardingActivity.class);
        startActivity(i);
    }

    private void goToUpdate() {
        Intent i = new Intent(SplashScreenActivity.this, UpdateActivity.class);
        startActivity(i);
    }

}