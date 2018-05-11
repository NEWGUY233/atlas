package com.atlas.crmapp.ecosphere;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.TopicDetailRvAdapter;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.VThreadsJson;
import com.atlas.crmapp.model.VisibleThreadsJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.TopBarScrollTransUtils;
import com.atlas.crmapp.view.ObserverRecyclerView;
import com.jaeger.library.StatusBarUtil;
import com.ta.utdid2.android.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TopicDetailActivity extends BaseStatusActivity {


    @BindView(R.id.rv_comments)
    ObserverRecyclerView mRvComments;
    @BindView(R.id.et_comment_content)
    EditText mEtCommentContent;
    @BindView(R.id.btn_send)
    Button mBtnSend;

    @BindView(R.id.ll_send_comment)
    View vSendComment;


    @BindView(R.id.ibBack)
    ImageButton ibBack;

    @BindView(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @BindView(R.id.textViewTitle)
    TextView tvTitle;

    private long id;
    private long lastRecId = 0;
    private List<VThreadsJson> alVVthre = new ArrayList<VThreadsJson>();
    private TopicDetailRvAdapter adapter;

    @OnClick(R.id.ibBack)
    void onClickBack(){
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        initView();
        mRvComments.setOnTopBarShowListener(onTopBarShowListener);
        StatusBarUtil.setTransparentForImageView(this, null);
    }

    public  void setSendCommentVisible(){
        vSendComment.setVisibility(View.VISIBLE);
    }

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }


    private ObserverRecyclerView.OnTopBarShowListener onTopBarShowListener = new ObserverRecyclerView.OnTopBarShowListener() {
        @Override
        public void onTopBarShow(boolean isShow, float alpha) {
            if(rlTopBar == null){
                return;
            }

            TopBarScrollTransUtils.setTitleBarBg(TopicDetailActivity.this, rlTopBar, isShow);
            if(isShow){
                tvTitle.setText(R.string.index_tag_detail);
                StatusBarUtil.setTranslucentForImageViewInFragment(TopicDetailActivity.this, Constants.STATUS_BAR_ALPHA.BAR_ALPHA , null);
                TopBarScrollTransUtils.setImage(ibBack, R.drawable.white_back);
            }else{
                StatusBarUtil.setTransparentForImageView(TopicDetailActivity.this, null);
                tvTitle.setText("");
                TopBarScrollTransUtils.setImage(ibBack, R.drawable.white_back_transparent);
            }
        }
    };

    private void initView() {
        id = getIntent().getLongExtra("id", 0);
        vSendComment.setVisibility(View.GONE);
        tvTitle.setText("");
        mRvComments.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(vSendComment.getVisibility() == View.VISIBLE){
                    vSendComment.setVisibility(View.GONE);
                    hideKeyboard(mEtCommentContent);
                }
            }
        });
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusLayout.setShowStatusLayout(false);
                if(getGlobalParams().isLogin()) {
                    if (StringUtils.isEmpty(mEtCommentContent.getText().toString())) {
                        Toast.makeText(TopicDetailActivity.this, R.string.t18, Toast.LENGTH_LONG).show();
                        return;
                    }

                    String str = mEtCommentContent.getText().toString();
                    String encodeStr = "";
                    for (int i=0; i<str.length(); i++) {
                        if (isEmojiCharacter(str.charAt(i))) {
                            encodeStr = encodeStr + string2unicode(String.valueOf(str.charAt(i)));
                        } else {
                            encodeStr = encodeStr + str.charAt(i);
                        }
                    }

                    BizDataRequest.requestReplyThread(TopicDetailActivity.this, id, encodeStr.trim(), new BizDataRequest.OnRequestResult() {
                        @Override
                        public void onSuccess() {
                            mEtCommentContent.getText().clear();
                            prepareActivityData();
                        }

                        @Override
                        public void onError(DcnException error) {
                            sendErrorMsg(error);
                        }
                    });
                } else {
                    showAskLoginDialog();
                }
            }
        });
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
        vSendComment.setVisibility(View.GONE);
        statusLayout.showLoading();
        if (getGlobalParams().isLogin()) {
            BizDataRequest.requestGetThread(TopicDetailActivity.this, id, null, new BizDataRequest.OnVThreads() {
                @Override
                public void onSuccess(VThreadsJson vThreadsJson) {
                    alVVthre.clear();
                    alVVthre.add(vThreadsJson);
                    lastRecId = 0;
                    mHandler.sendEmptyMessage(10);
                    loadReplyData(lastRecId);
                }

                @Override
                public void onError(DcnException error) {
                    sendErrorMsg(error);
                }
            });
        } else {
            BizDataRequest.requestGetThreadForVisitor(TopicDetailActivity.this, id, null, new BizDataRequest.OnVThreads() {
                @Override
                public void onSuccess(VThreadsJson vThreadsJson) {
                    alVVthre.clear();
                    alVVthre.add(vThreadsJson);
                    lastRecId = 0;
                    mHandler.sendEmptyMessage(10);
                    loadReplyData(lastRecId);
                }

                @Override
                public void onError(DcnException error) {
                    sendErrorMsg(error);
                }
            });
        }
    }

    private void loadReplyData(long maxId) {
        if (getGlobalParams().isLogin()) {
            BizDataRequest.requestGetReplyThreads(TopicDetailActivity.this, id, maxId, new BizDataRequest.OnVisibleThreads() {
                @Override
                public void onSuccess(VisibleThreadsJson visibleThreadsJson) {
                    statusLayout.showContent();
                    List<VThreadsJson> vthre = visibleThreadsJson.rows;
                    if(vthre != null && vthre.size() > 0) {
                        lastRecId = vthre.get(vthre.size() - 1).id;
                    }
                    alVVthre.addAll(vthre);
                    mHandler.sendEmptyMessage(10);
                }

                @Override
                public void onError(DcnException error) {
                    sendErrorMsg(error);
                }
            });
        } else {
//            BizDataRequest.requestGetReplyThreadsForVisitor(TopicDetailActivity.this, id, maxId, new BizDataRequest.OnVisibleThreads() {
//                @Override
//                public void onSuccess(VisibleThreadsJson visibleThreadsJson) {
//                    statusLayout.showContent();
//                    List<VThreadsJson> vthre = visibleThreadsJson.rows;
//                    if(vthre != null && vthre.size() > 0) {
//                        lastRecId = vthre.get(vthre.size() - 1).id;
//                    }
//                    alVVthre.addAll(vthre);
//
//                    mHandler.sendEmptyMessage(10);
//                }
//
//                @Override
//                public void onError(DcnException error) {
//                    sendErrorMsg(error);
//                }
//            });
        }
        statusLayout.setShowStatusLayout(true);
    }

    private void initAdapter() {
        mRvComments.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new TopicDetailRvAdapter(TopicDetailActivity.this, alVVthre);
        mRvComments.setAdapter(adapter);
    }

    private void sendErrorMsg(DcnException error) {
        statusLayout.showError(error);
    }

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    } else {
                        initAdapter();
                    }
                    break;
            }
        }
    };

    public void setFocus(){
        mEtCommentContent.requestFocus();
        InputMethodManager imm = (InputMethodManager) mEtCommentContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public void hideKeyboard(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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
}