package com.atlas.crmapp.usercenter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.MyCompanyDialogAdapter;
import com.atlas.crmapp.model.CompnayInfoJson;
import com.atlas.crmapp.model.ContractJson;
import com.atlas.crmapp.util.ContextUtil;

import java.util.List;


//合同列表 弹出框
public class MyContractsDialog extends DialogFragment {

    public interface MyContractsDialogListener {
        void onMyContractDialogSelected(ContractJson contract);
    }

    List<ContractJson> contracts;
    ListView listView;
    TextView title;
    ContractJson selectedContract;

    public MyContractsDialogListener listener;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_black_header_list_dialog, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        contracts = (List<ContractJson>) getArguments().getSerializable("contracts");
        listView = (ListView) view.findViewById(R.id.listView);
        title = (TextView) view.findViewById(R.id.tv_header);
        title.setText(R.string.t69);
        MyContractsAdapter adapter = new MyContractsAdapter(inflater, contracts);
        listView.setAdapter(adapter);

        //选择公司
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedContract = contracts.get(position);
                if (listener != null) {
                    listener.onMyContractDialogSelected(selectedContract);
                }
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (selectedContract == null) {
            listener.onMyContractDialogSelected(null);
        }
    }
}


class MyContractsAdapter extends BaseAdapter {
    List<ContractJson> contracts;
    LayoutInflater inflater;

    public MyContractsAdapter(LayoutInflater inflater, List<ContractJson> contracts) {
        this.contracts = contracts;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return contracts.size();
    }

    @Override
    public Object getItem(int position) {
        return contracts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contracts.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.item_my_contract_dialog,null);
        TextView mTVName = (TextView) view.findViewById(R.id.tv_contract_name);
        TextView mTVID = (TextView) view.findViewById(R.id.tv_contract_ID);

        ContractJson contract = contracts.get(position);
        mTVName.setText(contract.companyName);
        if(TextUtils.isEmpty(contract.code)){
            mTVID.setVisibility(View.GONE);
        }else{
            mTVID.setVisibility(View.VISIBLE);
            mTVID.setText(ContextUtil.getUtil().getContext().getString(R.string.t70) + contract.code);
        }


        return view;
    }


}

