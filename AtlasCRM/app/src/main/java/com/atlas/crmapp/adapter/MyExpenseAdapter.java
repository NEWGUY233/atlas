package com.atlas.crmapp.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.atlas.crmapp.R;
import com.atlas.crmapp.model.bean.MyExpenseModel;

import java.util.List;

/**
 * Created by A.Developer on 2017/3/19.
 */

public class MyExpenseAdapter extends BaseQuickAdapter<MyExpenseModel, BaseViewHolder> {


    public MyExpenseAdapter(List<MyExpenseModel> data) {
        super(R.layout.item_my_expense, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyExpenseModel item) {
        helper.setText(R.id.expense_date, item.getDate())
                .setText(R.id.expense_time, item.getTime())
                .setText(R.id.expense_price, item.getPrice())
                .setText(R.id.expense_address, item.getAddress())
                .setText(R.id.expense_status, item.getStatus());
    }

}
