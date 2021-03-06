package com.karkun.splashscreen;

import android.app.Application;
import android.os.SystemClock;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Windows on 1/01/2018.
 */

public class ApplicationSplashDelay extends Application {
    private static final String TAG = "Testing";

    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(3000);

        Realm.init(this);
        RealmConfiguration myconfiguration = new RealmConfiguration.Builder().name("karkunData.realm").build();
        Realm.setDefaultConfiguration(myconfiguration);
    }
}
