package com.atlas.crmapp.activity.index.fragment.index.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.adapter.AATagTagDetailAdapter;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.dagger.component.index.DaggerTagDetailActivityComponent;
import com.atlas.crmapp.dagger.module.index.TagDetailActivityModule;
import com.atlas.crmapp.model.VThreadsJson;
import com.atlas.crmapp.presenter.TagDetailActivityPresenter;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.util.GetCommonObjectUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.RefreshFootView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.liaoinstan.springview.widget.SpringView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/21.
 */

public class TagDetailActivity extends BaseStatusActivity implements AATagTagDetailAdapter.OnItemClick {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    VThreadsJson bean;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.all_comment)
    TextView all_comment;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_focus)
    ImageView ivFocus;
    @BindView(R.id.tv_focus)
    TextView tvFocus;


    @BindView(R.id.tv_hot)
    TextView tv_hot;
    @BindView(R.id.tv_new)
    TextView tv_new;

    @Inject
    TagDetailActivityPresenter presenter;

    @BindView(R.id.springView)
    SpringView springView;
    private RefreshFootView refreshFootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aat_tag_detail);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    AATagTagDetailAdapter adapter;
    String[] TYPE_LIKE = new String[]{"likeCnt_desc","createTime_desc"};
    String[] TYPE_TIME = new String[]{"createTime_desc"};
    String[] type = TYPE_LIKE;
    long page = 0;
    private void initView() {
        DaggerTagDetailActivityComponent.builder().tagDetailActivityModule(new TagDetailActivityModule(this))
                .build().inject(this);

        initToolbar();
        setTitle(getString(R.string.index_tag_detail));

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new AATagTagDetailAdapter(getContext());
        adapter.setClick(this);
        recyclerView.setAdapter(adapter);

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
                    ++page;
                    presenter.getCommentList(bean.getId(),page,type);
                }catch (Exception e){

                }
            }
        });

        tv_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TYPE_TIME.equals(type))
                    return;
                type = TYPE_TIME;
                tv_new.setTextColor(Color.parseColor("#FF2B3039"));
                tv_hot.setTextColor(Color.parseColor("#FF939393"));
                page = 0;
                presenter.getCommentList(bean.getId(),page,type);

            }
        });

        tv_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TYPE_LIKE.equals(type))
                    return;
                type = TYPE_LIKE;
                tv_new.setTextColor(Color.parseColor("#FF939393"));
                tv_hot.setTextColor(Color.parseColor("#FF2B3039"));
                page = 0;
                presenter.getCommentList(bean.getId(),page,type);
            }
        });

        tvTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean != null)
                startActivity(new Intent(TagDetailActivity.this,TagCentreActivity.class).putExtra("id",bean.getForumId()));
            }
        });
    }

    long id;
    private void initData() {
        if (getIntent() != null)
            bean = (VThreadsJson) getIntent().getSerializableExtra("data");

        if (bean != null) {
            updateView();
            id = bean.getId();
            presenter.getDetail(bean.getId());
            page = 0;
            presenter.getCommentList(bean.getId(), page,type);
        }else {
            try {
                springView.setVisibility(View.INVISIBLE);
                id = Long.valueOf(getIntent().getStringExtra(ActionUriUtils.id));
                presenter.getDetail(id);
                page = 0;
                presenter.getCommentList(id, page,type);
            }catch (Exception e){}
        }
    }

    private void updateView() {
        if (bean != null) {
            if (StringUtils.isEmpty(bean.getImage())) {
                ivBg.setVisibility(View.GONE);
            } else {
                ivBg.setVisibility(View.VISIBLE);
                GlideUtils.loadImageView(this, bean.getImage(), ivBg);
            }

            tvQuestion.setText(bean.getTitle());
            if (!StringUtils.isEmpty(bean.getForumName()))
                tvTag.setText(bean.getForumName());
            all_comment.setText(getString(R.string.all_comment).replace("%1$s", String.valueOf(bean.getCommentCnt())));
            tvContent.setText(bean.getContent());

            if (bean.isFoucs()) {
                ivFocus.setImageResource(R.mipmap.question_icon_collection);
                tvFocus.setText(R.string.followed);
            } else {
                ivFocus.setImageResource(R.mipmap.question_icon_collection_unsel);
                tvFocus.setText(R.string.follow);
            }
        }
    }

    @OnClick({R.id.ll_comment, R.id.ll_collect, R.id.ll_share})
    public void onViewClicked(View view) {
        if (!GlobalParams.getInstance().isLogin()){
            showAskLoginDialog();
            return;
        }

        switch (view.getId()) {
            case R.id.ll_comment:
                initCommentDialog();
                break;
            case R.id.ll_collect:
                presenter.collected(!bean.isFoucs(),bean.getId());
                break;
            case R.id.ll_share:
                initShareDialog();
                break;
        }
    }

    private void initCommentDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_tag_detail_comment);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);



        dialog.getWindow().findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tv = (EditText) dialog.getWindow().findViewById(R.id.comment);
                if (StringUtils.isEmpty(tv.getText().toString())) {
                    dialog.dismiss();
                    return;
                }
                presenter.commentTag(bean.getId(),initComment(tv.getText().toString()));
                dialog.dismiss();
            }
        });
    }

    private void initShareDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_tag_detail_share);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                presenter.postDynamic(((EditText)dialog.findViewById(R.id.et_comment)).getText().toString(),"topic:" + bean.getId(),"TOPIC");
            }
        });

        if (StringUtils.isEmpty(bean.getImage())){
            dialog.findViewById(R.id.d_icon).setVisibility(View.GONE);
        }else {
            dialog.findViewById(R.id.d_icon).setVisibility(View.VISIBLE);
            Glide.with(this).load(bean.getImage()).apply(new RequestOptions().centerCrop())
                    .into((ImageView) dialog.findViewById(R.id.d_icon));
        }

        ((TextView)dialog.findViewById(R.id.tv_content)).setText(bean.getContent());
    }

    public void setDetail(VThreadsJson bean) {
        springView.setVisibility(View.VISIBLE);
        this.bean = bean;
        updateView();
    }

    public void setCommentList(List<VThreadsJson> rows) {
        adapter.setRows(rows);
    }

    public void addCommentList(List<VThreadsJson> rows) {
        adapter.getRows().addAll(rows);
        adapter.notifyDataSetChanged();
        GetCommonObjectUtils.onFinishFreshAndLoad(springView);
    }


    public void commentSuccess(){
        presenter.getDetail(bean.getId());
        page = 0;
        presenter.getCommentList(bean.getId(), page,type);
    }

    public void onCollected(){
        bean.setFoucs(!bean.isFoucs());
        if (bean.isFoucs()) {
            ivFocus.setImageResource(R.mipmap.question_icon_collection);
            tvFocus.setText(R.string.followed);
        } else {
            ivFocus.setImageResource(R.mipmap.question_icon_collection_unsel);
            tvFocus.setText(R.string.follow);
        }
    }

    private String initComment(String str){
        String encodeStr = "";
        for (int i=0; i<str.length(); i++) {
            if (isEmojiCharacter(str.charAt(i))) {
                encodeStr = encodeStr + string2unicode(String.valueOf(str.charAt(i)));
            } else {
                encodeStr = encodeStr + str.charAt(i);
            }
        }

        return encodeStr;
    }

    public static String string2unicode(String s){
        int in;
        String st = "";
        for(int i=0;i<s.length();i++){
            in = s.codePointAt(i);
            st = st+"\\u"+Integer.toHexString(in).toUpperCase();
        }
        return st;
    }

    private boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

    @Override
    public void onClick(int position, VThreadsJson bean) {
        if (bean.isDeletable())
            showDeleteDialog(position,bean);
    }

    private void showDeleteDialog(int position, final VThreadsJson bean){
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_delete);
        ((TextView)dialog.findViewById(R.id.title)).setText(getString(R.string.txt_delete_comment));
        dialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.delete(bean.getId() + "");
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void onDelete(String id){
        presenter.getDetail(bean.getId());
        page = 0;
        presenter.getCommentList(bean.getId(), page,type);
    }
}
