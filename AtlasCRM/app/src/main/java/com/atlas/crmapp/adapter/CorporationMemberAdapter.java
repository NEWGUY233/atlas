package com.atlas.crmapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.model.CompanyMemberJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.GlideUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alex on 2017/4/21.
 */

public class CorporationMemberAdapter extends RecyclerView.Adapter<CorporationMemberAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CompanyMemberJson> data;
    public CorporationMemberAdapter(Context context, ArrayList<CompanyMemberJson> data) {
        this.context = context;
        this.data = data;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewHolder holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_corporation_member, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CompanyMemberJson member = data.get(position);
        String avatar = member.appUser.getAvatar();
        if(!TextUtils.isEmpty(avatar)){
            GlideUtils.loadCustomImageView(context,R.drawable.ic_user_default, LoadImageUtils.loadSmallImage(avatar),holder.mIvAvatar);
        }
        holder.mTvName.setText(member.appUser.getNick());
        if(member.type.equals(Constants.CorporationAuthTpye.OWNER)) {
            holder.mTvType.setText(context.getString(R.string.owner));
            holder.mBtnAuth.setVisibility(View.INVISIBLE);
        } else if(member.type.equals(Constants.CorporationAuthTpye.AUTHORIZED)) {
            holder.mTvType.setText(context.getString(R.string.authorized));
            holder.mBtnAuth.setText(context.getString(R.string.cancel_auth));
            holder.mBtnAuth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BizDataRequest.requestCancelAuthMember(context, member.id, new BizDataRequest.OnRequestResult() {
                        @Override
                        public void onSuccess() {
                            member.type = Constants.CorporationAuthTpye.COMMON;
                            //holder.mBtnAuth.setText(R.string.set_auth);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onError(DcnException error) {
                            //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        } else {
            holder.mBtnAuth.setText(context.getString(R.string.set_auth));
            holder.mBtnAuth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BizDataRequest.requestSetAuthMember(context, member.id, new BizDataRequest.OnRequestResult() {
                        @Override
                        public void onSuccess() {
                            member.type = Constants.CorporationAuthTpye.AUTHORIZED;
                            //holder.mBtnAuth.setText(R.string.cancel_auth);
                            notifyDataSetChanged();
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

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        @BindView(R.id.iv_avatar)
        ImageView mIvAvatar;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_type)
        TextView mTvType;
        @BindView(R.id.btn_auth)
        Button mBtnAuth;
    }
}