package com.atlas.crmapp.usercenter;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.model.CompnayInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;

import butterknife.BindView;
import butterknife.OnClick;

public class EditInfoActivity extends BaseActivity {

    String content;
    String type;

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.et_info)
    EditText etInfo;

    @BindView(R.id.tv_type)
    TextView tvContent;

    @OnClick(R.id.ibBack)
    void onBack() {
        this.finish();
    }

    @OnClick(R.id.btn_submit)
    void onEdit() {

        if(type.equals("description")) {
            company.description = etInfo.getText().toString();
        } else if(type.equals("contact")) {
            company.contact = etInfo.getText().toString();
        } else if(type.equals("phone")) {
            company.phone = etInfo.getText().toString();
        }

        BizDataRequest.requestUpdateCompany(EditInfoActivity.this, company.id, company.name, company.contact, company.phone, company.fax, company.city, company.address, company.thumbnail, company.description, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                finish();
            }

            @Override
            public void onError(DcnException error) {
                Toast.makeText(EditInfoActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    CompnayInfoJson company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        textViewTitle.setText(R.string.t56);
        company = (CompnayInfoJson) getIntent().getSerializableExtra("company");
        content = getIntent().getStringExtra("content");
        type =getIntent().getStringExtra("type");
        tvContent.setText(content);

        if(type.equals("description")) {
            etInfo.setText(company.description);
        } else if(type.equals("contact")) {
            etInfo.setText(company.contact);
        } else if(type.equals("phone")) {
            etInfo.setText(company.phone);
        }
    }

}
