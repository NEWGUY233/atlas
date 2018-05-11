package com.atlas.crmapp.common.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.common.statusLayout.OnRetryListener;
import com.atlas.crmapp.common.statusLayout.StatusLayoutImpl;
import com.atlas.crmapp.common.statusLayout.StatusLayoutManager;
import com.atlas.crmapp.util.StringUtils;

/**
 * Created by hoda on 2017/6/27.
 */

public abstract class BaseStatusActivity extends BaseActivity{

    public StatusLayoutManager statusLayoutManager;
    private StatusLayoutManager.Builder statusBuilder;
    protected Toolbar toolbar;
    protected TextView tvTitle;

    OnClickListener onClickListenerTopLeft;
    OnClickListener onClickListenerTopRight;

    private int menuResId;
    private String menuStr;
    private Menu menu;

    public StatusLayoutImpl statusLayout;

    public static final int NO_SHOW_TOP_BAR = -1;//setTopBar 的返回值为 -1 时，不显示 头布局

    public interface OnClickListener{
        void onClick();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        initStatusLayout(layoutResID);
        if(setTopBar() == 0){
            initToolbar();
        }
    }

    @Override
    protected void initActivityViews() {
        super.initActivityViews();

    }

    protected boolean isShowToolbarLeft(){
        return true;
    }

    protected int setTopBar(){
        return 0;
    }

    public void initStatusLayout(int layoutResID) {
        statusLayout = new StatusLayoutImpl(this, layoutResID, setTopBar(), onRetryListener, translucentStatusBar());
        setContentView(statusLayout.getStatusContenView());
    }

    OnRetryListener onRetryListener = new OnRetryListener() {
        @Override
        public void onRetry(View view, int id) {
            BaseStatusActivity.this.onRefresh(view, id);
        }
    };

    //--------------------------------------------------------
    protected void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            if(isShowToolbarLeft()){
                setTopLeftButton(R.drawable.white_back, new OnClickListener() {
                    @Override
                    public void onClick() {
                        finish();
                    }
                });
            }
        }
    }

    protected void setTitle(String title){
        if (!TextUtils.isEmpty(title) && tvTitle!= null){
            tvTitle.setText(title);
        }
    }

    protected void setTopLeftButton(){
        setTopLeftButton(R.drawable.white_back, null);
    }

    protected void setTopLeftButton(int iconResId, OnClickListener onClickListener){
        if(toolbar != null){
            toolbar.setNavigationIcon(iconResId);
            this.onClickListenerTopLeft = onClickListener;
        }
    }

    protected void setTopRightButton(String menuStr, OnClickListener onClickListener){
        this.onClickListenerTopRight = onClickListener;
        this.menuStr = menuStr;
    }

    protected void setTopRightButton(String menuStr, int menuResId, OnClickListener onClickListener){
        this.menuResId = menuResId;
        this.menuStr = menuStr;
        this.onClickListenerTopRight = onClickListener;
    }

    /**
     * 设置左右图标
     * @param iconLeftResId
     * @param menuResId
     */
    protected void setTopLeftRightIcon(int iconLeftResId, int menuResId){
        if( toolbar!= null){
            if(iconLeftResId != 0){
                toolbar.setNavigationIcon(iconLeftResId);
            }

            if(menuResId!= 0){
                if(menu != null){
                    menu.findItem(R.id.menu_right).setIcon(menuResId);
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if (menuResId != 0 || !TextUtils.isEmpty(menuStr)){
            getMenuInflater().inflate(R.menu.base_toolbar_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        if (menuResId != 0) {
            menu.findItem(R.id.menu_right).setIcon(menuResId);
        }
        if (!TextUtils.isEmpty(menuStr)){
            menu.findItem(R.id.menu_right).setTitle(menuStr);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && onClickListenerTopLeft != null){
            onClickListenerTopLeft.onClick();
        }
        else if (item.getItemId() == R.id.menu_right && onClickListenerTopRight != null){
            onClickListenerTopRight.onClick();
        }
        return true; // true 告诉系统我们自己处理了点击事件
    }


    //刷新调用方法
    protected void onRefresh(View view ,int id){

    }

    public void showToast(String content){
        if (!StringUtils.isEmail(content)){
            Toast.makeText(getApplicationContext(),content,Toast.LENGTH_LONG).show();
        }
    }

    public void startActivity(Class cl){
        startActivity(new Intent(this,cl));
    }

    public Context getContext(){
        return this;
    }

    public void setVerticalManager(RecyclerView recyclerView){
        LinearLayoutManager ll = new LinearLayoutManager(getContext());
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(ll);
    }

    public void setFinishedView(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setFinishedView(int id){
        findView(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public View getRightBtn(){
        return findView(R.id.menu_right);
    }

}
