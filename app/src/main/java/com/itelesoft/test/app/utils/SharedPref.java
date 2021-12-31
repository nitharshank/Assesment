package com.itelesoft.test.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

public class SharedPref {

    public static final String DEFAULT_STRING_VALUE = "";

    public static final String FILE_KEY = "com.msaas.companyName.projectName.file.key";
    public static final String VERIFIED_LOGGED = "com.msaas.companyName.projectName.verify.logged.key";

    public static final String USERNAME_KEY = "com.msaas.companyName.projectName.username.key";
    public static final String PASSWORD_KEY = "com.msaas.companyName.projectName.password.key";

    public static final String USER_VIEW_NAME_KEY = "com.msaas.companyName.projectName.userviewname.key";
    public static final String COMPANY_NAME_KEY = "com.msaas.companyName.projectName.companyname.key";

    public static final String USER_ROLE_KEY = "com.msaas.companyName.projectName.user.role.key";
    public static final String ACCOUNT_NUMBER_KEY = "com.msaas.companyName.projectName.account.number.key";

    public static final String DEFAULT_VALUE_FOR_STRING = "";

    public static SharedPreferences getPref(Context ctx) {
        return ctx.getSharedPreferences(SharedPref.FILE_KEY, Context.MODE_PRIVATE);
    }

    public static void saveString(Context act, String key, String value) {
        SharedPreferences.Editor editor = act.getSharedPreferences(
                SharedPref.FILE_KEY, Context.MODE_PRIVATE).edit();
//        editor.putString(Encryption.getInstance().encrypt(key), Encryption.getInstance().encrypt(value));
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveBoolean(Context act, String key, boolean value) {
        SharedPreferences.Editor editor = act.getSharedPreferences(
                SharedPref.FILE_KEY, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String getString(Context act, String key, String defaultval) {
        SharedPreferences prefs = act.getSharedPreferences(SharedPref.FILE_KEY,
                Context.MODE_PRIVATE);
//        return Encryption.getInstance().decrypt(prefs.getString(Encryption.getInstance().encrypt(key), defaultval));
        return prefs.getString(key, defaultval);
    }

    public static boolean getBoolean(Context act, String key,
                                     boolean defaultval) {
        SharedPreferences prefs = act.getSharedPreferences(SharedPref.FILE_KEY,
                Context.MODE_PRIVATE);
        return prefs.getBoolean(key, defaultval);
    }

}
