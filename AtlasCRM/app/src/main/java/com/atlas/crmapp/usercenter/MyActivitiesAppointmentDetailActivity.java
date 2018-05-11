package com.atlas.crmapp.usercenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.ActivitesUserInfoJson;
import com.atlas.crmapp.model.ActivitiesBindinfoJson;
import com.atlas.crmapp.model.ActivityJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.view.ActivitiesContactView;
import com.atlas.crmapp.view.ActivitiesSimpleInfoView;
import com.atlas.crmapp.view.CodeImageView;

import butterknife.BindView;

/**
 *
 * 我的预约  活动
 *
 */

public class MyActivitiesAppointmentDetailActivity extends BaseStatusActivity {

    @BindView(R.id.v_simple_info)
    ActivitiesSimpleInfoView vSimpleInfo;
    @BindView(R.id.valid_time)
    TextView validTime;
    @BindView(R.id.v_code)
    CodeImageView vCode;
    @BindView(R.id.tv_activties_num)
    TextView tvActivtiesNum;
    @BindView(R.id.rl_code)
    RelativeLayout rlCode;
    @BindView(R.id.v_contact)
    ActivitiesContactView vContact;



    private static final String KEY_BIND_INFO = "KEY_BIND_INFO";
    private static final String KEY_BOOK_ID = "KEY_BOOK_ID";

    private ActivitiesBindinfoJson bindinfoJson;
    private long bookId;
    private String opentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activities_appointment_detail);
        Intent intent = getIntent();
        if(intent != null){
            bookId = intent.getLongExtra(KEY_BOOK_ID, 0);
            bindinfoJson = (ActivitiesBindinfoJson) intent.getSerializableExtra(KEY_BIND_INFO);
        }
    }

    public static void newInstance(Activity activity, long bookId, ActivitiesBindinfoJson bindinfoJson){
        Intent intent = new Intent(activity, MyActivitiesAppointmentDetailActivity.class);
        intent.putExtra(KEY_BOOK_ID, bookId);
        intent.putExtra(KEY_BIND_INFO, bindinfoJson);
        activity.startActivity(intent);
    }

    @Override
    protected void initActivityViews() {
        super.initActivityViews();
        setTitle(getString(R.string.appointment_detail));
        prepareActivityData();
    }

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        if(bindinfoJson != null){
            validTime.setText(Html.fromHtml(getString(R.string.valid_time, FormatCouponInfo.getActivityValidTime(bindinfoJson.getStartTime(), bindinfoJson.getEndTime(), "-"))));
            vCode.updateView(bindinfoJson.getState(), Constants.CodeSkipType.TO_ACTIVITY_VERIFI + bookId + "/" + bindinfoJson.getSerialNum());//
            tvActivtiesNum.setText(FormatCouponInfo.getReminConsumedText(this, bindinfoJson.getQuantity(), bindinfoJson.getConsumed()));
            BizDataRequest.requestGetActivity(MyActivitiesAppointmentDetailActivity.this, bindinfoJson.getActivityId(), new BizDataRequest.OnActivity() {
                @Override
                public void onSuccess(ActivityJson activityJson) {
                    vSimpleInfo.updateViews(activityJson);

                    BizDataRequest.requestActivityBindinfo(MyActivitiesAppointmentDetailActivity.this, 0 , activityJson.id,  "", null, new BizDataRequest.OnResponseBindinfo() {
                        @Override
                        public void onSuccess(ActivitiesBindinfoJson bindinfoJson) {
                            vCode.updateView(bindinfoJson.getState(), Constants.CodeSkipType.TO_ACTIVITY_VERIFI + bookId + "/" + bindinfoJson.getSerialNum());
                        }

                        @Override
                        public void onError(DcnException error) {

                        }
                    });
                }

                @Override
                public void onError(DcnException error) {

                }
            });





        }

        BizDataRequest.requestActivityUserinfo(this, bookId, statusLayout, new BizDataRequest.OnResponseActivityUserinfo() {
            @Override
            public void onSuccess(ActivitesUserInfoJson userInfoJson) {
                vContact.updateView(userInfoJson);
            }

            @Override
            public void onError(DcnException error) {

            }
        });


        ScrollView view = (ScrollView)findViewById(R.id.sv_app_activity);
        view.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
    }

    @Override
    protected void updateActivityViews() {
        super.updateActivityViews();

    }



}
