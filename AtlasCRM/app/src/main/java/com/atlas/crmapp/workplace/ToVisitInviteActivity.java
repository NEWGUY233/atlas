package com.atlas.crmapp.workplace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.VisitInviteRecordJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.ShareHelper;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.OnClick;

public class ToVisitInviteActivity extends BaseStatusActivity {

    @BindView(R.id.tv_visit_date_colon)
    TextView tvVisitDateColon;
    @BindView(R.id.tv_visit_num_colon)
    TextView tvVisitNumColon;
    @BindView(R.id.tv_visit_unit_colon)
    TextView tvVisitUnitColon;
    @BindView(R.id.tv_visit_purpose_colon)
    TextView tvVisitPurposeColon;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.tvText)
    TextView  tvRight;
    @BindView(R.id.textViewTitle)
    TextView tvTitle;


    private VisitInviteRecordJson.RowsBean  recordJson ;
    private static String KEY_VISIT = "KEY_VISIT";


    @OnClick(R.id.ibBack)
    void onClickBack(){
        finish();
    }

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    @OnClick(R.id.ll_share_wx)
    void onClickToShareWx(){
        String content = GlobalParams.getInstance().getPersonInfoJson().getNick() + getString(R.string.visit_shate_content);
        ShareHelper.shareWechartFriend(this, R.drawable.ic_launcher, Constants.CUSTOM_URL.VISIT_URL + recordJson.getId(), getString(R.string.visit_share_title), content, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {

            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {

            }
        });

    }



    @OnClick(R.id.ll_share_msg)
    void onClickToShateMsg(){
        Spanned spanned = Html.fromHtml(getString(R.string.share_msg_visit_invite, GlobalParams.getInstance().getPersonInfoJson().getNick(), Constants.CUSTOM_URL.VISIT_URL + recordJson.getId()));
        ShareHelper.sendSMS(this, spanned.toString(), null, 100);
    }

    @OnClick(R.id.tvText)
    void onClickRight(){
        if(recordJson == null){
            Logger.e("recordJson is null");
            return;
        }
        BizDataRequest.requestCancelInvite(this, statusLayout, recordJson.getId(), new BizDataRequest.OnResponseCancelInvite() {
            @Override
            public void onSuccess() {
                EventBusFactory.getBus().post(new Event.EventObject(Constants.EventType.CANCEL_VISIT_INVITE, null));
                ToVisitInviteActivity.this.finish();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_visit_invite);
        Intent intent = getIntent();
        if (intent != null){
            recordJson = (VisitInviteRecordJson.RowsBean) intent.getSerializableExtra(KEY_VISIT);
        }

        tvTitle.setText(getString(R.string.visit_invite));
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(getString(R.string.cancel_visit));
        tvRight.setTextColor(ContextCompat.getColor(this, R.color.gray_simple));

        updateActivityViews();
    }

    @Override
    protected void updateActivityViews() {
        super.updateActivityViews();

        if (recordJson == null){
            return;
        }
        rlBottom.setVisibility(View.GONE);
        tvVisitDateColon.setText(Html.fromHtml(getString(R.string.visit_date_colon, DateUtil.formatTime(recordJson.getBookDate(), "yyyy-MM-dd"))));
        tvVisitNumColon.setText(Html.fromHtml(getString(R.string.visit_num_colon, recordJson.getVisitorNum())));
        tvVisitUnitColon.setText(Html.fromHtml(getString(R.string.visit_unit_colon, recordJson.getUnitName())));
        tvVisitPurposeColon.setText(recordJson.getPurpose());
    }

    public static void newInstance(Activity activity, VisitInviteRecordJson.RowsBean recordJson){
        Intent intent = new Intent(activity, ToVisitInviteActivity.class);
        intent.putExtra(KEY_VISIT, recordJson);
        activity.startActivity(intent);

    }
}
