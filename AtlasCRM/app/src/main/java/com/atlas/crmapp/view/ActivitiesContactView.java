package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.model.ActivitesUserInfoJson;
import com.atlas.crmapp.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 活动页面联系人信息
 * Created by hoda on 2017/12/13.
 */

public class ActivitiesContactView extends RelativeLayout {
    @BindView(R.id.v_line_1)
    View vLine1;
    @BindView(R.id.contact)
    TextView contact;
    @BindView(R.id.et_contact)
    EditText etContact;
    @BindView(R.id.v_line_2)
    View vLine2;
    @BindView(R.id.et_moblie)
    EditText etMoblie;
    @BindView(R.id.v_line_3)
    View vLine3;
    @BindView(R.id.et_wx)
    EditText etWx;
    @BindView(R.id.v_line_4)
    View vLine4;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.v_line_5)
    View vLine5;
    @BindView(R.id.tv_input_contat_info)
    TextView tvContactInfo ;

    private boolean isMustName;
    private boolean isMustMoblie = true;
    private boolean isMustWx;
    private boolean isMustRemark;

    private Context context;

    private String name;
    private String moblie;
    private String wx;
    private String remark;
    private OnInputCheckListener onInputCheckListener;
    private ActivitesUserInfoJson userInfoJson;

    public ActivitiesContactView(Context context) {
        super(context);
        initViews(context);

    }

    public ActivitiesContactView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);

    }

    public ActivitiesContactView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ActivitiesContactView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_activities_contact, this, true);
        ButterKnife.bind(this);
        etMoblie.setText(GlobalParams.getInstance().getPersonInfoJson().getMobile() + "");
    }


    public void updateView(OnInputCheckListener onInputCheckListener, ActivitesUserInfoJson userInfoJson){
        this.onInputCheckListener = onInputCheckListener;
        setUserInfoToView(userInfoJson);
        etMoblie.setText(GlobalParams.getInstance().getPersonInfoJson().getMobile());
        etContact.addTextChangedListener(textWatcher);
        etMoblie.addTextChangedListener(textWatcher);
        etWx.addTextChangedListener(textWatcher);
        etRemark.addTextChangedListener(textWatcher);
    }



    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            remark = etRemark.getText().toString().trim();
            moblie = etMoblie.getText().toString().trim();
            name = etContact.getText().toString().trim();
            wx = etWx.getText().toString().trim();
            checkInput();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };



    private void setUserInfoToView(ActivitesUserInfoJson userInfoJson){
        if(userInfoJson == null){
            return;
        }
        if(StringUtils.isNotEmpty(userInfoJson.getPhone()))
            etMoblie.setText(userInfoJson.getPhone());
        if(StringUtils.isNotEmpty(userInfoJson.getRemark()))
            etRemark.setText(userInfoJson.getRemark());
        else
            etRemark.setText(" ");

        if(StringUtils.isNotEmpty(userInfoJson.getName()))
            etContact.setText(userInfoJson.getName());
        else
            etContact.setText(" ");

        if(StringUtils.isNotEmpty(userInfoJson.getWechat()))
            etWx.setText(userInfoJson.getWechat());
        else
            etWx.setText(" ");
    }


    public void updateView(ActivitesUserInfoJson userInfoJson){

        if(userInfoJson != null){
            tvContactInfo.setText(context.getString(R.string.contact_info));
            vLine1.setVisibility(INVISIBLE);
            vLine2.setVisibility(INVISIBLE);
            vLine3.setVisibility(INVISIBLE);
            vLine4.setVisibility(INVISIBLE);
            vLine5.setVisibility(INVISIBLE);
            int color = ContextCompat.getColor(context,R.color.text_dark);
            etMoblie.setTextColor(color);
            etRemark.setTextColor(color);
            etContact.setTextColor(color);
            etWx.setTextColor(color);

            etMoblie.setEnabled(false);
            etRemark.setEnabled(false);
            etContact.setEnabled(false);
            etWx.setEnabled(false);
            setUserInfoToView(userInfoJson);
        }
    }

    private void checkInput(){
        if( onInputCheckListener != null){
            if(StringUtils.isEmpty(name) && isMustName){
                onInputCheckListener.onInputCheckListener(false, "");
            }else if(!StringUtils.isPhone(moblie) && isMustMoblie){
                onInputCheckListener.onInputCheckListener(false, "");
            }else if(StringUtils.isEmpty(wx) && isMustWx){
                onInputCheckListener.onInputCheckListener(false, "");
            }else if(StringUtils.isEmpty(remark) && isMustRemark){
                onInputCheckListener.onInputCheckListener(false, "");
            }else if(StringUtils.isNotEmpty(wx) && !StringUtils.isQQOrWX(wx)){
                onInputCheckListener.onInputCheckListener(false, "");
            }else{
                onInputCheckListener.onInputCheckListener(true, "");
            }
        }
    }


    public ActivitesUserInfoJson getActivitesUserInfo(){
        if(userInfoJson == null){
            userInfoJson = new ActivitesUserInfoJson();
        }
        userInfoJson.setPhone(etMoblie.getText().toString());
        userInfoJson.setName(etContact.getText().toString());
        userInfoJson.setRemark(etRemark.getText().toString());
        userInfoJson.setWechat(etWx.getText().toString());

        return  userInfoJson ;
    }


    public interface OnInputCheckListener{
        void onInputCheckListener(boolean isSuccess, String tip);
    }


}
