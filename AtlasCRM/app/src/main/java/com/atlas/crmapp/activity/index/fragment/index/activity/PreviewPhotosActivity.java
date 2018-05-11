package com.atlas.crmapp.activity.index.fragment.index.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.adapter.PreviewPhotosAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.widget.PhotoViewPager;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.DrawableBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/8.
 */

public class PreviewPhotosActivity extends BaseStatusActivity {

    @BindView(R.id.title_bar)
    RelativeLayout titleBar;
    @BindView(R.id.view_pager_photo)
    PhotoViewPager viewPagerPhoto;
    @BindView(R.id.v_indicator)
    FixedIndicatorView vIndicator;

    ArrayList<String> images;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.delete)
    ImageView delete;

    IndicatorViewPager.IndicatorPagerAdapter adapter;
    IndicatorViewPager idcMonthly;

    int position = 0;
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
        setContentView(R.layout.dialog_preview_photoes);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initView() {
        titleBar.setPadding(0, getStatusBarHeight(), 0, 0);
        vIndicator.setScrollBar(new DrawableBar(getContext(), R.drawable.selector_indicator_yellow));

        position = getIntent().getIntExtra("position", 0);

        String type = getIntent().getStringExtra("type");
        if (!StringUtils.isEmpty(type)){
            delete.setVisibility(View.GONE);
        }

    }

    private void initData() {
        images = getIntent().getStringArrayListExtra("images");

        idcMonthly = new IndicatorViewPager(vIndicator, viewPagerPhoto);
        adapter = new PreviewPhotosAdapter(images, this);
        idcMonthly.setAdapter(adapter);

        viewPagerPhoto.setCurrentItem(position);
        title.setText((position + 1) + "/" + images.size());

        viewPagerPhoto.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                title.setText((position + 1) + "/" + images.size());
            }
        });
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @OnClick({R.id.back, R.id.delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                setResult(0x112,new Intent().putStringArrayListExtra("images", images));
                finish();
                break;
            case R.id.delete:
                position = viewPagerPhoto.getCurrentItem();
                images.remove(position);
                setResult(0x112,new Intent().putStringArrayListExtra("images", images));
                if (images.size() == 0) {
                    finish();
                    return;
                }
//                adapter.notifyDataSetChanged();
                adapter = new PreviewPhotosAdapter(images, this);
                idcMonthly.setAdapter(adapter);
                if (position >= images.size())
                    position = images.size() - 1;

                viewPagerPhoto.setCurrentItem(position);
                title.setText((position + 1) + "/" + images.size());
                break;
        }
    }
}
