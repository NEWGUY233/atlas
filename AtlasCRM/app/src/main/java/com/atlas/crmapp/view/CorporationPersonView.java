package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.model.CompanyMemberJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoda_fang on 2017/6/7.
 * 企业员工 item
 */

public class CorporationPersonView extends LinearLayout{
    private Context context;
    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.btn_auth)
    Button mBtnAuth;

    private CompanyMemberJson member;
    
    public CorporationPersonView(Context context) {
        super(context);
        intiViews(context);
    }

    public CorporationPersonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        intiViews(context);
    }

    public CorporationPersonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intiViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CorporationPersonView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        intiViews(context);
    }

    private void intiViews(Context context){
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.item_corporation_member, this, true);
        ButterKnife.bind(this, view);
    }
    
    public void updateView(final CompanyMemberJson member){
        this.member = member;
        String avatar = member.appUser.getAvatar();
        if(!TextUtils.isEmpty(avatar)){
            GlideUtils.loadCustomImageView(context,R.drawable.ic_user_default, LoadImageUtils.loadSmallImage(avatar),mIvAvatar);
        }
        mTvName.setText(member.appUser.getNick());
        if(member.type.equals(Constants.CorporationAuthTpye.OWNER)) {
            mTvType.setText(context.getString(R.string.owner));
            mBtnAuth.setVisibility(View.INVISIBLE);
        } else if(member.type.equals(Constants.CorporationAuthTpye.AUTHORIZED)) {
            mTvType.setText(context.getString(R.string.authorized));
            mBtnAuth.setText(context.getString(R.string.cancel_auth));
            mBtnAuth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BizDataRequest.requestCancelAuthMember(context, member.id, new BizDataRequest.OnRequestResult() {
                        @Override
                        public void onSuccess() {
                            member.type = Constants.CorporationAuthTpye.COMMON;
                            updateView(member);
                        }

                        @Override
                        public void onError(DcnException error) {
                            //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        } else {
            mTvType.setText("");
            mBtnAuth.setText(context.getString(R.string.set_auth));
            mBtnAuth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BizDataRequest.requestSetAuthMember(context, member.id, new BizDataRequest.OnRequestResult() {
                        @Override
                        public void onSuccess() {
                            member.type = Constants.CorporationAuthTpye.AUTHORIZED;
                            updateView(member);
                        }

                        @Override
                        public void onError(DcnException error) {
                            // Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }
}
