package com.atlas.crmapp.util;

import android.view.animation.LinearInterpolator;

/**
 * Created by hoda on 2017/9/28.
 */

public class OverShootInterpolator extends LinearInterpolator {
    private float factor;

    public OverShootInterpolator() {
        this.factor = 2.0f;
    }

    public OverShootInterpolator(float factor) {
        this.factor = factor;
    }

    @Override
    public float getInterpolation(float input) {

        input -= 1.0;
        return (float) (input * input * ((factor + 1) * input + factor) + 1.0);
    }
}