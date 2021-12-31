package com.itelesoft.test.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itelesoft.test.app.R;
import com.itelesoft.test.app.adapters.NewsFeedItemAdapter;
import com.itelesoft.test.app.common.BaseActivity;
import com.itelesoft.test.app.config.AppConst;
import com.itelesoft.test.app.dtos.ProcessResult;
import com.itelesoft.test.app.dtos.response.Article;
import com.itelesoft.test.app.dtos.response.BeanNewsFeed;
import com.itelesoft.test.app.interfaces.listeners.OnItemClickListener;
import com.itelesoft.test.app.utils.AppUtil;
import com.itelesoft.test.app.viewModels.MainActivityViewModel;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, OnItemClickListener<Article>, SearchView.OnQueryTextListener {

    // Constants
    private static final String TAG = MainActivity.class.getSimpleName();

    // UI-Components
    private ProgressBar mProgressBar;
    private NestedScrollView mNestedSV;
    private Button mBtnCancel;
    private RecyclerView mRecyclerView;

    // Objects
    private boolean mBackPressedToExitOnce = false;
    private MainActivityViewModel mViewModel;
    private int pageNumber = 1;
    private String mQueryText = "";
    private List<Article> mArticles;
    private NewsFeedItemAdapter mAdapter;
    private int mTotalResults = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar(getResources().getString(R.string.main_activity_title_text), MainActivity.this);

        initView();
    }

    private void initView() {

        mViewModel = new MainActivityViewModel(MainActivity.this);
        mViewModel.initViewModel();

        mProgressBar = findViewById(R.id.activity_main_pb_loding);
        mNestedSV = findViewById(R.id.idNestedSV);
        mRecyclerView = findViewById(R.id.activity_main_rv_news_feed);

        mBtnCancel = findViewById(R.id.btn_toolbar_cancel);
        mBtnCancel.setOnClickListener(this);

        SearchView searchView = findViewById(R.id.sv_toolbar_query_text);
        searchView.setOnQueryTextListener(this);

        // Show Progress bar using ProgressStatus
        mViewModel.getLiveRefreshStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean refreshStatus) {
                if (refreshStatus) {
                    mProgressBar.setVisibility(View.VISIBLE);
                } else {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });


        // adding on scroll change listener method for nested scroll view.
        mNestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method, incrementing page number by 1,
                    // making progress bar visible and calling get data method.

                    if ((mTotalResults - (pageNumber * AppConst.PAGE_SIZE_VALUE)) > 0) {
                        Log.w(TAG, "----- All data fetched " + (mTotalResults - (pageNumber * AppConst.PAGE_SIZE_VALUE)));
                        pageNumber++;
                        fetchDataFromNewAPI(mQueryText, pageNumber);
                    } else {
                        // All News feed data has benn loaded
                        Log.w(TAG, "----- All data fetched ");
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (mBackPressedToExitOnce) {
            super.onBackPressed();

        } else {
            this.mBackPressedToExitOnce = true;
            Toast.makeText(this, getResources().getString(R.string.main_activity_back_button_press_text), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBackPressedToExitOnce = false;
                }
            }, AppConst.BACK_PRESSED_DELAY);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_toolbar_cancel:
                AppUtil.hideDefaultKeyboard(MainActivity.this);

                break;

            default:
                break;
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText.length() > 0)
            mBtnCancel.setVisibility(View.VISIBLE);
        else
            mBtnCancel.setVisibility(View.GONE);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String queryText) {
        mQueryText = queryText;
        fetchDataFromNewAPI(queryText, pageNumber);
        return false;
    }

    private void fetchDataFromNewAPI(String queryText, int pageNumber) {

        mViewModel.fetchNewsFeedProcess(queryText, String.valueOf(pageNumber));

        mViewModel.getLiveFetchNewsFeedProcessResult().observe(MainActivity.this, new Observer<ProcessResult<BeanNewsFeed>>() {
            @Override
            public void onChanged(ProcessResult<BeanNewsFeed> processResult) {
                if (processResult != null) {
                    if (processResult.getProcessStatus().equals(AppConst.PROCESS_RESULT_STATUS_SUCCESS)) {

                        mTotalResults = processResult.getResult().getTotalResults();

                        if (pageNumber == 1) {
                            populateViewWithData(processResult.getResult());
                        } else {
                            // Adding next set of data to the Adapter
                            mAdapter.addNewsFeed(processResult.getResult().getArticles());
                        }
                    } else if (processResult.getProcessStatus().equals(AppConst.PROCESS_RESULT_STATUS_UN_SUCCESS)) {
                        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                        AppUtil.showCustomStandardAlert(dialog, MainActivity.this,
                                getResources().getString(R.string.alert_text),
                                processResult.getMessage(),
                                getResources().getDrawable(R.drawable.ic_info),
                                null,
                                getResources().getString(R.string.ok_text), false);
                    }
                }
            }
        });
    }

    // Populate news feed recycler view
    private void populateViewWithData(BeanNewsFeed beanNewsFeed) {
        mArticles = beanNewsFeed.getArticles();
        if (mArticles != null && mArticles.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            setUpRecyclerView(mArticles);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            Toast.makeText(this, getResources().getString(R.string.main_activity_empty_result), Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpRecyclerView(List<Article> articles) {
        mAdapter = new NewsFeedItemAdapter(MainActivity.this, articles, this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(int position, final Article article) {

        Article selectedArticle = new Article(article.getAuthor(), article.getTitle(), article.getDescription(),
                article.getUrl(), article.getUrlToImage(), article.getPublishedAt(), article.getContent());

        Intent mIntent = new Intent(MainActivity.this,DetailActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(AppConst.EXTRA_NEWS_FEED_OBJ,selectedArticle);
        mIntent.putExtras(mBundle);

        AppUtil.startActivityWithExtra(MainActivity.this, mIntent);

    }

}
