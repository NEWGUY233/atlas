package com.atlas.crmapp.common.commonadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atlas.crmapp.listeners.OnRvItemClickListener;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/17
 *         Description :
 */

public abstract class BaseRvAdapter<T extends BaseRvViewHolder> extends RecyclerView.Adapter<T> {


    protected abstract int getLayoutId(int viewType);

    protected abstract T getViewHolder(View root, int viewType);

    protected OnRvItemClickListener mListener;

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);

        return getViewHolder(root, viewType);
    }

    public void setOnItemClickListener(OnRvItemClickListener listener) {
        this.mListener = listener;
    }



}
