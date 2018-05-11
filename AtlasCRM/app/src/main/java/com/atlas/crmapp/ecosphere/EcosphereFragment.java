package com.atlas.crmapp.ecosphere;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.EcosphereTabAdapter;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.huanxin.Constant;
import com.atlas.crmapp.huanxin.db.InviteMessgeDao;
import com.atlas.crmapp.usercenter.UserCardActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.lang.reflect.Field;

import butterknife.BindView;


public class EcosphereFragment extends BaseFragment {
    public static final String TAG = EcosphereFragment.class.getSimpleName();
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    @BindView(R.id.tv_tab)
    TextView unreadLabel;

    public static EcosphereFragment newInstance() {
        EcosphereFragment fragment = new EcosphereFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ecosystem;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        EcosphereTabAdapter tabAdapter = new EcosphereTabAdapter(getChildFragmentManager());
        mViewpager.setOffscreenPageLimit(3);
        mViewpager.setAdapter(tabAdapter);
        mTabLayout.setupWithViewPager(mViewpager);
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(mTabLayout, 40, 40);
            }
        });

        registerBroadcastReceiver();
        updateUnreadLabel();
    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }


    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                //Toast.makeText(getHoldingActivity(), "取消", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(getHoldingActivity(), "已搜索: "+result.getContents(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getHoldingActivity(), UserCardActivity.class);
                intent.putExtra("code",result.getContents());
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(getHoldingActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.NEW_MESSAGE);
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();

            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregisterBroadcastReceiver(){
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    /**
     * update unread message count
     */
    InviteMessgeDao inviteMessgeDao;
    public void updateUnreadLabel() {
        if(inviteMessgeDao == null){
            inviteMessgeDao = new InviteMessgeDao(getActivity());
        }
        int count = getUnreadMsgCountTotal();
        if (count > 0 || inviteMessgeDao.getUnreadMessagesCount()>0) {
            unreadLabel.setText("");
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMessageCount();
        for(EMConversation conversation:EMClient.getInstance().chatManager().getAllConversations().values()){
            if(conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount=chatroomUnreadMsgCount+conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal-chatroomUnreadMsgCount;
    }
}