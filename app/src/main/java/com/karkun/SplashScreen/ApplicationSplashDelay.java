package com.karkun.SplashScreen;

import android.app.Application;
import android.os.SystemClock;

/**
 * Created by Windows on 1/01/2018.
 */

public class ApplicationSplashDelay extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(3000);
    }
}
