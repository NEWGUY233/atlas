package com.atlas.crmapp.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.IndexFragment;
import com.atlas.crmapp.activity.index.fragment.index.activity.IndexPostNewsActivity;
import com.atlas.crmapp.activity.index.fragment.index.adapter.IndexRecyclerAdapter;
import com.atlas.crmapp.bean.DynamicSuccessBean;
import com.atlas.crmapp.bean.IndexMomentBean;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.dagger.component.index.DaggerMyDynamicActivityComponent;
import com.atlas.crmapp.dagger.module.index.MyDynamicActivityModule;
import com.atlas.crmapp.presenter.MyDynamicActivityPresenter;
import com.atlas.crmapp.util.ImageUtil;
import com.atlas.crmapp.util.SpUtil;
import com.atlas.crmapp.view.RefreshFootView;
import com.atlas.crmapp.view.RefreshHeaderView;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/11.
 */

public class MyDynamicActivity extends BaseStatusActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.springview)
    SpringView springView;
    private RefreshFootView refreshFootView;

    @Inject
    MyDynamicActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_recycler);
        ButterKnife.bind(this);

        initView();
        initData();

    }

    IndexRecyclerAdapter adapter;

    private void initView() {
        DaggerMyDynamicActivityComponent.builder().myDynamicActivityModule(new MyDynamicActivityModule(this))
                .build().inject(this);

        initToolbar();
        setTitle(getString(R.string.my_act));
        setTopRightButton(getString(R.string.index_news_submit), new OnClickListener() {
            @Override
            public void onClick() {
                startActivityForResult(new Intent(getContext(), IndexPostNewsActivity.class),0x333);
            }
        });

        setVerticalManager(recyclerView);
        adapter = new IndexRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);


        refreshFootView = new RefreshFootView(this);
        springView.setFooter(refreshFootView);
        springView.setHeader(new RefreshHeaderView(this));

        springView.setType(SpringView.Type.FOLLOW);

        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadmore() {
                try{
                    presenter.getMoment(adapter.getList().get(adapter.getItemCount()-1).getCreateTime());
                }catch (Exception e){}
            }
        });
    }

    private void initData() {
        presenter.getMoment(0);
    }

    public void setList(IndexMomentBean bean){
        springView.onFinishFreshAndLoad();
        adapter.setList(bean.getRows());
    }

    public void addList(IndexMomentBean bean){
        springView.onFinishFreshAndLoad();
        adapter.getList().addAll(bean.getRows());
        adapter.notifyDataSetChanged();
    }

    ImageUtil util;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x334 && requestCode == 0x334 ){
            presenter.getMoment(0);
        }else if (resultCode == 0x333 && requestCode == 0x333){
            ArrayList<String> imgs = data.getStringArrayListExtra("imgs");
            String content = data.getStringExtra("content");
            addItem(content,imgs);
            if (imgs == null || imgs.size() == 0){
                presenter.postDynamic(imgs,content);
            }else {
                util = new ImageUtil(this);
                util.uploadPhotoToServer(imgs, onPostImg);
            }
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


    public void postFailed(){
        bean.setFailed(true);
        adapter.notifyDataSetChanged();
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

        adapter.getList().add(0,bean);
        adapter.notifyDataSetChanged();

    }

    public void onSuccess(DynamicSuccessBean bean){
        this.bean.setId(bean.getId());
        this.bean.setPost(false);
        this.bean.setCreateTime(bean.getCreateTime());
    }
}
