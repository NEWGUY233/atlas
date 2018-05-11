package com.atlas.crmapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.model.ProductInfoJson;
import com.atlas.crmapp.model.VisibleCompanysJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Alex on 2017/4/21.
 */

public class ServiceListAdapter extends BaseQuickAdapter<ProductInfoJson, BaseViewHolder>{

    private Context context;
    private List<ProductInfoJson> data;

    public ServiceListAdapter(Context context, List<ProductInfoJson> data) {
        super(R.layout.item_service, data);
        this.data = data;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final ProductInfoJson item) {
        helper.setText(R.id.tv_name, item.name);
        final GlobalParams gp = GlobalParams.getInstance();
        helper.getView(R.id.btn_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity act = (BaseActivity)context;




                if(act.getGlobalParams().isLogin()) {
                    BizDataRequest.requestGetVisibleCompanys(context, true, null, new BizDataRequest.OnVisibleCompanysRequestResult()  {
                        @Override
                        public void onSuccess(VisibleCompanysJson visibleCompanysJson) {
                            if (visibleCompanysJson.rows.size() > 0) {
                                gp.setHasContract(true);
                                new AlertDialog.Builder(context).setTitle(R.string.text_67)
                                        .setMessage(context.getString(R.string.text_84)+item.name+context.getString(R.string.text_85))
                                        .setNegativeButton(R.string.cancel, null)
                                        .setPositiveButton(R.string.text_86, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                getRequestService(item.id);
                                            }
                                        })
                                        .show();
                            } else {
                                Toast.makeText(context, context.getString(R.string.you_is_not_vip), Toast.LENGTH_LONG).show();
                                gp.setHasContract(false);
                            }
                        }
                        @Override
                        public void onError(DcnException error) {
                        }
                    });
                } else {
                    act.showAskLoginDialog();
                }
            }

        });

    }


    public void getRequestService(long id) {
        BizDataRequest.requestService(context, GlobalParams.getInstance().getWorkplaceId(), id, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                new AlertDialog.Builder(context).setTitle(R.string.text_67)
                        .setMessage(R.string.text_87)
                        .setPositiveButton(R.string.done, null)
                        .show();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

}