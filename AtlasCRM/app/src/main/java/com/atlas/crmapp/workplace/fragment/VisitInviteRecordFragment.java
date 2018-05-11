package com.atlas.crmapp.workplace.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.VisitInviteRecordAdapter;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.VisitInviteRecordJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.workplace.ToVisitInviteActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * Created by hoda on 2017/12/22.
 */

public class VisitInviteRecordFragment extends BaseFragment {
    @BindView(R.id.rv_visit_record)
    RecyclerView rvVisitRecord;

    private VisitInviteRecordAdapter recordAdapter ;

    public static final String KEY_INDEX = "KEY_INDEX";
    private int index ;
    private String state;
    private VisitInviteRecordJson inviteRecordJson = new VisitInviteRecordJson();


    public static VisitInviteRecordFragment newInstance(Activity activity, int index){
        VisitInviteRecordFragment recordFragment = new VisitInviteRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_INDEX , index);
        recordFragment.setArguments(bundle);
        return  recordFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_visit_invite_record;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if(bundle != null){
            index = bundle.getInt(KEY_INDEX);
        }

        if(index == 0){
            state = Constants.VISIT_INVITE_RECORD.NORMAL;
        }else if(index == 1){
            state = Constants.VISIT_INVITE_RECORD.COMPLETE;
        }else if(index == 2){
            state = Constants.VISIT_INVITE_RECORD.CANCEL;
        }

        prepareFragmentData();

    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);

        prepareFragmentData();
    }

    @Override
    protected void prepareFragmentData() {
        super.prepareFragmentData();

        BizDataRequest.requestInvitationSearch(getActivity(), statusLayout, state, 0 , 50, new BizDataRequest.OnResponseVisitorInviteRecord() {
            @Override
            public void onSuccess(VisitInviteRecordJson recordJson) {
                rvVisitRecord.setLayoutManager(new LinearLayoutManager(getActivity()));
                VisitInviteRecordFragment.this.inviteRecordJson = recordJson;
                if(recordJson != null){
                    recordAdapter = new VisitInviteRecordAdapter(getActivity(), inviteRecordJson.getRows());
                    rvVisitRecord.setAdapter(recordAdapter);
                    recordAdapter.setEmptyView(R.layout.view_product_null, rvVisitRecord);
                    recordAdapter.setOnItemChildClickListener(onItemChildClickListener);
                }

                recordAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }


    BaseQuickAdapter.OnItemChildClickListener onItemChildClickListener = new BaseQuickAdapter.OnItemChildClickListener() {
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            if(view.getId() == R.id.rl_bottom){
                VisitInviteRecordJson.RowsBean rowsBean = inviteRecordJson.getRows().get(position);
               
                ToVisitInviteActivity.newInstance(getActivity(), rowsBean);
            }
        }
    };

    @Subscribe
    public void onClickCancelVisitToRefrest(Event.EventObject eventObject){
        if(eventObject != null && Constants.EventType.CANCEL_VISIT_INVITE.equals(eventObject.type)){
            if (index == 0 || index == 2){
                prepareFragmentData();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusFactory.getBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusFactory.getBus().unregister(this);
    }
}
