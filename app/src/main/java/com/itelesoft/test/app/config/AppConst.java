package com.itelesoft.test.app.config;

public class AppConst {

    // Sandbox URL for fetch News Feed
    public static final String SANDBOX_BASE_URL_FOR_ROLES = "https://newsapi.org/";

    // TODO API keys can be secure using Android NDK if required--- https://www.geeksforgeeks.org/securing-api-keys-using-android-ndk/
   // public static final String SANDBOX_API_KEY = "ccc544eb0dc943dabe29617f809f94ac";
    public static final String SANDBOX_API_KEY = "5088b7b10d074280a1cb5b4fec346108";

    // Production URL for for fetch News Feed
    public static final String PRODUCTION_BASE_URL_FOR_ROLES = "https://newsapi.org/";

    // TODO API keys can be secure using Android NDK if required--- https://www.geeksforgeeks.org/securing-api-keys-using-android-ndk/
    public static final String PRODUCTION_API_KEY = "ccc544eb0dc943dabe29617f809f94ac";

    public static final String PREFIX = "";
    public static final String CONTENT_TYPE = "application/json";

    public static final int CONNECTION_TIMEOUT = 180000; // 180 seconds
    public static final int DATA_TIMEOUT = 180000; // 180 seconds
    public static final int RESPONSE_SUCCESS_CODE = 200;
    public static final int SPLASH_TIME_OUT = 3000; // Splash screen time out
    public static final int BACK_PRESSED_DELAY = 2000; // onBackPressed()-> delay


    public static final String SORT_BY_VALUE = "popularity";
    public static final int PAGE_SIZE_VALUE = 10;

    // Process Result Actions
    public static final String PROCESS_RESULT_ACTION_LOGIN = "action_login";

    // Process Result Status
    public static final String PROCESS_RESULT_STATUS_SUCCESS = "SUCCESS";
    public static final String PROCESS_RESULT_STATUS_UN_SUCCESS = "UN-SUCCESS";

    // Intent extras
    public static final String EXTRA_NEWS_FEED_OBJ = "news_feed_obj";
    public static final String EXTRA_NEWS_FEED_SOURCE_OBJ = "news_feed_source_obj";

    // Intent extra values

    // Catch results

    // Request runtime permission

}
