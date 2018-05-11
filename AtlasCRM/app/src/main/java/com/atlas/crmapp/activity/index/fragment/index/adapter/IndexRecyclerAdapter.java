package com.atlas.crmapp.activity.index.fragment.index.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.NavFragmentsActivity;
import com.atlas.crmapp.activity.index.fragment.index.IndexFragment;
import com.atlas.crmapp.activity.index.fragment.index.activity.IndexDynamicDetailActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagCentreActivity;
import com.atlas.crmapp.bean.IndexMomentBean;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.usercenter.UserCardActivity;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.widget.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/16.
 */

public class IndexRecyclerAdapter extends RecyclerView.Adapter<IndexRecyclerAdapter.Holder> {
    Context c;
    private List<IndexMomentBean.RowsBean> list;
    public IndexRecyclerAdapter(Context c) {
        this.c = c;
    }

    IndexFragment fragment;
    public IndexRecyclerAdapter(IndexFragment fragment) {
        this.c = fragment.getContext();
        this.fragment = fragment;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.item_index_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final IndexMomentBean.RowsBean bean = list.get(position);

        holder.llType1.setVisibility(View.GONE);
        holder.llType2.setVisibility(View.GONE);
        holder.llType3.setVisibility(View.GONE);
        holder.rcvPic.setVisibility(View.GONE);
        if (bean.getSubDetail() != null){
            holder.llTag.setVisibility(View.VISIBLE);

            final IndexMomentBean.RowsBean.SubDetailBean subDetailBean = bean.getSubDetail();

            if (StringUtils.isEmpty(bean.getSubDetail().getSource())){
                holder.llTag.setVisibility(View.GONE);
            }else {
                holder.llTag.setVisibility(View.VISIBLE);
            }

            holder.tvTag.setText("「 " + bean.getSubDetail().getSource() + " 」");

            if (StringUtils.isEmpty(subDetailBean.getImg())){
                holder.llType1.setVisibility(View.VISIBLE);

                ((TextView)holder.llType1.findViewById(R.id.type1_title)).setText(subDetailBean.getTitle());
                ((TextView)holder.llType1.findViewById(R.id.type1_content)).setText(subDetailBean.getContent());
            }else {

                if (StringUtils.isEmpty(subDetailBean.getContent())){
                    holder.llType2.setVisibility(View.VISIBLE);
                    ((TextView)holder.llType2.findViewById(R.id.type2_title)).setText(subDetailBean.getTitle());
                    Glide.with(c).load(subDetailBean.getImg()).apply(new RequestOptions().centerCrop())
                            .into((ImageView) holder.llType2.findViewById(R.id.type2_pic));
                }else {
                    holder.llType3.setVisibility(View.VISIBLE);
                    ((TextView)holder.llType3.findViewById(R.id.type3_title)).setText(subDetailBean.getTitle());
                    ((TextView)holder.llType3.findViewById(R.id.type3_content)).setText(subDetailBean.getContent());
                    Glide.with(c).load(subDetailBean.getImg()).apply(new RequestOptions().centerCrop())
                            .into((ImageView) holder.llType3.findViewById(R.id.type3_pic));
                }

            }
            holder.include_content.setClickable(true);
            holder.include_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = ActionUriUtils.getIntent(c,bean.getSubDetail().getActionUri(),"","");
                    if (i != null)
                        c.startActivity(i);
                }
            });

            holder.tvTag.setClickable(true);
            holder.tvTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    c.startActivity(new Intent(c, TagCentreActivity.class).putExtra("id",subDetailBean.getForumId()));
                }
            });

        }else {
            holder.tvTag.setClickable(false);
            holder.include_content.setClickable(false);
            holder.rcvPic.setVisibility(View.VISIBLE);
            holder.llTag.setVisibility(View.GONE);

            List<String> list = bean.getImgs();
            if (list != null && list.size() > 0) {
                if (list.size() == 1) {
                    holder.rcvPic.setLayoutManager(new GridLayoutManager(c, 1));
                } else if (list.size() == 3) {
                    holder.rcvPic.setLayoutManager(new GridLayoutManager(c, 2));
                } else {
                    holder.rcvPic.setLayoutManager(new GridLayoutManager(c, 3));
                }
                holder.rcvPic.setAdapter(new IndexRecyclerPicAdapter(c, list));
            }else {
                holder.rcvPic.setAdapter(new IndexRecyclerPicAdapter(c, null));
            }
        }

        holder.tvComment.setText(StringUtils.unicode2String(bean.getContent()));
        if (StringUtils.isEmpty(bean.getContent())) {
            holder.tvComment.setVisibility(View.GONE);
        }else {
            holder.tvComment.setVisibility(View.VISIBLE);
        }

        IndexMomentBean.RowsBean.UserBean user = bean.getUser();
        Glide.with(c).load(user.getAvatar()).apply(new RequestOptions().error(R.mipmap.icon_informationheard))
                .into(holder.ivIcon);
        holder.tvName.setText(user.getNick());
        holder.tvCompany.setText(user.getCompany());
        if (StringUtils.isEmpty(user.getRelate())){
            holder.tvTips.setVisibility(View.GONE);
        }else {
            holder.tvTips.setVisibility(View.VISIBLE);
            if ("OFFICIAL".equals(user.getRelate())) {
                holder.tvTips.setImageResource(R.mipmap.tips_icon_offical);
                holder.ivIcon.setImageResource(R.mipmap.icon_homepic);
            }else if ("FRIEND".equals(user.getRelate()))
                holder.tvTips.setImageResource(R.mipmap.tips_icon_friend);
            else
                holder.tvTips.setVisibility(View.GONE);
        }

        holder.tv_num_comment.setText(StringUtils.numberCheck(bean.getCommentQuantity()) + "");
        holder.tv_num_like.setText(StringUtils.numberCheck(bean.getPraiseQuantity()) + "");

        if (bean.isPraised()){
            holder.iv_like.setImageResource(R.mipmap.icon_like_sel);
        }else {
            holder.iv_like.setImageResource(R.mipmap.icon_like_unsel);
        }

        holder.ll_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BizDataRequest.likeDynamic(c, bean.getId(), new BizDataRequest.OnRequestResult() {
                    @Override
                    public void onSuccess() {
                        getList().get(position).setPraiseQuantity(getList().get(position).getPraiseQuantity() + 1);
                        getList().get(position).setPraised(true);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onError(DcnException error) {

                    }
                });
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bean.isPost()) {
                    if (fragment != null)
                        fragment.startActivityForResult(new Intent(c, IndexDynamicDetailActivity.class).putExtra("data", bean), 0x334);
                    else
                        ((Activity) c).startActivityForResult(new Intent(c, IndexDynamicDetailActivity.class).putExtra("data", bean), 0x334);
                }
                if (bean.isFailed()){
                    if (onPostFailed != null)
                        onPostFailed.onFailed();
                }
            }
        });

        if (bean.isFailed()){
            holder.error_1.setVisibility(View.VISIBLE);
            holder.error_2.setVisibility(View.VISIBLE);
        }else {
            holder.error_1.setVisibility(View.GONE);
            holder.error_2.setVisibility(View.GONE);
        }

        if (bean.isPost()){
            holder.ll_like.setClickable(false);
        }else {
            holder.ll_like.setClickable(true);
        }

        holder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getUser() != null && !"OFFICIAL".equals(bean.getUser().getRelate()))
                    c.startActivity(new Intent(c, UserCardActivity.class).putExtra("id",bean.getUser().getId()));
            }
        });
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getUser() != null && !"OFFICIAL".equals(bean.getUser().getRelate()))
                    c.startActivity(new Intent(c, UserCardActivity.class).putExtra("id",bean.getUser().getId()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public List<IndexMomentBean.RowsBean> getList() {
        return list;
    }

    public void setList(List<IndexMomentBean.RowsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setOnPostFailed(OnPostFailed onPostFailed) {
        this.onPostFailed = onPostFailed;
    }

    class Holder extends RecyclerView.ViewHolder {
        CircleImageView ivIcon;
        TextView tvName;
        TextView tvTitle;
        TextView tvCompany;
        TextView tvComment;
        LinearLayout llType1;
        LinearLayout llType2;
        RelativeLayout llType3;
        RecyclerView rcvPic;
        TextView tvTag;
        ImageView tvTips;
        LinearLayout llTag;
        LinearLayout ll_comment;
        TextView tv_num_comment;
        LinearLayout ll_like;
        TextView tv_num_like;
        ImageView iv_like;

        View include_content;
        View error_1;
        View error_2;

        public Holder(View itemView) {
            super(itemView);
            ivIcon = (CircleImageView) itemView.findViewById(R.id.iv_icon);
            tvTips = (ImageView) itemView.findViewById(R.id.iv_tips);
            iv_like = (ImageView) itemView.findViewById(R.id.iv_like);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvCompany = (TextView) itemView.findViewById(R.id.tv_company);
            tvComment = (TextView) itemView.findViewById(R.id.tv_comment);
            tvTag = (TextView) itemView.findViewById(R.id.tv_tag);
            tv_num_comment = (TextView) itemView.findViewById(R.id.tv_num_comment);
            tv_num_like = (TextView) itemView.findViewById(R.id.tv_num_like);

            llType1 = (LinearLayout) itemView.findViewById(R.id.ll_type_1);
            llType2 = (LinearLayout) itemView.findViewById(R.id.ll_type_2);
            llType3 = (RelativeLayout) itemView.findViewById(R.id.ll_type_3);
            llTag = (LinearLayout) itemView.findViewById(R.id.ll_tag);

            rcvPic = (RecyclerView) itemView.findViewById(R.id.rcv_pic);

            ll_comment = (LinearLayout) itemView.findViewById(R.id.ll_comment);
            ll_like = (LinearLayout) itemView.findViewById(R.id.ll_like);

            include_content =  itemView.findViewById(R.id.include_content);
            error_1 =  itemView.findViewById(R.id.error_1);
            error_2 =  itemView.findViewById(R.id.error_2);
        }
    }

    private OnPostFailed onPostFailed;
    public interface OnPostFailed{
        void onFailed();
    }
}
