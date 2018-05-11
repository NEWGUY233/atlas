package com.atlas.crmapp.usercenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.BillingJson;
import com.atlas.crmapp.model.ContractJson;
import com.atlas.crmapp.model.ContractsJson;
import com.atlas.crmapp.model.CorporationMoneyJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CorporationMoneyActivity extends BaseStatusActivity {

    @BindView(R.id.titleButton)
    TextView titleButton;

//    @BindView(R.id.rv_bill)
//    RecyclerView listViewBill;

    @BindView(R.id.ibHome)
    ImageButton ibHome;
    @BindView(R.id.tv_big)
    TextView tvBig;
    @BindView(R.id.tv_small)
    TextView tvSmall;
    @BindView(R.id.tv_print)
    TextView tvPrint;

//    @BindView(R.id. springview)
//    SpringView springView;

    private int pageSize = 20;
    private boolean showProgreeDialog;

    private static final String INTENT_KEY_COMPANY_NAME = "companyName";
    private static final String INTENT_KEY_COMPANY_NAME_ = "companyNameId";
    private String companyName;

    @OnClick(R.id.ibBack)
    void onBack() {
        this.finish();
    }

    private List<ContractJson> contracts;
    private List<BillingJson> billings = new ArrayList<BillingJson>();
    //    private CorporationBillMenuAdapter adapter;
    long contractId;
    int page = 0;

    @Override
    protected int setTopBar() {
        return R.layout.view_action_bar_arr_down;
    }

    public static void startActivity(Context context, String companyName) {
        Intent intent = new Intent(context, CorporationMoneyActivity.class);
        intent.putExtra(INTENT_KEY_COMPANY_NAME, companyName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporation_money);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            companyName = intent.getStringExtra(INTENT_KEY_COMPANY_NAME);
//            contractId = intent.getLongExtra(INTENT_KEY_COMPANY_NAME_,0);
        }

        if (StringUtils.isNotEmpty(companyName)) {
            titleButton.setText(companyName);
        } else {
            titleButton.setText(getString(R.string.company_bill));
        }

//        adapter = new CorporationBillMenuAdapter(this,billings ,contractId);
//        listViewBill.setLayoutManager(new LinearLayoutManager(this));
//        listViewBill.addItemDecoration(GetCommonObjectUtils.getRvItemDecoration(this));
//        listViewBill.setAdapter(adapter);
//        springView.setType(SpringView.Type.FOLLOW);
//        springView.setFooter(new RefreshFootView(this));
//        springView.setListener(new SpringView.OnFreshListener() {
//            @Override
//            public void onRefresh() {
//
//            }
//
//            @Override
//            public void onLoadmore() {
//                page = page + 1;
//                loadBillings();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(springView != null)
//                        springView.onFinishFreshAndLoad();
//                    }
//                }, Constants.ToRefreshTime.REFRESH_TIME);
//            }
//        });
//        View view = LayoutInflater.from(this).inflate(R.layout.item_bill_menu, null, false);
//        TextView tvItem = (TextView) view.findViewById(R.id.tv_date);
//        view.findViewById(R.id.separatorView).setVisibility(View.VISIBLE);
//        tvItem.setText(getString(R.string.to_be_bill));
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(CorporationMoneyActivity.this, CorporationBillMonthActivity.class);
//                intent.putExtra("id", 0);
//                intent.putExtra("name", getString(R.string.to_be_bill));
//                intent.putExtra("contractId", contractId);
//                startActivity(intent);
//            }
//        });
//        adapter.addHeaderView(view);
        prepareActivityData();
    }

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        if (billings.size() == 0) {
            showProgreeDialog = false;
        } else {
            showProgreeDialog = true;
        }

        BizDataRequest.requestMyContracts(CorporationMoneyActivity.this, getGlobalParams().getAtlasId(), false, statusLayout, new BizDataRequest.OnContractsRequestRescult() {
            @Override
            public void onSuccess(ContractsJson contractsJson) {
                contracts = contractsJson.rows;

                boolean haveContract = false;

                if (contracts.size() > 0) {
                    for (ContractJson contractJson : contracts) {
                        if (contractJson.companyName.equals(companyName)) {
                            companyName = contractJson.companyName;
                            contractId = contractJson.id;
                            titleButton.setText(companyName);
                            haveContract = true;
//                            loadBillings();
                        }
                    }
                    if (!haveContract) {
                        statusLayout.showEmpty();
                    }
                    if (contractId == 0){
                        ContractJson contractJson = contracts.get(0);
                        companyName = contractJson.companyName;
                        contractId = contractJson.id;
                        titleButton.setText(companyName);
                    }

                    getData();
                } else {
                    Toast.makeText(CorporationMoneyActivity.this, getString(R.string.t50), Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });


    }

    private void getData() {
        BizDataRequest.requestGetCompanyLimit(CorporationMoneyActivity.this, contractId, statusLayout, new BizDataRequest.CorporationMoneyJsonRequestResult() {
            @Override
            public void onSuccess(CorporationMoneyJson corporationMoneyJson) {
                tvBig.setText(corporationMoneyJson.getBookingMinutesB() + getResources().getString(R.string.corporation_min));
                tvSmall.setText(corporationMoneyJson.getBookingMinutes() + getResources().getString(R.string.corporation_min));
                tvPrint.setText(corporationMoneyJson.getPrintingCharges() + getResources().getString(R.string.corporation_cny));
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareActivityData();
    }

    @OnClick(R.id.titleButton)
    void switchContact(View view) {
        MyContractsDialog dialog = new MyContractsDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("contracts", (Serializable) contracts);
        dialog.setArguments(bundle);
        dialog.listener = new MyContractsDialog.MyContractsDialogListener() {
            @Override
            public void onMyContractDialogSelected(ContractJson contract) {
                if (contract != null) {
                    companyName = contract.companyName;
                    titleButton.setText(companyName);
                    contractId = contract.id;
                    page = 0;
                    getData();
//                    loadBillings();
                }
            }
        };
        dialog.show(getSupportFragmentManager(), "MyContractDialog");
    }

//    private void loadBillings() {
//        BizDataRequest.requestGetContractBillings(CorporationMoneyActivity.this, page, pageSize, contractId, showProgreeDialog, statusLayout, new BizDataRequest.OnBillingsRequestRescult() {
//            @Override
//            public void onSuccess(BillingsJson billingsJson) {
//                if(billings.size() < pageSize){
//                    billings.clear();
//                    springView.setEnable(false);
//                }
//                billings.addAll(billingsJson.rows);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onError(DcnException error) {
//
//            }
//        });
//    }


}
