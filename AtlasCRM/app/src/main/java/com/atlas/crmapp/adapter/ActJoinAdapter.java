package com.atlas.crmapp.adapter;

import android.content.Context;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.commonadapter.BaseRvAdapter;
import com.atlas.crmapp.common.commonadapter.BaseRvViewHolder;
import com.atlas.crmapp.ecosphere.ParticipantsActivity;
import com.atlas.crmapp.model.ActivityUserJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.widget.CircleImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Alex on 2017/4/18.
 */

public class ActJoinAdapter extends BaseRvAdapter<ActJoinAdapter.ViewHolder> {

    private Context mContext;
    private List<ActivityUserJson> userList;
    private long id;
    public ActJoinAdapter(Context context,List<ActivityUserJson> dataList,long id) {
        this.mContext = context;
        userList = dataList;
        this.id = id;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_corporation_wp_fragment;
    }

    @Override
    protected ViewHolder getViewHolder(View root, int viewType) {
        return new ActJoinAdapter.ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ActJoinAdapter.ViewHolder holder, int position) {
        final ActivityUserJson user = userList.get(position);
        GlideUtils.loadCustomImageView(mContext, R.drawable.header_icon,LoadImageUtils.loadSmallImage(user.appUser.getAvatar()),holder.ivLogo );
        holder.ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParticipantsActivity.startActivity(mContext,id);
                //mContext.startActivity(new Intent(mContext, UserCardActivity.class).putExtra("uid", user.appUser.getUid()));
            }
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends BaseRvViewHolder {

        @BindView(R.id.iv_logo)
        CircleImageView ivLogo;
        public ViewHolder(View itemView) {
            super(itemView);
        }

    }
}