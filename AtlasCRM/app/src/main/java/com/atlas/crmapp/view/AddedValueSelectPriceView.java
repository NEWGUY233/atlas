package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.MeetingRoomComboJson;

/**
 * Created by hoda on 2017/12/26.
 */

public class AddedValueSelectPriceView extends RelativeLayout {
    private Context context;
    private TextView tvTypeName, tvTime, tvPrice;
    private MeetingRoomComboJson combo;

    public AddedValueSelectPriceView(Context context) {
        super(context);
        initView(context);
    }

    public AddedValueSelectPriceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AddedValueSelectPriceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AddedValueSelectPriceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }


    private void initView(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_added_value_select_price, this, true);
        tvTypeName = (TextView) findViewById(R.id.tv_type_name);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvPrice = (TextView) findViewById(R.id.tv_price);
    }


    public void updateViews(final MeetingRoomComboJson combo, final OnItemSelect onItemSelect){
        this.combo = combo;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemSelect != null){
                    onItemSelect.onSelect(combo);
                }
            }
        });
        if(combo != null){
            tvTime.setText(combo.getPeroid());
            int price = 0;
            if(combo.getAmount() != null){
                price = combo.getAmount().intValue();
            }

            tvPrice.setText(price + context.getString(R.string.yuan));
            tvTypeName.setText(combo.getName());
        }
    }

    public interface OnItemSelect{
        void onSelect(MeetingRoomComboJson combo);
    }


}
