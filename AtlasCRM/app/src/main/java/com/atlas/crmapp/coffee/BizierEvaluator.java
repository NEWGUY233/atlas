package com.atlas.crmapp.coffee;

import android.animation.TypeEvaluator;

import com.atlas.crmapp.model.bean.Point;

/**
 * Created by hofang_fang on 2017/6/1.
 * 购物动画 需要
 */

public class BizierEvaluator implements TypeEvaluator<Point> {
    private Point controllPoint;

    public BizierEvaluator(Point controllPoint) {
        this.controllPoint = controllPoint;
    }

    @Override
    public Point evaluate(float t, Point startValue, Point endValue) {
        int x = (int) ((1 - t) * (1 - t) * startValue.x + 2 * t * (1 - t) * controllPoint.x + t * t * endValue.x);
        int y = (int) ((1 - t) * (1 - t) * startValue.y + 2 * t * (1 - t) * controllPoint.y + t * t * endValue.y);
        return new Point(x, y);
    }

}
