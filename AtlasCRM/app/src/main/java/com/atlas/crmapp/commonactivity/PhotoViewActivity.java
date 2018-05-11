package com.atlas.crmapp.commonactivity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.adapter.MyImageAdapter;
import com.atlas.crmapp.model.DetailMediaJson;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.widget.PhotoViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PhotoViewActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = PhotoViewActivity.class.getSimpleName();
    private PhotoViewPager mViewPager;
    private int currentPosition;
    private MyImageAdapter adapter;
    private TextView mTvImageCount;
    //private TextView mTvSaveImage;
    private List<String> Urls;
    ArrayList<DetailMediaJson> imageList;

    @BindView(R.id.textViewTitle)
    TextView tvTitle;

    @OnClick(R.id.ibBack)
    void onClickBack(){
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        tvTitle.setText(R.string.text_96);

        initView();
        initData();
    }

    private void initView() {
        mViewPager = (PhotoViewPager) findViewById(R.id.view_pager_photo);
        mTvImageCount = (TextView) findViewById(R.id.tv_image_count);
        //mTvSaveImage = (TextView) findViewById(R.id.tv_save_image_photo);
        //mTvSaveImage.setOnClickListener(this);

    }

    private void initData() {

        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("currentPosition", 0);
//        HomeQuestionListModel.DataBeanX DataBean = ((HomeQuestionListModel.DataBeanX) intent.getSerializableExtra("questionlistdataBean"));
//        Urls = DataBean.getAttach().getImage().getOri();

        imageList =  (ArrayList<DetailMediaJson>) getIntent().getSerializableExtra("imagesList");
        Urls = new ArrayList<String>();
        for(int i=0;i<imageList.size();i++){
            Urls.add(imageList.get(i).url);
        }

        adapter = new MyImageAdapter(Urls, this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentPosition, false);
        mTvImageCount.setText(currentPosition+1 + "/" + Urls.size());
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                mTvImageCount.setText(currentPosition + 1 + "/" + Urls.size());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
}