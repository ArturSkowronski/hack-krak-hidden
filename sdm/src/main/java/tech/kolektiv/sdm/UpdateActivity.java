package tech.kolektiv.sdm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static tech.kolektiv.hiddengame.utils.FirebaseUtils.createBundle;
import static tech.kolektiv.hiddengame.utils.FirebaseUtils.createCustomBundle;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class UpdateActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    @OnClick(R.id.play)
    public void submit(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=tech.kolektiv.sdm"));
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("CLICKED_UPDATE"));
        mFirebaseAnalytics.logEvent("CLICKED_UPDATE", createCustomBundle(true));


        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("SEEN_UPDATE"));
        mFirebaseAnalytics.logEvent("SEEN_UPDATE", createCustomBundle(true));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);

    }
}
