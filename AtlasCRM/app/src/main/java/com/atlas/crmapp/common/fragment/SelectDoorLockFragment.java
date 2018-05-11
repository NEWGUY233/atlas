package com.atlas.crmapp.common.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.SelectDoorLockAdapter;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.commonactivity.SelectDoorLockActivity;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.LockJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hoda on 2017/11/14.
 */

public class SelectDoorLockFragment extends BaseFragment {
    @BindView(R.id.rv_select_lock)
    RecyclerView rvSelectLock;

    private static final String KEY_INDEX = "KEY_INDEX";


    private int index;

    private List<LockJson> currentLockJsons = new ArrayList<>();
    private SelectDoorLockAdapter selectDoorLockAdapter;



    public static SelectDoorLockFragment newInstance(Context context, int index){
        Bundle bundle = new Bundle();

        bundle.putInt(KEY_INDEX , index);
        SelectDoorLockFragment selectDoorLockFragment = new SelectDoorLockFragment();
        selectDoorLockFragment.setArguments(bundle);
        return selectDoorLockFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_door_lock;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            index = bundle.getInt(KEY_INDEX);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(index == 0){

            BizDataRequest.requestFindLastestOpenList(context, GlobalParams.getInstance().getAtlasId(), statusLayout, new BizDataRequest.OnResponsefindLastestOpenList() {
                @Override
                public void onSuccess(List<LockJson> lockJsons) {
                    currentLockJsons  = lockJsons;
                    setRvList();
                }

                @Override
                public void onError(DcnException error) {

                }
            });


        }else if (index == 1 || index == 2){
            String doorType = "";
            if(index == 1){
                doorType = Constants.DOOR_TYPE.EGCLOUD;
            }else{
                doorType = Constants.DOOR_TYPE.GZZK;
            }
            BizDataRequest.requestFindUUidList(getActivity(), doorType, new BizDataRequest.OnResponseFindUUidList() {
                @Override
                public void onSuccess(final List<LockJson> lockJsons) {
                    currentLockJsons = lockJsons;
                    setRvList();
                }
                @Override
                public void onError(DcnException error) {

                }
            });
        }
    }

    private void setRvList(){
        rvSelectLock.setLayoutManager(new LinearLayoutManager(getActivity()));
        selectDoorLockAdapter = new SelectDoorLockAdapter(currentLockJsons, index);
        rvSelectLock.setAdapter(selectDoorLockAdapter);
        selectDoorLockAdapter.setEmptyView(R.layout.view_product_null, rvSelectLock);
        selectDoorLockAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                LockJson lockJson =currentLockJsons.get(position);
                if(lockJson.isCollected()){
                    lockJson.setCollected(false);
                }else{
                    lockJson.setCollected(true);
                }
                selectDoorLockAdapter.notifyDataSetChanged();
                SelectDoorLockFragment.this.currentLockJsons = currentLockJsons;
                ((SelectDoorLockActivity)getActivity()).updateSelectNum();
            }
        });
    }

    public List<LockJson> getLockJsons() {
        return currentLockJsons;
    }


    public void notifyRvData(){
        if(selectDoorLockAdapter != null)
        selectDoorLockAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {

    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }
}
