package com.atlas.crmapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.common.commonadapter.BaseRvAdapter;
import com.atlas.crmapp.common.commonadapter.BaseRvViewHolder;
import com.atlas.crmapp.ecosphere.TopicDetailActivity;
import com.atlas.crmapp.model.VThreadsJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.hyphenate.util.DensityUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/21
 *         Description :
 */

public class TopicDetailRvAdapter extends BaseRvAdapter<BaseRvViewHolder> {

    private static final String[] bgColor = {"#4ac4cd","#feaa42","#e96b61",  "#acc76d","#84d0ab","#918fc3","#f2a5a8"};

    public static final int VT_HEADER = 0;
    public static final int VT_FIRST = 1;
    public static final int VT_NORMAL = 2;

    private Context context;
    private List<VThreadsJson> data;
    private Drawable drawableSelect;
    private Drawable drawableUnSelect;
    private Drawable drawableRSelect;
    private Drawable drawableRUnSelect;

    private boolean isShow;

    private static Map<String, Boolean> dataMap;

    public TopicDetailRvAdapter(Context context, List<VThreadsJson> data) {
        this.context = context;
        this.data = data;
        drawableSelect = context.getResources().getDrawable(R.drawable.ic_like_select);
        drawableUnSelect = context.getResources().getDrawable(R.drawable.ic_like_unselect);
        drawableRSelect = context.getResources().getDrawable(R.drawable.icr_like_select);
        drawableRUnSelect = context.getResources().getDrawable(R.drawable.icr_like_unselect);
        drawableSelect.setBounds(0, 0, drawableSelect.getMinimumWidth(), drawableSelect.getMinimumHeight());
        drawableUnSelect.setBounds(0, 0, drawableUnSelect.getMinimumWidth(), drawableUnSelect.getMinimumHeight());
        drawableRSelect.setBounds(0, 0, drawableRSelect.getMinimumWidth(), drawableRSelect.getMinimumHeight());
        drawableRUnSelect.setBounds(0, 0, drawableRUnSelect.getMinimumWidth(), drawableRUnSelect.getMinimumHeight());
        dataMap = new HashMap<>();
    }

    @Override
    protected int getLayoutId(int viewType) {
        switch (viewType) {
            case VT_HEADER:
                return R.layout.view_topic_detail_header;
            case VT_FIRST:
                return R.layout.item_topic_detail_first;
            default:
                return R.layout.item_topic_detail_comment;
        }
    }

    @Override
    protected BaseRvViewHolder getViewHolder(View root, int viewType) {
        switch (viewType) {
            case VT_HEADER:
                return new HeaderViewHolder(root);
            case VT_FIRST:
                return new FirstViewHolder(root);
            default:
                return new CommentViewHolder(root);
        }
    }

    @Override
    public void onBindViewHolder(BaseRvViewHolder holder, int position) {
        if (getItemViewType(position) == VT_HEADER) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) lp;
                sglp.setFullSpan(true);
                holder.itemView.setLayoutParams(sglp);
            }
            HeaderViewHolder headerVH = (HeaderViewHolder) holder;

            final VThreadsJson vThreadsJson = data.get(position);
            GlideUtils.loadCustomImageView(context, R.drawable.product, LoadImageUtils.loadMiddleImage(vThreadsJson.image),headerVH.mIvShape);
            headerVH.mTvTitle.setText(vThreadsJson.title);
            headerVH.mTvSubTitle.setText(vThreadsJson.content);
            headerVH.mTvNumComment.setText(String.valueOf(vThreadsJson.commentCnt));
            headerVH.mTvNumLike.setText(String.valueOf(vThreadsJson.likeCnt));
            if (vThreadsJson.like) {
                headerVH.mTvNumLike.setCompoundDrawables(drawableSelect, null, null, null);
            } else {
                headerVH.mTvNumLike.setCompoundDrawables(drawableUnSelect, null, null, null);
            }

            headerVH.mTvNumLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( ((BaseActivity)context).getGlobalParams().isLogin()) {
                        if (vThreadsJson.like) {
//                            BizDataRequest.requestCancelThreadLike(context, vThreadsJson.id, new BizDataRequest.OnRequestResult() {
//                                @Override
//                                public void onSuccess() {
//                                    vThreadsJson.likeCnt--;
//                                    vThreadsJson.like = false;
//                                    notifyDataSetChanged();
//                                }
//
//                                @Override
//                                public void onError(DcnException error) {
//                                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
//                                }
//                            });

                        } else {
                            BizDataRequest.requestSetThreadLike(context, vThreadsJson.id, new BizDataRequest.OnRequestResult() {
                                @Override
                                public void onSuccess() {
                                    vThreadsJson.likeCnt++;
                                    vThreadsJson.like = true;
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onError(DcnException error) {
                                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        ((BaseActivity)context).showAskLoginDialog();
                    }
                }
            });
        } else if (getItemViewType(position) == VT_NORMAL) {
//            StringBuilder sb = new StringBuilder();
//            sb.append("#");
//            for (int i = 0; i < 3; i++) {
//                sb.append(CommonUtils.getRandomHexString());
//            }
            int colorPosition =0;
            colorPosition = position%7;

            final CommentViewHolder commentVH = (CommentViewHolder) holder;
            commentVH.mCardview.setCardBackgroundColor(Color.parseColor(bgColor[colorPosition]));

            final VThreadsJson vThreadsJson = data.get(position - 1);
            Log.e("tw", "id::" + vThreadsJson.getId());
            String avatar = vThreadsJson.appUser.getAvatar();
            if (avatar != null && !avatar.equals("")) {
                GlideUtils.loadCustomImageView(context, R.drawable.ic_topic_user, LoadImageUtils.loadSmallImage(avatar), commentVH.mIvAvatar );
            }else
                commentVH.mIvAvatar.setImageResource(R.drawable.ic_topic_user);
            commentVH.mTvName.setText(vThreadsJson.appUser.getNick());
            if (null != vThreadsJson.content && vThreadsJson.content.length() > 10){
                commentVH.mTvComment.setTextSize(DensityUtil.sp2px(context, 5));
            }else {
                commentVH.mTvComment.setTextSize(DensityUtil.sp2px(context, 7));
            }


            commentVH.mTvComment.setText(unicode2String(vThreadsJson.content));
            commentVH.mTvNumLike.setText(String.valueOf(vThreadsJson.likeCnt));

            if (vThreadsJson.like) {
                commentVH.mTvNumLike.setCompoundDrawables(drawableRSelect, null, null, null);
            } else {
                commentVH.mTvNumLike.setCompoundDrawables(drawableRUnSelect, null, null, null);
            }

            commentVH.mTvNumLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( ((BaseActivity)context).getGlobalParams().isLogin()) {
                        if (vThreadsJson.like) {
//                            BizDataRequest.requestCancelThreadLike(context, vThreadsJson.id, new BizDataRequest.OnRequestResult() {
//                                @Override
//                                public void onSuccess() {
//                                    vThreadsJson.likeCnt--;
//                                    vThreadsJson.like = false;
//                                    notifyDataSetChanged();
//                                }
//
//                                @Override
//                                public void onError(DcnException error) {
//                                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
//                                }
//                            });
                        } else {
                            BizDataRequest.requestSetThreadLike(context, vThreadsJson.id, new BizDataRequest.OnRequestResult() {
                                @Override
                                public void onSuccess() {
                                    vThreadsJson.likeCnt++;
                                    vThreadsJson.like = true;
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onError(DcnException error) {
                                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    } else {
                        ((BaseActivity)context).showAskLoginDialog();
                    }
                }
            });

            if (null == dataMap.get(vThreadsJson.getId() + "")){
                commentVH.mTvComment.post(new Runnable() {
                    @Override
                    public void run() {
                        Layout l = commentVH.mTvComment.getLayout();
                        if (l != null) {
                            int lines = l.getLineCount();
                            if (lines > 0) {
                                if (l.getEllipsisCount(lines - 1) > 0) {
                                    commentVH.ivMore.setVisibility(View.VISIBLE);
                                    dataMap.put(vThreadsJson.getId() + "", true);
                                }else {
                                    commentVH.ivMore.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                });
            }else {
                commentVH.ivMore.setVisibility(View.VISIBLE);
            }

            if (null != dataMap.get(vThreadsJson.getId() + "_show") && dataMap.get(vThreadsJson.getId() + "_show")){
                commentVH.mTvComment.setMaxLines(30);
                commentVH.ivMore.setImageResource(R.drawable.ic_top);
            }else {
                commentVH.mTvComment.setMaxLines(2);
                commentVH.ivMore.setImageResource(R.drawable.ic_bottom);
            }

            commentVH.ivMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShow = !isShow;
                    if (!isShow){
                        commentVH.mTvComment.setMaxLines(2);
                        commentVH.ivMore.setImageResource(R.drawable.ic_bottom);
                        dataMap.put(vThreadsJson.getId() + "_show", false);
                    }else {
                        commentVH.mTvComment.setMaxLines(30);
                        commentVH.ivMore.setImageResource(R.drawable.ic_top);
                        dataMap.put(vThreadsJson.getId() + "_show", true);
                    }
                }
            });
        }else if (getItemViewType(position) == VT_FIRST) {
            FirstViewHolder firstVH = (FirstViewHolder) holder;
            firstVH.mRlFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TopicDetailActivity topic = (TopicDetailActivity) context;
                    topic.setSendCommentVisible();
                    topic.setFocus();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        switch (position) {
            case 0:
                return VT_HEADER;
            case 1:
                return VT_FIRST;
            default:
                return VT_NORMAL;
        }
    }

    class CommentViewHolder extends BaseRvViewHolder implements  Serializable{


        @BindView(R.id.cardview)
        CardView mCardview;
        @BindView(R.id.esv_avatar)
        tangxiaolv.com.library.EffectiveShapeView mIvAvatar;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_comment)
        TextView mTvComment;
        @BindView(R.id.tv_num_like)
        TextView mTvNumLike;
        @BindView(R.id.iv_more)
        ImageView ivMore;

        public CommentViewHolder(View itemView) {
            super(itemView);
        }
    }

    class HeaderViewHolder extends BaseRvViewHolder {

        @BindView(R.id.shape_iv)
        tangxiaolv.com.library.EffectiveShapeView mIvShape;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_sub_title)
        TextView mTvSubTitle;
        @BindView(R.id.tv_num_comment)
        TextView mTvNumComment;
        @BindView(R.id.tv_num_like)
        TextView mTvNumLike;

        public HeaderViewHolder(View itemView) {  super(itemView); }
    }

    class FirstViewHolder extends BaseRvViewHolder {

        @BindView(R.id.rl_first)
        RelativeLayout mRlFirst;

        public FirstViewHolder(View itemView) {  super(itemView); }
    }


    public static String unicode2String(String s) {
        List<String> list =new ArrayList<String>();
        String zz="\\\\u[0-9,a-z,A-Z]{4}";

        //正则表达式用法参考API
        Pattern pattern = Pattern.compile(zz);
        Matcher m = pattern.matcher(s);
        while(m.find()){
            list.add(m.group());
        }
        for(int i=0,j=2;i<list.size();i++){
            String st = list.get(i).substring(j, j+4);

            //将得到的数值按照16进制解析为十进制整数，再強转为字符
            char ch = (char) Integer.parseInt(st, 16);
            //用得到的字符替换编码表达式
            s = s.replace(list.get(i), String.valueOf(ch));
        }
        return s;
    }


}
