package tech.kolektiv.forum;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.tbruyelle.rxpermissions.RxPermissions;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        ((ForumApplication) getApplication()).component().inject(this);

        setContentView(R.layout.activity_main);

        RxPermissions.getInstance(this)
                .request(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(aBoolean -> {
                    if (savedInstanceState == null) {
                        if (aBoolean) {
                            GameFragment fragment = new GameFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("amount", 0);
                            fragment.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.container, fragment)
                                    .commit();
                        } else {
                            finish();
                        }
                    }
                });

    }


    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
