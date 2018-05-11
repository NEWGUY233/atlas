package com.atlas.crmapp.adapter;

import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.LockJson;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by hoda on 2017/11/16.
 */

public class DoorLockCollectAdapter extends BaseItemDraggableAdapter<LockJson, BaseViewHolder> {
    private boolean isOpentEidt;
    public DoorLockCollectAdapter(List<LockJson> lockJsons , boolean isOpentEidt) {
        super(R.layout.item_door_lock_collect, lockJsons);
        this.isOpentEidt = isOpentEidt;
    }

    public void setOpentEidt(boolean opentEidt) {
        isOpentEidt = opentEidt;
    }

    @Override
    protected void convert(BaseViewHolder helper, LockJson item) {
        View ivDelete = helper.getView(R.id.iv_delete);
        View ivMenu = helper.getView(R.id.iv_menu);
        if(isOpentEidt){
            ivDelete.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.VISIBLE);
        }else{
            ivDelete.setVisibility(View.GONE);
            ivMenu.setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_door_name, item.getDoorName())
        .addOnClickListener(R.id.iv_delete);
    }
}
