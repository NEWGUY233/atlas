package com.atlas.crmapp.selector.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.selector.adapter.SelectorImageAdapter;
import com.atlas.crmapp.selector.entry.Folder;
import com.atlas.crmapp.selector.entry.Image;
import com.atlas.crmapp.selector.model.ImageModel;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/3.
 */

public class ImageSelectorActivity extends BaseStatusActivity {
    Folder folder;
    ArrayList<Image> images;
    SelectorImageAdapter imageAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    ArrayList<Folder> mFolders;
    private static final int PERMISSION_REQUEST_CODE = 0X00000011;
    int totalSize = 9;

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
        setContentView(R.layout.selector_activity_image);
        ButterKnife.bind(this);

        initView();
        checkPermissionAndLoadImages();
        ;
//        initData();
    }

    private void initView() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        findView(R.id.ll_title).setPadding(0, getStatusBarHeight(), 0, 0);
//        setFinishedView(R.id.back);
//        setFinishedView(R.id.cancel);
        GridLayoutManager gm = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(gm);
        recyclerView.setNestedScrollingEnabled(false);
        totalSize = getIntent().getIntExtra("size",9);

        imageAdapter = new SelectorImageAdapter(getContext(),totalSize);

        tvNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0x111, new Intent().putExtra("images", imageAdapter.getList()));
                finish();
            }
        });

        View v = new View(getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (getResources().getDisplayMetrics().density * 48 + getStatusBarHeight()));
        v.setLayoutParams(lp);
        imageAdapter.addHeaderView(v);



    }

    private void initData(Intent data) {
        Object obj = data.getParcelableExtra("folder");
        if (obj == null || !(obj instanceof Folder)) {
            checkPermissionAndLoadImages();
            return;
        }
        folder = (Folder) obj;
        ArrayList<Image> imgs = new ArrayList<>();
        for (Image img : folder.getImages()){
            if (new File(img.getPath()).exists())
                imgs.add(img);
        }
        folder.setImages(imgs);

        imageAdapter.setNewData(folder.getImages());
        recyclerView.setAdapter(imageAdapter);
        recyclerView.scrollToPosition(folder.getImages().size() - 1);


        images = data.getParcelableArrayListExtra("images");
        tvNumber.setText("完成(" + images.size() + ")");
        imageAdapter.setList(images);
        imageAdapter.setClick(new SelectorImageAdapter.OnClick() {
            @Override
            public void onClick() {
                tvNumber.setText("完成(" + images.size() + ")");
            }
        });

    }

    private void initFirstTime() {

        ArrayList<Image> imgs = new ArrayList<>();
        for (Image img : mFolders.get(0).getImages()){
            if (new File(img.getPath()).exists())
                imgs.add(img);
        }
        mFolders.get(0).setImages(imgs);

        imageAdapter.setNewData(mFolders.get(0).getImages());
        recyclerView.setAdapter(imageAdapter);
        recyclerView.scrollToPosition(mFolders.get(0).getImages().size() - 1);

        images = new ArrayList<>();
        tvNumber.setText("完成(" + images.size() + ")");
        imageAdapter.setList(images);
        imageAdapter.setClick(new SelectorImageAdapter.OnClick() {
            @Override
            public void onClick() {
                tvNumber.setText("完成(" + images.size() + ")");
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

    /**
     * 检查权限并加载SD卡里的图片。
     */
    private void checkPermissionAndLoadImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            Toast.makeText(this, "没有图片", Toast.LENGTH_LONG).show();
            return;
        }
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
            //有权限，加载图片。
            loadImageForSDCard();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 从SDCard加载图片。
     */
    private void loadImageForSDCard() {
        ImageModel.loadImageForSDCard(this, new ImageModel.DataCallback() {
            @Override
            public void onSuccess(ArrayList<Folder> folders) {
                mFolders = folders;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mFolders != null && !mFolders.isEmpty()) {
//                            adapter.setFolders(mFolders);
                            initFirstTime();
                        }
                    }
                });
            }
        });
    }

    /**
     * 处理权限申请的回调。
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //允许权限，加载图片。
                loadImageForSDCard();
            } else {
                //拒绝权限，弹出提示框。

//                showExceptionDialog();
                finish();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x111 && resultCode == 0x111 && data != null) {
            initData(data);
        }
    }

    @OnClick({R.id.back, R.id.cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                startActivityForResult(new Intent(this,FoldersActivity.class).putExtra("images",images),0x111);
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }
}
