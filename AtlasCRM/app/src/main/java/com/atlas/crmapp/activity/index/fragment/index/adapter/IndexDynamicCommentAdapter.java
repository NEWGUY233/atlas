package com.atlas.crmapp.activity.index.fragment.index.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.bean.DynamicCommentBean;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.usercenter.UserCardActivity;
import com.atlas.crmapp.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Created by Leo on 2018/3/20.
 */

public class IndexDynamicCommentAdapter extends RecyclerView.Adapter<IndexDynamicCommentAdapter.Holder> {
    Context c;
    private List<DynamicCommentBean.RowsBean> list;
    public IndexDynamicCommentAdapter(Context c){
        this.c = c;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.item_index_dynamic_comment,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final DynamicCommentBean.RowsBean bean = list.get(position);

        holder.name.setText(bean.getUser().getNick());
        holder.content.setText(bean.getContent());
        holder.company.setText(StringUtils.isEmpty(bean.getUser().getCompany()) ? c.getString(R.string.company_none) : "· " +  bean.getUser().getCompany());

        Glide.with(c).load(bean.getUser().getAvatar()).apply(new RequestOptions().error(R.mipmap.icon_informationheard)).into(holder.icon);
        if (bean.isLike()){
            holder.like.setImageResource(R.mipmap.icon_like_sel);
        }else {
            holder.like.setImageResource(R.mipmap.icon_like_unsel);
        }

        if (bean.getPraiseQuantity() == 0)
            holder.num_like.setVisibility(View.GONE);
        else
            holder.num_like.setVisibility(View.VISIBLE);
        holder.num_like.setText(StringUtils.numberCheck(bean.getPraiseQuantity()) + "");
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (!GlobalParams.getInstance().isLogin()){
                   return;
               }
               if (bean.isLike())return;

                BizDataRequest.likeDynamicComment(c, bean.getId(), new BizDataRequest.OnRequestResult() {
                    @Override
                    public void onSuccess() {
                        getList().get(position).setLike(true);
                        getList().get(position).setPraiseQuantity(getList().get(position).getPraiseQuantity() + 1);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onError(DcnException error) {

                    }
                });
            }
        });

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getUser() != null)
                    c.startActivity(new Intent(c, UserCardActivity.class).putExtra("id",bean.getUser().getId()));
            }
        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getUser() != null)
                    c.startActivity(new Intent(c, UserCardActivity.class).putExtra("id",bean.getUser().getId()));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click != null)
                    if (bean.getUser() != null
                            && "OWN".equals(bean.getUser().getRelate()))
                        click.onDelete(position,bean.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public List<DynamicCommentBean.RowsBean> getList() {
        return list;
    }

    public void setList(List<DynamicCommentBean.RowsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<DynamicCommentBean.RowsBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }



    public void setClick(OnDeleteClick click) {
        this.click = click;
    }

    protected class Holder extends RecyclerView.ViewHolder{
        ImageView icon;
        ImageView like;

        TextView name;
        TextView company;
        TextView content;
        TextView num_like;
        public Holder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            like = (ImageView) itemView.findViewById(R.id.iv_like);

            name = (TextView) itemView.findViewById(R.id.tv_name);
            company = (TextView) itemView.findViewById(R.id.tv_company);
            content = (TextView) itemView.findViewById(R.id.tv_comment);
            num_like = (TextView) itemView.findViewById(R.id.tv_num_like);
        }
    }

    private OnDeleteClick click;
    public interface OnDeleteClick{
        void onDelete(int position,String id);
    }
}
