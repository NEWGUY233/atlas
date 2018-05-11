package com.atlas.crmapp.activity.index.fragment.index.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.model.VThreadsJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.usercenter.UserCardActivity;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.widget.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Administrator on 2018/3/20.
 */

public class AATagTagDetailAdapter extends RecyclerView.Adapter<AATagTagDetailAdapter.Holder> {
    Context c;
    private List<VThreadsJson> rows;
    private static final String[] bgColor = {"#4ac4cd","#feaa42","#e96b61",  "#acc76d","#84d0ab","#918fc3","#f2a5a8"};
    public AATagTagDetailAdapter(Context c) {
        this.c = c;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.item_index_aat_tag_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final VThreadsJson bean = rows.get(position);
        Glide.with(c).load(bean.getAppUser().getAvatar() == null ? "" : bean.getAppUser().getAvatar())
                .apply(new RequestOptions().error(R.mipmap.icon_informationheard)).into(holder.ivIcon);
        holder.tvName.setText(bean.getAuthorName());
        holder.tvCompany.setText(bean.getAppUser().getCompany());
        holder.tvContent.setText(unicode2String(bean.getContent()));
        holder.tvTime.setText(DateUtil.formatTime(bean.getCreateTime(),"yy-MM-dd"));

        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BizDataRequest.requestSetThreadLike(c, bean.id, new BizDataRequest.OnRequestResult() {
                    @Override
                    public void onSuccess() {
                        if (!GlobalParams.getInstance().isLogin()){
                            ((BaseActivity) c).showAskLoginDialog();
                            return;
                        }
                        getRows().get(position).setLike(true);
                        getRows().get(position).setLikeCnt(getRows().get(position).getLikeCnt() + 1);
                        notifyDataSetChanged();

                    }

                    @Override
                    public void onError(DcnException error) {
                        Toast.makeText(c, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        holder.rl_bg.setBackgroundColor(Color.parseColor(bgColor[position%7]));

        if (bean.isLike()){
            holder.ivLike.setImageResource(R.mipmap.icon_like_sel);
            holder.ivLike.setClickable(false);
        }else {
            holder.ivLike.setImageResource(R.mipmap.icon_like_unsel);
            holder.ivLike.setClickable(true);
        }

        if (bean.isMore()){
            holder.tvContent.setMaxLines(200);
        }else {
            holder.tvContent.setMaxLines(3);
        }

        holder.tvLikeNum.setText((bean.getLikeCnt() == 0 ? "" : StringUtils.numberCheck(bean.getLikeCnt())) + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click != null)
                    click.onClick(position,bean);
            }
        });

        holder.ll_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRows().get(position).isMore()){
                    if (click != null)
                        click.onClick(position,bean);
                    return;
                }
                getRows().get(position).setMore(true);
                notifyDataSetChanged();
            }
        });

        holder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getAppUser() != null)
                    c.startActivity(new Intent(c, UserCardActivity.class).putExtra("id",bean.getAppUser().getId()));
            }
        });

        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getAppUser() != null)
                    c.startActivity(new Intent(c, UserCardActivity.class).putExtra("id",bean.getAppUser().getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return rows == null ? 0 : rows.size();
    }

    public List<VThreadsJson> getRows() {
        return rows;
    }

    public void setRows(List<VThreadsJson> rows) {
        this.rows = rows;
        notifyDataSetChanged();
    }

    public void setClick(OnItemClick click) {
        this.click = click;
    }

    protected class Holder extends RecyclerView.ViewHolder {
        CircleImageView ivIcon;
        ImageView ivLike;
        TextView tvName;
        TextView tvTime;
        TextView tvLikeNum;
        TextView tvCompany;
        TextView tvContent;
        RelativeLayout rl_bg;
        LinearLayout ll_more;

        public Holder(View itemView) {
            super(itemView);
            ivIcon = (CircleImageView) itemView.findViewById(R.id.iv_icon);
            ivLike = (ImageView) itemView.findViewById(R.id.iv_like);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvCompany = (TextView) itemView.findViewById(R.id.tv_company);
            tvLikeNum = (TextView) itemView.findViewById(R.id.tv_like_num);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            rl_bg = (RelativeLayout) itemView.findViewById(R.id.rl_bg);
            ll_more = (LinearLayout) itemView.findViewById(R.id.ll_show);
        }
    }


    public static String unicode2String(String s) {
        List<String> list =new ArrayList<String>();
        String zz="\\\\u[0-9,a-z,A-Z]{4}";

        //正则表达式用法参考API
        Pattern pattern = Pattern.compile(zz);
        Matcher m = pattern.matcher(s);
        while(m.find()){
            list.add(m.group());
        }
        for(int i=0,j=2;i<list.size();i++){
            String st = list.get(i).substring(j, j+4);

            //将得到的数值按照16进制解析为十进制整数，再強转为字符
            char ch = (char) Integer.parseInt(st, 16);
            //用得到的字符替换编码表达式
            s = s.replace(list.get(i), String.valueOf(ch));
        }
        return s;
    }

    private OnItemClick click;
    public interface OnItemClick{
        void onClick(int position,VThreadsJson bean);
    }
}
