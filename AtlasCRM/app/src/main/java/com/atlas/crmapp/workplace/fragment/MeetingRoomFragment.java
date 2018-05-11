package com.atlas.crmapp.workplace.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.MeetingBookingListAdapter;
import com.atlas.crmapp.adapter.MeetingScheduleAdapter;
import com.atlas.crmapp.common.BroadcastKeys;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.MeetingRoomJson;
import com.atlas.crmapp.model.SaleMeetingRoomsJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.GetCommonObjectUtils;
import com.atlas.crmapp.util.UiUtil;
import com.atlas.crmapp.view.DateScheduleView;
import com.atlas.crmapp.widget.MeetingSearchDialog;
import com.atlas.crmapp.widget.RecycleViewListViewDivider;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/13
 *         Description :
 */

public class MeetingRoomFragment extends BaseFragment implements MeetingSearchDialog.SearchListener {

    @BindView(R.id.textViewTitle)
    TextView tvTitle;
    @BindView(R.id.tv_top_month)
    TextView tvTopMonth;

    @OnClick(R.id.ibBack)
    void onBackClick() {
        getHoldingActivity().finish();
    }

    @BindView(R.id.v_date_schedule)
    DateScheduleView vDateSchedule;

    @BindView(R.id.rv_detail)
    RecyclerView mRvDetail;
    @BindView(R.id.ibHome)
    ImageButton ibTop;

    private MeetingSearchDialog mSearchDialog;
    private MeetingBookingListAdapter adpter;

    public int page = 0;
    int pagesize = 1000;
    public List<MeetingRoomJson> rows_al = new ArrayList<MeetingRoomJson>();
    public String date;
    public int capacity;


    @OnClick({R.id.ibHome})
    void onClick(){
        if (mSearchDialog == null) {
            mSearchDialog = MeetingSearchDialog.newInstance();
        }

        if (!mSearchDialog.isAdded() && !mSearchDialog.isVisible() && !mSearchDialog.isRemoving()) {
            mSearchDialog.setTargetFragment(this, 1000);
            mSearchDialog.show(getHoldingActivity().getSupportFragmentManager(), "search_dialog");
        }
    }

    public static MeetingRoomFragment newInstance() {
        MeetingRoomFragment fragment = new MeetingRoomFragment();
        return fragment;
    }

    @Override
    protected int setTopBar() {
        return R.layout.view_meeting_room_top;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_meeting_room;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        tvTitle.setText(getString(R.string.reserve_room));
        ibTop.setVisibility(View.VISIBLE);
        ibTop.setImageResource(R.drawable.ic_filter);
        vDateSchedule.updateView(onItemClickListener, 15);
        mRvDetail.setLayoutManager(new LinearLayoutManager(getHoldingActivity()));
        mRvDetail.addItemDecoration( new RecycleViewListViewDivider(context, LinearLayout.HORIZONTAL, UiUtil.dipToPx(context,8 ), Color.parseColor("#F2F2F2")));
        date = vDateSchedule.getCurrentDate();
        tvTopMonth.setText(DateUtil.dateToString(new Date(), "yyyy/MM"));
        prepareFragmentData();

    }

    MeetingScheduleAdapter.OnItemClickListener onItemClickListener = new MeetingScheduleAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(String date, int position, String formatDate) {
            MeetingRoomFragment.this.date = date;
            MeetingRoomFragment.this.page = 0;
            if(adpter != null){
                adpter.setDate(date);
                tvTopMonth.setText(formatDate);
            }
            prepareFragmentData();
        }
    };

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {
        // 注册支付成功的广播消息
        IntentFilter filter_dynamic = new IntentFilter();
        filter_dynamic.addAction(BroadcastKeys.INTENT_FILTER_PAY_SUCCESS);
        getHoldingActivity().registerReceiver(paySuccessReceiver, filter_dynamic);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getHoldingActivity().unregisterReceiver(paySuccessReceiver);
    }

    private BroadcastReceiver paySuccessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //在该fragment存活期间，并且收到支付成功广播，
            // 只有一种可能性，就是支付了预约会议室成功，
            //所以这里不用判断是什么订单支付成功。
            prepareFragmentData();
        }
    };

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareFragmentData();
    }


    @Override
    protected void prepareFragmentData() {
        super.prepareFragmentData();
        Logger.d("Logger---------0");
        rows_al.clear();
        if(adpter != null){
            adpter.notifyDataSetChanged();
        }
        Logger.d("Logger---------1");
        BizDataRequest.requestOnlineSaleMeetingRooms(getContext(), page, pagesize, getGlobalParams().getWorkplaceId(), date, capacity, false , statusLayout, new BizDataRequest.OnSaleMeetingRooms() {
            @Override
            public void onSuccess(SaleMeetingRoomsJson saleMeetingRoomsJson) {
                Logger.d("Logger---------2");
                rows_al.addAll(saleMeetingRoomsJson.rows);
                if(adpter == null){
                    adpter = new MeetingBookingListAdapter(getHoldingActivity(), rows_al, date);
                    adpter.addHeaderView(GetCommonObjectUtils.getRvBgDivideItem(context, mRvDetail));
                    mRvDetail.setAdapter(adpter);

                }
                adpter.setDate(date);

                Logger.d("Logger---------3");
                if(rows_al.size() == 0 ){
                    adpter.setEmptyView(R.layout.view_product_null, mRvDetail);
                }
                adpter.notifyDataSetChanged();
            }
            @Override
            public void onError(DcnException error) {
            }
        });
    }

    @Override
    public void onSearchComplete(String amount, String date) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999) {
            prepareFragmentData();
        } else {
            Bundle bundle = data.getExtras();
            date = bundle.getString("date");
            vDateSchedule.getMeetingScheduleAdapter().highlightDate(date);
            if (!TextUtils.isEmpty(date)) {
                date = date.substring(0, 10);
            }
            capacity = bundle.getInt("amount", 0);
            Logger.d("amount---" + capacity + "----date---" + date);
            page = 0;

            prepareFragmentData();
            if (mSearchDialog != null) {
                mSearchDialog.dismiss();
            }
        }
    }


}
