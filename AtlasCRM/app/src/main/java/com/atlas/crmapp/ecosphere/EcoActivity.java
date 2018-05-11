package com.atlas.crmapp.ecosphere;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.ecosphere.fragments.ActivitiesFragment;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 活动推荐 更多
 */

public class EcoActivity extends BaseActivity {

    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @OnClick(R.id.ibBack)
    void onBack() {
        this.finish();
    }

    private String mBizcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if(intent != null){
            mBizcode = getIntent().getStringExtra("bizcode");
            textViewTitle.setText(intent.getStringExtra(InParamter.titleStr));
        }
        initData();
    }


    private void initData() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ActivitiesFragment fragment = ActivitiesFragment.newInstance(mBizcode);
        fragmentTransaction.add(R.id.fl_container, fragment);
        fragmentTransaction.commit();
    }

    public interface InParamter{
        String titleStr = "title";//type  String;
    }
}
