package com.atlas.crmapp.selector.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.selector.entry.Image;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/4.
 */

public class SelectorImageAdapter extends BaseQuickAdapter<Image,BaseViewHolder> {

    Context context;
    int totalSize = 0;
    public SelectorImageAdapter(Context context,int size){
        super(R.layout.selector_item_image);
        this.context = context;
        this.totalSize = size;
    }

    private ArrayList<Image> list;

    @Override
    protected void convert(BaseViewHolder helper, Image item) {
        item = getData().get(getData().size() - helper.getPosition());
        Glide.with(context).load(item.getPath()).apply(new RequestOptions().centerCrop().dontAnimate()).into((ImageView) helper.getView(R.id.iv_img));
        ((TextView)helper.getView(R.id.click)).setText("");
        helper.getView(R.id.click).setBackgroundResource(R.mipmap.icon_choseimage_unsel);

        if (list == null)
            list = new ArrayList<>();

        if (list.size() >= totalSize){
            helper.getView(R.id.iv_img_).setVisibility(View.VISIBLE);
        }

        boolean isCheck = false;
        for (int i = 0; i < list.size() ; i ++){
            if (item.equals(list.get(i))){
                ((TextView)helper.getView(R.id.click)).setText((i+1) + "");
                helper.getView(R.id.click).setBackgroundResource(R.drawable.bg_yellow_circle);
                helper.getView(R.id.iv_img_).setVisibility(View.GONE);
                isCheck = true;
                break;
            }
        }

        final Image finalItem = item;
        final boolean finalIsCheck = isCheck;
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() >= totalSize){
                    if (finalIsCheck)
                        list.remove(finalItem);
                }else {
                    if (finalIsCheck)
                        list.remove(finalItem);
                    else
                        list.add(finalItem);

                }

                notifyDataSetChanged();
                if (click != null)
                    click.onClick();
            }
        });

    }

    public ArrayList<Image> getList() {
        return list;
    }

    public void setList(ArrayList<Image> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private OnClick click;

    public void setClick(OnClick click) {
        this.click = click;
    }

    public interface OnClick{
        void onClick();
    }
}
