package com.atlas.crmapp.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.ActivityUserJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


/**
 * Created by tabzhou on 01/06/2017.
 */

public class ParticipantsAdapter extends BaseQuickAdapter<ActivityUserJson,BaseViewHolder>{
    private final Context mContext;
    private List<ActivityUserJson> dataSource;
    public ParticipantsAdapter(Context context, List<ActivityUserJson> data) {
        super(R.layout.item_participant, data);
        this.mContext = context;
        this.dataSource = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, ActivityUserJson item) {
        GlideUtils.loadImageCircleView(mContext,R.drawable.header_icon,LoadImageUtils.loadSmallImage(item.appUser.getAvatar()),(ImageView) helper.getView(R.id.item_participant_avatar));
        helper.setText(R.id.item_participant_name, item.appUser.getNick())
                .setText(R.id.item_participant_company, item.appUser.getCompany());
    }


}
