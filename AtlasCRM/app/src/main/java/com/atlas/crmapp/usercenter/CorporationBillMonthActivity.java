package com.atlas.crmapp.usercenter;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.CorporationBillAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.BillingItemJson;
import com.atlas.crmapp.model.BillingItemsJson;
import com.atlas.crmapp.model.NoBillingItemJson;
import com.atlas.crmapp.model.NoBillingItemsJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.FormatCouponInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CorporationBillMonthActivity extends BaseStatusActivity {


    @BindView(R.id.listViewBill)
    ListView listViewBill;


    @BindView(R.id.tv_total)
    TextView mTvTotal;

    CorporationBillAdapter billAdapter;
    ArrayList<BillingItemJson> billings = new ArrayList<BillingItemJson>();
    long id;
    long contractId;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporation_bill_month);

        id = getIntent().getLongExtra("id", 0);
        contractId = getIntent().getLongExtra("contractId", 0);
        setTitle(getIntent().getStringExtra("name"));
        loadData();
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        loadData();
    }

    private void loadData() {
        if(id == 0) {
            BizDataRequest.requestGetNoBillingItems(CorporationBillMonthActivity.this, 0, 100, contractId, statusLayout, new BizDataRequest.OnNoBillingItemsRequestRescult() {
                @Override
                public void onSuccess(NoBillingItemsJson noBillingItemsJson) {
                    List<NoBillingItemJson> rows = noBillingItemsJson.rows;
                    for(NoBillingItemJson row : rows) {
                        BillingItemJson item = new BillingItemJson();
                        item.amount = row.actualAmount;
                        item.billDate = row.createTime;
                        item.type = row.briefing;
                        item.userName=row.userName;
                        billings.add(item);
                    }

                    billAdapter = new CorporationBillAdapter(billings);
                    listViewBill.setAdapter(billAdapter);
                    billAdapter.notifyDataSetChanged();
                    setTotlePrice();
                }

                @Override
                public void onError(DcnException error) {

                }
            });
        } else {
            BizDataRequest.requestGetBillingItems(CorporationBillMonthActivity.this, 0, 100, id, statusLayout, new BizDataRequest.OnBillingItemsRequestRescult() {
                @Override
                public void onSuccess(BillingItemsJson billingItemsJson) {
                    List<BillingItemJson> rows = billingItemsJson.rows;
                    billings.addAll(rows);

                    billAdapter = new CorporationBillAdapter(billings);
                    listViewBill.setAdapter(billAdapter);
                    billAdapter.notifyDataSetChanged();
                    setTotlePrice();
                }

                @Override
                public void onError(DcnException error) {

                }
            });
        }
    }

    private void setTotlePrice(){
        if(billings!=null){
            double totleAmount = 0;
            for(int i=0;i<billings.size();i++){
                BillingItemJson bill= billings.get(i);
                totleAmount+= bill.amount;
            }
            mTvTotal.setText(FormatCouponInfo.formatDoublePrice(totleAmount, 2));
        }
    }
}
