/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.atlas.crmapp.adapter.wheel;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atlas.crmapp.bean.RegionCodeBean;
import com.atlas.crmapp.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * The simple Array wheel adapter
 */
public class PhoneWheelAdapter extends AbstractWheelTextAdapter {

    // items
    private List<String> items;

//    /**
//     * Constructor
//     * @param context the current context
//     * @param items the items
//     */
//    public PhoneWheelAdapter(Context context, List<String> items) {
//        super(context);
//
//        //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
//        this.items = items;
//    }

    public PhoneWheelAdapter(Context context, List<RegionCodeBean> list) {
        super(context);

        //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
        if (list == null || list.size() == 0)
            this.items = AppUtil.getAreaList();
        else{
            items = new ArrayList<>();
            for (RegionCodeBean bean : list){
                items.add(bean.getRegion() + " " + bean.getZip_code());
            }
        }

    }

    @Override
    public String getItemText(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        if (index >= 0 && index < items.size()) {
            if (convertView == null) {
                convertView = getView(itemResourceId, parent);
            }
            TextView textView = getTextView(convertView, itemTextResourceId);
            if (textView != null) {
                String text = getItemText(index);
                if (text == null) {
                    text = "";
                }
                textView.setText(text);
                textView.setPadding(0,3,0,3);
                if (itemResourceId == TEXT_VIEW_ITEM_RESOURCE) {
                    configureTextView(textView);
                }
            }
            return convertView;
        }
        return null;
    }
}
