package com.atlas.crmapp.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.AddedValueJson;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by hoda on 2017/12/25.
 *
 */

public class AddedValueServiceAdapter extends BaseQuickAdapter<AddedValueJson, BaseViewHolder> {
    private Context context;
    public AddedValueServiceAdapter(Context context, List<AddedValueJson> data) {
        super(R.layout.item_added_value_service, data);


    }

    @Override
    protected void convert(BaseViewHolder helper, AddedValueJson item) {

        ImageView ivAddCheck = helper.getView(R.id.iv_add_check);
        ivAddCheck.setImageResource(item.isSelect() ? R.drawable.pay_channel_select : R.drawable.pay_channel_not_select);
        List<AddedValueJson.Combos>  combosList = item.getCombos();
        if(combosList != null  && combosList.size() > 0){
            AddedValueJson.Combos combos = combosList.get(0);
            helper.setText(R.id.tv_added_price , combos.getPrice().intValue() + "元" )
                    .setText(R.id.tv_added_name, combos.getName())
                    .addOnClickListener(R.id.rl_main);
        }
    }

}
