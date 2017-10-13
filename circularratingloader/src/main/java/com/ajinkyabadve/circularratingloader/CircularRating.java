package com.ajinkyabadve.circularratingloader;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ajinkyabadve on 13/10/17.
 */

public class CircularRating extends View {
    private int ratingInPercent;
    private float ratingInFloat;
    private Paint mGridPaint;
    private float mCentreX;
    private float mCentreY;
    private float mTotalX;
    private float mTotalY;
    private RectF rect;
    private float strokeWidth = 100;

    public CircularRating(Context context) {
        super(context);
        init(context, null);
    }

    public CircularRating(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircularRating(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircularRating(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        mCentreX = getX() + parentWidth / 2;
        mCentreY = getY() + parentHeight / 2;
        mTotalX = getX() + parentWidth;
        mTotalY = getY() + parentHeight;
        this.setMeasuredDimension(parentWidth, parentHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs,
                R.styleable.CircularRating);
        ratingInFloat = attributes.getFloat(R.styleable.CircularRating_rating, 0);
        attributes.recycle();
        ratingInPercent = (int) Math.ceil((1 - ratingInFloat) * 100); // gives 2
        float downloaded = 100;
        float total = 200;
        float percent = (100 * downloaded) / total;
        ratingInPercent = (int) percent;
        mGridPaint = new Paint();
        mGridPaint.setStyle(Paint.Style.STROKE);
        mGridPaint.setColor(Color.RED);
        mGridPaint.setStrokeWidth(strokeWidth);
        mGridPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        //size 200 x200 example
        rect = new RectF(0, 0, 0, 0);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = canvas.getHeight() / 2;
        int width = canvas.getWidth() / 2;
//        width = (int) (width - (strokeWidth));
//        height = (int) (height - (strokeWidth));
        rect.set(width - mCentreX, height - mCentreX, width + mCentreX, height + mCentreX);
        final int sweepAngle = ratingInPercent * 360 / 100;
        canvas.drawArc(rect, 180, sweepAngle, false, mGridPaint);
//        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
//        int[] mymargin = {
//                getDpByPixel(lp.leftMargin),
//                getDpByPixel(lp.rightMargin),
//                getDpByPixel(lp.topMargin),
//                getDpByPixel(lp.bottomMargin),
//        };
//        setLayoutParams(lp);
//        setPadding(getDpByPixel(strokeWidth), getDpByPixel(strokeWidth), getDpByPixel(strokeWidth), getDpByPixel(strokeWidth));
    }

    private int getDpByPixel(float px) {
        return (int) ((int) (px / getResources().getDisplayMetrics().density) - strokeWidth);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
