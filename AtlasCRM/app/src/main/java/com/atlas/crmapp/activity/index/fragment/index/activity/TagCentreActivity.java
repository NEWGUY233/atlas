package com.atlas.crmapp.activity.index.fragment.index.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.adapter.AATagTagAdapter;
import com.atlas.crmapp.bean.CircleDetailBean;
import com.atlas.crmapp.bean.TagBean;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.dagger.component.index.DaggerTagCentreActivityComponent;
import com.atlas.crmapp.dagger.module.index.TagCentreActivityModule;
import com.atlas.crmapp.model.VThreadsJson;
import com.atlas.crmapp.presenter.TagCentreActivityPresenter;
import com.atlas.crmapp.util.GetCommonObjectUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.util.TopBarScrollTransUtils;
import com.atlas.crmapp.view.MScroll;
import com.atlas.crmapp.view.RefreshFootView;
import com.atlas.crmapp.widget.CircleImageView;
import com.jaeger.library.StatusBarUtil;
import com.liaoinstan.springview.widget.SpringView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/21.
 */

public class TagCentreActivity extends BaseStatusActivity {


    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.iv_icon)
    CircleImageView ivIcon;
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.top_line)
    View topLine;
    @BindView(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @BindView(R.id.mscroll)
    MScroll mScroll;

    @Inject
    TagCentreActivityPresenter presenter;
    @BindView(R.id.tv_follow)
    TextView tvFollow;
    @BindView(R.id.tv_answer)
    TextView tvAnswer;
    @BindView(R.id.tv_tag_)
    TextView tvTag_;
    @BindView(R.id.tvText)
    TextView tvText;
    @BindView(R.id.rl_1)
    RelativeLayout rl1;
    @BindView(R.id.springView)
    SpringView springView;
    private RefreshFootView refreshFootView;

    @Override
    protected boolean translucentStatusBar() {
        return true;
    }

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_centre);
        ButterKnife.bind(this);

        initView();

        initData();
    }

    AATagTagAdapter adapter;

    private void initView() {
        DaggerTagCentreActivityComponent.builder().tagCentreActivityModule(new TagCentreActivityModule(this))
                .build().inject(this);

        setFinishedView(R.id.ibBack);
        LinearLayoutManager ll = new LinearLayoutManager(getContext());
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(ll);
        adapter = new AATagTagAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        mScroll.setOnScroll(new MScroll.OnScroll() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                if (t > 100) {
                    TopBarScrollTransUtils.setTitleBarBg(TagCentreActivity.this, rlTopBar, true);
                    StatusBarUtil.setTranslucentForImageView(TagCentreActivity.this, Constants.STATUS_BAR_ALPHA.BAR_ALPHA, null);
                    textViewTitle.setText(getString(R.string.index_tag_circle));
                } else {
                    TopBarScrollTransUtils.setTitleBarBg(TagCentreActivity.this, rlTopBar, false);
                    StatusBarUtil.setTransparentForImageView(TagCentreActivity.this, null);
                    textViewTitle.setText("");
                }
            }
        });

        refreshFootView = new RefreshFootView(this);
        springView.setFooter(refreshFootView);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadmore() {
                try {
                    presenter.getBodyList(adapter.getList().get(adapter.getItemCount() - 1).getId(), id);
                }catch (Exception e){

                }

            }
        });
    }

    TagBean bean;
    long id = 0;
    private void initData() {
        if (getIntent() != null)
            bean = (TagBean) getIntent().getSerializableExtra("data");

        if (bean != null) {
            id = bean.getId();
            GlideUtils.loadImageView(this, bean.getThumbnail(), ivIcon);
            tvTag.setText(bean.getName());
            presenter.getList(id);
            presenter.getBodyList(0, id);
        }else {
            try {
                String i = getIntent().getStringExtra("id");
                id = Long.valueOf(i);
            }catch (Exception e){
                id = getIntent().getLongExtra("id",0);
            }
            presenter.getList(id);
            presenter.getBodyList(0, id);
        }


    }

    public void setData(CircleDetailBean bean) {
        GlideUtils.loadImageView(this, bean.getThumbnail(), ivIcon);
        tvTag.setText(bean.getName());
        tvFollow.setText(StringUtils.isEmpty(bean.getFocusSum()) ? "0" : StringUtils.numberCheck(bean.getFocusSum().replace(".0","")));
        tvAnswer.setText(StringUtils.isEmpty(bean.getCommentSum()) ? "0" :  StringUtils.numberCheck(bean.getCommentSum().replace(".0","")));
        tvTag_.setText(StringUtils.isEmpty(bean.getThreadSum()) ? "0" :  StringUtils.numberCheck(bean.getThreadSum().replace(".0","")));
    }

    public void setData(List<VThreadsJson> list) {
        adapter.setList(list);
    }

    public void addData(List<VThreadsJson> list) {
        adapter.getList().addAll(list);
        GetCommonObjectUtils.onFinishFreshAndLoad(springView);
    }

}
