package com.atlas.crmapp.adapter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.listeners.OnNumberChangeListener;
import com.atlas.crmapp.model.bean.ItemCoffeeProduct;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.widget.pinnedheaderlist.SectionedBaseAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/16
 *         Description :
 */

public class CoffeeProductItemAdapter extends SectionedBaseAdapter {



    private List<ItemCoffeeProduct> mList;
    private OnNumberChangeListener mListener;

    public CoffeeProductItemAdapter(List<ItemCoffeeProduct> list) {
        mList = list;
    }

    @Override
    public Object getItem(int section, int position) {
        return mList.get(section).getProductEntityList().get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return mList.size();
    }

    @Override
    public int getCountForSection(int section) {
        return mList.get(section).getProductEntityList().size();
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {

        final ItemViewHolder holder;

        final ItemCoffeeProduct.ProductEntity child = mList.get(section).getProductEntityList().get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coffee_product, null);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ItemViewHolder) convertView.getTag();
        }

        holder.mTvItemCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                child.setNum(Integer.parseInt(s.toString()));
                holder.mIvSub.setVisibility(child.getNum() > 0 ? View.VISIBLE : View.GONE);
                holder.mTvItemCount.setVisibility(holder.mIvSub.getVisibility());


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.mTvName.setText(child.getName());
        holder.mTvPrice.setText(FormatCouponInfo.getYuanStr() + child.getPrice());

        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {

        HeaderViewHolder holder;
        ItemCoffeeProduct item = mList.get(section);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coffee_product_header, parent, false);
            holder = new HeaderViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        holder.mTvHeader.setText(item.getCategoryName());

        return convertView;
    }

    public void setOnNumberChangeListener(OnNumberChangeListener listener) {
        this.mListener = listener;
    }


    class ItemViewHolder {

        @BindView(R.id.tv_item_count)
        TextView mTvItemCount;
        @BindView(R.id.iv_sub)
        ImageView mIvSub;
        @BindView(R.id.iv_add)
        ImageView mIvAdd;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_price)
        TextView mTvPrice;

        public ItemViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.iv_add)
        void onAddClick() {
            int count = getItemCount();
            mTvItemCount.setText(++count + "");
            if (mListener != null) {
                mListener.onChanged();
            }
        }

        @OnClick(R.id.iv_sub)
        void onSubClick() {
            int count = getItemCount();
            if (--count >= 0) {
                mTvItemCount.setText(count + "");
            }
            if (mListener != null) {
                mListener.onChanged();
            }
        }

        private int getItemCount() {

            if (TextUtils.isEmpty(mTvItemCount.getText().toString())) {
                return 0;
            } else {
                return Integer.parseInt(mTvItemCount.getText().toString());
            }

        }
    }

    class HeaderViewHolder {

        @BindView(R.id.tv_header)
        TextView mTvHeader;

        public HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
