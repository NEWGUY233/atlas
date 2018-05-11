package com.atlas.crmapp.selector.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.selector.adapter.FoldersAdapter;
import com.atlas.crmapp.selector.entry.Folder;
import com.atlas.crmapp.selector.entry.Image;
import com.atlas.crmapp.selector.model.ImageModel;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/3.
 */

public class FoldersActivity extends BaseStatusActivity {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    FoldersAdapter adapter;
    ArrayList<Folder> mFolders;
    ArrayList<Image> images;
    private static final int PERMISSION_REQUEST_CODE = 0X00000011;

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selector_activity_folder);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.black_text));
        initViews();
    }

    private void initViews(){
        ButterKnife.bind(this);
        setFinishedView(R.id.cancel);
        setVerticalManager(recyclerView);

        checkPermissionAndLoadImages();

        adapter = new FoldersAdapter(this);
        recyclerView.setAdapter(adapter);

        images = getIntent().getParcelableArrayListExtra("images");
        if (images == null)
            images = new ArrayList<>();

        adapter.setClick(new FoldersAdapter.ItemClick() {
            @Override
            public void onClick(Folder folder, int position, View view) {
                setResult(0x111,new Intent(getContext(), ImageSelectorActivity.class).putExtra("folder",folder).putExtra("images",images));
                finish();
//                startActivityForResult(,0x111);
            }
        });
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
                            adapter.setFolders(mFolders);
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
        if (requestCode == 0x111 && resultCode == 0x111 && data != null){
            this.images = data.getParcelableArrayListExtra("images");
        }
    }
}
