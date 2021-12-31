package com.itelesoft.test.app.common;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.SearchView;
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


        if (toolbarName.isEmpty()) {
            // mToolbar.setBackgroundColor(Color.TRANSPARENT);
        }

        if (activity.getClass().getSimpleName().equals(MainActivity.class.getSimpleName())) {
            SearchView searchView = findViewById(R.id.sv_toolbar_query_text);
            searchView.setFocusable(true);
            searchView.setFocusableInTouchMode(true);
            searchView.requestFocus();
            searchView.setSelected(true);


            int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView textView = searchView.findViewById(id);
            textView.setTextColor(Color.BLACK);
            textView.setHint("Search");
            //textView.setHighlightColor(getResources().getColor(R.color.colorHint));
            textView.setHintTextColor(Color.BLACK);
            searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + "Search" + "</font>"));
        }

        setSupportActionBar(mToolbar);
        if (!activity.getClass().getSimpleName().equals(MainActivity.class.getSimpleName())) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

}