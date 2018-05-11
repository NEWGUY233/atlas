package com.atlas.crmapp.model.multipleItem;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by hoda on 2017/7/19.
 */

public class FindRvMultipleItem implements MultiItemEntity{
    public static final int REAL_MSG = 0;
    public static final int COUP_MSG = 1;
    private int itemType;

    public FindRvMultipleItem(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }


}
