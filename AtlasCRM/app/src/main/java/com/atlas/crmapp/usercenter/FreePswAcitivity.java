package com.atlas.crmapp.usercenter;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class FreePswAcitivity extends BaseStatusActivity {

    @BindView(R.id.item_set_free_pws)
    LinearLayout itemSetFreePws;

    @BindView(R.id.sc_free_pws_pay)
    SwitchCompat scFreePwsPay;

    @BindView(R.id.ll_free_pay_num)
    LinearLayout llFreePayNum;
    @BindView(R.id.iv_free_0)
    ImageView ivFree0;
    @BindView(R.id.iv_free_50)
    ImageView ivFree50;
    @BindView(R.id.iv_free_100)
    ImageView ivFree100;
    @BindView(R.id.iv_free_200)
    ImageView ivFree200;


    @OnClick({R.id.rl_free_pay_0, R.id.rl_free_pay_50, R.id.rl_free_pay_100, R.id.rl_free_pay_200})
    void onClickFreePay(View view) {
        String tag = (String) view.getTag();
        int tagFreePay = Integer.parseInt(tag);
        if(tagFreePay<0){
            tagFreePay = -1;
        }else if (tagFreePay > 101){
            tagFreePay = 200;
        }else if(tagFreePay > 51){
            tagFreePay = 100;
        } else if(tagFreePay > 1){
            tagFreePay = 50;
        }else{
            tagFreePay = 0;
        }
        GlobalParams.getInstance().getPersonInfoJson().setNoCountPassword(tagFreePay);
        requestUpdateUserInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_psw_acitivity);
        setTitle(getString(R.string.free_pws_pay));
        updateActivityViews();
    }

    @Override
    protected void updateActivityViews() {
        super.updateActivityViews();

        scFreePwsPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scFreePwsPay.isChecked()){
                    GlobalParams.getInstance().getPersonInfoJson().setNoCountPassword(0);
                }else {
                    GlobalParams.getInstance().getPersonInfoJson().setNoCountPassword(-1);
                }
                requestUpdateUserInfo();
            }
        });
        updateFreePay();
    }

    private void updateFreePay() {
        int freePriceNum = GlobalParams.getInstance().getPersonInfoJson().getNoCountPassword();
        if (freePriceNum >= 0) {
            scFreePwsPay.setChecked(true);
            llFreePayNum.setVisibility(View.VISIBLE);
            if (freePriceNum == 0) {
                ivFree0.setImageResource(R.drawable.sex_selected);
                ivFree50.setImageResource(R.drawable.sex_not_select);
                ivFree100.setImageResource(R.drawable.sex_not_select);
                ivFree200.setImageResource(R.drawable.sex_not_select);
            } else if (freePriceNum == 50) {
                ivFree0.setImageResource(R.drawable.sex_not_select);
                ivFree50.setImageResource(R.drawable.sex_selected);
                ivFree100.setImageResource(R.drawable.sex_not_select);
                ivFree200.setImageResource(R.drawable.sex_not_select);
            } else if (freePriceNum == 100) {
                ivFree0.setImageResource(R.drawable.sex_not_select);
                ivFree50.setImageResource(R.drawable.sex_not_select);
                ivFree100.setImageResource(R.drawable.sex_selected);
                ivFree200.setImageResource(R.drawable.sex_not_select);
            } else if (freePriceNum == 200) {
                ivFree0.setImageResource(R.drawable.sex_not_select);
                ivFree50.setImageResource(R.drawable.sex_not_select);
                ivFree100.setImageResource(R.drawable.sex_not_select);
                ivFree200.setImageResource(R.drawable.sex_selected);
            }
        } else {
            scFreePwsPay.setChecked(false);
            llFreePayNum.setVisibility(View.GONE);
        }
    }

    private void requestUpdateUserInfo() {
        HashMap<String ,Object> params = new HashMap<>();
        params.put("noCountPassword", GlobalParams.getInstance().getPersonInfoJson().getNoCountPassword());
        BizDataRequest.requestModifyUserInfo(FreePswAcitivity.this, params, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                updateActivityViews();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

}
