package com.atlas.crmapp.activity.index.fragment.index.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.activity.IndexTopAllActivity;
import com.atlas.crmapp.bean.IndexTopBean;
import com.atlas.crmapp.coffee.CoffeeListActivity;
import com.atlas.crmapp.common.BizCode;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.commonactivity.BlueToothAcitvity;
import com.atlas.crmapp.fitness.AppointmentClassActivity;
import com.atlas.crmapp.model.CityCenterJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.usercenter.MyCouponActivity;
import com.atlas.crmapp.usercenter.RechargeActivity;
import com.atlas.crmapp.workplace.MeetingRoomActivity;
import com.atlas.crmapp.workplace.VisitInviteActivity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */

public class IndexTopAllAdapter extends RecyclerView.Adapter<IndexTopAllAdapter.Holder>{
    Context c;
    public IndexTopAllAdapter(Context c){
        this.c = c;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IndexTopAllAdapter.Holder(LayoutInflater.from(c).inflate(R.layout.item_index_top_all_item,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final IndexTopBean bean = list.get(position);
        holder.iv_bg.setImageResource(bean.getId());
        holder.tv_title.setText(bean.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (bean.getId() == R.mipmap.subnav_icon_more)
//                    getContext().startActivity(new Intent(getContext(), IndexTopAllActivity.class));

                 if (bean.getId() == R.mipmap.subnav_icon_rechange)
                    getContext().startActivity(new Intent(getContext(), RechargeActivity.class));

                else if (bean.getId() == R.mipmap.subnav_icon_visit)
                    invite();

                else if (bean.getId() == R.mipmap.subnav_icon_coupon)
                    getContext().startActivity(new Intent(getContext(), MyCouponActivity.class));

                else if (bean.getId() == R.mipmap.subnav_icon_meeting)
                    getContext().startActivity(new Intent(getContext(), MeetingRoomActivity.class));

                else if (bean.getId() == R.mipmap.subnav_icon_class)
                    getContext().startActivity(new Intent(getContext(), AppointmentClassActivity.class));

                else if (bean.getId() == R.mipmap.subnav_icon_coffee)
                    toCoffeeList();

                else if (bean.getId() == R.mipmap.subnav_icon_bluetoothdoor)
                    getContext().startActivity(new Intent(getContext(), BlueToothAcitvity.class));
            }
        });
    }

    List<IndexTopBean> list;
    public void setList(List<IndexTopBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView tv_title;
        ImageView iv_bg;
        public Holder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_name);
            iv_bg = (ImageView) itemView.findViewById(R.id.iv_icon);
        }
    }

    private Context getContext(){
        return this.c;
    }

    public void invite(){
        BizDataRequest.requestVisitCityCenterList(getContext(), null, new BizDataRequest.OnResponseGetCenterList() {
            @Override
            public void onSuccess(List<CityCenterJson> centerListJsons) {
                if (centerListJsons != null  && centerListJsons.size() > 0) {
                    VisitInviteActivity.newInstance((Activity) getContext());
                } else {
                    Toast.makeText(getContext(), getContext().getString(R.string.you_unser_visit), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    void toCoffeeList() {
        GlobalParams.getInstance().setCurrentBizCode(currentBiz(GlobalParams.getInstance().getCoffeeId(), GlobalParams.getInstance().getCoffeeCode()));
        Intent intent =new Intent(getContext(), CoffeeListActivity.class);
        intent.putExtra("type", GlobalParams.getInstance().getCurrentBizCode().getBizCode());
        intent.putExtra("unitId", GlobalParams.getInstance().getCurrentBizCode().getUnitId());
        intent.putExtra("name", GlobalParams.getInstance().getCurrentBizCode().getBizName());
        getContext().startActivity(intent);
        //getHoldingActivity().showToast("本月推荐-更多");
    }

    private BizCode currentBiz(long id, String code){
        BizCode bizCode = new BizCode();
        bizCode.setUnitId(id);
        bizCode.setBizName(GlobalParams.getInstance().getBusinesseName(code));
        bizCode.setBizCode(code);
        return  bizCode;
    }

}
