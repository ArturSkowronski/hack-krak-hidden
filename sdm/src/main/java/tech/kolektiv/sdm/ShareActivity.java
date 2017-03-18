package tech.kolektiv.sdm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static tech.kolektiv.hiddengame.utils.FirebaseUtils.createBundle;
import static tech.kolektiv.hiddengame.utils.FirebaseUtils.createCustomBundle;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ShareActivity extends AppCompatActivity {

    private static final String LOG_TAG = "LOG";
    private FirebaseAnalytics mFirebaseAnalytics;

    @OnClick(R.id.share)
    public void onShareResult(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://gethiddenstory.com");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @OnClick(R.id.share_with_fb)
    public void onShareResultFB(View view) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        CallbackManager callbackManager = CallbackManager.Factory.create();
        final ShareDialog shareDialog = new ShareDialog(this);

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d(LOG_TAG, "success");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(LOG_TAG, "error");
            }

            @Override
            public void onCancel() {
                Log.d(LOG_TAG, "cancel");
            }
        });


        if (shareDialog.canShow(ShareLinkContent.class)) {

            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://gethiddenstory.com"))
                    .setContentDescription("Step outside and experience cinematic story in the real world with GPS Audio App.")
                    .setContentTitle("Hidden Story about The Pope John Paul II... revealed!")
                    .setImageUrl(Uri.parse("http://kolektiv.tech/sdm/share-sdm-md.png"))
                    .build();

            shareDialog.show(content);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("SEEN_UPDATE"));
        mFirebaseAnalytics.logEvent("SEEN_UPDATE", createCustomBundle(true));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);

    }
}
