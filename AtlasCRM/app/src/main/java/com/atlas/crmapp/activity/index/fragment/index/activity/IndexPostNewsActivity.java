package com.atlas.crmapp.activity.index.fragment.index.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.adapter.PostNewsAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.selector.entry.Image;
import com.atlas.crmapp.selector.views.FoldersActivity;
import com.atlas.crmapp.selector.views.ImageSelectorActivity;
import com.atlas.crmapp.util.FileUtils;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.util.TakePhoneHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

import static com.atlas.crmapp.util.TakePhoneHelper.CROP_PHOTO;
import static com.atlas.crmapp.util.TakePhoneHelper.REQUEST_CODE_PICK_IMAGE;

/**
 * Created by Administrator on 2018/3/19.
 */
@RuntimePermissions
public class IndexPostNewsActivity extends BaseStatusActivity {

    private String uploadUrl;
    private File output;
    private final String headIconSamllName = "post_news";
    private int picName = 0;
    private boolean isMale = true;
    List<String> list;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.tv_length)
    TextView tv_length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_post_news);
        ButterKnife.bind(this);
        initView();

    }

    PostNewsAdapter adapter;
    private void initView() {
        initToolbar();
        setTopLeftButton(R.mipmap.nav_icon_close, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        setTitle(getString(R.string.index_news_title));

        setTopRightButton(getString(R.string.index_news_submit), new OnClickListener() {
            @Override
            public void onClick() {
                if (StringUtils.isEmpty(et_content.getText().toString()) && (adapter.getList() == null || adapter.getList().size() == 0)) {
                    showToast(getString(R.string.input_text_img));
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("imgs",(ArrayList)adapter.getList());
                intent.putExtra("content",et_content.getText().toString());
                setResult(0x333,intent);
                finish();
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        adapter = new PostNewsAdapter(getContext());
        recyclerView.setAdapter(adapter);
        list = new ArrayList<>();

        adapter.setClick(new PostNewsAdapter.OnAddClick() {
            @Override
            public void onClick() {
                alertStudentDialog();
            }
        });

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_length.setText(s.length() + "/140");
            }
        });
    }

    private void alertStudentDialog() {
        TakePhoneHelper.alertStudentDialog(this, new TakePhoneHelper.OnClickItemListener() {
            @Override
            public void onClickItemListener(int position) {
                switch (position) {
                    case 0:
                        IndexPostNewsActivityPermissionsDispatcher.takePhotoWithCheck(IndexPostNewsActivity.this);
//                        startActivity(FoldersActivity.class);
                        break;
                    case 1:
//                        IndexPostNewsActivityPermissionsDispatcher.selectPhotoWithCheck(IndexPostNewsActivity.this);
                        IndexPostNewsActivityPermissionsDispatcher.selectPhotoWithCheck(IndexPostNewsActivity.this);
                        break;
                }
            }
        });
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void selectPhoto() {
//        TakePhoneHelper.choosePhotoIntent(IndexPostNewsActivity.this);
        startActivityForResult(new Intent(getContext(),ImageSelectorActivity.class).putExtra("size",9 - adapter.getList().size()),0x111);
    }

    /**
     * 拍照,Manifest.permission.WRITE_EXTERNAL_STORAGE
     */
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void takePhoto() {
        FileUtils.delete(output);
        output = TakePhoneHelper.getNewOnlyImageFile();
        TakePhoneHelper.takePhotoIntent(this, output);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void noCameraPermission() {
        showToast(getString(R.string.please_open_permisstion_camera));
    }


    @Override
    public void onActivityResult(int req, int res, Intent data) {
        switch (req) {
            /**
             * 拍照的请求标志
             */
            case CROP_PHOTO:
                if (res == RESULT_OK) {
                    TakePhoneHelper.getTakePhotoPath(output, iPhotoPath);
                } else {
                    FileUtils.delete(output);
                }
                break;
            /**
             * 从相册中选取图片的请求标志
             */
            case REQUEST_CODE_PICK_IMAGE:
                if (res == RESULT_OK) {
                    TakePhoneHelper.getChoosePhoto(IndexPostNewsActivity.this, data, iPhotoPath);
                }
                break;
            case 0x111:
                if (data == null || res != 0x111)
                    return;
                ArrayList<Image> list  = data.getParcelableArrayListExtra("images");
                adapter.setList(list);
                break;
            case 0x112:
                if (data == null || res != 0x112)
                    return;
                List<String> list_s = data.getStringArrayListExtra("images");
                adapter.setList(list_s);
                break;
            default:
                break;
        }
    }


    TakePhoneHelper.IPhotoPath iPhotoPath = new TakePhoneHelper.IPhotoPath() {
        @Override
        public void getTakePhotoPath(String path) {
            uploadPhoto(path);
        }
    };

    //上传图片
    private void uploadPhoto(String filePath) {
        String path = TakePhoneHelper.getCompressFilePath(this, headIconSamllName + picName + String.valueOf(System.currentTimeMillis()/1000), filePath);
        File file = new File(path);
        if (file.exists()) {
            if (adapter.getList() == null){
//                Log.i("uploadPhoto","filePath getList = " + file.getAbsolutePath);
                list = new ArrayList<>();
                list.add(file.getAbsolutePath());
                adapter.setList(list);
            }else {
//                Log.i("uploadPhoto","filePath add = " + file.getAbsolutePath());
                list = adapter.getList();;
                list.add(file.getAbsolutePath());
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }
            ++picName;
            FileUtils.delete(output);

        } else {
            showToast(getString(R.string.image_not_exist_retry_select));
        }
    }

    TakePhoneHelper.IUploadSuccess iUploadSuccess = new TakePhoneHelper.IUploadSuccess() {
        @Override
        public void getDownloadUrl(String serverImagePath) {
            uploadUrl = serverImagePath;
            FileUtils.delete(output);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        IndexPostNewsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        FileUtils.delete(output);
    }

}
