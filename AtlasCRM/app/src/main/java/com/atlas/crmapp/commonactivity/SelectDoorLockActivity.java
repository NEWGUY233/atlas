package com.atlas.crmapp.commonactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.ViewPagerAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.common.fragment.SelectDoorLockFragment;
import com.atlas.crmapp.model.CollectLockItem;
import com.atlas.crmapp.model.LockJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hoda on 2017/11/14.
 */

public class SelectDoorLockActivity extends BaseStatusActivity {
    @BindView(R.id.vp_door_lock)
    ViewPager vpDoorLock;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.tvText)
    TextView tvRight;
    @BindView(R.id.textViewTitle)
    TextView tvTitle;

    private Map<Long ,Object > selectLockMap = new HashMap<>();
    private String lockJsons ;
    private List<LockJson> currentLockJsons ;
    private ArrayList<String> tabList = new ArrayList<>();
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private static final String KEY_LOCKS = "KEY_LOCKS";
    private static final String KEY_CUR_COLLECT_LOCKS = "KEY_CUR_COLLECT_LOCKS";
    private ViewPagerAdapter viewPagerAdapter;
    public static final int REQUEST_CODE_TO_SELECT = 110;



    @OnClick(R.id.ibBack)
    void onClickBack(){
        finish();
    }

    @OnClick(R.id.tv_enter)
    void onClickToEnterCollect (){
        List<CollectLockItem> collectLockItems = new ArrayList<>();
        if(selectLockMap.size() == 0){
            updateSelectNum();
        }
       for(long key : selectLockMap.keySet()){
           LockJson lockJson = (LockJson) selectLockMap.get(key);
           CollectLockItem collectLockItem = new CollectLockItem();
           collectLockItem.setDoorId(lockJson.getDoorId());
           collectLockItem.setDoorType(lockJson.getDoorType());
           collectLockItems.add(collectLockItem);
       }

        BizDataRequest.requestCollectDoor(this, collectLockItems, statusLayout, new BizDataRequest.OnResponseCollectDoor() {
            @Override
            public void onSuccess() {
                setResult(112);
                SelectDoorLockActivity.this.finish();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_door_lock);
        tvTitle.setText(getString(R.string.door_lock_select));
        Intent intent = getIntent();
        if(intent!= null){
            lockJsons = intent.getStringExtra(KEY_LOCKS);
            currentLockJsons = (List<LockJson>) intent.getSerializableExtra(KEY_CUR_COLLECT_LOCKS);

        }
        tabList.add(getString(R.string.text_97));
        tabList.add(getString(R.string.text_98));
        tabList.add(getString(R.string.text_99));
        fragmentList.add(SelectDoorLockFragment.newInstance(this, 0 ));
        fragmentList.add(SelectDoorLockFragment.newInstance(this, 1 ));
        fragmentList.add(SelectDoorLockFragment.newInstance(this, 2 ));
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList, tabList);
        vpDoorLock.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentList, tabList));
        tabLayout.setupWithViewPager(vpDoorLock);
        vpDoorLock.setOffscreenPageLimit(2);
        tvRight.setTextColor(ContextCompat.getColor(this, R.color.gray_simple));
        tvRight.setTextSize(12);
        tvRight.setVisibility(View.VISIBLE);
        if(lockJsons != null){
            updateTopSelect(currentLockJsons.size());
        }else{
            updateTopSelect(0);
        }

    }

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    public static void newInstance(Activity context, String lockJsons, List<LockJson>  collectLockJsons ){
        Intent intent = new Intent(context, SelectDoorLockActivity.class);
        intent.putExtra(KEY_LOCKS, lockJsons);
        intent.putExtra(KEY_CUR_COLLECT_LOCKS, (Serializable) collectLockJsons);
        context.startActivityForResult(intent, REQUEST_CODE_TO_SELECT);
    }



    private void updateTopSelect(int num){
        String  textStr = "";
        if (num > 0){
            textStr = getString(R.string.text_100) + num;
        }
        tvRight.setText(textStr);
    }

    public void updateSelectNum(){
        selectLockMap.clear();
        SelectDoorLockFragment currentFragment = (SelectDoorLockFragment) viewPagerAdapter.getItem(vpDoorLock.getCurrentItem());
        List<LockJson> currentFragmentLockJsons = currentFragment.getLockJsons();

        for (Fragment fragment : fragmentList){
            SelectDoorLockFragment lockFragment = (SelectDoorLockFragment) fragment;
            List<LockJson> fragmentLockJsons = lockFragment.getLockJsons();
            if(fragmentLockJsons != null && fragmentLockJsons.size() > 0){
                for(LockJson lockJson :fragmentLockJsons){
                    if(fragmentLockJsons != currentFragment){
                        for(LockJson currentFragmentLockJson : currentFragmentLockJsons){
                            if(lockJson.getDoorId() == currentFragmentLockJson.getDoorId() && lockJson.getDoorType() .equals(currentFragmentLockJson.getDoorType()) ){
                                lockJson.setCollected(currentFragmentLockJson.isCollected());
                            }
                        }
                    }

                    if(lockJson.isCollected()){
                        selectLockMap.put(lockJson.getDoorId(), lockJson);
                    }
                }
            }
            lockFragment.notifyRvData();
        }
        updateTopSelect(selectLockMap.size());
    }





}
