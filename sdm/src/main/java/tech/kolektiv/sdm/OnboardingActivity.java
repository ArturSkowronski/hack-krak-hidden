package tech.kolektiv.sdm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import javax.inject.Inject;

import butterknife.ButterKnife;
import tech.kolektiv.hiddengame.utils.ConnectorsProvider;
import tech.kolektiv.sdm.onboarding.OboardingPagerAdapter;

public class OnboardingActivity extends AppCompatActivity {

    ViewPager mViewPager;

    @Inject
    ConnectorsProvider connectorsProvider;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SDMApplication) getApplication()).component().inject(this);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black_trans80));
        }

        setContentView(R.layout.activity_onboarding);
        SharedPreferences settings2 = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings2.edit();
        editor.remove("currentState");
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        // Commit the edits!
        editor.apply();
        OboardingPagerAdapter mSectionsPagerAdapter = new OboardingPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        connectorsProvider.checkConnectivity(OnboardingActivity.this, GameActivity.class);
    }

}
