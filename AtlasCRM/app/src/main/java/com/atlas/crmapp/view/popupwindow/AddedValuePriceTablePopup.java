package com.atlas.crmapp.view.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.MeetingRoomComboJson;
import com.atlas.crmapp.view.AddedValueSelectPriceView;

import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hoda on 2017/9/28.广告
 */

public class AddedValuePriceTablePopup extends BasePopupWindow {

    private View contentView;
    private List<MeetingRoomComboJson> combos;
    private LinearLayout llSelectPrice;
    private TextView tvUnitPrice;// 每小时价格
    private Context context;
    private AddedValueSelectPriceView.OnItemSelect onItemSelect;
    private long unitPrice;


    public AddedValuePriceTablePopup(Activity context, List<MeetingRoomComboJson> combos, long unitPrice, AddedValueSelectPriceView.OnItemSelect onItemSelect) {
        super(context);
        this.unitPrice = unitPrice;
        this.context  = context;
        this.combos = combos;
        this.onItemSelect = onItemSelect;
        bindEvent();

    }

    private void bindEvent(){
        llSelectPrice = (LinearLayout) contentView.findViewById(R.id.ll_select_price);
        tvUnitPrice = (TextView) contentView.findViewById(R.id.tv_unit_price);

        contentView.findViewById(R.id.rl_base_unit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemSelect != null){
                    onItemSelect.onSelect(null);
                }
            }
        });
        tvUnitPrice.setText(unitPrice + context.getString(R.string.yuan_unit));
        if(combos != null && combos.size() > 0){
            for(MeetingRoomComboJson combo : combos){
                AddedValueSelectPriceView selectPriceView = new AddedValueSelectPriceView(context);
                selectPriceView.updateViews(combo, onItemSelect);
                llSelectPrice.addView(selectPriceView);
            }
        }else{
            contentView.findViewById(R.id.tv_none).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return contentView.findViewById(R.id.rl_click_dismiss_added);
    }

    @Override
    public View onCreatePopupView() {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.popup_added_value_price_table, null);
        return contentView;
    }

    @Override
    public View initAnimaView() {
        return contentView.findViewById(R.id.ll_main);
    }


}
