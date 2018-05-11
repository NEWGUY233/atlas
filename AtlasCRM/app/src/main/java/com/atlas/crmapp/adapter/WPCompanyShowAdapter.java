package com.atlas.crmapp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.commonadapter.BaseRvAdapter;
import com.atlas.crmapp.common.commonadapter.BaseRvViewHolder;
import com.atlas.crmapp.model.CompnayInfoJson;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Alex on 2017/3/28.
 */

public class WPCompanyShowAdapter extends BaseRvAdapter<WPCompanyShowAdapter.ViewHolder> {

    private Context mContext;
    private List<CompnayInfoJson> companyList;

    public WPCompanyShowAdapter(Context context,List<CompnayInfoJson> dataList) {
        this.mContext = context;
        companyList = dataList;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_corporation_wp_fragment;
    }

    @Override
    protected ViewHolder getViewHolder(View root, int viewType) {
        return new ViewHolder(root);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //int[] images = {R.drawable.ic_main_coffee, R.drawable.ic_main_wp, R.drawable.ic_main_fitness, R.drawable.ic_main_kitchen,R.drawable.ic_main_coffee, R.drawable.ic_main_wp, R.drawable.ic_main_wp, R.drawable.ic_main_wp, R.drawable.ic_main_wp, R.drawable.ic_main_wp, R.drawable.ic_main_wp};
        //String[] str = {"http://pic55.nipic.com/file/20141209/19462408_091857535000_2.jpg","http://img4.imgtn.bdimg.com/it/u=819201812,3553302270&fm=23&gp=0.jpg","http://pic55.nipic.com/file/20141209/19462408_091857535000_2.jpg","http://pic55.nipic.com/file/20141209/19462408_091857535000_2.jpg","http://pic55.nipic.com/file/20141209/19462408_091857535000_2.jpg","http://img4.imgtn.bdimg.com/it/u=819201812,3553302270&fm=23&gp=0.jpg","http://pic55.nipic.com/file/20141209/19462408_091857535000_2.jpg","http://pic55.nipic.com/file/20141209/19462408_091857535000_2.jpg","http://pic55.nipic.com/file/20141209/19462408_091857535000_2.jpg","http://pic55.nipic.com/file/20141209/19462408_091857535000_2.jpg","http://pic55.nipic.com/file/20141209/19462408_091857535000_2.jpg","http://pic55.nipic.com/file/20141209/19462408_091857535000_2.jpg","http://pic55.nipic.com/file/20141209/19462408_091857535000_2.jpg","http://pic55.nipic.com/file/20141209/19462408_091857535000_2.jpg","http://pic55.nipic.com/file/20141209/19462408_091857535000_2.jpg"};

        holder.ivLogo.setImageResource(R.drawable.ic_user_default);
//       CompnayInfoJson info = companyList.get(position);
//        Glide.with(mContext).load(info.thumbnail).into(holder.ivLogo);
    }


    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public class ViewHolder extends BaseRvViewHolder {

        @BindView(R.id.iv_logo)
        ImageView ivLogo;

        public ViewHolder(View itemView) {
             super(itemView);
        }

    }
}