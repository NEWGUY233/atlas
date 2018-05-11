package com.atlas.crmapp.common;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.atlas.crmapp.adapter.MyMenuAdapter;
import com.atlas.crmapp.model.MyMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 2017/3/10.
 */

public class MyMenuHelper {

    private Context mContext;
    private ListView mListView;
    private List<MyMenu> mDataList;
    private MyMenuAdapter mDataAdapter;

    public interface OnItemClickEvent {
        public void OnDone(MyMenu item);
    }

    public MyMenuHelper(Context context, ListView listView, final OnItemClickEvent onItemClickEvent) {
        mContext = context;
        mListView = listView;
        mDataList = new ArrayList<MyMenu>();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                MyMenu item = mDataList.get(position);
                onItemClickEvent.OnDone(item);
            }
        });
    }

    public void addMenu(String name, int icon) {
        addMenu(String.valueOf(mDataList.size()), "", name, icon);
    }

    public void addMenu(String no, String name, int icon) {
        addMenu(no, "", name, icon);
    }

    public void addMenu(String no, String group, String name, int icon) {
        MyMenu item = new MyMenu();
        item.setNo(no);
        item.setGroup(group);
        item.setName(name);
        item.setIcon(icon);
        item.setTip(0);
        mDataList.add(item);
    }

    public void setName(int position, String name) {
        mDataList.get(position).setName(name);
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
        mDataList.get(position).setTip(tip);
        mDataAdapter.notifyDataSetChanged();
    }

    public void setData() {
        if(mDataAdapter == null) {
            mDataAdapter = new MyMenuAdapter(mContext);
            mDataAdapter.setData(mDataList);
            mListView.setAdapter(mDataAdapter);
        }
        mDataAdapter.notifyDataSetChanged();
    }
}
