package com.atlas.crmapp.usercenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.MyCompanyDialogAdapter;
import com.atlas.crmapp.model.CompnayInfoJson;

import java.util.List;


interface MyCompanyDialogListener {
    void onMyCompanyDialogSelected(CompnayInfoJson company);
}

public class MyCompanyDialog extends DialogFragment {

    List<CompnayInfoJson> companies;
    ListView listView;
    TextView title;

    MyCompanyDialogListener listener;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_black_header_list_dialog, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        companies = (List<CompnayInfoJson>) getArguments().getSerializable("companies");
        listView = (ListView) view.findViewById(R.id.listView);
        title = (TextView) view.findViewById(R.id.tv_header);
        title.setText(R.string.t68);
        MyCompanyDialogAdapter adapter = new MyCompanyDialogAdapter(getActivity(), companies);
        listView.setAdapter(adapter);

        //选择公司
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CompnayInfoJson selectedCompany = companies.get(position);
                if (listener != null) {
                    listener.onMyCompanyDialogSelected(selectedCompany);
                }
                dismiss();
            }
        });

        return view;
    }

}


