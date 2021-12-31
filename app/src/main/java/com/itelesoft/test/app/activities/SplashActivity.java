package com.itelesoft.test.app.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.itelesoft.test.app.R;
import com.itelesoft.test.app.utils.AppUtil;

public class  SplashActivity extends AppCompatActivity {

    // Constants

    // UI Components

    // Objects
    private Boolean mTabletDevice;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        initView();
    }

    private void initView() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        AppUtil.startActivityWithExtra(SplashActivity.this, intent);
    }
}
