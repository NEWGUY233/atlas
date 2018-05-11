package com.atlas.crmapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2018/3/21.
 */

public class MScroll extends ScrollView {
    public MScroll(Context context) {
        super(context);
    }

    public MScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScroll != null)
            onScroll.onScrollChanged(l, t, oldl, oldt);
    }

    private OnScroll onScroll;

    public OnScroll getOnScroll() {
        return onScroll;
    }

    public void setOnScroll(OnScroll onScroll) {
        this.onScroll = onScroll;
    }

    public interface OnScroll{
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}
