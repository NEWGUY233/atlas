package com.atlas.crmapp.commonactivity;

import android.app.Activity;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 门锁收藏
 * Created by hoda on 2017/11/14.
 */

public class DoorLockCollectActivity extends BaseStatusActivity {

    @BindView(R.id.rv_collect_door)
    RecyclerView rvCollectDoor;
    @BindView(R.id.tv_bottom)
    TextView tvBottom;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;

    private String lockJsons ;
    private List<LockJson> collectLockJsons = new ArrayList<>();
    private DoorLockCollectAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    private ItemDragAndSwipeCallback mItemDragAndSwipeCallback;
    private static final String KEY_LOCKS = "KEY_LOCKS";
    public static final int REQUEST_CODE_TO_COLLECT = 101;
    private boolean isPrepareActivityDataed ;

    @OnClick(R.id.tv_bottom)
    void onClickToSelect(){
        SelectDoorLockActivity.newInstance(this, lockJsons, collectLockJsons);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_lock_collect);
        Intent intent = getIntent();
        if(intent != null){
            lockJsons =  intent.getStringExtra(KEY_LOCKS);
        }
        setTitle(getString(R.string.my_collect_door));
        tvBottom.setText(getString(R.string.add_collect_door));
        setTopRightButton(getString(R.string.complete), onClickToEdit);
        if(toolbar != null){
            toolbar.setNavigationIcon(R.drawable.button_white);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        prepareActivityData();
    }


    OnClickListener onClickToEdit = new OnClickListener() {
        @Override
        public void onClick() {
           //DoorLockCollectEditActivity.newInstance(DoorLockCollectActivity.this, collectLockJsons);
            requestCollectDoor();
        }
    };

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        if(!isPrepareActivityDataed){
            prepareActivityData();
        }else{
            requestCollectDoor();
        }

    }

    private void requestCollectDoor(){
        List<CollectLockItem> collectLockItems = new ArrayList<>();

        for(LockJson lockJson : collectLockJsons){
            CollectLockItem collectLockItem = new CollectLockItem();
            collectLockItem.setDoorType(lockJson.getDoorType());
            collectLockItem.setDoorId(lockJson.getDoorId());
            collectLockItems.add(collectLockItem);
        }

        BizDataRequest.requestCollectDoor(this, collectLockItems, statusLayout, new BizDataRequest.OnResponseCollectDoor() {
            @Override
            public void onSuccess() {
                DoorLockCollectActivity.this.finish();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }


    public static void newInstance(Activity context, String lockJsons ){
        Intent intent = new Intent(context, DoorLockCollectActivity.class);
        intent.putExtra(KEY_LOCKS, lockJsons);
        context.startActivityForResult(intent,REQUEST_CODE_TO_COLLECT);

    }

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();

        BizDataRequest.requestFindCollectList(this, new BizDataRequest.OnResponseFindCollectList() {
            @Override
            public void onSuccess(List<LockJson> lockJsons) {
                collectLockJsons.clear();
                collectLockJsons.addAll(lockJsons);
                updateActivityViews();
                isPrepareActivityDataed = true;
            }

            @Override
            public void onError(DcnException error) {

            }
        });

    }

    private void showStatust(){
        if(collectLockJsons != null && collectLockJsons.size() > 0){

            rlNull.setVisibility(View.GONE);
        }else{
            rlNull.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void updateActivityViews() {
        super.updateActivityViews();
        if(adapter == null){
            rvCollectDoor.setLayoutManager(new LinearLayoutManager(this));
            adapter = new DoorLockCollectAdapter(collectLockJsons, true);
            adapter.addHeaderView(GetCommonObjectUtils.getRvBgDivideItem(this, rvCollectDoor));
        }
        if(collectLockJsons != null && collectLockJsons.size() > 0){
            rvCollectDoor.setAdapter(adapter);
            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if(view.getId() == R.id.iv_delete){
                        if(position >= 0){
                            adapter.remove(position);
                            showStatust();
                        }
                    }
                }
            });

        }
        showStatust();
        adapter.notifyDataSetChanged();



        OnItemDragListener onItemDragListener = new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                showStatust();
            }
        };

  /*      OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
                showStatust();
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

            }
        };*/

        mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(rvCollectDoor);
        //mItemDragAndSwipeCallback.setDragMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN);
        mItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        adapter.disableSwipeItem();
        //adapter.setOnItemSwipeListener(onItemSwipeListener);
        adapter.enableDragItem(mItemTouchHelper);
        adapter.setOnItemDragListener(onItemDragListener);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DoorLockCollectEditActivity.REQUEST_CODE_TO_EDIT || requestCode == SelectDoorLockActivity.REQUEST_CODE_TO_SELECT){
            prepareActivityData();
        }
    }


}
