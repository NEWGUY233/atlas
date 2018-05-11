package com.atlas.crmapp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.fitness.PersonalCoachDetailActivity;
import com.atlas.crmapp.model.CoachJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Alex on 2017/4/21.
 */

public class FitnessPersonalCoachAdapter extends BaseQuickAdapter<CoachJson, BaseViewHolder> {

    private Context context;
    private List<CoachJson> data;

    public FitnessPersonalCoachAdapter(Context context, List<CoachJson> data) {
        super(R.layout.item_fitness_coach, data);
        this.context = context;
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, final CoachJson item) {
        GlideUtils.loadCustomImageView(context, R.drawable.ic_product_thum, LoadImageUtils.loadSmallImage(item.thumbnail), (ImageView) helper.getView(R.id.iv_thumbnail));
        Glide.with(context).load(LoadImageUtils.loadSmallImage(item.thumbnail)).apply(new RequestOptions().centerCrop()).into((ImageView) helper.getView(R.id.iv_thumbnail));
        helper.setText(R.id.tv_name, item.name)
                .setText(R.id.tv_description, item.major)
                .setText(R.id.tv_aptitude, item.aptitude);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PersonalCoachDetailActivity.newInstance(context, item);
                /*Intent intent = new Intent(context, UserCardActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(UserCardActivity.KEY_COACH,item);
                intent.putExtras(b);
                context.startActivity(intent);*/
            }
        });

    }

}