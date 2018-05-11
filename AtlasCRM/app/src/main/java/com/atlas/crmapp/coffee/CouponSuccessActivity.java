package com.atlas.crmapp.coffee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.usercenter.MyCouponActivity;

/**
 * Created by hoda on 2017/7/19.
 */

public class CouponSuccessActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  setContentView(R.layout.view_get_coupon_success);
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CouponSuccessActivity.this.finish();
            }
        });
        findViewById(R.id.iv_close)
*/


        setContentView(R.layout.activity_coupon_success);
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CouponSuccessActivity.this.finish();
            }
        });
        findViewById(R.id.v_start_coupon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CouponSuccessActivity.this, MyCouponActivity.class));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        overridePendingTransition(0, 0);
    }
}
