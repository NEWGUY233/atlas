package com.atlas.crmapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.CompnayInfoJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Created by tabzhou on 19/05/2017.
 */
// Adapter

public class MyCompanyDialogAdapter extends BaseAdapter {
    List<CompnayInfoJson> companies;
    Context context;
    LayoutInflater inflater;
    public MyCompanyDialogAdapter(Context context, List<CompnayInfoJson> companies) {
        this.context = context;
        this.companies = companies;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return companies.size();
    }

    @Override
    public Object getItem(int position) {
        return companies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return companies.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.item_my_company_dialog,null);
        ImageView companyIcon = (ImageView) view.findViewById(R.id.companyIcon);
        TextView companyName = (TextView) view.findViewById(R.id.companyName);
        TextView companyIntro = (TextView) view.findViewById(R.id.companyIntro);
        CompnayInfoJson company = companies.get(position);
        Glide.with(context).load(LoadImageUtils.loadSmallImage(company.thumbnail)).apply(new RequestOptions().placeholder(R.drawable.ic_product_thum)).into(companyIcon);
        companyName.setText(company.name);
        companyIntro.setText(company.description);

        return view;
    }


}
