package com.atlas.crmapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.db.model.PushMsg;
import com.atlas.crmapp.usercenter.MyCouponActivity;
import com.atlas.crmapp.usercenter.MyScoreActivity;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.CheckOverSizeTextView;
import com.atlas.crmapp.widget.FindDialog;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/20
 *         Description :
 */

public class FindDialogRvAdapter extends BaseMultiItemQuickAdapter<PushMsg, BaseViewHolder> {


    private List<PushMsg> data;
    private Context context;
    private ImageView ivRight;
    private int couponRed , fitnessGreen, newMsgYellow;

    public FindDialogRvAdapter(Context context, List<PushMsg> data, FindDialog findDialog) {
        super( data);
        addItemType(FormatCouponInfo.getCouponItemType(Constants.PushMsgTpye.COUPON_BIND_MSG), R.layout.item_find_coupon);
        addItemType(FormatCouponInfo.getCouponItemType(Constants.PushMsgTpye.REAL_MSG), R.layout.item_find_real_msg);
        this.context = context ;
        this.data = data;
        couponRed = ContextCompat.getColor(context, R.color.red);
        fitnessGreen = ContextCompat.getColor(context, R.color.green);
        newMsgYellow = ContextCompat.getColor(context, R.color.btn_yellow);


    }

    @Override
    protected void convert(BaseViewHolder helper, final PushMsg item) {
        Log.i("OnClickListener","type = " + item.type );
        Log.i("OnClickListener","convert = " + item.actionType );
        Log.i("OnClickListener","content = " + item.content );
        Log.i("OnClickListener","getAction = " + item.getAction() );
        Log.i("OnClickListener","getTitle = " + item.getTitle() );
        if (helper.getItemViewType() == FormatCouponInfo.getCouponItemType(Constants.PushMsgTpye.REAL_MSG)){

            final ImageView ivFindLeftIcon = helper.getView(R.id.iv_real_msg);
            final CheckOverSizeTextView tvMsg = helper.getView(R.id.tv_msg_message);

            final TextView tvShowHide = helper.getView(R.id.tv_show_hide_text);

            CardView cvFindMsg = helper.getView(R.id.cv_find_msg_bg);

            tvShowHide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tvMsgMaxLine = tvMsg.getMaxLines();
                    if( tvMsgMaxLine == 1){
                        tvMsg.setMaxLines(50);
                        tvShowHide.setText(R.string.text_77);
                    }else if( tvMsgMaxLine == 50){
                        tvMsg.setMaxLines(1);
                        tvShowHide.setText(R.string.show_all_text);
                    }
                }
            });


            tvMsg.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if (tvMsg.isOverFlowed() && charSequence.length() > 10) {//超出一行
                        tvShowHide.setVisibility(View.VISIBLE);
                    }else{
                        tvShowHide.setVisibility(View.GONE);
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });



            if(StringUtils.isNotEmpty(item.action)){
                ivFindLeftIcon.setImageResource(R.drawable.ic_find_msg);
                cvFindMsg.setCardBackgroundColor(newMsgYellow);
            }else if(Constants.PushMsgTpye.COUPON_BIND_MSG.equals(item.getActionType())){
                ivFindLeftIcon.setImageResource(R.drawable.ic_find_coupon);
                cvFindMsg.setCardBackgroundColor(couponRed);
            }else if(Constants.PushMsgTpye.ENTRY.equals(item.getActionType())){
                ivFindLeftIcon.setImageResource(R.drawable.ic_find_fitness);
                cvFindMsg.setCardBackgroundColor(fitnessGreen);
            }else{
                ivFindLeftIcon.setImageResource(R.drawable.ic_find_msg);
                cvFindMsg.setCardBackgroundColor(newMsgYellow);
            }

            String content = item.content;
            if(StringUtils.isEmpty(content)){
                content = item.title;
            }
            if(StringUtils.isEmpty(content)){
                tvMsg.setHeight(1);
            }
            helper.setText(R.id.tv_msg_title, item.title)
                    .setText(R.id.tv_msg_message, content)//content
                    .setText(R.id.tv_time, DateUtil.formatTime(item.date, "yyyy.MM.dd HH:mm"));
        }else{
            ivRight = helper.getView(R.id.cv_right_img);
            //GlideUtils.loadCustomImageView(context, R.drawable.ic_meeting_pruduct, LoadImageUtils.loadSmallImage(item.thumbnail), (ImageView) helper.getView(R.id.iv_coupon_icon));
            helper.setText(R.id.tv_coupon_name, item.title)
                    .setText(R.id.tv_coupon_price, item.price)
                    .setText(R.id.tv_coupon_remark, item.remark)
                    .setText(R.id.tv_expired_date, item.content);
            if(item.have){
                ivRight.setImageResource(R.drawable.ic_stamp_received);
                ivRight.setBackgroundColor(Color.parseColor("#e6e6e6"));
                ivRight.setEnabled(false);
            } else {
                ivRight.setImageResource(R.drawable.iv_coupon_nottake);
                ivRight.setBackgroundColor(Color.parseColor("#FED731"));
                ivRight.setEnabled(true);
            }
            helper.addOnClickListener(R.id.cv_right_img);

        }


        helper.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("OnClickListener","action = " + item.action );
                        if(StringUtils.isNotEmpty(item.action)){
                            Intent intent = ActionUriUtils.getIntent(context, item.action, item.thumbnail, item.title);
                            if(intent != null){
                                context.startActivity(intent);
                            }
                        }
                        if(Constants.PushMsgTpye.COUPON_BIND_MSG.equals(item.getActionType())){
                            context.startActivity(new Intent(context, MyCouponActivity.class));
                        }

                        if ("bonuspoints".equals(item.getActionType())){
                            context.startActivity(new Intent(context, MyScoreActivity.class));
                        }else if ("job".equals(item.getActionType())){

                        }else{

                        }
                    }
                }
        );


    }


 /*   private int getAvailableWidth()
    {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }
    private boolean isOverFlowed()
    {
        Paint paint = getPaint();
        float width = paint.measureText(getText().toString());
        if (width > getAvailableWidth()) return true;
        return false;
    }*/
}
