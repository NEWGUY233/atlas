package com.atlas.crmapp.adapter;

import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.LockJson;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by hoda on 2017/11/16.
 */

public class SelectDoorLockAdapter extends BaseQuickAdapter<LockJson, BaseViewHolder> {
    public SelectDoorLockAdapter( List<LockJson> data, int index) {
        super(R.layout.item_select_door_lock, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final LockJson item) {
        final ImageView ivSelection = helper.getView(R.id.iv_selection);
        updateSelectView(ivSelection, item.isCollected());
        helper.setText(R.id.tv_door_name, item.getDoorName());
        helper.addOnClickListener(R.id.rl_select_lock_main);
    }

    private void  updateSelectView(ImageView ivSelection, boolean isSelect){
        if(isSelect){
            ivSelection.setImageResource(R.drawable.pay_channel_select);
        }else{
            ivSelection.setImageResource(R.drawable.pay_channel_not_select);
        }
    }
}
