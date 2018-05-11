package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.StringUtils;

/**
 * Created by hoda on 2017/10/10.
 */

public class CodeDialogTopInfoView extends RelativeLayout {
    private Context context;
    private TextView tvCompany,tvName;
    private ImageView ivHeader ;
    public CodeDialogTopInfoView(Context context) {
        super(context);
        initViews(context);
    }

    public CodeDialogTopInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);

    }

    public CodeDialogTopInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CodeDialogTopInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_code_dialog_top_info, this, true);
        tvCompany = (TextView) findViewById(R.id.tv_company);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivHeader  = (ImageView) findViewById(R.id.iv_header);
        updateViews();
    }

    public void updateViews(){
        PersonInfoJson personInfoJson = GlobalParams.getInstance().getPersonInfoJson();
        if(personInfoJson != null){
            String company = personInfoJson.getCompany();
            String name = personInfoJson.getNick();
            if(StringUtils.isNotEmpty(company)){
                tvCompany.setText(" | " + company);
            }
            if(StringUtils.isNotEmpty(name)){
                tvName.setText(name);
            }
            GlideUtils.loadCustomImageView(context, R.drawable.ic_user_default, LoadImageUtils.loadSmallImage(personInfoJson.getAvatar()) ,ivHeader);
        }
    }
}
