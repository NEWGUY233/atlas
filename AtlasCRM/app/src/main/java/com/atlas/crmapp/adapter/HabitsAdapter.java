package com.atlas.crmapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.HabitsJson;
import com.atlas.crmapp.model.ScoreGetDetailJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.GlideUtils;

import java.util.List;

/**
 * Created by Leo on 2018/2/1.
 */

public class HabitsAdapter extends RecyclerView.Adapter<HabitsAdapter.Holder> {

    Context c;
    private List<HabitsJson> list;
    public HabitsAdapter(Context context){
        this.c = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.item_habits,parent,false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        HabitsJson bean = list.get(position);
        holder.itemView.getLayoutParams().height = (int) (width / 118.0 * 145.0);

        GlideUtils.loadImageView(c, LoadImageUtils.small(bean.getImg()),holder.iv_rs);
        holder.tv_title.setText("#" + bean.getName());
        if (bean.isSelected())
            holder.v_cover.setVisibility(View.VISIBLE);
        else
            holder.v_cover.setVisibility(View.GONE);

        holder.iv_rs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                for (HabitsJson bean : list)
                    if (bean.isSelected())
                        count++;
                if (count <= 3) {
                    if (count == 3 && !list.get(position).isSelected())
                        return;
                    list.get(position).setSelected(!list.get(position).isSelected());
                    notifyDataSetChanged();
                    if (onCheck != null)
                        onCheck.onCheck(list.get(position).isSelected() ? count + 1 : count - 1);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<HabitsJson> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<HabitsJson> getList(){
        return this.list;
    }

    public void addLit(List<HabitsJson> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    int width;
    public void setWidth(int width){
        this.width = width / 3;

    }

    public void setOnCheck(OnCheck onCheck) {
        this.onCheck = onCheck;
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView iv_rs;
        View v_cover;
        TextView tv_title;
        public Holder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            v_cover = itemView.findViewById(R.id.v_cover);
            iv_rs = (ImageView) itemView.findViewById(R.id.iv_rs);
        }
    }

    private OnCheck onCheck;
    public interface OnCheck{
        void onCheck(int count);
    }
}
