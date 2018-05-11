package com.atlas.crmapp.workplace;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.MeetingRoomJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.workplace.view.MeetingScheduleView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeetingDetailActivity extends BaseStatusActivity {

    //会议室名称
    @BindView(R.id.tv_name)
    TextView mTvName;
    //会议室编号
    @BindView(R.id.tv_m_code)
    TextView mTvCode;
    //容纳人数
    @BindView(R.id.tv_capacity)
    TextView mTvCapacity;

    @BindView(R.id.tv_price)
    TextView mTvPrice;

    @BindView(R.id.eq_tv_projection)
    TextView mTvprojection;

    @BindView(R.id.eq_tv_white_board)
    TextView mTvWhite_board;

    @BindView(R.id.eq_tv_laser)
    TextView mTvLaser;

    @BindView(R.id.eq_tv_vga)
    TextView mTvVga;

    @BindView(R.id.schedule_view_bar)
    MeetingScheduleView scheduleViewBar;

    private String id;
    private String date;
    private MeetingRoomJson data;


    @OnClick(R.id.btn_booking)
    void onSubmit(){
        if (getGlobalParams().isLogin()) {
            Intent intent = new Intent(MeetingDetailActivity.this, MeetingRoomAppointmentActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("date", date);
            MeetingDetailActivity.this.startActivityForResult(intent, 999);
        } else {
            showAskLoginDialog();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        umengPageTitle = getString(R.string.umeng_meeting_room_detail);
        setContentView(R.layout.activity_meeting_detail);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        date = getIntent().getStringExtra("date");
        prepareActivityData();
    }


    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
    BizDataRequest.requestMeetingRoomInfo(this, id, date, statusLayout, new BizDataRequest.OnMeetingRoomInfo() {

            @Override
            public void onSuccess(MeetingRoomJson meetingRoomJson) {
                data = meetingRoomJson;
                mHandler.sendEmptyMessageDelayed(10, 200);
            }

            @Override
            public void onError(DcnException error) {
                Message message = new Message();
                message.obj = error.getDescription();
                message.what = 404;
                mHandler.sendMessage(message);
            }
        });
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareActivityData();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    mTvName.setText(data.facility.name);
                    setTitle(data.facility.name);
                    mTvCapacity.setText(data.capacity + "");
                    mTvCode.setText(data.facility.code);
                    mTvPrice.setText(FormatCouponInfo.formatUnitTimePrice(data.facility.price));//FormatCouponInfo.formatUnitTimePrice(data.facility.price, data.unit)
                    List<MeetingRoomJson.Feature> featureList = data.featureList;
                    for (int i = 0; i < featureList.size(); i++) {
                        MeetingRoomJson.Feature map = featureList.get(i);
                        if (map.name.equals("投影仪")) {
                            mTvprojection.setVisibility(View.VISIBLE);
                        }

                        if (map.name.equals("白板")) {
                            mTvWhite_board.setVisibility(View.VISIBLE);
                        }

                        if (map.name.equals("激光笔")) {
                            mTvLaser.setVisibility(View.VISIBLE);
                        }

                        if (map.name.equals("VGA切换器")) {
                            mTvVga.setVisibility(View.VISIBLE);
                        }
                    }
                    List<MeetingRoomJson.OccupyTime> occlist= data.occupyTimes;
                    scheduleViewBar.showScheduleView(occlist, data.openTime, data.closeTime);

                    break;

            }

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 999)
        {
            if (resultCode == 999) {
                setResult(999);
                finish();
            }
        }
    }



}
