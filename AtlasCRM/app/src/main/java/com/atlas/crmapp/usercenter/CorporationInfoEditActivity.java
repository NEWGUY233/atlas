package com.atlas.crmapp.usercenter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.CompnayInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.FileUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.util.TakePhoneHelper;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class CorporationInfoEditActivity extends BaseStatusActivity {
//TODO:(1)所有文本可以直接编辑（需要修改布局文件配置）
//TODO:(2)右上角有保存按钮，点击提交修改

    private static final int CROP_PHOTO = 2;
    private static final int REQUEST_CODE_PICK_IMAGE = 3;
    private final String headIconSamllName ="corporation_small_icon";
    private File output;
    private String imageUrl;

    @BindView(R.id.iv_logo)
    ImageView mIvLogo;
    @BindView(R.id.et_name)
    EditText mTvName;
    @BindView(R.id.et_description)
    EditText mTvDescription;
    @BindView(R.id.et_contact)
    EditText mTvContract;
    @BindView(R.id.et_phone)
    EditText mTvPhone;


    private CompnayInfoJson company;

    private void onEdit() {
        String contract = mTvContract.getText().toString();
        String phone = mTvPhone.getText().toString();
        if(StringUtils.isEmpty(contract)){
            showToast(getString(R.string.t51));
            return;
        }
        if(StringUtils.isEmpty(phone)){
            showToast(getString(R.string.t52));
            return;
        }
        BizDataRequest.requestUpdateCompany(CorporationInfoEditActivity.this, company.id, mTvName.getText().toString(), contract, phone, company.fax, company.city, company.address, imageUrl, mTvDescription.getText().toString(), new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                finish();
            }

            @Override
            public void onError(DcnException error) {
                Toast.makeText(CorporationInfoEditActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    void onBack() {
        new AlertDialog.Builder(this).setTitle(R.string.text_67)
                .setMessage(R.string.t53)
                .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CorporationInfoEditActivity.this.finish();
                    }
                })
                .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onEdit();
                    }
                })
                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            onBack();
        }
        return false;
    }

    @OnClick(R.id.iv_logo)
    void chooseLogo() {
        alertStudentDialog();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporation_info_edit);
        setTitle(getString(R.string.t54));
        company = (CompnayInfoJson) getIntent().getSerializableExtra("company");
        if(!TextUtils.isEmpty(company.thumbnail)) {
            GlideUtils.loadCustomImageView(this, R.drawable.ic_reg_logo, LoadImageUtils.loadSmallImage(company.thumbnail), mIvLogo);
        }
        mTvName.setText(company.name);
        mTvDescription.setText(company.description);
        mTvContract.setText(company.contact);
        mTvPhone.setText(company.phone);
        imageUrl = company.thumbnail;

        setTopRightButton(getString(R.string.save), new OnClickListener() {
            @Override
            public void onClick() {
                onEdit();
            }
        });
        setTopLeftButton(R.drawable.white_back, new OnClickListener() {
            @Override
            public void onClick() {
                onBack();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.delete(output);
    }

    private void alertStudentDialog() {
        TakePhoneHelper.alertStudentDialog(this, new TakePhoneHelper.OnClickItemListener() {
            @Override
            public void onClickItemListener(int position) {
                switch (position) {
                    case 0:
                        CorporationInfoEditActivityPermissionsDispatcher.takePhotoWithCheck(CorporationInfoEditActivity.this);
                        break;
                    case 1:
                        CorporationInfoEditActivityPermissionsDispatcher.selectPhotoWithCheck(CorporationInfoEditActivity.this);
                        break;
                }
            }
        });
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void selectPhoto(){
        TakePhoneHelper.choosePhotoIntent(CorporationInfoEditActivity.this);
    }

    /**
     * 拍照,Manifest.permission.WRITE_EXTERNAL_STORAGE
     */
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE })
    void takePhoto() {
        FileUtils.delete(output);
        output = TakePhoneHelper.getNewOnlyImageFile();
        TakePhoneHelper.takePhotoIntent(this, output);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE })
    void noCameraPermission(){
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
                    TakePhoneHelper.getChoosePhoto(this, data, iPhotoPath);
                }
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
    private void uploadPhoto(String filePath){
        String path = TakePhoneHelper.getCompressFilePath(this, headIconSamllName +".jpg", filePath);
        File file = new File(path);
        if (file.exists()) {
            GlideUtils.loadCustomImageView(this, R.drawable.header_icon,  new File(filePath), mIvLogo);
            TakePhoneHelper.uploadPhotoToServer(this, path, iUploadSuccess);
        }else{
            showToast(getString(R.string.image_not_exist_retry_select));
        }
    }

    TakePhoneHelper.IUploadSuccess iUploadSuccess = new TakePhoneHelper.IUploadSuccess() {
        @Override
        public void getDownloadUrl(String serverImagePath) {
            imageUrl = serverImagePath;
            FileUtils.delete(output);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CorporationInfoEditActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


}
