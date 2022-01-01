package com.itelesoft.test.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.itelesoft.test.app.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppUtil {

    // Land/Open Wireless Settings
    public static void showWirelessSystemSettingsDialog(Context context) {
        context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
    }

    // Check network connection availability
    public static boolean checkNetworkConnection(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo activeNetworks = connectivityManager.getActiveNetworkInfo();
        if (activeNetworks != null && activeNetworks.isConnected()) {
            return activeNetworks.isConnectedOrConnecting();
        }
        return false;
    }

    // hide keyboard
    public static void hideDefaultKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    // start Activity with intentExtras
    public static void startActivityWithExtra(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    // confirm alert dialog box (Updated UI)
    public static void showCustomConfirmAlert(final AlertDialog dialog, Activity activity, String title, String message,
                                              View.OnClickListener yesClickListener, View.OnClickListener noClickListener,
                                              String yesText, String noText, boolean cancelableState) {

        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_custom_confirm_alert, null);
        dialogView.setLayerType(View.LAYER_TYPE_SOFTWARE, null); // This line is for hardware acceleration false
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setView(dialogView);
        dialog.setCancelable(cancelableState);

        TextView tvTitle = dialogView.findViewById(R.id.custom_alert_tv_title);
        TextView tvMessage = dialogView.findViewById(R.id.custom_alert_tv_description);
        Button btnCancel = dialogView.findViewById(R.id.custom_alert_btn_cancel);
        Button btnSettings = dialogView.findViewById(R.id.custom_alert_btn_settings);
        btnCancel.setOnClickListener(noClickListener);
        btnSettings.setOnClickListener(yesClickListener);

        if (noClickListener == null)
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        if (yesClickListener == null)
            btnSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        if (title.isEmpty()) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }

        tvMessage.setText(message);
        btnCancel.setText(noText);
        btnSettings.setText(yesText);

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Log.i("Screen Width: ", "" + width);

        dialog.show();
        dialog.getWindow().setLayout(width - 80, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public static String formatPublishedDate(String dateStr) {
        try {
            //String dateInString = "2014-10-05T15:23:01Z";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date date = formatter.parse(dateStr.replaceAll("Z$", "+0000"));

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            return dateFormat.format(date);
        } catch (Exception e) {
            return "-";
        }
    }

    public static String getToday() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }


}