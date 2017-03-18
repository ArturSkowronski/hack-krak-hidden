package tech.kolektiv.sdm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tbruyelle.rxpermissions.RxPermissions;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static tech.kolektiv.hiddengame.utils.FirebaseUtils.createBundle;
import static tech.kolektiv.hiddengame.utils.FirebaseUtils.createCustomBundle;

public class GameActivity extends AppCompatActivity {

    GameFragment fragment;


    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        ((SDMApplication) getApplication()).component().inject(this);

        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("WENT_THROUGH_ONBOARDING"));
        mFirebaseAnalytics.logEvent("WENT_THROUGH_ONBOARDING", createCustomBundle(true));

        RxPermissions.getInstance(this)
                .request(ACCESS_COARSE_LOCATION,
                        ACCESS_FINE_LOCATION,
                        READ_PHONE_STATE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        fragment = new GameFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("amount", 0);

                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("CONFIRMED_PERMISSIONS"));
                        mFirebaseAnalytics.logEvent("CONFIRMED_PERMISSIONS", createCustomBundle(true));

                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.container, fragment)
                                .commitAllowingStateLoss();
                    } else {
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
