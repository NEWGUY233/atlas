package com.atlas.crmapp.usercenter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.IndexActivity;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.util.SpUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/2.
 */

public class SettingLanguageActivity extends BaseStatusActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    LanguageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_language);
        ButterKnife.bind(this);

        initToolbar();
        setTitle(getString(R.string.language));
        setTopRightButton(getString(R.string.save), new OnClickListener() {
            @Override
            public void onClick() {
                initLanguage();
                startActivity(new Intent(getContext(),IndexActivity.class).putExtra("from","SettingLanguageActivity"));
            }
        });
        lang = new String[]{getString(R.string.language_cn_cn),getString(R.string.language_cn_wt),getString(R.string.language_en)};
        setVerticalManager(recyclerView);
        adapter = new LanguageAdapter();
        int type = (int) SpUtil.getLong(getContext(),SpUtil.LANGUAGE,-1);

        if (type == -1) {
            String locale = Locale.getDefault().toString();
            if (Locale.SIMPLIFIED_CHINESE.toString().equals(locale)) {
                type = 0;
            } else if (Locale.TRADITIONAL_CHINESE.toString().equals(locale) || "zh-rHK".equals(locale) ) {
                type = 1;
            } else if (locale.startsWith("en")) {
                type = 2;
            } else {
                type = 0;
            }
        }

        adapter.setPosition(type);
        recyclerView.setAdapter(adapter);
    }

    private void initLanguage(){
//        int type = adapter.getPosition();
//        //获取当前资源对象
//        Resources resources = getResources();
//        //获取设置对象
//        Configuration configuration = resources.getConfiguration();
//        //获取屏幕参数
//        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
//
//
//        //发送结束所有activity的广播
//        switch (type){
//            case 0:
//                configuration.locale = Locale.SIMPLIFIED_CHINESE;
//                break;
//            case 1:
//                configuration.locale = Locale.TRADITIONAL_CHINESE;
//                break;
//            case 2:
//                configuration.locale = Locale.ENGLISH;
//                break;
//        }
//        resources.updateConfiguration(configuration, displayMetrics);
//
//        SpUtil.putLong(getContext(),SpUtil.LANGUAGE,adapter.getPosition());

        initLanguage(getContext());
        initLanguage(getApplicationContext());
    }

    private void initLanguage(Context context){
        int type = adapter.getPosition();
        //获取当前资源对象
        Resources resources = context.getResources();
        //获取设置对象
        Configuration configuration = resources.getConfiguration();
        //获取屏幕参数
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();


        //发送结束所有activity的广播
        switch (type){
            case 0:
                configuration.locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case 1:
                configuration.locale = Locale.TRADITIONAL_CHINESE;
                break;
            case 2:
                configuration.locale = Locale.ENGLISH;
                break;
        }
        resources.updateConfiguration(configuration, displayMetrics);

        SpUtil.putLong(getContext(),SpUtil.LANGUAGE,adapter.getPosition());
    }



    class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.Holder>{
        private int position = 0;

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.item_setting_language,parent,false));
        }

        @Override
        public void onBindViewHolder(Holder holder, final int position) {
            holder.name.setText(lang[position]);
            if (position == this.position){
                holder.check.setVisibility(View.VISIBLE);
            }else
                holder.check.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LanguageAdapter.this.position = position;
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return lang.length;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        class Holder extends RecyclerView.ViewHolder{
            TextView name;
            ImageView check;
            public Holder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.tv_language);
                check = (ImageView) itemView.findViewById(R.id.iv_check);
            }
        }
    }

    String[] lang;

}
