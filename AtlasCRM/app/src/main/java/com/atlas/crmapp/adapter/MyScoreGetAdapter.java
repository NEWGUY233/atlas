package com.atlas.crmapp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.ScoreGetJson;
import com.atlas.crmapp.util.StringUtils;

import java.util.List;

/**
 * Created by Leo on 2018/2/1.
 */

public class MyScoreGetAdapter extends RecyclerView.Adapter<MyScoreGetAdapter.Holder> {

    Context c;
    public MyScoreGetAdapter(Context context){
        this.c = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.item_score_get,parent,false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        final ScoreGetJson bean = list.get(position);

        holder.tv_title.setText(bean.getName());
        holder.tv_score.setText(bean.getPoints() + "");

        if (!StringUtils.isEmpty(bean.getDescription())) {

            holder.ll_detail.setVisibility(View.VISIBLE);
            holder.iv_arrow.setVisibility(View.VISIBLE);

            String[] detail = bean.getDescription().trim().split("\n");
            if (detail.length > 3) {
                holder.tv_rules.setText(bean.getDescription().trim() + "");
//                String content = getContent(detail);
//                holder.iv_arrow.setVisibility(View.VISIBLE);
//                holder.tv_rules.setMaxLines(3);
                if (bean.isOpen()){
                    holder.tv_rules.setMaxLines(65530);
                    holder.iv_arrow.setVisibility(View.GONE);
                }else {
                    holder.tv_rules.setMaxLines(3);
                    holder.iv_arrow.setVisibility(View.VISIBLE);
                }
            } else {
                if (check(holder.tv_rules,detail[0].trim())){
                    if (bean.isOpen()){
                        holder.tv_rules.setMaxLines(65530);
                        holder.iv_arrow.setVisibility(View.GONE);
                    }else {
                        holder.tv_rules.setMaxLines(3);
                        holder.iv_arrow.setVisibility(View.VISIBLE);
                    }
                }else {
                    holder.tv_rules.setMaxLines(3);
                    holder.iv_arrow.setVisibility(View.GONE);
                }

                holder.tv_rules.setText(bean.getDescription().trim() + "");
            }

            Log.i("onBindViewHolder","check =" + check(holder.tv_rules,bean.getDescription()));

            holder.iv_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.get(position).setOpen(true);
                    notifyDataSetChanged();
                }
            });
        }else {
            holder.ll_detail.setVisibility(View.GONE);
            holder.iv_arrow.setVisibility(View.GONE);
        }

       checkScore(holder.iv_arrow_,holder.tv_get,bean,position);

    }

    private List<ScoreGetJson> list;
    private String score;

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<ScoreGetJson> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setClick(OnTransformationClick click) {
        this.click = click;
    }

    public void setScore(String score) {
        this.score = score;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView iv_arrow;
        ImageView iv_arrow_;
        LinearLayout ll_detail;

        TextView tv_title;
        TextView tv_score;
        TextView tv_get;
        TextView tv_rules;

        public Holder(View itemView) {
            super(itemView);
            iv_arrow = (ImageView) itemView.findViewById(R.id.iv_arrow);
            iv_arrow_ = (ImageView) itemView.findViewById(R.id.iv_arrow_);
            ll_detail = (LinearLayout) itemView.findViewById(R.id.ll_detail);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_score = (TextView) itemView.findViewById(R.id.tv_score);
            tv_get = (TextView) itemView.findViewById(R.id.tv_get);
            tv_rules = (TextView) itemView.findViewById(R.id.tv_rules);
        }
    }

    private OnTransformationClick click;

    public interface OnTransformationClick{
        void onTransformClick(int id,int score,int position);
    }

    private String getContent(String[] strings){
        String content = "";
        for (int i = 0; i < 3; i++) {
            if (i != 2)
                content += strings[i] + "\n";
            else
                content += strings[i];
        }
        return content;
    }

    private void checkScore(ImageView arrow,TextView tv_get, final ScoreGetJson bean, final int position){
        //判断积分是否足够
        if (StringUtils.isEmpty(score)){
            tv_get.setTextColor(c.getResources().getColor(R.color.score_grey));
            tv_get.setText(c.getResources().getString(R.string.user_score_jfbz));
            tv_get.setBackgroundResource(R.drawable.bg_deyellow_score);
            arrow.setVisibility(View.GONE);
            tv_get.setClickable(false);
        }else {
            try {
                if (Integer.valueOf(score) >= bean.getPoints()){
                    tv_get.setClickable(true);
                    tv_get.setTextColor(c.getResources().getColor(R.color.score_black));
                    tv_get.setText(c.getResources().getString(R.string.user_score_trans));
                    tv_get.setBackgroundResource(R.drawable.bg_yellow_score);
                    tv_get.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (click != null)
                                click.onTransformClick(bean.getId(),bean.getPoints(),position);
                        }
                    });
                    arrow.setVisibility(View.VISIBLE);
                }else {
                    tv_get.setClickable(false);
                    tv_get.setTextColor(c.getResources().getColor(R.color.score_grey));
                    tv_get.setText(c.getResources().getString(R.string.user_score_jfbz));
                    tv_get.setBackgroundResource(R.drawable.bg_deyellow_score);
                    arrow.setVisibility(View.GONE);
                }
            }catch (Exception e){
                tv_get.setClickable(false);
                tv_get.setTextColor(c.getResources().getColor(R.color.score_grey));
                tv_get.setText(c.getResources().getString(R.string.user_score_jfbz));
                tv_get.setBackgroundResource(R.drawable.bg_deyellow_score);
                arrow.setVisibility(View.GONE);
            }
        }

        //判断是否已经兑换
        if (StringUtils.isEmpty(bean.getExchangeState())){
            tv_get.setClickable(false);
        }else {
            if ("ALLOW".equals(bean.getExchangeState())) {
//                tv_get.setClickable(true);
//                tv_get.setTextColor(c.getResources().getColor(R.color.score_black));
//                tv_get.setText(c.getResources().getString(R.string.user_score_trans));
//                tv_get.setBackgroundResource(R.drawable.bg_yellow_score);
//                tv_get.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (click != null)
//                            click.onTransformClick(bean.getId(),bean.getPoints(),position);
//                    }
//                });
//                arrow.setVisibility(View.VISIBLE);
            } else {
                tv_get.setClickable(false);
                tv_get.setTextColor(c.getResources().getColor(R.color.score_grey));
                tv_get.setText(c.getResources().getString(R.string.user_score_done));
                tv_get.setBackgroundResource(R.drawable.bg_deyellow_score);
                arrow.setVisibility(View.GONE);
            }
        }
    }


    private int countLength(TextView tv,String text){
        Paint paint = new Paint();
        paint.setTextSize(tv.getTextSize());
        int text_width = (int) paint.measureText(text);// 得到总体长度
        // int width = text_width/text.length();//每一个字符的长度
        tv.getPaddingLeft();
        tv.getPaddingLeft();

        return text_width;
    }

    private int getWidth(){
        Resources resources = c.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        return width;
    }

    private boolean check(TextView tv,String text){
        int textLength = countLength(tv,text);
        int viewWidth = (int) (getWidth() - ((tv.getPaddingLeft() + tv.getPaddingRight()) / 12.0 * 13.0));

        float f = (float) ((textLength*1.0) / (viewWidth*1.0));
        Log.i("onBindViewHolder","f =" + f + " ; textLength = " + textLength + " ; viewWidth = " + viewWidth);
        if (f >= 3.0)
            return true;

        return false;
    }
}
