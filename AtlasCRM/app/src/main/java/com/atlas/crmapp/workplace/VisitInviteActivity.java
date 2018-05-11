package com.atlas.crmapp.workplace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.VisitCentersBean;
import com.atlas.crmapp.model.VisitInviteRecordJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.popupwindow.VisitonBookDatePopup;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hoda on 2017/12/21.
 */

public class VisitInviteActivity extends BaseStatusActivity {

    @BindView(R.id.et_visit_center)
    EditText etVisitCenter;
    @BindView(R.id.et_visit_time)
    EditText etVisitTime;
    @BindView(R.id.et_visit_num)
    EditText etVisitNum;
    @BindView(R.id.et_visit_purpose)
    EditText etVisitPurpose;
    @BindView(R.id.tv_send_visit)
    TextView tvSendVisit;
    @BindView(R.id.tvText)
    TextView  tvRight;
    @BindView(R.id.textViewTitle)
    TextView tvTitle;
    @BindView(R.id.ll_main)
    RelativeLayout rlMain;


    private VisitCentersBean visitCentersBean;
    private long curSelectDate;
    private int visitNum;

    @OnClick(R.id.ibBack)
    void onClickBack(){
        finish();
    }

    @OnClick(R.id.tvText)
    void onClickRight(){
        VisitInviteRecordActivity.newInstance(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_invite);
        EventBusFactory.getBus().register(this);


        visitCentersBean = new VisitCentersBean();
        //visitCentersBean.setName(GlobalParams.getInstance().getAtlasName());
        //visitCentersBean.setId(GlobalParams.getInstance().getAtlasId());
        tvTitle.setText(getString(R.string.visit_invite));
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(getString(R.string.visit_record));
        tvRight.setTextColor(ContextCompat.getColor(this, R.color.gray_simple));

        etVisitPurpose.addTextChangedListener(textWatcher);
        etVisitNum.addTextChangedListener(textWatcher);
        //FormatCouponInfo.addLayoutListener(rlMain, tvSendVisit);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            checkInput();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void checkInput (){
        String  visitNumStr = etVisitNum.getText().toString();
        String  purpose = etVisitPurpose.getText().toString().trim();
        visitNum = 0;
        if(StringUtils.isEmpty(etVisitCenter.getText().toString()) || StringUtils.isEmpty(visitNumStr) || StringUtils.isEmpty(purpose) || StringUtils.isEmpty(etVisitTime.getText().toString())  || StringUtils.isEmpty(etVisitTime.getText().toString().trim())){
            FormatCouponInfo.setViewToTransparent(tvSendVisit, true);
        }else{
            visitNum = Integer.valueOf(visitNumStr);
            if (visitNum  <= 0 ){
                FormatCouponInfo.setViewToTransparent(tvSendVisit, true);
            }else{
                FormatCouponInfo.setViewToTransparent(tvSendVisit, false);
            }
        }
    }



    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    public static void newInstance(Activity activity) {
        activity.startActivity(new Intent(activity, VisitInviteActivity.class));
    }


    @OnClick({R.id.et_visit_center, R.id.et_visit_time, R.id.tv_send_visit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_visit_center:
                SelectVisitCenterActivity.newInstance(this,  visitCentersBean );
                break;
            case R.id.et_visit_time:
                hideKeyboard(tvSendVisit);
                VisitonBookDatePopup bookDatePopup = new VisitonBookDatePopup(VisitInviteActivity.this, new VisitonBookDatePopup.OnClickOkListener() {
                    @Override
                    public void onClickOk(String selectDate) {
                        curSelectDate = DateUtil.dataOne(selectDate + " 23:59");
                        etVisitTime.setText(selectDate);
                        checkInput();
                    }
                });
                bookDatePopup.setPopupWindowFullScreen(true);
                bookDatePopup.showPopupWindow();
                break;
            case R.id.tv_send_visit:
                requestVisitorInvite();
                break;
        }
    }

    private void requestVisitorInvite(){

        BizDataRequest.requestVisitorInvite(VisitInviteActivity.this, visitCentersBean.getId(), etVisitPurpose.getText().toString().trim(), visitNum, curSelectDate, statusLayout,
                new BizDataRequest.OnResponseVisitorInvite() {
                    @Override
                    public void onSuccess(VisitInviteRecordJson.RowsBean bean) {
                        if(bean != null){
                            if(visitCentersBean != null){
                                bean.setUnitName(visitCentersBean.getName());
                            }
                            ToVisitInviteActivity.newInstance(VisitInviteActivity.this, bean);
                            VisitInviteActivity.this.finish();
                        }
                    }

                    @Override
                    public void onError(DcnException error) {

                    }
                });


    }

    @Subscribe
    public void onEventSelectBack(Event.EventObject eventObject){
        if(eventObject != null){
            if (Constants.EventType.CLICK_SELECT_CENTER_BACK.equals(eventObject.type)){
                if(eventObject.object != null){
                    visitCentersBean = (VisitCentersBean) eventObject.object;
                    etVisitCenter.setText(visitCentersBean.getName());
                }else{
                    etVisitCenter.setText("");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusFactory.getBus().unregister(this);
    }


}
