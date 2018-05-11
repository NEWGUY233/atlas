package com.atlas.crmapp.ecosphere;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.coffee.OrderConfirmActivity;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.ActivitesUserInfoJson;
import com.atlas.crmapp.model.ActivityJson;
import com.atlas.crmapp.model.ActvityApplyJson;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.UiUtil;
import com.atlas.crmapp.view.ActivitiesContactView;
import com.atlas.crmapp.view.ActivitiesSimpleInfoView;
import com.atlas.crmapp.view.flowlayout.FlowLayout;
import com.atlas.crmapp.view.flowlayout.TagAdapter;
import com.atlas.crmapp.view.flowlayout.TagFlowLayout;
import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class ActivitiesApplyEnterActivity extends BaseStatusActivity {

    @BindView(R.id.v_simple_info)
    ActivitiesSimpleInfoView vSimpleInfo;
    @BindView(R.id.tv_combo)
    TextView tvCombo;
    @BindView(R.id.fl_combo)
    TagFlowLayout flCombo;
    @BindView(R.id.tv_sumbit_oreder)
    TextView tvSumbitOrder;
    @BindView(R.id.tv_pay_acount)
    TextView tvPayAcount;
    @BindView(R.id.v_activities_contact)
    ActivitiesContactView vContactView;
    @BindView(R.id.sv_activity_apply)
    ScrollView scMain;



    private int paddingLeft;
    private int paddingTop;
    public static final String KEY_ID = "KEY_ID";
    public static final String KEY_ACTIVITY_BEAN = "KEY_ACTIVITY_BEAN";
    private ActivityJson activityJson;
    private List<ActivityJson.Combo> combos = new ArrayList<>();
    private ActivityJson.Combo currtCombo ;
    private TagAdapter tagAdapter;
    private int unSelectPosition;




    @OnClick(R.id.tv_sumbit_oreder)
    void onClickSubmit(){
        ActvityApplyJson actvityApplyJson = new ActvityApplyJson();
        if(currtCombo != null){
            actvityApplyJson.setComboId(currtCombo.getId());
        }
        if(activityJson != null){
            actvityApplyJson.setActivityId(activityJson.getId());
        }
        ActivitesUserInfoJson activitesUserInfoJson = vContactView.getActivitesUserInfo();
        actvityApplyJson.setUserInfo(activitesUserInfoJson);

        BizDataRequest.requestV2ApplyActivity(this, actvityApplyJson, statusLayout,  new BizDataRequest.OnResponseV2ApplyActivity() {
            @Override
            public void onSuccess(ResponseOpenOrderJson responseOpenOrderJson) {
                ResponseOpenOrderJson confirmOrder = responseOpenOrderJson;
                Intent intent = new Intent(ActivitiesApplyEnterActivity.this, OrderConfirmActivity.class);
                intent.putExtra("confirmOrder", (Serializable) confirmOrder);
                startActivityForResult(intent, 999);
                ActivitiesApplyEnterActivity.this.finish();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_apply_enter);
        paddingLeft = UiUtil.dipToPx(this, 12);
        paddingTop = UiUtil.dipToPx(this, 8);
        Intent intent = getIntent();
        if (intent != null){
            activityJson = (ActivityJson) intent.getSerializableExtra(KEY_ACTIVITY_BEAN);
        }
        prepareActivityData();
        update();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initActivityViews() {
        super.initActivityViews();
        setTitle(getString(R.string.enter_apply));
        scMain.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scMain.setFocusable(true);
        scMain.setFocusableInTouchMode(true);
        scMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });

    }

    public static void newInstance(Activity activity,  ActivityJson activityJson){
        Intent intent = new Intent(activity, ActivitiesApplyEnterActivity.class);
        intent.putExtra(KEY_ACTIVITY_BEAN, activityJson);
        activity.startActivity(intent);
    }

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        BizDataRequest.requestActivityUsers(this, statusLayout, new BizDataRequest.OnResponseActivityUsers() {
            @Override
            public void onSuccess(List<ActivitesUserInfoJson> userInfoJsons) {
                vContactView.updateView(new ActivitiesContactView.OnInputCheckListener() {
                    @Override
                    public void onInputCheckListener(boolean isSuccess, String tip) {
                        FormatCouponInfo.setViewToTransparent(tvSumbitOrder, !isSuccess);
                        tvSumbitOrder.setClickable(isSuccess);
                    }
                }, userInfoJsons != null && userInfoJsons.size() > 0 ? userInfoJsons.get(0) : null);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    protected void updateActivityViews() {
        super.updateActivityViews();
    }



    private int onSelectedPosition = -1;
    private void update(){
        if(activityJson != null){
            vSimpleInfo.updateViews(activityJson, true);
        }

        combos = activityJson.getCombos();

        if(combos != null && combos.size() > 0){
            tagAdapter = new TagAdapter<ActivityJson.Combo>(combos)
            {

                @Override
                public View getView(FlowLayout parent, int position, ActivityJson.Combo s)
                {
                    TextView view = new TextView(ActivitiesApplyEnterActivity.this);
                    view.setTextColor(ContextCompat.getColor(ActivitiesApplyEnterActivity.this, R.color.text_dark));
                    view.setBackgroundResource(R.drawable.flow_activities_un_selected);
                    view.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
                    view.setText(s.getName());
                    return view;
                }

                @Override
                public void onSelected(int position, View view) {
                    super.onSelected(position, view);
                    onSelectedPosition = position;
                    currtCombo = combos.get(position);
                    view.setBackgroundResource(R.drawable.button_yellow);
                    ((TextView) view).setTextColor(ContextCompat.getColor(ActivitiesApplyEnterActivity.this, R.color.text_dark));
                    tvCombo.setText(currtCombo.getName().toString());
                    tvPayAcount.setText(Html.fromHtml(getString(R.string.pay_acount, FormatCouponInfo.getYuanStr() + currtCombo.getPrice().intValue())));

                    Logger.d("onSelected position "  + position);
                }

                @Override
                public void unSelected(final int position, final View view) {
                    super.unSelected(position, view);

                    int size = flCombo.getSelectedList().size();
                    Logger.d("size    " + size);
                    Logger.d("unSelected position "  + position);
                    view.setBackgroundResource(R.drawable.flow_activities_un_selected);
                    unSelectPosition = position;
                    handler.post(r);
                }

            };

            flCombo.setAdapter(tagAdapter);
            tagAdapter.setSelectedList(0);


        }else{

        }





    }

    private Handler handler = new Handler();
    private Runnable r  = new Runnable() {
        @Override
        public void run() {
            if(flCombo.getSelectedList().size() == 0){
                tagAdapter.setSelectedList(unSelectPosition);
            }
        }
    };



}
