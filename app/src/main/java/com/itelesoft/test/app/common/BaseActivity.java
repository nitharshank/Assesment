package com.itelesoft.test.app.common;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.itelesoft.test.app.R;
import com.itelesoft.test.app.activities.MainActivity;

public class BaseActivity extends AppCompatActivity {

    // constants
    private static final String TAG = BaseActivity.class.getSimpleName();

    // Objects
    private Toolbar mToolbar;

    public BaseActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setToolbar(String toolbarName, Activity activity) {

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        TextView tvTitle = findViewById(R.id.tv_toolbar_title);
        tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvTitle.setFocusable(true);
        tvTitle.setFocusableInTouchMode(true);
        tvTitle.requestFocus();
        tvTitle.setSingleLine(true);
        tvTitle.setSelected(true);
        tvTitle.setMarqueeRepeatLimit(-1);
        tvTitle.setText(toolbarName);

        setSupportActionBar(mToolbar);

        // If MainActivity
        if (activity.getClass().getSimpleName().equals(MainActivity.class.getSimpleName())) {
            EditText searchEt = findViewById(R.id.et_toolbar_query_text);
            searchEt.setFocusable(true);
            searchEt.setFocusableInTouchMode(true);
        } else {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

}