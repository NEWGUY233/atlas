package com.atlas.crmapp.usercenter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.adapter.MyExpenseAdapter;
import com.atlas.crmapp.model.bean.MyExpenseModel;
import com.atlas.crmapp.widget.RecycleViewListViewDivider;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyExpenseListActivity extends BaseActivity {

    @BindView(R.id.textViewTitle)
    TextView mTitle;
    @BindView(R.id.ibHome)
    ImageButton mRightBtn;
    @BindView(R.id.tvText)
    TextView mRightText;
    @BindView(R.id.expense_recyclerview)
    RecyclerView recyclerview;

    private ArrayList<MyExpenseModel> models = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_expense_list);

        ButterKnife.bind(this);
        mTitle.setText(R.string.t74);
        mRightText.setText(R.string.t75);
        mRightText.setVisibility(View.VISIBLE);
        mRightBtn.setVisibility(View.GONE);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addItemDecoration(new RecycleViewListViewDivider(this, LinearLayout.HORIZONTAL, 1, Color.parseColor("#ebebeb")));
        initData();
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            MyExpenseModel mm = new MyExpenseModel();
            mm.setDate(getString(R.string.t76));
            mm.setTime("12:12");
            mm.setPrice("88");
            mm.setAddress(getString(R.string.t77));
            if (i == 3){
                mm.setStatus(1);
            } else if (i == 4){
                mm.setStatus(2);
            } else {
                mm.setStatus(0);
            }
            models.add(mm);
        }
        recyclerview.setAdapter(new MyExpenseAdapter(models));
    }

    @OnClick({R.id.ibBack, R.id.tvText})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                finish();
                break;
            case R.id.tvText:
                break;
        }
    }
}
