package com.atlas.crmapp.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.activity.IndexTopAllActivity;
import com.atlas.crmapp.adapter.MyViewPagerAdapter;
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
import com.atlas.crmapp.usercenter.RechargeRecordActivity;
import com.atlas.crmapp.workplace.MeetingRoomActivity;
import com.atlas.crmapp.workplace.VisitInviteActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */

public class IndexTopViewpager extends LinearLayout {
    public IndexTopViewpager(Context context) {
        super(context);
        initView();
    }

    public IndexTopViewpager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public IndexTopViewpager(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    ImageView iv_close;
    ViewPager viewPager;
    View line1;
    View line2;
    protected void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_index_top_viewpager,this,true);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        viewPager = (ViewPager) findViewById(R.id.viewpager_top);

        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
    }

    public void setTopBar(List<IndexTopBean> list){
        iv_close.setVisibility(GONE);
        line1.setVisibility(VISIBLE);
        line2.setVisibility(GONE);
        setList(list);
    }

    public void setDialog(List<IndexTopBean> list){
        iv_close.setVisibility(VISIBLE);
        line1.setVisibility(GONE);
        line2.setVisibility(VISIBLE);
        setList(list);
    }


    List<IndexTopBean> list;
    private void setList(List<IndexTopBean> list){
        if (list == null || list.size() == 0)
            initList();
        else
            this.list = list;

        viewPager.setAdapter(new Adapter());

    }


    private void initList(){
        initItem();
    }


    private void initItem(){
        list = new ArrayList<>();

        IndexTopBean bean = new IndexTopBean();
        bean.setName(getContext().getString(R.string.index_top_bluetooth));
        bean.setId(R.mipmap.subnav_icon_bluetoothdoor);
        list.add(bean);

        bean = new IndexTopBean();
        bean.setName(getContext().getString(R.string.index_top_food));
        bean.setId(R.mipmap.subnav_icon_coffee);
        list.add(bean);

        bean = new IndexTopBean();
        bean.setName(getContext().getString(R.string.index_top_class));
        bean.setId(R.mipmap.subnav_icon_class);
        list.add(bean);

        bean = new IndexTopBean();
        bean.setName(getContext().getString(R.string.index_top_meeting));
        bean.setId(R.mipmap.subnav_icon_meeting);
        list.add(bean);

        bean = new IndexTopBean();
        bean.setName(getContext().getString(R.string.index_top_yhq));
        bean.setId(R.mipmap.subnav_icon_coupon);
        list.add(bean);

        bean = new IndexTopBean();
        bean.setName(getContext().getString(R.string.index_top_invite));
        bean.setId(R.mipmap.subnav_icon_visit);
        list.add(bean);

        bean = new IndexTopBean();
        bean.setName(getContext().getString(R.string.index_top_money));
        bean.setId(R.mipmap.subnav_icon_rechange);
        list.add(bean);

        bean = new IndexTopBean();
        bean.setName(getContext().getString(R.string.index_top_all));
        bean.setId(R.mipmap.subnav_icon_more);
        list.add(bean);
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

    public void setClick(OnItemClick click) {
        this.click = click;
    }

    protected class Adapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            RecyclerView recyclerView = new RecyclerView(getContext());
//            LinearLayoutManager ll = new LinearLayoutManager(getContext());
//            ll.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            recyclerView.setLayoutParams(lp);
            recyclerView.setAdapter(new RCAdapter(position));
            container.addView(recyclerView);
            return recyclerView;
        }

    }

    protected class RCAdapter extends RecyclerView.Adapter<RCAdapter.Holder>{
        int pager;
        public RCAdapter(int pager){
            this.pager = pager;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.item_index_top_item,parent,false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            final IndexTopBean bean = list.get((pager)*4 + position);
            holder.name.setText(bean.getName());
            holder.name.setCompoundDrawablesWithIntrinsicBounds(0,bean.getId(),0,0);

            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (click != null)
                        click.onClick(bean.getName(),bean.getId());

                    if (bean.getId() == R.mipmap.subnav_icon_more)
                        getContext().startActivity(new Intent(getContext(), IndexTopAllActivity.class));

                    else if (bean.getId() == R.mipmap.subnav_icon_rechange)
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

        @Override
        public int getItemCount() {
            return 4;
        }

        class Holder extends RecyclerView.ViewHolder{
            TextView name;

            public Holder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.tv_name);
            }
        }
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

    private OnItemClick click;
    public interface OnItemClick{
        void onClick(String name,int rId);
    }
}
