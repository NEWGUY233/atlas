package com.atlas.crmapp.activity.index.fragment.index.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.adapter.IndexDynamicCommentAdapter;
import com.atlas.crmapp.activity.index.fragment.index.adapter.IndexRecyclerPicAdapter;
import com.atlas.crmapp.bean.DynamicCommentBean;
import com.atlas.crmapp.bean.IndexMomentBean;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.dagger.component.index.DaggerIndexDynamicDetailActivityComponent;
import com.atlas.crmapp.dagger.module.index.IndexDynamicDetailActivityModule;
import com.atlas.crmapp.model.VThreadsJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.presenter.IndexDynamicDetailActivityPresenter;
import com.atlas.crmapp.usercenter.UserCardActivity;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.RefreshFootView;
import com.atlas.crmapp.widget.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.liaoinstan.springview.widget.SpringView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/22.
 */

public class IndexDynamicDetailActivity extends BaseStatusActivity {

    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.iv_icon)
    CircleImageView ivIcon;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_tips)
    ImageView ivTips;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.type1_title)
    TextView type1Title;
    @BindView(R.id.type1_content)
    TextView type1Content;
    @BindView(R.id.ll_type_1)
    LinearLayout llType1;
    @BindView(R.id.type2_pic)
    ImageView type2Pic;
    @BindView(R.id.type2_title)
    TextView type2Title;
    @BindView(R.id.ll_type_2)
    LinearLayout llType2;
    @BindView(R.id.type3_pic)
    ImageView type3Pic;
    @BindView(R.id.type3_title)
    TextView type3Title;
    @BindView(R.id.type3_content)
    TextView type3Content;
    @BindView(R.id.ll_type_3)
    RelativeLayout llType3;
    @BindView(R.id.rcv_pic)
    RecyclerView rcvPic;
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.tv_tag_text)
    TextView tvTagText;
    @BindView(R.id.ll_tag)
    LinearLayout llTag;
    @BindView(R.id.tv_num_comment)
    TextView tvNumComment;
    @BindView(R.id.ll_comment)
    LinearLayout llComment;
    @BindView(R.id.iv_like)
    ImageView ivLike;
    @BindView(R.id.tv_num_like)
    TextView tvNumLike;
    @BindView(R.id.all_comment)
    TextView allComment;
    @BindView(R.id.ll_like)
    LinearLayout llLike;
    @BindView(R.id.ll_3)
    LinearLayout ll3;
    @BindView(R.id.include_content)
    View ll2;
    @BindView(R.id.ll_comment_)
    LinearLayout llComment_;

    @BindView(R.id.tv_hot)
    TextView tv_hot;
    @BindView(R.id.tv_new)
    TextView tv_new;

    @BindView(R.id.springView)
    SpringView springView;
    private RefreshFootView refreshFootView;
    @Inject
    IndexDynamicDetailActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_detail_dynamic);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    IndexDynamicCommentAdapter adapter;
    long page = 0;
    private void initView() {
        DaggerIndexDynamicDetailActivityComponent.builder()
                .indexDynamicDetailActivityModule(new IndexDynamicDetailActivityModule(this))
                .build().inject(this);

        initToolbar();
        setTitle(getString(R.string.text_69));
        setTopRightButton("", R.mipmap.nav_icon_more, new OnClickListener() {
            @Override
            public void onClick() {
                if (bean!= null && bean.isDeletable()){
                    initDeletePop();
                }

            }
        });

        tvTime.setVisibility(View.VISIBLE);
        tvLocation.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);

        LinearLayoutManager ll = new LinearLayoutManager(getContext());
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(ll);
        adapter = new IndexDynamicCommentAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        adapter.setClick(new IndexDynamicCommentAdapter.OnDeleteClick() {
            @Override
            public void onDelete(int position, String id) {
                showDeleteDialog(position,id);
            }
        });

        allComment.setText(getString(R.string.all_comment).replace("%1$s", "0"));

        llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bean.isPraised())
                    BizDataRequest.likeDynamic(getContext(), bean.getId(), new BizDataRequest.OnRequestResult() {
                        @Override
                        public void onSuccess() {
                            bean.setPraised(true);
                            bean.setPraiseQuantity(bean.getPraiseQuantity() + 1);
                            ivLike.setImageResource(R.mipmap.icon_like_sel);
                            tvNumLike.setText(bean.getPraiseQuantity() + getString(R.string.dynamic_like));
                        }

                        @Override
                        public void onError(DcnException error) {
                        }
                    });
            }
        });

        tv_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TYPE_TIME.equals(type))
                    return;
                type = TYPE_TIME;
                page = 0;
                tv_new.setTextColor(Color.parseColor("#FF2B3039"));
                tv_hot.setTextColor(Color.parseColor("#FF939393"));
                presenter.getComment(bean.getId(),type,page);
            }
        });

        tv_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TYPE_LIKE.equals(type))
                    return;
                type = TYPE_LIKE;
                page = 0;
                tv_new.setTextColor(Color.parseColor("#FF939393"));
                tv_hot.setTextColor(Color.parseColor("#FF2B3039"));
                presenter.getComment(bean.getId(),type,page);
            }
        });

        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean != null && bean.getUser() != null && !"OFFICIAL".equals(bean.getUser().getRelate()))
                    startActivity(new Intent(getContext(), UserCardActivity.class).putExtra("id",bean.getUser().getId()));
            }
        });
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean != null && bean.getUser() != null && !"OFFICIAL".equals(bean.getUser().getRelate()))
                    startActivity(new Intent(getContext(), UserCardActivity.class).putExtra("id",bean.getUser().getId()));
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
                    ++page;
                    presenter.getComment(bean.getId(), type, page);
                }catch (Exception e){}
            }
        });
    }

    IndexMomentBean.RowsBean bean;
    String[] TYPE_LIKE = new String[]{"praiseQuantity_desc","createTime_desc"};
    String[] TYPE_TIME = new String[]{"createTime_desc"};
    String[] type = TYPE_LIKE;
    private void initData() {
        bean = (IndexMomentBean.RowsBean) getIntent().getSerializableExtra("data");
        if (bean == null) {
            this.bean = new IndexMomentBean.RowsBean();

            String id = getIntent().getStringExtra("id");
            if (StringUtils.isEmpty(id))
                return;
            this.bean.setId(id);

            presenter.getComment(bean.getId(),type,page);
            presenter.getDetail(bean.getId());
            return;
        }

        presenter.getComment(bean.getId(),type,page);
        presenter.getDetail(bean.getId());

        llTag.setVisibility(View.INVISIBLE);
        initUser();
        llType1.setVisibility(View.GONE);
        llType2.setVisibility(View.GONE);
        llType3.setVisibility(View.GONE);
        rcvPic.setVisibility(View.GONE);
        if (bean.getSubDetail() != null)
            initContent();
        else
            initPic();

        tvTime.setVisibility(View.VISIBLE);
        tvTime.setText(DateUtil.formatTime(bean.getCreateTime(),"yy-MM-dd HH:mm"));
    }

    public void noneDynamic(){
        springView.setVisibility(View.GONE);
        llComment_.setVisibility(View.GONE);
    }

    private void initUser(){
        if (bean == null)
            return;
        IndexMomentBean.RowsBean.UserBean user = bean.getUser();
        Glide.with(this).load(user.getAvatar()).apply(new RequestOptions().error(R.mipmap.icon_informationheard))
                .into(ivIcon);
        tvName.setText(user.getNick());
        tvCompany.setText(user.getCompany());
        if (StringUtils.isEmpty(user.getRelate())){
            ivTips.setVisibility(View.GONE);
        }else {
            ivTips.setVisibility(View.VISIBLE);
            if ("OFFICIAL".equals(user.getRelate())) {
                ivTips.setImageResource(R.mipmap.tips_icon_offical);
                ivIcon.setImageResource(R.mipmap.icon_homepic);
            }else if ("FRIEND".equals(user.getRelate()))
                ivTips.setImageResource(R.mipmap.tips_icon_friend);
            else
                ivTips.setVisibility(View.GONE);
        }

        tvNumComment.setText(StringUtils.numberCheck(bean.getCommentQuantity()) + getString(R.string.dynamic_comment));
        tvNumLike.setText(StringUtils.numberCheck(bean.getPraiseQuantity()) + getString(R.string.dynamic_like));
        allComment.setText(getString(R.string.all_comment).replace("%1$s",StringUtils.numberCheck(bean.getCommentQuantity()) + ""));
        if (bean.isPraised()){
            ivLike.setImageResource(R.mipmap.icon_like_sel);
        }else {
            ivLike.setImageResource(R.mipmap.icon_like_unsel);
        }

        tvComment.setText(StringUtils.unicode2String(bean.getContent()));

    }

    private void initContent(){
        if (this.bean == null)
            return;
        final IndexMomentBean.RowsBean.SubDetailBean bean = this.bean.getSubDetail();
        if (bean == null){
            return;
        }else {
            if (StringUtils.isEmpty(bean.getImg())){
                llType1.setVisibility(View.VISIBLE);

                ((TextView)llType1.findViewById(R.id.type1_title)).setText(bean.getTitle());
                ((TextView)llType1.findViewById(R.id.type1_content)).setText(bean.getContent());
            }else {

                if (StringUtils.isEmpty(bean.getContent())){
                    llType2.setVisibility(View.VISIBLE);
                    ((TextView)llType2.findViewById(R.id.type2_title)).setText(bean.getTitle());
                    Glide.with(this).load(bean.getImg()).apply(new RequestOptions().centerCrop())
                            .into((ImageView) llType2.findViewById(R.id.type2_pic));
                }else {
                    llType3.setVisibility(View.VISIBLE);
                    ((TextView)llType3.findViewById(R.id.type3_title)).setText(bean.getTitle());
                    ((TextView)llType3.findViewById(R.id.type3_content)).setText(bean.getContent());
                    Glide.with(this).load(bean.getImg()).apply(new RequestOptions().centerCrop())
                            .into((ImageView) llType3.findViewById(R.id.type3_pic));
                }

            }
            tvTagText.setVisibility(View.VISIBLE);
            tvTag.setVisibility(View.VISIBLE);

            if (StringUtils.isEmpty(bean.getSource())){
                tvTagText.setVisibility(View.GONE);
                tvTag.setVisibility(View.GONE);
            }

            tvTag.setText("「 " + bean.getSource() + " 」");
            ll2.setClickable(true);
            ll2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(ActionUriUtils.getIntent(getContext(),bean.getActionUri(),"",""));
                }
            });

            tvTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), TagCentreActivity.class).putExtra("id",bean.getForumId()));
                }
            });

        }


    }

    private void initPic(){
        ll2.setClickable(false);
        tvTag.setClickable(false);
        rcvPic.setVisibility(View.VISIBLE);
        List<String> list = bean.getImgs();
        if (list != null && list.size() > 0) {
            if (list.size() == 1) {
                rcvPic.setLayoutManager(new GridLayoutManager(this, 1));
            } else if (list.size() == 3) {
                rcvPic.setLayoutManager(new GridLayoutManager(this, 2));
            } else {
                rcvPic.setLayoutManager(new GridLayoutManager(this, 3));
            }
            rcvPic.setAdapter(new IndexRecyclerPicAdapter(this, list));
        }
    }
    PopupWindow pop;
    private void initDeletePop(){
        if (pop == null) {
            pop = new PopupWindow(this);
            pop.setContentView(LayoutInflater.from(this).inflate(R.layout.pop_delete, null));
            pop.setBackgroundDrawable(new ColorDrawable());
            pop.setOutsideTouchable(true);
            pop.setFocusable(true);
            pop.showAsDropDown(getRightBtn());
            pop.getContentView().findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop.dismiss();
                    if (bean.isDeletable()){
                        showDeleteDialog();
                    }
                }
            });
        }else {
            if (pop.isShowing())
                pop.dismiss();
            else
                pop.showAsDropDown(getRightBtn());
        }
    }

    private void showDeleteDialog(){
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_delete);
        dialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deleteDynamic(bean.getId());
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

    public void delete(){
        setResult(0x334);
    }

    public void setAdapter(DynamicCommentBean list){
        adapter.setList(list.getRows());
//        allComment.setText(getString(R.string.all_comment).replace("%1$s",StringUtils.numberCheck(list.getRecordsTotal()) + ""));
//        tvNumComment.setText(StringUtils.numberCheck(list.getRecordsTotal()) + getString(R.string.dynamic_comment));
    }

    public void addAdapter(DynamicCommentBean list){
        adapter.addList(list.getRows());
//        allComment.setText(getString(R.string.all_comment).replace("%1$s",StringUtils.numberCheck(list.getRecordsTotal()) + ""));
//        tvNumComment.setText(StringUtils.numberCheck(list.getRecordsTotal()) + getString(R.string.dynamic_comment));
        springView.onFinishFreshAndLoad();
    }

    @OnClick({R.id.ll_comment, R.id.ll_comment_})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_comment:
            case R.id.ll_comment_:
                initInputDialog();
                break;
        }
    }

    private void initInputDialog() {
        if (!GlobalParams.getInstance().isLogin()){
            showAskLoginDialog();
            return;
        }
        final AlertDialog dialog = new AlertDialog.Builder(getContext(), R.style.DialogTheme).create();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setWindowAnimations(android.R.style.Animation_InputMethod);
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.setContentView(R.layout.dialog_dynamic_comment);
        setFocus((EditText) dialog.findViewById(R.id.ed_input));

        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setComment(bean.getId(),((EditText) dialog.findViewById(R.id.ed_input)).getText().toString());
                dialog.dismiss();
            }
        });

    }

    public void onCommentSuccess(){
        presenter.getDetail(bean.getId());
        page = 0;
        presenter.getComment(bean.getId(),type,page);
    }

    public void setDetail(IndexMomentBean.RowsBean bean){
        this.bean = bean;

        initUser();
        llType1.setVisibility(View.GONE);
        llType2.setVisibility(View.GONE);
        llType3.setVisibility(View.GONE);
        rcvPic.setVisibility(View.GONE);
        if (bean.getSubDetail() != null) {

            initContent();
        }else {
            tvTagText.setVisibility(View.INVISIBLE);
            tvTag.setVisibility(View.INVISIBLE);
            initPic();
        }
        tvLocation.setText(bean.getUnitName());
        tvTime.setVisibility(View.VISIBLE);
        tvTime.setText(DateUtil.formatTime(bean.getCreateTime(),"yy-MM-dd HH:mm"));

        llTag.setVisibility(View.VISIBLE);

    }

    private void showDeleteDialog(final int position, final String id){
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_delete);
        ((TextView)dialog.findViewById(R.id.title)).setText(getString(R.string.txt_delete_comment));
        dialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deleteComment(position,id);
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

    public void onCommentDelete(final int position, final String id){
        if (adapter.getList().get(position).getId().equals(id)){
            adapter.getList().remove(position);
            adapter.notifyDataSetChanged();
            allComment.setText(getString(R.string.all_comment).replace("%1$s",StringUtils.numberCheck(adapter.getList().size()) + ""));
            tvNumComment.setText(StringUtils.numberCheck(adapter.getList().size()) + getString(R.string.dynamic_comment));
        }else {
            page = 0;
            presenter.getComment(bean.getId(),type,page);
            presenter.getDetail(bean.getId());
        }


    }
}
