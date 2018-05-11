package com.atlas.crmapp.common;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.atlas.crmapp.adapter.MyFormMenuAdapter;
import com.atlas.crmapp.model.MyFormMenuModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 2017/3/20.
 */

public class MyFormMenuHelper {

    private Context mContext;
    private ListView mListView;
    private List<MyFormMenuModel> mDataList;
    private MyFormMenuAdapter mDataAdapter;

    public interface OnItemClickEvent {
        public void OnDone(MyFormMenuModel item);
    }

    public MyFormMenuHelper(Context context, ListView listView, final MyFormMenuHelper.OnItemClickEvent onItemClickEvent) {
        mContext = context;
        mListView = listView;
        mDataList = new ArrayList<MyFormMenuModel>();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                MyFormMenuModel item = mDataList.get(position);
                onItemClickEvent.OnDone(item);
            }
        });
    }


    public void addMenu(String no, String group, String name, int icon,String content,int type,String logo) {
        MyFormMenuModel item = new MyFormMenuModel();
        item.setNo(no);
        item.setGroup(group);
        item.setName(name);
        item.setIcon(icon);
        item.setContent(content);
        item.setType(type);
        item.setLogo(logo);
        mDataList.add(item);
    }

    public void setName(int position, String name) {
        mDataList.get(position).setName(name);
        mDataAdapter.notifyDataSetChanged();
    }

    public void setContent(int position,String message){
        mDataList.get(position).setContent(message);
        mDataAdapter.notifyDataSetChanged();
    }

    public void setLogo(String filePath){
        mDataList.get(0).setLogo(filePath);
        mDataAdapter.notifyDataSetChanged();
    }

    public void removeMenu(String no) {
        for(int i = 0; i < mDataList.size(); i++) {
            if(mDataList.get(i).getNo().equals(no)) {
                mDataList.remove(i);
                mDataAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    public void setTip(int position, int tip) {

        mDataAdapter.notifyDataSetChanged();
    }

    public void setData() {
        if(mDataAdapter == null) {
            mDataAdapter = new MyFormMenuAdapter(mContext);
            mDataAdapter.setData(mDataList);
            mListView.setAdapter(mDataAdapter);
        }
        mDataAdapter.notifyDataSetChanged();
    }
}