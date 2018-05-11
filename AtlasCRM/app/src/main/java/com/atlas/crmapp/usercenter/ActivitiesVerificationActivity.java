package com.atlas.crmapp.usercenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.ActivitesUserInfoJson;
import com.atlas.crmapp.model.ActivitiesBindinfoJson;
import com.atlas.crmapp.model.ActivityJson;
import com.atlas.crmapp.model.ConsumedJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.view.ActivitiesContactView;
import com.atlas.crmapp.view.ActivitiesSimpleInfoView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.atlas.crmapp.R.id.tv_current_num;

// csr 核销

public class ActivitiesVerificationActivity extends BaseStatusActivity {

    @BindView(R.id.v_simple_info)
    ActivitiesSimpleInfoView vSimpleInfo;
    @BindView(R.id.tv_remain_used_num)
    TextView tvRemainUsedNum;
    @BindView(R.id.tv_set_avild)
    TextView tvSetAvild;
    @BindView(R.id.tv_set_order)
    TextView tvSetOrder;
    @BindView(R.id.tv_set_create)
    TextView tvSetCreate;
    @BindView(R.id.iv_sub)
    ImageView ivSub;
    @BindView(tv_current_num)
    TextView tvCurrentNum;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.tv_enter)
    TextView tvEnter;
    @BindView(R.id.v_contact)
    ActivitiesContactView vContact;



    private String ADD = "ADD";
    private String SUB = "SUB";


    private int currentSelectNum = 1;

    private int quantity ;//总名额

    private int consumed ;//已核销
    private int remained;// 剩余名额
    private String state;//订单状态：UNPAID- 未付款，PAID - 已支付，COMPLETE - 已核消

    private final static String KEY_ACTIVITY_URL = "KEY_ACTIVITY_URL";
    private long bookId;
    private String serialNum;

    //activity/{orderId}/{serialNum }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_verification);
        Intent intent = getIntent();
        if (intent!= null){
            String url = intent.getStringExtra(KEY_ACTIVITY_URL);
            String urls[] = url.split("/");
            if(urls.length >2){
                bookId = Long.valueOf(urls[2]);
                serialNum = urls[3];
            }
            setTitle(getString(R.string.appointment_detail));
            prepareActivityData();

        }
    }


    public static void newInstance(Activity activity , String activityCodeUrl){
        Intent intent = new Intent(activity, ActivitiesVerificationActivity.class);
        intent.putExtra("KEY_ACTIVITY_URL", activityCodeUrl);
        activity.startActivity(intent);

    }


    @Override
    protected void initActivityViews() {
        super.initActivityViews();
    }


    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        requestGetActivity(bookId);
    }

    @Override
    protected void updateActivityViews() {
        super.updateActivityViews();
    }

    @OnClick({R.id.iv_sub, R.id.iv_add, R.id.tv_enter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_sub:
                currentSelectNum = currentSelectNum - 1;
                updateSelectNum();
                break;
            case R.id.iv_add:
                currentSelectNum = currentSelectNum + 1;
                updateSelectNum();
                break;
            case R.id.tv_enter:
                requestActivityConsumed();
                break;
        }
    }


    private void updateSelectNum(){
        if(remained <= 0 || currentSelectNum <= 0){
            currentSelectNum = 0;
        }else{
             if(currentSelectNum > quantity ){
                 currentSelectNum = quantity;
             }
        }

        if(currentSelectNum == 1 ){
            ivSub.setClickable(false);
        }else{
            ivSub.setClickable(true);
        }

        if(currentSelectNum >= remained){
            ivAdd.setClickable(false);
        }else{
            ivAdd.setClickable(true);
        }

        if(currentSelectNum == 1 && remained ==1 ){
            ivSub.setClickable(false);
            ivAdd.setClickable(false);
        }
        tvCurrentNum.setText(currentSelectNum + "");

        if(currentSelectNum <= 0){
            FormatCouponInfo.setViewToTransparent(tvEnter, true);
        }else{
            FormatCouponInfo.setViewToTransparent(tvEnter, false);
        }
        tvRemainUsedNum.setText(Html.fromHtml(getString(R.string.activity_remain_and_used_num, remained, consumed)));
    }



    private void requestActivityConsumed(){
        BizDataRequest.requestActivityConsumed(this, currentSelectNum, serialNum, statusLayout, new BizDataRequest.OnResponseConsumed() {
            @Override
            public void onSuccess(ConsumedJson consumedJson) {
                showToast(getString(R.string.t48) + currentSelectNum + " "+ getString(R.string.text_ren) );
                quantity = consumedJson.getQuantity();
                state = consumedJson.getState();
                consumed = consumedJson.getConsumed();
                remained = quantity - consumed;
                updateSelectNum();

            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private void requestGetActivity(long bookId){


        BizDataRequest.requestActivityBindinfo(this, bookId, 0, "", statusLayout, new BizDataRequest.OnResponseBindinfo() {
            @Override
            public void onSuccess(ActivitiesBindinfoJson bindinfoJson) {

                BizDataRequest.requestGetActivity(ActivitiesVerificationActivity.this, bindinfoJson.getActivityId(), new BizDataRequest.OnActivity() {
                    @Override
                    public void onSuccess(ActivityJson activityJson) {
                        vSimpleInfo.updateViews(activityJson);
                    }

                    @Override
                    public void onError(DcnException error) {

                    }
                });
                updateOrderInfo(bindinfoJson);
            }

            @Override
            public void onError(DcnException error) {

            }
        });



        BizDataRequest.requestActivityUserinfo(this, bookId, statusLayout, new BizDataRequest.OnResponseActivityUserinfo() {
            @Override
            public void onSuccess(ActivitesUserInfoJson userInfoJson) {
                vContact.updateView(userInfoJson);
            }

            @Override
            public void onError(DcnException error) {

            }
        });

    }



    private void updateOrderInfo(ActivitiesBindinfoJson bindinfoJson){
        tvSetAvild.setText(FormatCouponInfo.getActivityValidTime(bindinfoJson.getStartTime(), bindinfoJson.getEndTime(), "-"));
        tvSetOrder.setText(bindinfoJson.getOrderId()  + "");
        tvSetCreate.setText(bindinfoJson.getCreateBy() == null ? "" :bindinfoJson.getCreateBy());
        state = bindinfoJson.getState();
        quantity = bindinfoJson.getQuantity();
        consumed = bindinfoJson.getConsumed();
        remained = quantity - consumed;
        serialNum = bindinfoJson.getSerialNum();
        updateSelectNum();
    }






}
