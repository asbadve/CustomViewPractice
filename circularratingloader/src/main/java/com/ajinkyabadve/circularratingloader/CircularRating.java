package com.ajinkyabadve.circularratingloader;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by ajinkyabadve on 13/10/17.
 */

public class CircularRating extends View {

    private static final String TAG = CircularRating.class.getSimpleName();
    private static final int START_ANGLE = 0;
    private int mRatingInPercent = 0;
    private int mRatingInPercentFinal;

    private float ratingInFloat;
    private Paint mActualArcPaint;
    private Paint mBackgroundArcPaint;

    private float mCentreX;
    private RectF mActualRectF;
    private RectF mBackgroundRectF;
    private float mStrokeWidth = pxToDp(150);
    int mSweepAngle = 0;
    boolean mIsToShowRatingAfterAnimation = false;
    private Paint mTextPaint;
    private boolean mIsToAnimateRating = false;

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
//        mCentreY = getY() + parentHeight / 2;
//        mTotalX = getX() + parentWidth;
//        mTotalY = getY() + parentHeight;
        this.setMeasuredDimension(parentWidth, parentHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs,
                R.styleable.CircularRating);
        ratingInFloat = attributes.getFloat(R.styleable.CircularRating_rating, 0);
        attributes.recycle();

        float total = 100;
        float percent = (100 * ratingInFloat) / total;
        mRatingInPercentFinal = (int) percent;

        mActualArcPaint = new Paint();
        mActualArcPaint.setStyle(Paint.Style.STROKE);
        mActualArcPaint.setColor(Color.RED);
        mActualArcPaint.setStrokeWidth(mStrokeWidth);
        mActualArcPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mBackgroundArcPaint = new Paint();
        mBackgroundArcPaint.set(mActualArcPaint);
        mBackgroundArcPaint.setColor(Color.BLACK);
        mBackgroundArcPaint.setStrokeWidth(mStrokeWidth + 30);


        mActualRectF = new RectF(0, 0, 0, 0);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(150);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = canvas.getHeight() / 2;
        int width = canvas.getWidth() / 2;

        mActualRectF.set(width - mCentreX + (int) mStrokeWidth,//left
                height - mCentreX + (int) mStrokeWidth,//top
                width + mCentreX - (int) mStrokeWidth,//right
                height + mCentreX - (int) mStrokeWidth);//bottom

        if (!mIsToAnimateRating) {
            changeSweepAngle(mRatingInPercentFinal);
            drawRating(canvas);
        }
        canvas.drawArc(mActualRectF, 0, 360, false, mBackgroundArcPaint);

        canvas.drawArc(mActualRectF, START_ANGLE, mSweepAngle, false, mActualArcPaint);


        if (mIsToShowRatingAfterAnimation) {
            mIsToShowRatingAfterAnimation = false;
            drawRating(canvas);
        }
    }

    private void drawRating(Canvas canvas) {
        canvas.drawText("" /*+ ratingInFloat + "%"*/, mActualRectF.centerX(), mActualRectF.centerY(), mTextPaint);
    }

    public void changeSweepAngle(int ratingInPercent) {
        mSweepAngle = ratingInPercent * 360 / 100;
    }

    private int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public void animateArc(long duration) {
        mIsToAnimateRating = true;
        ValueAnimator animation = ValueAnimator.ofInt(mRatingInPercent, mRatingInPercentFinal);
        animation.setDuration(duration);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.d(TAG, "onAnimationStart() called with: animation = [" + animation + "]");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "onAnimationEnd() called with: animation = [" + animation + "]");
                mIsToShowRatingAfterAnimation = true;
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.d(TAG, "onAnimationCancel() called with: animation = [" + animation + "]");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.d(TAG, "onAnimationRepeat() called with: animation = [" + animation + "]");
            }
        });
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d(TAG, "onAnimationUpdate() called with: animation = [" + animation.getAnimatedValue() + "]");
                changeSweepAngle((Integer) animation.getAnimatedValue());
                invalidate();
            }

        });
        animation.start();
    }

    private static void setTextSizeForWidth(Paint paint, float desiredWidth,
                                            String text) {

        // Pick a reasonably large value for the test. Larger values produce
        // more accurate results, but may cause problems with hardware
        // acceleration. But there are workarounds for that, too; refer to
        // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
        final float testTextSize = 48f;

        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSize = testTextSize * desiredWidth / bounds.width();

        // Set the paint for that size.
        paint.setTextSize(desiredTextSize);
    }


}
