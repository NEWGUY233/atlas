package com.atlas.crmapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.ecosphere.TopicDetailActivity;
import com.atlas.crmapp.model.VThreadsJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Alex on 2017/5/16.
 */

public class TalkAdapter extends BaseQuickAdapter<VThreadsJson, BaseViewHolder> {

    private Context context;
    private List<VThreadsJson> data;

    public TalkAdapter(Context context, List<VThreadsJson> data) {
        super(R.layout.item_topic, data);
        this.context =context ;
        this.data =data;
    }

    @Override
    protected void convert(BaseViewHolder helper, final VThreadsJson item) {
        GlideUtils.loadCustomImageView(context, R.drawable.product, LoadImageUtils.loadMiddleImage(item.getImage()), (ImageView)helper.getView(R.id.shape_iv));
        //Glide.with(context).load(LoadImageUtils.loadMiddleImage(item.getImage())).dontAnimate().placeholder(R.drawable.banner).centerCrop().into((ImageView) helper.getView(R.id.shape_iv));
        helper.setText(R.id.tv_title, item.title)
                .setText(R.id.tv_sub_title, item.content)
                .setText(R.id.tv_num_comment, String.valueOf(item.commentCnt))
                .setText(R.id.tv_num_like, String.valueOf(item.likeCnt));
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TopicDetailActivity.class);
                intent.putExtra("id", item.getId());
                context.startActivity(intent);
            }
        });
    }


    /*public TalkAdapter(Context context, ArrayList<VThreadsJson> data) {
        this.context = context;
        this.data = data;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return data.size() == 0 ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position) {

            return TYPE_ITEM;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_topic, parent,
                    false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_footer, parent,
                    false);
            return new FootViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemHolder = ((ItemViewHolder) holder);
            final VThreadsJson vThreadsJson =  data.get(position);

            itemHolder.tv_title.setText(vThreadsJson.title);
            itemHolder.tv_sub_title.setText(vThreadsJson.content);
            itemHolder.tv_num_like.setText(String.valueOf(vThreadsJson.likeCnt));
            itemHolder.tv_num_comment.setText(String.valueOf(vThreadsJson.commentCnt));

            Glide.with(context).load(LoadImageUtils.loadMiddleImage(vThreadsJson.image)).placeholder(R.drawable.banner).dontAnimate().centerCrop().into(((ItemViewHolder) holder).shape_iv);

            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, position);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(holder.itemView, position);
                        return false;
                    }
                });
            }
        }
    }


    static class ItemViewHolder extends ViewHolder {

        TextView tv_title;
        TextView tv_sub_title;
        TextView tv_num_comment;
        TextView tv_num_like;
        tangxiaolv.com.library.EffectiveShapeView shape_iv;

        public ItemViewHolder(View view) {
            super(view);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_sub_title = (TextView) itemView.findViewById(R.id.tv_sub_title);
            tv_num_comment = (TextView) itemView.findViewById(R.id.tv_num_comment);
            tv_num_like = (TextView) itemView.findViewById(R.id.tv_num_like);
            shape_iv= (tangxiaolv.com.library.EffectiveShapeView) itemView.findViewById(R.id.shape_iv);
        }
    }

    static class FootViewHolder extends ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }
*/
}
