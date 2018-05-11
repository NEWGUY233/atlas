package com.atlas.crmapp.usercenter.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.MyAppointmentAdapter;
import com.atlas.crmapp.coffee.OrderConfirmActivity;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.ActivitiesBindinfoJson;
import com.atlas.crmapp.model.ResponseMyAppointmentJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.usercenter.MyActivitiesAppointmentDetailActivity;
import com.atlas.crmapp.util.GetCommonObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by A.Developer on 2017/3/21.
 */

public class MyAppointmentFragment extends BaseFragment {

    public final static String APPOINTMENT_STATE_COMPLETE = "AppointmentStateComplete";//已完成
    public final static String APPOINTMENT_STATE_ONGOING = "AppointmentStateOngoing";//进行中
    private String appointmentState;
    private MyAppointmentAdapter adapter;
    private RecyclerView recyclerView;
    @BindView(R.id.rl_main)
    RelativeLayout vMain;
    List<ResponseMyAppointmentJson.MyAppointment> alVVthre = new ArrayList<ResponseMyAppointmentJson.MyAppointment>();
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_appointment;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflateView.findViewById(R.id.appointment_recyclerView);
        adapter = new MyAppointmentAdapter( alVVthre);
        recyclerView.addItemDecoration(GetCommonObjectUtils.getRvItemDecoration(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.addHeaderView(GetCommonObjectUtils.getRvBgDivideItem(context, recyclerView));
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId() == R.id.rl_main){
                    if(alVVthre != null && alVVthre.size() > 0){
                        toAppointmentActivity (alVVthre.get(position));
                    }
                }
            }
        });
        prepareFragmentData();
    }

    private void toAppointmentActivity(final ResponseMyAppointmentJson.MyAppointment myAppointment){
        if(Constants.AppointmentType.ACTIVITY.equals(myAppointment.getType())){
            BizDataRequest.requestActivityBindinfo(getActivity(), myAppointment.getId() , 0,  "", statusLayout, new BizDataRequest.OnResponseBindinfo() {
                @Override
                public void onSuccess(ActivitiesBindinfoJson bindinfoJson) {
                    if(bindinfoJson != null){
                        if(Constants.ACTIVITIES_APPLY_STATUS.UNPAID.equals(bindinfoJson.getState())){
                            OrderConfirmActivity.newInstance(getActivity(), bindinfoJson.getOrderId());
                        }else{
                            MyActivitiesAppointmentDetailActivity.newInstance(getActivity(), myAppointment.getId(), bindinfoJson);
                        }
                    }
                }

                @Override
                public void onError(DcnException error) {

                }
            });
        }
    }




    @Override
    protected void initFragmentData(Bundle savedInstanceState) {
        appointmentState = getArguments().getString("state");
    }

    @Override
    public void onResume() {
        super.onResume();
        //if (adapter != null)
        adapter.notifyDataSetChanged();
    }

    public static MyAppointmentFragment newInstance(String appointmentState) {
        MyAppointmentFragment fragment = new MyAppointmentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("state", appointmentState);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    protected void prepareFragmentData() {
        super.prepareFragmentData();
        if (appointmentState == APPOINTMENT_STATE_COMPLETE) {
            getMyAppointmentList(Constants.Appointment.SUB_STATE_COMPLETE);
        } else {
            getMyAppointmentList(Constants.Appointment.SUB_STATE_OFF);
            getMyAppointmentList(Constants.Appointment.SUB_STATE_ON);
        }
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareFragmentData();
    }

    //已完成: COMPLETE
    //进行中: ON , OFF type 穿给空时 查询所有预约， MEETING 时 办公室预约
    private void getMyAppointmentList(final String subState){
        BizDataRequest.requestMyAppointment(getActivity(), "", subState, 0, 100, 0, statusLayout, new BizDataRequest.OnResponseMyAppointmentJson() {
            @Override
            public void onSuccess(ResponseMyAppointmentJson responseMyAppointmentJson) {
                List<ResponseMyAppointmentJson.MyAppointment> vthre = responseMyAppointmentJson.getRows();
                //让'进行中'的数据保持在列表前面
                if (subState == Constants.Appointment.SUB_STATE_ON) {
                    alVVthre.addAll(0, vthre);
                } else {
                    alVVthre.addAll(vthre);
                }
                if(alVVthre.size() == 0){
                    adapter.setEmptyView(R.layout.view_product_null, recyclerView);

                }
                Logger.d("--alVVthre---" + appointmentState +"   "+ alVVthre.size());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(DcnException error) {

            }
        });

    }

}
