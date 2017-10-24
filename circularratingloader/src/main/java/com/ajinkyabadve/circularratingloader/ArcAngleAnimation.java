package com.ajinkyabadve.circularratingloader;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by ajinkyabadve on 24/10/17.
 */

public class ArcAngleAnimation extends Animation {
    private CircularRating circularRating;
    private float oldAngle;
    private float newAngle;

    private float startAngle;
    private float sweepAngle;

    public ArcAngleAnimation(CircularRating circularRating, int newAngle) {
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
    }
}
