package com.itelesoft.test.app.activities;

import android.os.Bundle;

import com.itelesoft.test.app.R;
import com.itelesoft.test.app.common.BaseActivity;
import com.itelesoft.test.app.config.AppConst;
import com.itelesoft.test.app.dtos.response.Article;

public class DetailActivity extends BaseActivity {

    // Constants
    private static final String TAG = MainActivity.class.getSimpleName();

    //Objects
    private Article mArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getIntent().getSerializableExtra("TEST") != null) {
            mArticle = (Article) getIntent().getSerializableExtra(AppConst.EXTRA_NEWS_FEED_OBJ);
        }

        setToolbar("", DetailActivity.this);

        initView();
    }


    private void initView() {

    }


    //Toolbar back button pressed
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
