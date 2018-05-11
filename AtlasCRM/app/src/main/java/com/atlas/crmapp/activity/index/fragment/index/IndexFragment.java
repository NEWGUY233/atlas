package com.atlas.crmapp.activity.index.fragment.index;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.IndexActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.ActiveAndTagActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.IndexPostNewsActivity;
import com.atlas.crmapp.activity.index.fragment.index.adapter.IndexActiveAdapter;
import com.atlas.crmapp.activity.index.fragment.index.adapter.IndexLocationAdapter;
import com.atlas.crmapp.activity.index.fragment.index.adapter.IndexRecyclerAdapter;
import com.atlas.crmapp.bean.DynamicSuccessBean;
import com.atlas.crmapp.bean.IndexMomentBean;
import com.atlas.crmapp.bean.LocationBean;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.dagger.component.index.DaggerIndexFragmentComponent;
import com.atlas.crmapp.dagger.component.index.IndexFragmentComponent;
import com.atlas.crmapp.dagger.module.index.IndexFragmentModule;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.presenter.IndexFragmentPresenter;
import com.atlas.crmapp.register.RegInfoActivity_;
import com.atlas.crmapp.tim.model.FriendshipInfo;
import com.atlas.crmapp.usercenter.RechargeActivity;
import com.atlas.crmapp.usercenter.UserCardActivity;
import com.atlas.crmapp.util.ImageUtil;
import com.atlas.crmapp.util.SpUtil;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.IndexTopViewpager;
import com.atlas.crmapp.view.MSpringView;
import com.atlas.crmapp.view.RefreshFootView;
import com.atlas.crmapp.view.RefreshHeaderView;
import com.atlas.crmapp.view.ViewPagerIndicatorOnBottomView_;
import com.atlas.crmapp.widget.CodeDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.liaoinstan.springview.container.BaseHeader;
import com.orhanobut.logger.Logger;
import com.tencent.imsdk.TIMManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.atlas.crmapp.util.ActionUriUtils.content;

/**
 * Created by Administrator on 2018/3/15.
 */

public class IndexFragment extends BaseFragment implements IndexRecyclerAdapter.OnPostFailed, IndexTopViewpager.OnItemClick {
    @BindView(R.id.viewpager)
    ViewPagerIndicatorOnBottomView_ viewpager;
    @BindView(R.id.recyclerView_active)
    RecyclerView recyclerViewActive;
    @BindView(R.id.recyclerView_index)
    RecyclerView recyclerViewIndex;
    @BindView(R.id.include_viewpager)
    IndexTopViewpager include_viewpager;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.scroll_view)
    ScrollView scroll_view;
    @BindView(R.id.springview)
    MSpringView springView;

    @Inject
    IndexFragmentPresenter presenter;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @BindView(R.id.re_pop)
    RecyclerView re_pop;

    @BindView(R.id.iv_user_icon)
    ImageView iv_user;

    @BindView(R.id.anim_plus)
    LottieAnimationView anim_plus;

    private RefreshFootView refreshFootView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {

    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
//        viewpager.initTestList();

        initView();
        getData();
//        startActivity(new Intent(getContext(), RegInfoActivity_.class));
        return rootView;
    }

    protected boolean isFirst = true;

    public IndexFragmentComponent component;

    IndexActiveAdapter activeAdapter;
    IndexRecyclerAdapter recyclerAdapter;
    IndexLocationAdapter locationAdapter;
    String uid;
    String uid_;
    int scrollY = 0;
    String user = "";
    private void initView() {
        if (getActivity() != null && getActivity() instanceof IndexActivity) {
            component = DaggerIndexFragmentComponent.builder().appComponent(((IndexActivity) getActivity()).getAppComponent())
                    .indexFragmentModule(new IndexFragmentModule(this))
                    .build();
            component.inject(this);
        }

        anim_plus.setVisibility(View.VISIBLE);
        textView.setText(R.string.app_name);

        LinearLayoutManager ll = new LinearLayoutManager(context);
        ll.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewActive.setLayoutManager(ll);

        activeAdapter = new IndexActiveAdapter(context);
        recyclerViewActive.setAdapter(activeAdapter);

        LinearLayoutManager ll1 = new LinearLayoutManager(context);
        ll1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewIndex.setLayoutManager(ll1);

        user = TIMManager.getInstance().getLoginUser();

        recyclerAdapter = new IndexRecyclerAdapter(this);
        recyclerAdapter.setOnPostFailed(this);
        recyclerViewIndex.setAdapter(recyclerAdapter);
        recyclerViewIndex.setNestedScrollingEnabled(false);

        LinearLayoutManager ll2 = new LinearLayoutManager(context);
        ll2.setOrientation(LinearLayoutManager.VERTICAL);
        re_pop.setLayoutManager(ll2);

        locationAdapter = new IndexLocationAdapter(getContext());
        re_pop.setAdapter(locationAdapter);
        re_pop.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scroll_view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    IndexFragment.this.scrollY = scrollY;
                    initLocationPop(v.getId());
                }
            });
        }

        springView.setHeader(new IndexHeader());

        refreshFootView = new RefreshFootView(getActivity());
        springView.setFooter(refreshFootView);

        springView.setType(MSpringView.Type.FOLLOW);

        springView.setListener(new MSpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                anim_plus.setProgress(1f);
            }

            @Override
            public void onLoadmore() {
                try{
                    presenter.getMoment(uid,recyclerAdapter.getList().get(recyclerAdapter.getItemCount() - 1).getCreateTime());
                }catch (Exception e){}

            }
        });

        uid = 1 + "";
        uid_ = getGlobalParams().getAtlasId() + "";
        tvLocation.setText(getString(R.string.all_world));

        locationAdapter.setClick(new IndexLocationAdapter.OnItemClick() {
            @Override
            public void onClick(String uid, int position,String name) {
                IndexFragment.this.uid = uid;
                presenter.getMoment(uid,0);
                tvLocation.setText(name);
                re_pop.setVisibility(View.GONE);
            }
        });

        if (GlobalParams.getInstance().isLogin()){
            Glide.with(context).load(SpUtil.getString(context,SpUtil.ICON,"")).into(iv_user);
        }else {
            iv_user.setImageResource(R.mipmap.icon_informationheard);
        }

        util = new ImageUtil(getContext());

        anim_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scrollY <= 0) {
                    if (anim_plus.getProgress() == 0) {
                        springView.callFresh();
                        anim_plus.setSpeed(1f);
                        anim_plus.playAnimation(0, 1f);
                    } else {
                        springView.onFinishFreshAndLoad();
                        anim_plus.setSpeed(-1f);
                        anim_plus.playAnimation(1f, 0);
                    }
                }else {
                    initBox();
                }
            }
        });

        springView.setOnPullListener(new MSpringView.OnPullListener() {
            @Override
            public void onPull(int y) {
                if (scrollY > 100)
                    return;

                if (y == 0) {
                    anim_plus.setProgress(0);
                }else if (y >0 && y <= springView.getHeadHeight()){
                    float f = (float) y / (float) springView.getHeadHeight();
                    anim_plus.setProgress(f);
                }else {
                    anim_plus.setProgress(1f);
                }
            }

            @Override
            public void onUp(int y) {
                if (scrollY > 100)
                    return;
                if (y >  springView.getHeadHeight()/2) {
                    anim_plus.setSpeed(1f);
                    anim_plus.playAnimation(anim_plus.getProgress(), 1f);
                } else {
                    anim_plus.setSpeed(-1f);
                    anim_plus.playAnimation(anim_plus.getProgress(), 0);
                }
            }
        });
    }

    ImageUtil util;
    private void getData(){
        presenter.getBannerRequestResource();
        presenter.getRecommendRequestResource();
        presenter.getMoment(uid,0);
        presenter.getLocation();
    }

    public void setBanner(ResourceJson resourceJson){
        if (resourceJson == null || resourceJson.rows == null || resourceJson.rows.size() == 0)
            return;
        viewpager.updateViews((ArrayList<ResourceJson.ResourceMedia>) resourceJson.rows.get(0).resourceMedias);
        viewpager.cancel();
        viewpager.startTimer();
    }

    public void setActiveAdapter(List<ResourceJson.ResourceMedia> list){
        activeAdapter.setList(list);
    }

    public void setRecyclerViewIndex(IndexMomentBean bean){
        recyclerAdapter.setList(bean.getRows());
        springView.onFinishFreshAndLoad();
    }

    public void addRecyclerViewIndex(IndexMomentBean bean){
        recyclerAdapter.getList().addAll(bean.getRows());
        recyclerAdapter.notifyDataSetChanged();
        springView.onFinishFreshAndLoad();
    }

    public void setPop(final List<LocationBean> list){
        locationAdapter.setList(list);
        if (list != null && list.size() != 0)
            tvLocation.setText(list.get(list.size() -1).getName());
        else
            tvLocation.setText("");
    }


    @OnClick({R.id.iv_box, R.id.iv_scan_to_scan, R.id.iv_code, R.id.tv_location, R.id.btn_submit, R.id.ll_all, R.id.ll_all_, R.id.scroll_view, R.id.tv_active_tag})
    public void onViewClicked(View view) {

        initLocationPop(view.getId());
        switch (view.getId()) {
            case R.id.iv_box:
                initBox();
                break;
            case R.id.iv_scan_to_scan:
                toScan();
                break;
            case R.id.iv_code:
                initScan();
                break;
            case R.id.tv_active_tag:
                startActivity(new Intent(getContext(), ActiveAndTagActivity.class));
                break;
            case R.id.tv_location:
//                initLocationPop(view.getId());
                break;
            case R.id.btn_submit:
                if (!getGlobalParams().isLogin()) {
                    showAskLoginDialog(getHoldingActivity());
                    return;
                }
                startActivityForResult(new Intent(getContext(), IndexPostNewsActivity.class),0x333);
                break;
        }
    }

    private void toScan() {
        if (getGlobalParams().isLogin()) {
            IntentIntegrator.forSupportFragment(this)
                    .setBarcodeImageEnabled(false)
                    .setOrientationLocked(true)
                    .setPrompt(getString(R.string.text_70))
                    .initiateScan(IntentIntegrator.QR_CODE_TYPES);
        } else {
            showAskLoginDialog(getHoldingActivity());
        }
    }

    CodeDialog mCodeDialog;

    private void initScan() {
        if ((getHoldingActivity()).getGlobalParams().isLogin()) {

            if (mCodeDialog == null) {
                mCodeDialog = CodeDialog.newInstance();
            }

            if (!mCodeDialog.isAdded() && !mCodeDialog.isVisible() && !mCodeDialog.isRemoving()) {
                mCodeDialog.show(getHoldingActivity().getSupportFragmentManager(), "code_dialog");
            }
        } else {
            showAskLoginDialog(getHoldingActivity());
        }
    }

    private void initBox() {
        initTopPop();
    }

    PopupWindow popTop;
    private void initTopPop() {
        IndexTopViewpager v = new IndexTopViewpager(getContext());
        v.setDialog(null);
        popTop = new PopupWindow(v, getResources().getDisplayMetrics().widthPixels, (int) (getResources().getDisplayMetrics().density * 184), true);
        popTop.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white_color)));
        v.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popTop.dismiss();
            }
        });
        popTop.setAnimationStyle(R.style.PopAnim);
        popTop.showAtLocation(getView(), Gravity.NO_GRAVITY, 0, 0);

        v.setClick(this);
    }

    private void initLocationPop(int id) {
        if (id == R.id.tv_location) {
            if (re_pop.getVisibility() == View.VISIBLE) {
                re_pop.setVisibility(View.GONE);
            } else {
                re_pop.setVisibility(View.VISIBLE);
            }
        }else {
            re_pop.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
            } else {
                if (requestCode == IntentIntegrator.REQUEST_CODE) {

                    final String urlCode = result.getContents();
                    if (TextUtils.isEmpty(urlCode)) {
                        return;
                    }
                    Logger.d("result.getContents()====" + urlCode);
                    Intent intent = null;
                    if (urlCode.contains(Constants.CodeSkipType.TO_USER)) {
                        intent = new Intent(getHoldingActivity(), UserCardActivity.class);
                        intent.putExtra("code", result.getContents());
                        startActivity(intent);
                    } else if (urlCode.contains(Constants.CodeSkipType.TO_RECHARGE)) {
                        intent = new Intent(getActivity(), RechargeActivity.class);
                        String[] urlSplit = urlCode.split("/");
                        intent.putExtra("referral", urlSplit[urlSplit.length - 1]);
                        startActivity(intent);
                    } else if (urlCode.contains(Constants.CodeSkipType.TO_ACTIVITY_VERIFI)) {
                        presenter.requestAppUserGeteam(urlCode);
                    } else if (urlCode.contains(Constants.CodeSkipType.LOGIN_PC)) {
                        presenter.printLog(StringUtils.getLoginInfo(urlCode));
                    } else if (urlCode.contains(Constants.CodeSkipType.UNLOCK_PRINT)) {
                        presenter.unlockPrint(StringUtils.unLockPrint(urlCode));
                    }
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (resultCode == 0x333 && requestCode == 0x333){
            ArrayList<String> imgs = data.getStringArrayListExtra("imgs");
            String content = data.getStringExtra("content");
            addItem(content,imgs);
            if (imgs == null || imgs.size() == 0){
                presenter.postDynamic(imgs,content);
            }else
                util.uploadPhotoToServer(imgs,onPostImg);
        }else if (resultCode == 0x334 && requestCode == 0x334){
            presenter.getMoment(uid,0);
        }
    }
    IndexMomentBean.RowsBean bean;
    private void addItem(String content,List<String> list){
        bean = new IndexMomentBean.RowsBean();
        bean.setImgs(list);
        bean.setContent(content);
        bean.setPost(true);
        bean.setCreateTime(System.currentTimeMillis());
        IndexMomentBean.RowsBean.UserBean user = new IndexMomentBean.RowsBean.UserBean();
        user.setNick(SpUtil.getString(getContext(),SpUtil.NICK,""));
        user.setAvatar(SpUtil.getString(getContext(),SpUtil.ICON,""));
        user.setCompany(getGlobalParams().getPersonInfoJson().getCompany());
        user.setRelate("OWN");
        bean.setUser(user);

        recyclerAdapter.getList().add(0,bean);
        recyclerAdapter.notifyDataSetChanged();

    }

    public void postFailed(){
        bean.setFailed(true);
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailed() {
        ArrayList<String> imgs = (ArrayList<String>) bean.getImgs();
        String content = bean.getContent();
        bean.setFailed(false);
        recyclerAdapter.notifyDataSetChanged();
        if (imgs == null || imgs.size() == 0){
            presenter.postDynamic(imgs,content);
        }else
            util.uploadPhotoToServer(imgs,onPostImg);
    }

    public void onSuccess(DynamicSuccessBean bean){
        this.bean.setId(bean.getId());
        this.bean.setPost(false);
        this.bean.setCreateTime(bean.getCreateTime());
    }

    @Override
    public void onClick(String name, int rId) {
        springView.onFinishFreshAndLoad();
        anim_plus.setProgress(0);
        if (popTop != null && popTop.isShowing())
            popTop.dismiss();
    }

    private class IndexHeader extends BaseHeader {

        @Override
        public View getView(LayoutInflater inflater, ViewGroup viewGroup) {
            View view = inflater.inflate(R.layout.header_index, viewGroup, true);
            IndexTopViewpager vp = (IndexTopViewpager) view.findViewById(R.id.header_tools);
            vp.setTopBar(null);
            vp.setClick(IndexFragment.this);
            return view;
        }

        @Override
        public void onPreDrag(View rootView) {

        }

        @Override
        public void onDropAnim(View rootView, int dy) {

        }

        @Override
        public void onLimitDes(View rootView, boolean upORdown) {

        }

        @Override
        public void onStartAnim() {

        }

        @Override
        public void onFinishAnim() {

        }
    }

    ImageUtil.OnPostImg onPostImg = new ImageUtil.OnPostImg() {
        @Override
        public void getDownloadUrl(String serverImagePath) {
        }

        @Override
        public void getDownloadUrlList(List<String> serverImagePath) {
            presenter.postDynamic(serverImagePath,bean.getContent());
        }

        @Override
        public void onFailed() {
            postFailed();
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){

        }else {
            Glide.with(getContext()).load(getGlobalParams().getPersonInfoJson().getAvatar())
                    .apply(new RequestOptions().error(R.mipmap.icon_informationheard))
                    .into(iv_user);

            String uid = GlobalParams.getInstance().getAtlasId() + "";
            if (!uid_.equals(uid)){
                this.uid_ = uid;
                this.uid = 1 + "";
                getData();
            }

            String user = TIMManager.getInstance().getLoginUser();
            if (this.user == null && user == null)
                return;
            else if (this.user == null && user != null) {
                this.user = user;
                getData();
            }
            else if (this.user != null && !this.user.equals(user)){
                this.user = user;
                getData();
            }

        }
    }

    public void click(){
        if (scrollY > 5){
            scroll_view.smoothScrollTo(0,0);
        }else {
            getData();
        }
    }

}
