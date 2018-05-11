package com.atlas.crmapp.coffee;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.CoffeeCategoryTabAdapter;
import com.atlas.crmapp.coffee.fragment.CoffeeProductListFragment;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.db.hepler.ProductCartHelper;
import com.atlas.crmapp.db.model.ProductCart;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.CartJson;
import com.atlas.crmapp.model.OpenOrderJson;
import com.atlas.crmapp.model.ProductCategoryJson;
import com.atlas.crmapp.model.ProductInfoJson;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.model.SKUJson;
import com.atlas.crmapp.model.bean.Point;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.view.BottomAccountView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.badgeview.BGABadgeFrameLayout;


public class CoffeeListActivity extends BaseStatusActivity {


    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private Animation scale ;

    @BindView(R.id.v_bottom_account)
    BottomAccountView vBottomAccount;

    private BGABadgeFrameLayout mBadgeView;

    @BindView(R.id.lv_product)
    ListView lvProduction;

    @BindView(R.id.ll_popo_lv)
    View llPopoLv;//点击底部弹出层

    @OnClick(R.id.v_alpha)
    void onAlphaViewClick(){
        llPopoLv.setVisibility(View.GONE);
    }


    @OnClick(R.id.btn_booking)
    void onPayClick() {
        createCoffeeOrder();
    }

    private CoffeeProductRvAdapters adapters;
    public String type;
    public long unitId;
    public String name;
    private CoffeeCategoryTabAdapter tabAdapter;
    private List<CoffeeProductListFragment> mFragments = new ArrayList<>();
    public List<ProductCategoryJson.RowsBean> rows_al = new ArrayList<ProductCategoryJson.RowsBean>();
    public ArrayList<ProductInfoJson> proinfojson = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusFactory.getBus().register(this);
        setContentView(R.layout.activity_new_coffee_list);
        type = getIntent().getStringExtra("type");//类型咖啡/厨房
        unitId =getIntent().getLongExtra("unitId",-1);
        name = getIntent().getStringExtra("name");//类型咖啡/厨房
        setTitle(name);
        setTopLeftButton(R.drawable.white_back, new OnClickListener() {
            @Override
            public void onClick() {
                onBack();
            }
        });
        if(Constants.BIZ_CODE.COFFEE.equalsIgnoreCase(type)){
            vBottomAccount.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusFactory.getBus().unregister(this);
    }

    @Subscribe
    public void onPaySuccess(Event.EventObject object){
        if(object != null  && Constants.EventType.ORDER_COMPLETE.equalsIgnoreCase(object.type)){
            clearProduct();
        }
    }

    private void onBack() {
        setResult(RESULT_OK);
        this.finish();
    }


    @Override
    protected void initActivityViews() {
        super.initActivityViews();
        mBadgeView = (BGABadgeFrameLayout) vBottomAccount.findViewById(R.id.badge_view);
        mBadgeView.showTextBadge("0");
        adapters = new CoffeeProductRvAdapters(this, proinfojson);
        lvProduction.setAdapter(adapters);
        mBadgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(llPopoLv.getVisibility() == View.GONE){
                    llPopoLv.setVisibility(View.VISIBLE);
                }else{
                    llPopoLv.setVisibility(View.GONE);
                }
            }
        });
        vBottomAccount.updateVies(0, 0);
        prepareActivityData();
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareActivityData();
    }


    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        BizDataRequest.requestProductCategory(this, type, statusLayout, new BizDataRequest.OnRequestProductCategory() {
            @Override
            public void onSuccess(ProductCategoryJson productCategoryJson) {
                List<ProductCategoryJson.RowsBean> rlrows = productCategoryJson.getRows();
                rows_al.addAll(rlrows);
                mFragments = new ArrayList<CoffeeProductListFragment>();
                String[] strTabTitle;
//                        = new String[rows_al.size()];
                List<String> name = new ArrayList<String>();
                for (ProductCategoryJson.RowsBean b : rlrows){
//                    name.add(b.getName());
//                    mFragments.add(CoffeeProductListFragment.newInstance(b.getId(), unitId, type));
                         for (ProductCategoryJson.RowsBean.ChildrensBean childrensBean : b.getChildrens()){
                             name.add(childrensBean.getName());
                             mFragments.add(CoffeeProductListFragment.newInstance(childrensBean.getId(), unitId, type));
                         }
                }

                strTabTitle = new String[name.size()];

                for (int i = 0; i < name.size(); i++) {
                    strTabTitle[i] = name.get(i);
                }

                tabAdapter = new CoffeeCategoryTabAdapter(getSupportFragmentManager(),mFragments, strTabTitle);
                if(rows_al.size() > 1){
                    mViewpager.setOffscreenPageLimit(rows_al.size() - 1);
                }
                mViewpager.setAdapter(tabAdapter);
                mTabLayout.setupWithViewPager(mViewpager);
                mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    public void getIv_sub(ProductInfoJson al_proinfo) {
        for (int i = 0; i < proinfojson.size(); i++) {
            if (proinfojson.get(i).id == al_proinfo.id) {
                if(proinfojson.get(i).number <= 0) {
                    proinfojson.remove(i);
                    ProductCartHelper.removeProduct(al_proinfo.id, unitId);
                }else {
                    ProductCartHelper.updateProduct(al_proinfo.id, al_proinfo.number, type, unitId);
                }
                mBadgeView.showTextBadge(proinfojson.size() + "");
                tabAdapter.updateProductInfo(al_proinfo.id, al_proinfo.number, unitId);
                break;
            }
        }
        updateBottom();
    }


    public void getIv_add(ProductInfoJson al_proinfo) {
        boolean isadd = true;
        for (int i = 0; i < proinfojson.size(); i++) {
            if (proinfojson.get(i).id == al_proinfo.id) {
                isadd = false;
            }
        }
        if (isadd == true) {//如果是新产品就添加
            proinfojson.add(al_proinfo);
        }
        tabAdapter.updateProductInfo(al_proinfo.id, al_proinfo.number, unitId);
        ProductCartHelper.updateProduct(al_proinfo.id, al_proinfo.number, type, unitId);

        updateBottom();
    }


    //加入购物车动画  http://www.jianshu.com/p/7c94a15b8206
    public void playAnimation(int[] position){

        final ImageView mImg = new ImageView(this);
        mImg.setImageResource(R.drawable.circle_dot);
        mImg.setScaleType(ImageView.ScaleType.MATRIX);
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        rootView.addView(mImg);
        Logger.d(position[0] + "position----");
        int[] des = new int[2];
        mBadgeView.getLocationInWindow(des);

        /*动画开始位置，也就是物品的位置;动画结束的位置，也就是购物车的位置 */
        Point startPosition = new Point(position[0], position[1]);
        Point endPosition = new Point(des[0]+mBadgeView.getWidth()/2, des[1]+mBadgeView.getHeight()/2-50);

        int pointX = (startPosition.x + endPosition.x) / 2 - 100;
        int pointY = startPosition.y - 200;
        Point controllPoint = new Point(pointX, pointY);

        /*
        * 属性动画，依靠TypeEvaluator来实现动画效果，其中位移，缩放，渐变，旋转都是可以直接使用
        * 这里是自定义了TypeEvaluator， 我们通过point记录运动的轨迹，然后，物品随着轨迹运动，
        * 一旦轨迹发生变化，就会调用addUpdateListener这个方法，我们不断的获取新的位置，是物品移动
        * */
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new BizierEvaluator(controllPoint),startPosition, endPosition);
        valueAnimator.setDuration(500);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Point point = (Point) valueAnimator.getAnimatedValue();
                mImg.setX(point.x);
                mImg.setY(point.y);
            }
        });

        /**
         * 动画结束，移除掉小圆圈
         */
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ViewGroup rootView = (ViewGroup) CoffeeListActivity.this.getWindow().getDecorView();
                rootView.removeView(mImg);
                if(scale == null){
                    scale = AnimationUtils.loadAnimation(CoffeeListActivity.this, R.anim.anim_scale);
                }
                mBadgeView.startAnimation(scale);
            }
        });
    }


    public void updateBottom(){
        double pric = 0;
        int num = 0;
        for (int j = 0; j < proinfojson.size(); j++) {
            SKUJson d = FormatCouponInfo.getDefaultSKUJson(proinfojson.get(j).productSkus);
            pric = pric + d.onlinePrice * proinfojson.get(j).number;
            num = num + proinfojson.get(j).number;
        }
        if(proinfojson.size() == 0){
            llPopoLv.setVisibility(View.GONE);
        }
        vBottomAccount.updateVies(pric, num);
        adapters.notifyDataSetChanged();
    }


    //咖啡创建订单
    private void createCoffeeOrder(){
        if (getGlobalParams().isLogin()) {
            BizDataRequest.requestOpenOrder(CoffeeListActivity.this, orderJson(), new BizDataRequest.OnResponseOpenOrderJson() {
                @Override
                public void onSuccess(ResponseOpenOrderJson responseOpenOrderJson) {
                    ResponseOpenOrderJson confirmOrder = responseOpenOrderJson;
                    Intent intent = new Intent(CoffeeListActivity.this, OrderConfirmActivity.class);
                    intent.putExtra("type", getGlobalParams().getCurrentBizCode().getBizCode());
                    intent.putExtra("confirmOrder", (Serializable) confirmOrder);
                    startActivityForResult(intent, 999);
                }

                @Override
                public void onError(DcnException error) {
                    dismissLoading();
                }
            });
        } else {
            showAskLoginDialog();
        }
    }

    private OpenOrderJson orderJson(){
        double total =0.00;
        OpenOrderJson order = new OpenOrderJson();
        order.unitId = unitId;
        order.items = new ArrayList<CartJson>();
        for(int i = 0; i< proinfojson.size(); i++){
            CartJson cart = new CartJson();
            cart.count = proinfojson.get(i).number;
            cart.skuId = FormatCouponInfo.getDefaultSKUJson(proinfojson.get(i).productSkus).id;
            order.items.add(cart);
            total += FormatCouponInfo.getDefaultSKUJson(proinfojson.get(i).productSkus).onlinePrice* proinfojson.get(i).number;
        }
        order.amount =total;
        return order;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000)
        {
            if (resultCode == RESULT_OK) {
                long pid = data.getLongExtra("productId",0);
                ProductCart productDBModel = ProductCartHelper.getProduct(pid, unitId);
                if (productDBModel == null)
                    return ;
                ProductInfoJson productInfoJson = tabAdapter.updateShowProduct(pid, unitId, productDBModel.getNum());
                if (productInfoJson != null) {
                    boolean has = false;
                    for (int i = 0; i < proinfojson.size(); i++) {
                        if (proinfojson.get(i).id == pid) {
                            has = true;
                            proinfojson.get(i).number = productDBModel.getNum();
                        }
                    }
                    if (!has) {
                        proinfojson.add(productInfoJson);
                    }
                    updateBottom();
                }
            }
        } else if (requestCode == 999) {
            if (resultCode == 999) {
                clearProduct();
            }
        }

    }


    private void clearProduct(){

        ProductCartHelper.removeAllWidthUnitId(unitId);
        for (int i=0; i<proinfojson.size(); i++) {
            tabAdapter.updateShowProduct(proinfojson.get(i).id, unitId, 0);
        }
        proinfojson.clear();
        updateBottom();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(llPopoLv != null && llPopoLv.getVisibility() == View.VISIBLE){
                llPopoLv.setVisibility(View.GONE);
            }else{
                onBack();

            }
        }
        return false;
    }


    class CoffeeProductRvAdapters extends BaseAdapter {
        ArrayList<ProductInfoJson> data;
        Context c;

        public CoffeeProductRvAdapters(Context c, ArrayList<ProductInfoJson> al_proinfojson) {
            this.data = al_proinfojson;
            this.c = c;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder h;
            if (convertView == null) {
                h = new ViewHolder();
                convertView = LayoutInflater.from(c).inflate(R.layout.item_coffee_g, null);
                h.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                h.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
                h.tv_item_count = (TextView) convertView.findViewById(R.id.tv_item_count);
                h.iv_sub = (ImageView) convertView.findViewById(R.id.iv_sub);
                h.iv_add = (ImageView) convertView.findViewById(R.id.iv_add);
                if(Constants.BIZ_CODE.COFFEE.equalsIgnoreCase(type)){
                    h.iv_add.setVisibility(View.VISIBLE);
                }
                convertView.setTag(h);
            } else {
                h = (ViewHolder) convertView.getTag();
            }
            final ProductInfoJson map = data.get(position);
            h.tv_name.setText(map.name);


            SKUJson t = FormatCouponInfo.getDefaultSKUJson(map.productSkus);
            h.tv_price.setText(FormatCouponInfo.getYuanStr()+t.onlinePrice );
            h.tv_item_count.setText(map.number+"");
            h.iv_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    proinfojson.get(position).number--;
                    getIv_sub(map);
                    adapters.notifyDataSetChanged();

                }
            });

            h.iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    proinfojson.get(position).number++;
                    getIv_add(map);
                    adapters.notifyDataSetChanged();


                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView tv_name, tv_price, tv_item_count;
            ImageView iv_sub, iv_add;
        }
    }


}
