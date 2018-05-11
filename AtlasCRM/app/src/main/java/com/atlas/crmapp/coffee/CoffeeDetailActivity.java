package com.atlas.crmapp.coffee;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.db.hepler.ProductCartHelper;
import com.atlas.crmapp.db.model.ProductCart;
import com.atlas.crmapp.model.ProductInfoJson;
import com.atlas.crmapp.model.SKUJson;
import com.atlas.crmapp.model.bean.Point;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.ProductInfoView;
import com.atlas.crmapp.view.ProductIntroduceView;
import com.atlas.crmapp.view.ViewPagerIndicatorInsideView;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.badgeview.BGABadgeImageView;

public class CoffeeDetailActivity extends BaseStatusActivity {

    @BindView(R.id.vp_coffee_detail)
    ViewPagerIndicatorInsideView vpCoffeeDetail;

    @BindView(R.id.v_produt_inroduce)
    ProductIntroduceView vIntroduce;

    @BindView(R.id.v_product_info)
    ProductInfoView vProductInfo;

    @BindView(R.id.textViewTitle)
    TextView mTbTvTitle;

    @BindView(R.id.vs_dot_start)
    View vDotStart;
    @BindView(R.id.iv_market)
    ImageView ivMarket;
    @BindView(R.id.rl_add_to_cart)
    RelativeLayout rlAddToCart;

    @BindView(R.id.ibHome)
    BGABadgeImageView ibHome;

    @BindView(R.id.btn_add_to_cart)
    Button btn_add_to_cart;

    private int mNum;
    long mProductId;
    long mUnitId;
    String mbizCode;
    boolean mfromList;
    ProductInfoJson pinfo;
    private String title;

    private final int TO_COFFEELIST_REQUEST_CODE = 1100;

    @OnClick(R.id.ibBack)
    void onBack(){
        if (mfromList) {
            setResult(RESULT_OK, new Intent().putExtra("productId", mProductId));
        }
        this.finish();


    };

    @OnClick({R.id.ibHome, R.id.iv_market})
    void onMarket(){
        if (mfromList) {
            setResult(RESULT_OK, new Intent().putExtra("productId", mProductId));
            this.finish();
        } else {
            Intent intent = new Intent(CoffeeDetailActivity.this, CoffeeListActivity.class);
            intent.putExtra("type", mbizCode);
            intent.putExtra("unitId", mUnitId);
            intent.putExtra("name", title);
            CoffeeDetailActivity.this.startActivityForResult(intent, TO_COFFEELIST_REQUEST_CODE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_detail);
        mProductId = getIntent().getLongExtra("productId",0);
        mUnitId = getIntent().getLongExtra("unitId",0);
        mbizCode = getIntent().getStringExtra("bizCode");
        mfromList = getIntent().getBooleanExtra("fromList", false);
        title = getGlobalParams().getBizName(mbizCode);
        if(StringUtils.isEmpty(title)){
            title = getGlobalParams().getCurrentBizCode().getBizName();
        }
        mTbTvTitle.setText(title);

        prepareActivityData();
    }

    @Override
    protected int setTopBar() {
        return R.layout.titlebarwithbadge;
    }

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
    BizDataRequest.requestProductInfo(this, mProductId+"", statusLayout,new BizDataRequest.OnProductInfo() {
            @Override
            public void onSuccess(ProductInfoJson productInfoJson) {
                pinfo = productInfoJson;
                if(pinfo != null){
                    mUnitId = pinfo.unitId;
                    mbizCode = pinfo.bizCode;
                }
                updateActivityViews();
            }

            @Override
            public void onError(DcnException error) {
                if(error.getCode() != Constants.NetWorkCode.NO_NET_WORK){
                    showToast(error.getMessage());
                    CoffeeDetailActivity.this.finish();
                }

            }
        });
    }

    @Override
    protected void updateActivityViews() {
        super.updateActivityViews();
        title = getGlobalParams().getBizName(mbizCode);
        if(StringUtils.isEmpty(title)){
            title = getGlobalParams().getCurrentBizCode().getBizName();
        }
        mTbTvTitle.setText(title);

        /*下期*/
        if(Constants.BIZ_CODE.COFFEE.equalsIgnoreCase(mbizCode)){
            ibHome.setVisibility(View.VISIBLE);
            ivMarket.setVisibility(View.VISIBLE);
            rlAddToCart.setVisibility(View.VISIBLE);
        }

        SKUJson map = FormatCouponInfo.getDefaultSKUJson(pinfo.productSkus);

        vProductInfo.updateViews(pinfo.name, map.onlinePrice, pinfo.description);
        vpCoffeeDetail.updateViews(map.productMedias);
        vIntroduce.updateVies(pinfo.detailMedias ,"",pinfo.detail);
        updateBadge();

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductCart productCart = ProductCartHelper.getProduct(mProductId, mUnitId);
                if(productCart == null){
                    ProductCartHelper.insertProduct(mProductId, 1, mbizCode, mUnitId);
                }else {
                    ProductCartHelper.updateProduct(mProductId, productCart.getNum() + 1, mbizCode, mUnitId);
                }
                updateBadge();
                int[] loc = new int[2];
                vDotStart.getLocationInWindow(loc);
                CoffeeDetailActivity.this.playAnimation(loc);
            }
        });

    }

    //更新顶部导航 购物车
    private void updateBadge(){
        List<ProductCart> productCartList = ProductCartHelper.getAllProduct(mUnitId);
        int productCount = 0;
        for(ProductCart productCart : productCartList){
            productCount = productCount + productCart.getNum();
        }
        if (productCount > 0) {
            ibHome.showTextBadge(String.valueOf(productCount));
        } else {
            ibHome.hiddenBadge();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TO_COFFEELIST_REQUEST_CODE)
        {
           updateBadge();
        }
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareActivityData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
        }
        return false;
    }

    //加入购物车动画
    public void playAnimation(int[] position){
        final ImageView mImg = new ImageView(this);
        mImg.setImageResource(R.drawable.circle_dot);
        mImg.setScaleType(ImageView.ScaleType.MATRIX);
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        rootView.addView(mImg);
        Logger.d(position[0] + "position----");
        int[] des = new int[2];
        ibHome.getLocationInWindow(des);

        /*动画开始位置，也就是物品的位置;动画结束的位置，也就是购物车的位置 */
        Point startPosition = new Point(position[0], position[1]);
        Point endPosition = new Point(des[0]+ibHome.getWidth()/2, des[1]+ibHome.getHeight()/2-50);

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
                ViewGroup rootView = (ViewGroup) CoffeeDetailActivity.this.getWindow().getDecorView();
                rootView.removeView(mImg);
            }
        });
    }
}
