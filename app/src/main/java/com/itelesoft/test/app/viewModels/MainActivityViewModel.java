package com.itelesoft.test.app.viewModels;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.itelesoft.test.app.R;
import com.itelesoft.test.app.config.AppConst;
import com.itelesoft.test.app.database.model.TB_SearchHistory;
import com.itelesoft.test.app.dtos.ProcessResult;
import com.itelesoft.test.app.dtos.response.BeanError;
import com.itelesoft.test.app.dtos.response.BeanNewsFeed;
import com.itelesoft.test.app.interfaces.listeners.RxResponseListener;
import com.itelesoft.test.app.repositories.MainActivityRepository;
import com.itelesoft.test.app.retrofit.FetchNewsFeedRx;
import com.itelesoft.test.app.utils.AppExecutor;
import com.itelesoft.test.app.utils.AppUtil;

import java.util.List;

import retrofit2.Response;


public class MainActivityViewModel extends ViewModel {

    // Constants
    private static final String TAG = MainActivityRepository.class.getSimpleName();

    // Objects
    private Activity mActivity;
    private MutableLiveData<List<TB_SearchHistory>> mLiveQueryTextList;
    private MutableLiveData<Boolean> mLiveProgressStatus;
    private MutableLiveData<ProcessResult<BeanNewsFeed>> mLiveFetchNewsFeedProgressResult;

    public MainActivityViewModel(Activity activity) {
        this.mActivity = activity;
    }

    public void initViewModel() {
        MainActivityRepository.getInstance();

        mLiveProgressStatus = new MutableLiveData<>();
        mLiveProgressStatus.setValue(false);
    }


    //-------- Login Process
    public void fetchNewsFeedProcess(String queryText, String pageNumber) {
        try {
            mLiveFetchNewsFeedProgressResult = new MutableLiveData<>();

            if (AppUtil.checkNetworkConnection(mActivity)) {

                mLiveProgressStatus.postValue(true);

                callFetchNewsFeedRxWay(queryText, pageNumber);

            } else {
                final AlertDialog dialog = new AlertDialog.Builder(mActivity).create();
                AppUtil.showCustomConfirmAlert(dialog,
                        mActivity,
                        mActivity.getResources().getString(R.string.no_internet_connection_text),
                        mActivity.getResources().getString(R.string.turn_on_internet_connection_text),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                AppUtil.showWirelessSystemSettingsDialog(mActivity);
                            }
                        },
                        null,
                        mActivity.getResources().getString(R.string.settings_text),
                        mActivity.getResources().getString(R.string.cancel_text),
                        false);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            ProcessResult<BeanNewsFeed> result = new ProcessResult<>(AppConst.PROCESS_RESULT_ACTION_LOGIN, AppConst.PROCESS_RESULT_STATUS_UN_SUCCESS,
                    AppConst.PROCESS_RESULT_STATUS_UN_SUCCESS + "\nSystem Error:" + ex.getMessage(), null);
            mLiveFetchNewsFeedProgressResult.postValue(result);
            mLiveProgressStatus.postValue(false);
        }
    }


    private void callFetchNewsFeedRxWay(final String queryText, final String pageNumber) {

        new FetchNewsFeedRx(mActivity, queryText, pageNumber, new RxResponseListener<JsonElement>() {
            @Override
            public void serviceResponse(Response<JsonElement> serviceResponseResponse) {
                Gson gson = new Gson();
                if (serviceResponseResponse.isSuccessful()) {
                    BeanNewsFeed newsFeed = gson.fromJson(serviceResponseResponse.body(), BeanNewsFeed.class);
                    Log.w(TAG, "------ isSuccessful" + newsFeed.getArticles().size());

                    ProcessResult<BeanNewsFeed> result = new ProcessResult<>(AppConst.PROCESS_RESULT_ACTION_LOGIN,
                            AppConst.PROCESS_RESULT_STATUS_SUCCESS, newsFeed.getStatus(), newsFeed);

                    mLiveFetchNewsFeedProgressResult.postValue(result);
                } else {
                    try {
                        BeanError newsFeed = gson.fromJson(serviceResponseResponse.body(), BeanError.class);
                        ProcessResult<BeanNewsFeed> result = new ProcessResult<>(AppConst.PROCESS_RESULT_ACTION_LOGIN,
                                AppConst.PROCESS_RESULT_STATUS_UN_SUCCESS, newsFeed.getMessage(), null);
                        mLiveFetchNewsFeedProgressResult.postValue(result);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void serviceThrowable(Throwable throwable) {
                Log.i(TAG, throwable.toString());
                mLiveProgressStatus.postValue(false);
            }

            @Override
            public void serviceComplete() {
                mLiveProgressStatus.postValue(false);
            }
        }).callServiceRXWay();
    }

    public void insertNote(String title) {
        AppExecutor.getInstance().diskIO().execute(() -> {
            //perform code for database operation
            MainActivityRepository.getInstance().insertQueryTextToLocalDb((new TB_SearchHistory(title)));
        });
    }

    public void getLiveQueryTextListProcess() {
        mLiveQueryTextList = new MutableLiveData<>();
        List<TB_SearchHistory>[] queryTextList = new List[]{null};

        AppExecutor.getInstance().diskIO().execute(() -> {
            //perform code for database operation
            queryTextList[0] = MainActivityRepository.getInstance().getAllQueryTextFromLocalDb();
            mLiveQueryTextList.postValue(queryTextList[0]);
        });
    }

    public MutableLiveData<ProcessResult<BeanNewsFeed>> getLiveFetchNewsFeedProcessResult() {
        return mLiveFetchNewsFeedProgressResult;
    }

    public MutableLiveData<Boolean> getLiveRefreshStatus() {
        return mLiveProgressStatus;
    }

}
