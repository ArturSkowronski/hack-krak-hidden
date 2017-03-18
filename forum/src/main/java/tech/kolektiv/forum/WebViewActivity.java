package tech.kolektiv.forum;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {
    public static final String PREFS_NAME = "MyPrefsFile";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_webview);

        SharedPreferences settings2 = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings2.edit();
        editor.remove("currentState");

        // Commit the edits!
        editor.apply();
        WebView myWebView = (WebView) findViewById(R.id.myWebView);
        myWebView.loadUrl("https://kolektiv.typeform.com/to/Vj4gB6");
        myWebView.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (Uri.parse(url).getHost().equals("www.centerend.com")) {
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i1 = new Intent(WebViewActivity.this, GameActivity.class);
        startActivity(i1);
    }
}