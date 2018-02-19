package com.karkun.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.karkun.activities.ActivityMain;

/**
 * Created by Windows on 1/01/2018.
 */

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       Intent intent = new Intent(this, ActivityMain.class);
       startActivity(intent);
       finish();
    }
}
