package tech.kolektiv.hiddengame.utils;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by arturskowronski on 22/07/16.
 */
public class FirebaseUtils {
    public static Bundle createBundle(String event){
        Bundle bundle2 = new Bundle();
        bundle2.putString(FirebaseAnalytics.Param.ITEM_ID, event);
        bundle2.putString(FirebaseAnalytics.Param.ITEM_NAME, event);
        bundle2.putString(FirebaseAnalytics.Param.CONTENT_TYPE, event);
        return bundle2;
    }

    public static Bundle createCustomBundle(String value){
        Bundle bundle2 = new Bundle();
        bundle2.putString(FirebaseAnalytics.Param.VALUE, value);
        return bundle2;
    }

    public static Bundle createCustomBundle(boolean value){
        Bundle bundle2 = new Bundle();
        bundle2.putBoolean(FirebaseAnalytics.Param.VALUE, value);
        return bundle2;
    }
}

