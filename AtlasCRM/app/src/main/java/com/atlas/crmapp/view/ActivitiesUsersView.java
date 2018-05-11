package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.ActivityUserJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.GlideUtils;

import java.util.List;

/**
 * Created by hoda on 2017/11/7.
 */

public class ActivitiesUsersView extends LinearLayout {

    private ImageView ivUser1,ivUser2,ivUser3,ivUser4,ivUser5,ivUser6;
    private RelativeLayout rlUser6;
    private List<ActivityUserJson> users;
    private OnClickListener onClickListener;
    private TextView tvUserNum;
    private long recordsTotal;

    private Context context;
    public ActivitiesUsersView(Context context) {
        super(context);
        initViews(context);

    }

    public ActivitiesUsersView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);

    }

    public ActivitiesUsersView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ActivitiesUsersView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.view_activities_users, this, true);
        ivUser1 = (ImageView) findViewById(R.id.iv_user_1);
        ivUser2 = (ImageView) findViewById(R.id.iv_user_2);
        ivUser3 = (ImageView) findViewById(R.id.iv_user_3);
        ivUser4 = (ImageView) findViewById(R.id.iv_user_4);
        ivUser5 = (ImageView) findViewById(R.id.iv_user_5);
        ivUser6 = (ImageView) findViewById(R.id.iv_user_6);
        rlUser6 = (RelativeLayout) findViewById(R.id.rl_user_6);
        tvUserNum = (TextView) findViewById(R.id.tv_user_num);
    }

    public void updateViews(List<ActivityUserJson> users,long recordsTotal,  OnClickListener onClickListener){
        this.onClickListener = onClickListener;
        this.recordsTotal = recordsTotal;
        this.users = users;
        int itemSize = users.size();
        if(users == null || itemSize == 0){
            this.setVisibility(GONE);
        }else{
            if(itemSize > 0){
                setImage(0, ivUser1);
            }

            if(itemSize > 1){
                setImage(1, ivUser2);
            }

            if(itemSize > 2){
                setImage(2, ivUser3);
            }

            if(itemSize > 3){
                setImage(3, ivUser4);
            }

            if(itemSize > 4){
                setImage(4, ivUser5);
            }

            if(itemSize > 5){
                rlUser6.setVisibility(VISIBLE);
                tvUserNum.setText("+" + (recordsTotal - 5) );
                setImage(5, ivUser6);
            }
        }

    }

    private void setImage(int index , ImageView imageView){
        if(index > users.size()){
            index = 0;
        }
        imageView.setVisibility(VISIBLE);
        if(onClickListener != null){
            imageView.setOnClickListener(onClickListener);
        }
        GlideUtils.loadCustomImageView(context, R.drawable.header_icon, LoadImageUtils.loadSmallImage(users.get(index).appUser.getAvatar()), imageView );
    }
}
