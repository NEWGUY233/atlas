package com.atlas.crmapp.wxapi;


import com.atlas.crmapp.R;
import com.atlas.crmapp.usercenter.MyOrderActivity;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

//	@BindView(R.id.textViewTitle)
//	TextView textViewTitle;
//
//	@BindView(R.id.tv_amount)
//	TextView mTvAount;

	@OnClick(R.id.ibBack)
	void onBack() {
		this.finish();
	}

//	@OnClick(R.id.btn_complete)
//	void onComplate() {
//		this.finish();
//	}
//
//	@OnClick(R.id.btn_order)
//	void onCheckOrder() {
//		this.startActivity(new Intent(WXPayEntryActivity.this, MyOrderActivity.class));
//
//	}

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
		ButterKnife.bind(this);
//		textViewTitle.setText("完成订单");

    	api = WXAPIFactory.createWXAPI(this, "wx03265a3e5e061e25");
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		int errCode = resp.errCode;

		Intent intent = new Intent("com.atlas.crmapp.paysuccess");
		intent.putExtra("err_code",errCode);

		switch (errCode) {
			case 0:
				Toast.makeText(this, R.string.s94,Toast.LENGTH_LONG).show();
				intent.putExtra("err_msg",getString(R.string.s94));
				break;
			case -1:
				Toast.makeText(this, R.string.s95,Toast.LENGTH_LONG).show();
				intent.putExtra("err_msg",getString(R.string.s95));
				break;
			case -2:
				Toast.makeText(this, R.string.s96,Toast.LENGTH_LONG).show();
				intent.putExtra("err_msg",getString(R.string.s96));
				break;
		}
		Log.e("TAG", "errCodes" + errCode);
		//finish();//这里重要，如果没有 finish（）；将留在微信支付后的界面.
		sendBroadcast(intent);
        finish();
	}
}