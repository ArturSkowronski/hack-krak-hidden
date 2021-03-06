package tech.kolektiv.forum;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import javax.inject.Inject;

import butterknife.ButterKnife;
import tech.kolektiv.forum.onboarding.OnboardingPagerAdapter;
import tech.kolektiv.hiddengame.utils.ConnectorsProvider;

public class OnboardingActivity extends AppCompatActivity {

    ViewPager mViewPager;

    @Inject
    ConnectorsProvider connectorsProvider;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ForumApplication) getApplication()).component().inject(this);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black_trans80));
        }

        setContentView(R.layout.activity_onboarding);

        OnboardingPagerAdapter mSectionsPagerAdapter = new OnboardingPagerAdapter(getSupportFragmentManager());
        mViewPager =  (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        connectorsProvider.checkConnectivity(OnboardingActivity.this, GameActivity.class);
    }

}
