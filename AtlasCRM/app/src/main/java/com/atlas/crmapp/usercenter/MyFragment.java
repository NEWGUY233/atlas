package com.atlas.crmapp.usercenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.commonactivity.WebActivity;
import com.atlas.crmapp.db.hepler.PushMsgHepler;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.MyScoreJson;
import com.atlas.crmapp.model.ResponseMyInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.push.ReadPushMsg;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends BaseFragment {

    public static final String TAG = MyFragment.class.getSimpleName();

    @BindView(R.id.tv_nick)
    TextView mTvNick;
    @BindView(R.id.tv_center)
    TextView tvCenter;
    @BindView(R.id.tv_company)
    TextView mTvCompany;
    @BindView(R.id.iv_avatar)
    ImageView mTvAvatar;
    @BindView(R.id.item_corporate_account)
    LinearLayout llCorporateAccount;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_core)
    TextView tvCore;

    @BindView(R.id.iv_coupn_red_dot)
    View ivCoupnRedDot; //我的优惠券红点
    @BindView(R.id.v_line_corporate_account)
    View lineAccount;
    @BindView(R.id.iv_setting_red_dot)
    View vSettringRedDot;

    @BindView(R.id.v_line_corporate_account1)
    View lineAccount1;
    @BindView(R.id.item_my_company)
    LinearLayout llCorporateAccount1;

    @BindView(R.id.line_print)
    View line_print;
    @BindView(R.id.item_my_print)
    LinearLayout item_my_print;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean isLogin = GlobalParams.getInstance().isLogin();

    private String mParam1;
    private String mParam2;


    public MyFragment() {
        // Required empty public constructor
    }




    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_center;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        EventBusFactory.getBus().register(this);
        if(isLogin) {
            mTvNick.setText(getGlobalParams().getPersonInfoJson().getNick());
            mTvCompany.setVisibility(View.VISIBLE);
            mTvCompany.setText(getGlobalParams().getPersonInfoJson().getCompany());
            String avatar = getGlobalParams().getPersonInfoJson().getAvatar();
            if(avatar != null && avatar.length()>4) {
                try {
                    setImage(avatar,mTvAvatar);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if(getGlobalParams().isHasContract()) {
                llCorporateAccount.setVisibility(View.GONE);
                lineAccount.setVisibility(View.GONE);

                llCorporateAccount1.setVisibility(View.VISIBLE);
                lineAccount1.setVisibility(View.VISIBLE);

//                lineAccount1.setVisibility(View.GONE);
//                llCorporateAccount1.setVisibility(View.GONE);

            }else {
                lineAccount.setVisibility(View.GONE);
                llCorporateAccount.setVisibility(View.GONE);

                lineAccount1.setVisibility(View.GONE);
                llCorporateAccount1.setVisibility(View.GONE);
            }

            if (getGlobalParams().getAtlasId() == 4){
                line_print.setVisibility(View.GONE);
                item_my_print.setVisibility(View.GONE);
            }else {
                line_print.setVisibility(View.VISIBLE);
                item_my_print.setVisibility(View.VISIBLE);
            }
        } else {
            mTvCompany.setVisibility(View.GONE);
        }
        int num = PushMsgHepler.selectUnreadMsgNumber(Constants.PushMsgTpye.COUPON_BIND_MSG);
        if(GlobalParams.getInstance().isLogin()){
            Logger.d("num-----" + num);
            ivCoupnRedDot.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
        }else{
            ivCoupnRedDot.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getReadPushMsgShowCoupnRedDot(ReadPushMsg readPushMsg){
        if(readPushMsg!= null){
            if(Constants.PushMsgTpye.COUPON_BIND_MSG.equals(readPushMsg.getType())){
                ivCoupnRedDot.setVisibility(readPushMsg.isHaveUnRead() == true? View.VISIBLE : View.GONE);
            }
        }
    }


    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        isLogin = GlobalParams.getInstance().isLogin();

        if(isLogin) {
            mTvNick.setText(getGlobalParams().getPersonInfoJson().getNick());
            String myCompany = getGlobalParams().getPersonInfoJson().getCompany();
            if(StringUtils.isNotEmpty(myCompany)){
                mTvCompany.setVisibility(View.VISIBLE);
                mTvCompany.setText(myCompany);
            }else{
                mTvCompany.setVisibility(View.GONE);
            }

            String avatar = getGlobalParams().getPersonInfoJson().getAvatar();
            if(avatar != null && avatar.length()>4) {
                try {
                    setImage(avatar, mTvAvatar);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }else{
                mTvAvatar.setImageResource(R.drawable.header_icon);
            }
            if(getGlobalParams().isHasContract()) {
                llCorporateAccount.setVisibility(View.GONE);
                lineAccount.setVisibility(View.GONE);

                llCorporateAccount1.setVisibility(View.VISIBLE);
                lineAccount1.setVisibility(View.VISIBLE);

//                lineAccount1.setVisibility(View.GONE);
//                llCorporateAccount1.setVisibility(View.GONE);
            }else {
                lineAccount.setVisibility(View.GONE);
                llCorporateAccount.setVisibility(View.GONE);

                lineAccount1.setVisibility(View.GONE);
                llCorporateAccount1.setVisibility(View.GONE);
            }

        } else {
            mTvCompany.setVisibility(View.INVISIBLE);
            mTvCompany.setText("");
            mTvNick.setText(R.string.t78);
            mTvAvatar.setImageResource(R.drawable.ic_user_default);
        }

        String atlasName = GlobalParams.getInstance().getAtlasName();
        if(StringUtils.isNotEmpty(atlasName)){
            tvCenter.setText(atlasName + " >");

            if (getGlobalParams().getAtlasId() == 4){
                line_print.setVisibility(View.GONE);
                item_my_print.setVisibility(View.GONE);
            }else {
                line_print.setVisibility(View.VISIBLE);
                item_my_print.setVisibility(View.VISIBLE);
            }
        }



        if(tvAmount != null) {
            tvAmount.setText("");
            if(isLogin){
                BizDataRequest.requestMyInfo(getActivity(), false, null, new BizDataRequest.OnResponseMyInfoJson() {
                    @Override
                    public void onSuccess(ResponseMyInfoJson responseMyInfoJson) {
                        if(responseMyInfoJson != null){
                            String  price = FormatCouponInfo.formatDoublePrice(responseMyInfoJson.getAmount() , 2);
                            if(StringUtils.isNotEmpty(price)){
                                if(tvAmount != null)
                                    tvAmount.setText(price + getString(R.string.yuan));
                            }
                        }
                    }

                    @Override
                    public void onError(DcnException error) {

                    }
                });
            }
        }
        if(tvCore != null) {
            tvCore.setText("");
            if(isLogin){
                BizDataRequest.requestMyScore(getActivity(), false, null, new BizDataRequest.OnResponseMyScoreJson() {
                    @Override
                    public void onSuccess(MyScoreJson responseMyScoreJson) {
                        if(responseMyScoreJson != null){
                            String  price = FormatCouponInfo.formatDoublePrice(responseMyScoreJson.getPoints() , 0);
                            if(StringUtils.isNotEmpty(price)){
                                if(tvCore != null)
                                    tvCore.setText(price);
                            }
                        }
                    }

                    @Override
                    public void onError(DcnException error) {

                    }
                });

            }
        }

        if(StringUtils.isNotEmpty(FormatCouponInfo.getOnBuglyUpdateVersion())){
            vSettringRedDot.setVisibility(View.VISIBLE);
        }else{
            vSettringRedDot.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusFactory.getBus().unregister(this);
    }

    @OnClick({R.id.item_vip_benefit, R.id.tv_nick, R.id.item_user_info, R.id.item_my_order,
            R.id.item_my_reservation, R.id.item_my_wallet, R.id.item_my_coupon,
            R.id.item_corporate_account, R.id.item_setting, R.id.iv_avatar,
            R.id.item_my_code, R.id.item_my_score, R.id.item_my_company, R.id.item_my_print,R.id.item_my_act})
    public void onClick(View view) {
        if(view.getId() == R.id.item_setting) {
            //skipToActivity(ActivitiesApplyEnterActivity.class);
            skipToActivity(MySettingActivity.class);
        }else if(view.getId() == R.id.item_vip_benefit){
            WebActivity.newInstance(getActivity(), Constants.CUSTOM_URL.VIP_BENFIT ,getActivity().getString(R.string.vip_benefit), true);
        } else {
            if(isLogin) {
                switch (view.getId()) {
                    case R.id.item_user_info:
                        skipToActivity(UserInfoActivity.class);
                        break;
                    case R.id.item_my_order:
                        skipToActivity(MyOrderActivity.class);
                        break;
                    case R.id.item_my_reservation:
                        skipToActivity(MyAppointmentActivity.class);
                        break;
                    case R.id.item_my_wallet:
                        skipToActivity(MyWalletActivity.class);
                        break;
                    case R.id.item_my_coupon:
                        skipToActivity(MyCouponActivity.class);
                        break;
                    case R.id.item_corporate_account:
                        skipToActivity(MyCompanyActivity.class);
                        break;
                    case R.id.item_my_code:
                        skipToActivity(MyCodeActivity.class);
                        break;
                    case  R.id.iv_avatar:
                    case R.id.tv_nick:
                        skipToActivity(UserInfoActivity.class);
                        break;
                    case R.id.item_my_score:
//                      我的积分
//                        Toast.makeText(getContext(),getString(R.string.user_center_score),Toast.LENGTH_LONG).show();
                        skipToActivity(MyScoreActivity.class);
                        break;
                    case R.id.item_my_company:
//                       企业账号
//                        Toast.makeText(getContext(),getString(R.string.user_center_company),Toast.LENGTH_LONG).show();
                        skipToActivity(MyCompany_Activity.class);
                        break;
                    case R.id.item_my_print:
//                       打印记录
//                        Toast.makeText(getContext(),getString(R.string.user_center_print),Toast.LENGTH_LONG).show();
                        skipToActivity(MyPrintActivity.class);
                        break;
                    case R.id.item_my_act:
                        skipToActivity(MyDynamicActivity.class);
                        break;
                }
            } else {
                showAskLoginDialog(getHoldingActivity());
            }
        }
    }

    @OnClick(R.id.tv_center)
    void onClickToChangerCenter(){
        skipToActivity(ChangeCityCenterActivity.class);
    }

    private void skipToActivity(Class cls){
        Intent intent = new Intent(getActivity(), cls);
        getActivity().startActivity(intent);
    }

    private void setImage(String avatar, ImageView imageView){
        if(!TextUtils.isEmpty(avatar)){
            if ("http".equals(avatar.substring(0,4))){
                //显示网络
                GlideUtils.loadCustomImageView(getContext(), R.drawable.header_icon,LoadImageUtils.loadSmallImage(avatar),imageView);
            }else {
                // 显示本地的
                File file = new File(avatar);
                Glide.with(getContext()).load(file).apply(new RequestOptions().dontAnimate().placeholder(R.drawable.header_icon)).into(imageView);
            }
        }
    }
}
