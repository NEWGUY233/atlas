package com.atlas.crmapp.workplace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.atlas.crmapp.R;
import com.atlas.crmapp.commonactivity.CouponListActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceDetailActivity extends AppCompatActivity {

    @OnClick(R.id.btnSubmit)
    void toCouponList() {
        this.startActivity(new Intent(this, CouponListActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        ButterKnife.bind(this);
    }
}
