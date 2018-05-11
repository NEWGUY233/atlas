package com.atlas.crmapp.adapter;

import android.view.View;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.commonadapter.BaseRvAdapter;
import com.atlas.crmapp.common.commonadapter.BaseRvViewHolder;

import butterknife.BindView;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/19
 *         Description :
 */

public class CouponListRvAdapter extends BaseRvAdapter<CouponListRvAdapter.CouponViewHolder> {


    private int selectedPos = 0;


    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_single_choice_coupon;
    }

    @Override
    protected CouponViewHolder getViewHolder(View root, int viewType) {
        return new CouponViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final CouponViewHolder holder, int position) {
        holder.mIvSelection.setSelected(position == selectedPos);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldSelected = selectedPos;
                if (selectedPos == holder.getAdapterPosition()) {
                    selectedPos = -1;
                } else {
                    selectedPos = holder.getAdapterPosition();
                    notifyItemChanged(selectedPos);
                }
                notifyItemChanged(oldSelected);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class CouponViewHolder extends BaseRvViewHolder {

        @BindView(R.id.iv_selection)
        ImageView mIvSelection;

        public CouponViewHolder(View itemView) {
            super(itemView);
        }
    }
}
