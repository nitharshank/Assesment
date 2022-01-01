package com.itelesoft.test.app.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.squareup.picasso.Transformation;

public class RoundedCornersTransform implements Transformation {

    private int mBorderSize = 50;
    private int mCornerRadius = 50;
    // private int mColor= Color.BLACK;

    @Override
    public Bitmap transform(Bitmap source) {
        // TODO Auto-generated method stub
        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xFFF3F2F2;
        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, source.getWidth(), source.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = mCornerRadius;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, rect, rect, paint);

        // draw border
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) mBorderSize);
        canvas.drawRoundRect(rectF, mCornerRadius, mCornerRadius, paint);
        //-------------------

        if (source != output) source.recycle();

        return output;
    }

    @Override
    public String key() {
        // TODO Auto-generated method stub
        return "grayscaleTransformation()";
    }

}
