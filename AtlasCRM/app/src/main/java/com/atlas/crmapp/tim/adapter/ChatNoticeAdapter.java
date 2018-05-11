package com.atlas.crmapp.tim.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.bean.ChatDynamicBean;
import com.atlas.crmapp.bean.ChatNoticeBean;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.db.model.PushMsg;
import com.atlas.crmapp.usercenter.MyCouponActivity;
import com.atlas.crmapp.usercenter.MyPrintActivity;
import com.atlas.crmapp.usercenter.MyScoreActivity;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.widget.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Created by Administrator on 2018/5/2.
 */

public class ChatNoticeAdapter extends RecyclerView.Adapter<ChatNoticeAdapter.Holder> {
    Context context;
    private List<ChatNoticeBean.RowsBean> list;

    public ChatNoticeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_chat_notice, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final ChatNoticeBean.RowsBean item = list.get(position);
        holder.time.setText(DateUtil.formatTime(item.getCreateTime(),"MM-dd HH:mm"));

        holder.content.setText(StringUtils.isEmpty(item.getContent())?item.getTitle():item.getContent());
        holder.ll_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = ActionUriUtils.getIntent(context,bean.getActionType())
                if(StringUtils.isNotEmpty(item.getActionUri())){
                    Intent intent = ActionUriUtils.getIntent(context, item.getActionUri(), "", item.getTitle());
                    if(intent != null){
                        context.startActivity(intent);
                    }
                }
                if(Constants.PushMsgTpye.COUPON_BIND_MSG.equals(item.getType())){
                    context.startActivity(new Intent(context, MyCouponActivity.class));
                }

                if ("bonuspoints".equals(item.getType())){
                    context.startActivity(new Intent(context, MyScoreActivity.class));
                }else if ("print-job".equals(item.getType())){
                    context.startActivity(new Intent(context, MyPrintActivity.class));
                }else{

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public List<ChatNoticeBean.RowsBean> getList() {
        return list;
    }

    public void setList(List<ChatNoticeBean.RowsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<ChatNoticeBean.RowsBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView time;
        TextView content;
        LinearLayout ll_content;
        public Holder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
            time = (TextView) itemView.findViewById(R.id.time);
            ll_content = (LinearLayout) itemView.findViewById(R.id.ll_content);
        }
    }
}
