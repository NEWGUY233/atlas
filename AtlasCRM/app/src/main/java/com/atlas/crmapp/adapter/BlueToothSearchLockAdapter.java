package com.atlas.crmapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.statusLayout.IStatusLayout;
import com.atlas.crmapp.model.LockJson;
import com.atlas.crmapp.view.BlueToothSearchLockButtonView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by hoda on 2017/10/10.
 */

public class BlueToothSearchLockAdapter  extends BaseQuickAdapter<LockJson, BaseViewHolder>{
    private Context context;
    private OnOpentDoorListener onOpentDoorListener;
    private List<LockJson> data ;
    private boolean isAlreadyOpent;//是否已经开门，防止多次开门
    private RecyclerView recyclerView;
    private IStatusLayout statusLayout;

    public BlueToothSearchLockAdapter(Context context, List<LockJson> data, OnOpentDoorListener onOpentDoorListener, RecyclerView recyclerView, IStatusLayout statusLayout) {
        super(R.layout.item_blue_tooth_search_lock, data);
        this.context = context;
        this.onOpentDoorListener = onOpentDoorListener;
        this.data = data;
        this.recyclerView =recyclerView;
        this.statusLayout = statusLayout;
    }

    public void setData(List<LockJson> data) {
        this.data = data;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final LockJson item) {
        helper.addOnClickListener(R.id.rl_item_main);
        final BlueToothSearchLockButtonView buttonView = helper.getView(R.id.rl_item_main);

        // 当是一个门时开门
        /*if(data != null && data.size() == 1 ){
            onOpentDoorListener.onRequestOpneDoor(item, buttonView, true);
        }*/
        buttonView.updateViews(item, new BlueToothSearchLockButtonView.OnCanOpenDoor() {
            @Override
            public void onCanOpenListener() {
                onOpentDoorListener.onRequestOpneDoor(item, buttonView, false);
                //requestRemoteOpenLock(item, buttonView, false);
            }
        });

    }

    public interface OnOpentDoorListener{
        /*//开锁失败
        void onOpentDoorFail(BlueToothSearchLockButtonView buttonView, String errorMsg);
        //开锁成功
        void onOpentDoorSuccess(BlueToothSearchLockButtonView buttonView, String msg);*/
        //请求开锁
        void onRequestOpneDoor(LockJson lockJson, BlueToothSearchLockButtonView buttonView, boolean isOneDoorOpen);

    }

}
