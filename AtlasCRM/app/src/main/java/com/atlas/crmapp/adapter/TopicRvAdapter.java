package com.atlas.crmapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.commonadapter.BaseRvAdapter;
import com.atlas.crmapp.common.commonadapter.BaseRvViewHolder;
import com.atlas.crmapp.model.VThreadsJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/20
 *         Description :
 */

public class TopicRvAdapter extends BaseRvAdapter<BaseRvViewHolder> {
    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_topic;
    }
    private Context context;
    private ArrayList<VThreadsJson> data;

    public TopicRvAdapter(Context context, ArrayList<VThreadsJson> data) {
        this.context = context;
        this.data = data;
    }

    MyViewHolder holder1;
    @Override
    protected BaseRvViewHolder getViewHolder(View root, int viewType) {
        holder1=new MyViewHolder(root);
        return new BaseRvViewHolder(root);
    }

    @Override
    public void onBindViewHolder(BaseRvViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(holder==null){
            holder1.tv_title.setTag(position);
        }

        VThreadsJson vThreadsJson =  data.get(position);
        holder1.tv_title.setText(vThreadsJson.title);
        holder1.tv_sub_title.setText(vThreadsJson.content);
        holder1.tv_num_like.setText(String.valueOf(vThreadsJson.likeCnt));
        holder1.tv_num_comment.setText(String.valueOf(vThreadsJson.commentCnt));
        Glide.with(context).load(LoadImageUtils.loadMiddleImage(vThreadsJson.image)).apply(new RequestOptions().centerCrop()).into(holder1.shape_iv);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    class MyViewHolder extends BaseRvViewHolder {
        TextView tv_title;
        TextView tv_sub_title;
        TextView tv_num_comment;
        TextView tv_num_like;
        tangxiaolv.com.library.EffectiveShapeView shape_iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_sub_title = (TextView) itemView.findViewById(R.id.tv_sub_title);
            tv_num_comment = (TextView) itemView.findViewById(R.id.tv_num_comment);
            tv_num_like = (TextView) itemView.findViewById(R.id.tv_num_like);
            shape_iv= (tangxiaolv.com.library.EffectiveShapeView) itemView.findViewById(R.id.shape_iv);
        }
    }
}
