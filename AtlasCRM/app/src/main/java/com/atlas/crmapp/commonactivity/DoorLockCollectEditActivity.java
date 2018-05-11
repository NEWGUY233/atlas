package com.atlas.crmapp.commonactivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.DoorLockCollectAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.CollectLockItem;
import com.atlas.crmapp.model.LockJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.GetCommonObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hoda on 2017/11/27.
 */

public class DoorLockCollectEditActivity extends BaseStatusActivity {
    @BindView(R.id.rv_collect_door)
    RecyclerView rvCollectDoor;
    @BindView(R.id.tv_bottom)
    TextView tvBottom;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;


    private List<LockJson> lockJsons ;
    private DoorLockCollectAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    private ItemDragAndSwipeCallback mItemDragAndSwipeCallback;
    private static final String KEY_LOCKS = "KEY_LOCKS";
    public  static final int REQUEST_CODE_TO_EDIT = 100;

    @OnClick(R.id.tv_bottom)
    void onClickToEnter(){
        List<CollectLockItem> collectLockItems = new ArrayList<>();

        for(LockJson lockJson : lockJsons){
            CollectLockItem collectLockItem = new CollectLockItem();
            collectLockItem.setDoorType(lockJson.getDoorType());
            collectLockItem.setDoorId(lockJson.getDoorId());
            collectLockItems.add(collectLockItem);
        }


        BizDataRequest.requestCollectDoor(this, collectLockItems, statusLayout, new BizDataRequest.OnResponseCollectDoor() {
            @Override
            public void onSuccess() {
                DoorLockCollectEditActivity.this.finish();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }


    public static void newInstance(Activity context, List<LockJson> lockJsons ){
        Intent intent = new Intent(context, DoorLockCollectEditActivity.class);
        intent.putExtra(KEY_LOCKS, (Serializable) lockJsons);
        context.startActivityForResult(intent, REQUEST_CODE_TO_EDIT);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_lock_collect);
        Intent intent = getIntent();
        if(intent != null){
            lockJsons = (List<LockJson>) intent.getSerializableExtra(KEY_LOCKS);
        }

        setTitle(getString(R.string.complete));
        tvBottom.setText(getString(R.string.complete));
        updateActivityViews();
      /*
        if(lockJsons == null){
            this.finish();
        }*/
    }

    @Override
    protected void updateActivityViews() {
        super.updateActivityViews();

        if(adapter == null){
            rvCollectDoor.setLayoutManager(new LinearLayoutManager(this));
            adapter = new DoorLockCollectAdapter(lockJsons, true);
            adapter.addHeaderView(GetCommonObjectUtils.getRvBgDivideItem(this, rvCollectDoor));
        }

        if(lockJsons != null && lockJsons.size() > 0){
            rvCollectDoor.setAdapter(adapter);
            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if(view.getId() == R.id.iv_delete){
                        lockJsons.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
            rlNull.setVisibility(View.GONE);
        }else{
            rlNull.setVisibility(View.VISIBLE);
        }



        OnItemDragListener onItemDragListener = new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {

            }
        };

        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

            }
        };

        mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(rvCollectDoor);
        //mItemDragAndSwipeCallback.setDragMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN);
        mItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        adapter.enableSwipeItem();
        adapter.setOnItemSwipeListener(onItemSwipeListener);
        adapter.enableDragItem(mItemTouchHelper);
        adapter.setOnItemDragListener(onItemDragListener);

    }
}
