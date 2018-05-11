package com.atlas.crmapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.atlas.crmapp.R;

import github.hellocsl.cursorwheel.CursorWheelLayout;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/15
 *         Description :
 */

public class ImageCursorWheelLayout extends CursorWheelLayout {


    public ImageCursorWheelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onInnerItemSelected(View v) {
        super.onInnerItemSelected(v);
        if (v == null) {
            return;
        }
//        ImageView iv = (ImageView) v.findViewById(R.id.wheel_menu_item_iv);
//        iv.setSelected(true);
    }

    @Override
    protected void onInnerItemUnselected(View v) {
        super.onInnerItemUnselected(v);
        if (v == null) {
            return;
        }
//        ImageView iv = (ImageView) v.findViewById(R.id.wheel_menu_item_iv);
//        iv.setSelected(false);
    }
}
