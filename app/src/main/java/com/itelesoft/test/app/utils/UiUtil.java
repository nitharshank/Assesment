package com.itelesoft.test.app.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class UiUtil {

    // set font size based on screen size (for TextView, EditText)
    public static void setTextViewFontSizeBasedOnScreenDensity(Context context, TextView tv, double size, boolean makeBold) {
        DisplayMetrics dMetrics = context.getResources().getDisplayMetrics();
        final float WIDE = dMetrics.widthPixels;
        int valueWide = (int) (WIDE / size / (dMetrics.scaledDensity));
        if (makeBold) {
            tv.setTypeface(Typeface.DEFAULT_BOLD);
        }
        tv.setTextSize(valueWide);
    }

    // set Button font size based on screen size
    public static void setButtonFontSizeBasedOnScreenDensity(Context context, Button btn, double size, int style) {
        DisplayMetrics dMetrics = context.getResources().getDisplayMetrics();
        final float WIDE = dMetrics.widthPixels;
        int valueWide = (int) (WIDE / size / (dMetrics.scaledDensity));
        btn.setTextSize(valueWide);
        btn.setTypeface(btn.getTypeface(), style);
    }

    /**
     * when the layout height is set as a weight, it will resize if we use
     * Manifest - android:windowSoftInputMode="adjustResize"
     * for that we have to get actual weighted size by pixels and set it again
     **/
    public static void setHeightMeasuringWeight(@NonNull final View view) {
        view.post(new Runnable() {
            @Override
            public void run() {
                view.getLayoutParams().height = view.getMeasuredHeight();
            }
        });
    }


    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    // String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    String text = tv.getText().toString();
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    /*tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);*/
                    tv.setText(tv.getText().toString());
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {

            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "Read less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, ".. Read more", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

}
