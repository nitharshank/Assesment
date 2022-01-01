package com.itelesoft.test.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itelesoft.test.app.R;
import com.itelesoft.test.app.adapters.NewsFeedItemAdapter;
import com.itelesoft.test.app.adapters.SearchHistoryAdapter;
import com.itelesoft.test.app.common.BaseActivity;
import com.itelesoft.test.app.config.AppConst;
import com.itelesoft.test.app.database.model.TB_SearchHistory;
import com.itelesoft.test.app.dtos.ProcessResult;
import com.itelesoft.test.app.dtos.response.Article;
import com.itelesoft.test.app.dtos.response.BeanNewsFeed;
import com.itelesoft.test.app.interfaces.listeners.OnHistoryItemClickListener;
import com.itelesoft.test.app.interfaces.listeners.OnItemClickListener;
import com.itelesoft.test.app.utils.AppUtil;
import com.itelesoft.test.app.viewModels.MainActivityViewModel;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, OnItemClickListener<Article>, OnHistoryItemClickListener<TB_SearchHistory>
        /*, SearchView.OnQueryTextListener*/ {

    // Constants
    private static final String TAG = MainActivity.class.getSimpleName();

    // UI-Components
    private ProgressBar mProgressBar;
    private NestedScrollView mNestedSV;
    private EditText mEtSearch;
    private Button mBtnCancel;
    private RecyclerView mRecyclerView, mRvSearchHistory;

    // Objects
    private boolean mBackPressedToExitOnce = false;
    private MainActivityViewModel mViewModel;
    private int pageNumber = 1;
    private List<Article> mArticles;
    private NewsFeedItemAdapter mAdapter;
    private SearchHistoryAdapter mSearchHistoryAdapter;
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
        mNestedSV = findViewById(R.id.activity_main_nsv_news_feed);
        mRecyclerView = findViewById(R.id.activity_main_rv_news_feed);
        mRvSearchHistory = findViewById(R.id.activity_main_rv_search_history);

        mBtnCancel = findViewById(R.id.btn_toolbar_cancel);
        mBtnCancel.setOnClickListener(this);

        mEtSearch = findViewById(R.id.et_toolbar_query_text);

        // When press done in soft key board
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    fetchDataFromNewAPI(mEtSearch.getText().toString(), pageNumber);
                    AppUtil.hideDefaultKeyboard(MainActivity.this);
                    return true;
                }
                return false;
            }
        });

        // Text change listener
        mEtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    //mBtnCancel.setVisibility(View.GONE);
                    // populateSearchHistoryViewWithData();
                } else {
                    //mBtnCancel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                mNestedSV.setVisibility(View.GONE);
                pageNumber = 1; // Set to default value
            }
        });

        // Handling edittext click/ focus event
        mEtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Log.w(TAG, "----- hasFocus");
                    mBtnCancel.setVisibility(View.VISIBLE);
                    populateSearchHistoryViewWithData();
                } else {
                    mBtnCancel.setVisibility(View.GONE);
                }
            }
        });

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
                        fetchDataFromNewAPI(mEtSearch.getText().toString(), pageNumber);
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

        resetView();
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
                resetView();
                break;
            case R.id.et_toolbar_query_text:
                populateSearchHistoryViewWithData();
                break;

            default:
                break;
        }
    }

    /*@Override
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
    }*/

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
            mNestedSV.setVisibility(View.VISIBLE);
            setUpRecyclerView(mArticles);
        } else {
            mNestedSV.setVisibility(View.GONE);
            Toast.makeText(this, getResources().getString(R.string.main_activity_empty_result), Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpRecyclerView(List<Article> articles) {
        mAdapter = new NewsFeedItemAdapter(MainActivity.this, articles, this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerView.setAdapter(mAdapter);
    }

    // Populate search history data
    private void populateSearchHistoryViewWithData() {

        mViewModel.getLiveQueryTextListProcess();

        mViewModel.getLiveSearchHistoryProcessResult().observe(MainActivity.this, result -> {

            if (result != null) {
                if (result.size() > 0) {
                    mRvSearchHistory.setVisibility(View.VISIBLE);
                    setUpSearchHistoryRecyclerView(result);
                } else {
                    mRvSearchHistory.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setUpSearchHistoryRecyclerView(List<TB_SearchHistory> searchHistoryList) {
        AppUtil.hideDefaultKeyboard(MainActivity.this);
        mSearchHistoryAdapter = new SearchHistoryAdapter(MainActivity.this, searchHistoryList, this);
        mRvSearchHistory.setHasFixedSize(true);
        mRvSearchHistory.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRvSearchHistory.setAdapter(mSearchHistoryAdapter);
    }

    @Override
    public void onItemClick(int position, final Article article) {

        Article selectedArticle = new Article(article.getAuthor(), article.getTitle(), article.getDescription(),
                article.getUrl(), article.getUrlToImage(), article.getPublishedAt(), article.getContent());

        Intent mIntent = new Intent(MainActivity.this, DetailActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(AppConst.EXTRA_NEWS_FEED_OBJ, selectedArticle);
        mIntent.putExtras(mBundle);

        AppUtil.startActivityWithExtra(MainActivity.this, mIntent);

    }

    @Override
    public void onSearchItemClick(int position, TB_SearchHistory historyItem) {
        pageNumber = 1;
        mEtSearch.setText(historyItem.getQueryText());
        mEtSearch.setSelection(historyItem.getQueryText().length());
        mNestedSV.setVisibility(View.VISIBLE);
        mRvSearchHistory.setVisibility(View.GONE);
        fetchDataFromNewAPI(historyItem.getQueryText(), pageNumber);
    }

    private void resetView() {
        pageNumber = 1; // Set to default value
        mEtSearch.clearFocus();
        mNestedSV.setVisibility(View.GONE);
        mRvSearchHistory.setVisibility(View.GONE);
        mBtnCancel.setVisibility(View.GONE);
        mEtSearch.setText("");

        //Clear News feed Recyclerview
        if (mArticles != null) {
            mArticles.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

}
