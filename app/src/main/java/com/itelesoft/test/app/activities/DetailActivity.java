package com.itelesoft.test.app.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itelesoft.test.app.R;
import com.itelesoft.test.app.common.BaseActivity;
import com.itelesoft.test.app.config.AppConst;
import com.itelesoft.test.app.dtos.response.Article;
import com.itelesoft.test.app.dtos.response.Source;
import com.itelesoft.test.app.utils.AppUtil;
import com.itelesoft.test.app.utils.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

public class DetailActivity extends BaseActivity implements View.OnClickListener {

    // Constants
    private static final String TAG = DetailActivity.class.getSimpleName();

    // UI Components
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private TextView mTvSourceName, mTvPublishedDate, mTvTitle, mTvDescription, mTvContent;
    private Button mBtnReadFullArticle;

    // Objects
    private Article mArticle;
    private Source mSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getIntent().getSerializableExtra(AppConst.EXTRA_NEWS_FEED_OBJ) != null) {
            mArticle = (Article) getIntent().getSerializableExtra(AppConst.EXTRA_NEWS_FEED_OBJ);
        }

        if (getIntent().getSerializableExtra(AppConst.EXTRA_NEWS_FEED_SOURCE_OBJ) != null) {
            mSource = (Source) getIntent().getSerializableExtra(AppConst.EXTRA_NEWS_FEED_SOURCE_OBJ);
        }

        setToolbar("", DetailActivity.this);

        initView();
    }


    private void initView() {

        mImageView = findViewById(R.id.activity_detail_iv_image);
        mProgressBar = findViewById(R.id.activity_detail_pb_image_progress);
        mTvSourceName = findViewById(R.id.activity_detail_tv_source_name);
        mTvPublishedDate = findViewById(R.id.activity_detail_tv_date);
        mTvTitle = findViewById(R.id.activity_detail_tv_author);
        mTvDescription = findViewById(R.id.activity_detail_tv_description);
        mTvContent = findViewById(R.id.activity_detail_tv_content);

        mBtnReadFullArticle = findViewById(R.id.activity_detail_btn_read_full);
        mBtnReadFullArticle.setOnClickListener(this);

        populateViewWithData();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_detail_btn_read_full:
                readFullArticleBtnClickEvent();
                break;

            default:
                break;
        }
    }

    //Toolbar back button pressed
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void populateViewWithData() {

        if (mArticle != null) {
            if (mArticle.getUrlToImage() != null && !mArticle.getUrlToImage().isEmpty()) {

                Picasso.with(getApplicationContext()).load(mArticle.getUrlToImage()).transform(
                        new RoundedCornersTransform()).into(mImageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        mProgressBar.setVisibility(View.GONE);
                        populateTextData();
                    }

                    @Override
                    public void onError() {
                        mProgressBar.setVisibility(View.GONE);
                        populateTextData();
                    }
                });

            } else {
                // TODO Image url not found-----
                populateTextData();
            }
        }


    }

    private void populateTextData() {

        Typeface faceBold = Typeface.createFromAsset(getAssets(), "Montserrat_Bold.otf");
        Typeface faceMedium = Typeface.createFromAsset(getAssets(), "Montserrat_Medium.otf");

        if (mSource != null && mSource.getName() != null && !mSource.getName().isEmpty()) {
            mTvSourceName.setText(mSource.getName());
            mTvSourceName.setTypeface(faceBold);
        }

        if (mArticle.getPublishedAt() != null && !mArticle.getPublishedAt().isEmpty()) {
            mTvPublishedDate.setText(AppUtil.formatPublishedDate(mArticle.getPublishedAt()));
            mTvPublishedDate.setTypeface(faceBold);
        }

        if (mArticle.getTitle() != null && !mArticle.getTitle().isEmpty()) {
            mTvTitle.setText(mArticle.getTitle());
            mTvTitle.setTypeface(faceBold);
        }

        if (mArticle.getDescription() != null && !mArticle.getDescription().isEmpty()) {
            mTvDescription.setText(mArticle.getDescription());
            mTvDescription.setTypeface(faceBold);
        }

        if (mArticle.getContent() != null && !mArticle.getContent().isEmpty()) {
            mTvContent.setText(mArticle.getContent());
            mTvContent.setTypeface(faceMedium);
        }
    }

    private void readFullArticleBtnClickEvent() {
        if (mArticle.getUrl() != null && !mArticle.getUrl().isEmpty()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mArticle.getUrl()));
            startActivity(browserIntent);
        }
    }

}
