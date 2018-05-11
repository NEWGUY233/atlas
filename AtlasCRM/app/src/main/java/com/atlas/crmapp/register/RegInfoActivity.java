package com.atlas.crmapp.register;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.MainActivity;
import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.activity.index.IndexActivity;
import com.atlas.crmapp.adapter.wheel.NumericWheelAdapter;
import com.atlas.crmapp.common.Utils;
import com.atlas.crmapp.huanxin.HuanXinManager;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.FileUtils;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.RegisterCommonUtils;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.util.TakePhoneHelper;
import com.atlas.crmapp.view.wheel.WheelView;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shaohui.bottomdialog.BaseBottomDialog;
import me.shaohui.bottomdialog.BottomDialog;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

import static com.atlas.crmapp.util.TakePhoneHelper.CROP_PHOTO;
import static com.atlas.crmapp.util.TakePhoneHelper.REQUEST_CODE_PICK_IMAGE;

@RuntimePermissions
public class RegInfoActivity extends BaseActivity {

    private String uploadUrl;
    private File output;
    private final String headIconSamllName = "head_small_icon";
    private boolean isMale = true;

    @BindView(R.id.iv_header)
    ImageView ivHeader;

    @BindView(R.id.etNickName)
    EditText etNickName;

    @BindView(R.id.etCompany)
    EditText etCompany;

    @BindView(R.id.etReferrerMobile)
    EditText etReferrerMobile;

    @BindView(R.id.etBirthday)
    EditText etBirthday;

    @BindView(R.id.tv_complete_login)
    TextView tvComplete;

    @BindView(R.id.iv_male)
    ImageView ivMale;

    @BindView(R.id.iv_female)
    ImageView ivFemale;


    @OnClick({R.id.btn_upload, R.id.iv_header})
    void onUpload() {
        alertStudentDialog();
    }

    @OnClick(R.id.iv_back)
    void onBack() {
        exit();
    }

    @OnClick({R.id.tv_female, R.id.tv_male, R.id.iv_male, R.id.iv_female})
    void onClickSex(View view) {
        int id = view.getId();
        if (id == R.id.tv_female || id == R.id.iv_female) {
            isMale = false;
        } else {
            isMale = true;
        }
        if (isMale) {
            ivMale.setImageResource(R.drawable.sex_selected);
            ivFemale.setImageResource(R.drawable.sex_not_select);
        } else {
            ivMale.setImageResource(R.drawable.sex_not_select);
            ivFemale.setImageResource(R.drawable.sex_selected);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_info);
        etNickName.addTextChangedListener(textWatcher);
        etCompany.addTextChangedListener(textWatcher);

    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (StringUtils.isEmpty(etNickName.getText().toString().trim()) || StringUtils.isEmpty(etCompany.getText().toString().trim())) {
                FormatCouponInfo.setViewToTransparent(tvComplete, true);
            } else {
                FormatCouponInfo.setViewToTransparent(tvComplete, false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected boolean translucentStatusBar() {
        return true;
    }

    @OnClick(R.id.tv_complete_login)
    void onSubmit() {

        String nickName = etNickName.getText().toString();
        String company = etCompany.getText().toString();


        if (TextUtils.isEmpty(nickName)) {
            Toast.makeText(RegInfoActivity.this, getResources().getString(R.string.please_input_you_name), Toast.LENGTH_LONG).show();
            return;
        }

//        if (!Pattern.compile("^[a-zA-Z0-9\\u4E00-\\u9FA5]+$").matcher(nickName).matches())


        Pattern p = Pattern.compile("[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]");
        Pattern p1 = Pattern.compile("[a-zA-Z0-9\\u4e00-\\u9fa5]+");
//
        if (p.matcher(nickName).find() || !p1.matcher(nickName).find()) {
            Toast.makeText(RegInfoActivity.this, getResources().getString(R.string.please_input_you_name_correct), Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(company)) {
            Toast.makeText(RegInfoActivity.this, getResources().getString(R.string.please_input_company_name), Toast.LENGTH_LONG).show();
            return;
        }

        if (p.matcher(company).find() || !p1.matcher(company).find()) {
            Toast.makeText(RegInfoActivity.this, getResources().getString(R.string.please_input_company_name_correct), Toast.LENGTH_LONG).show();
            return;
        }

        String sex = "";
        if (isMale) {
            sex = "MALE";
        } else {
            sex = "FEMALE";
        }

        if (StringUtils.isEmpty(etBirthday.getText().toString())){
            showToast(getString(R.string.t37));
            return;
        }

        if (System.currentTimeMillis() < Long.valueOf(DateUtil.dateToStamp(etBirthday.getText().toString(),"yyyy - MM"))){
            showToast(getString(R.string.t38));
            return;
        }

        editUserInfo(nickName, sex, company);
    }


    private void editUserInfo(final String nickName, final String gendar, final String mCompanyName) {

        HashMap params = new HashMap();
        params.put("nick", nickName);
        params.put("avatar", uploadUrl);
        params.put("gender", gendar);
        params.put("birthday", DateUtil.dateToStamp(etBirthday.getText().toString(),"yyyy - MM"));
        if (StringUtils.isNotEmpty(mCompanyName)) {
            params.put("company", mCompanyName.trim());
        }
        String referrerMobile = etReferrerMobile.getText().toString();
        if ((referrerMobile.length() != 11 || !StringUtils.isPhone(referrerMobile)) && !StringUtils.isEmpty(referrerMobile)) {
            showToast(getString(R.string.t39));
            return;
        }
        if (StringUtils.isNotEmpty(referrerMobile)) {
            params.put("referrerMobile", referrerMobile);
            String mobile = getGlobalParams().getPersonInfoJson().getMobile();
            if (StringUtils.isNotEmpty(mobile) && mobile.contains(referrerMobile)) {
                showToast(getString(R.string.t40));
                return;
            }
        }
        BizDataRequest.requestModifyUserInfo(RegInfoActivity.this, params, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                HuanXinManager.login(getGlobalParams().getPersonInfoJson().getId() + "", getGlobalParams().getPersonInfoJson().getUid());
                PersonInfoJson personInfoJson = getGlobalParams().getPersonInfoJson();
                personInfoJson.setNick(nickName);
                personInfoJson.setCompany(mCompanyName);
                personInfoJson.setGender(gendar);
                personInfoJson.setAvatar(uploadUrl);

                Utils.storeAccountToken(RegInfoActivity.this, getGlobalParams().getAccessToken(), getGlobalParams().getRefreshToken(), personInfoJson);
//                Intent intent = new Intent(RegInfoActivity.this, MainActivity.class);
                Intent intent = new Intent(RegInfoActivity.this, IndexActivity.class);
                RegInfoActivity.this.startActivity(intent);
                RegInfoActivity.this.finish();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private void alertStudentDialog() {
        TakePhoneHelper.alertStudentDialog(this, new TakePhoneHelper.OnClickItemListener() {
            @Override
            public void onClickItemListener(int position) {
                switch (position) {
                    case 0:
                        RegInfoActivityPermissionsDispatcher.takePhotoWithCheck(RegInfoActivity.this);
                        break;
                    case 1:
                        RegInfoActivityPermissionsDispatcher.selectPhotoWithCheck(RegInfoActivity.this);
                        break;
                }
            }
        });
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void selectPhoto() {
        TakePhoneHelper.choosePhotoIntent(RegInfoActivity.this);
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
                    TakePhoneHelper.getChoosePhoto(RegInfoActivity.this, data, iPhotoPath);
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
    private void uploadPhoto(String filePath) {
        String path = TakePhoneHelper.getCompressFilePath(this, headIconSamllName + ".jpg", filePath);
        File file = new File(path);
        if (file.exists()) {
            GlideUtils.loadCustomImageView(this, R.drawable.header_icon, new File(filePath), ivHeader);
            TakePhoneHelper.uploadPhotoToServer(this, path, iUploadSuccess);
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
        RegInfoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.delete(output);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this).setTitle(R.string.text_67)
                    .setMessage(getString(R.string.yes_or_no_exit_login))
                    .setNegativeButton(R.string.text_no, null)
                    .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            exit();
                        }
                    })
                    .show();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        RegisterCommonUtils.logout(RegInfoActivity.this);
        RegInfoActivity.this.finish();
    }

    BottomDialog dialog;
    @OnClick(R.id.etBirthday)
    public void onViewClicked() {
        if (dialog == null) {
            dialog = BottomDialog.create(getSupportFragmentManager()).setViewListener(new BottomDialog.ViewListener() {

                @Override
                public void bindView(View v) {
                    final WheelView year = (WheelView) v.findViewById(R.id.hour);
                    initHour(year);
                    year.setVisibleItems(7);
                    year.setCurrentItem(118);

                    final WheelView mouth = (WheelView) v.findViewById(R.id.mins);
                    initMouth(mouth);
                    mouth.setVisibleItems(7);
                    mouth.setCurrentItem(0);

                    v.findViewById(R.id.set).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            etBirthday.setText(year.getCurrentItem() + 1900 + " - " + ((mouth.getCurrentItem() + 1)%12 == 0 ? 12 :(mouth.getCurrentItem()+1)%12));
                            dialog.dismiss();
                        }
                    });

                    v.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                }
            }).setLayoutRes(R.layout.view_timepick)
                    .setDimAmount(0.5f)
                    .setCancelOutside(true)
                    .setTag("DateDialog");
            dialog.show();;
        }else
             dialog.show();
    }

    private void initHour(WheelView hour) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this, 1900, 2018, "%02d");
        numericWheelAdapter.setLabel(" " + getString(R.string.year));
        hour.setViewAdapter(numericWheelAdapter);
//        hour.setCyclic(true);
    }

    private void initMouth(WheelView mouth) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this, 1, 12, "%02d");
        numericWheelAdapter.setLabel(" " + getString(R.string.month));
        mouth.setViewAdapter(numericWheelAdapter);
//        mouth.setCyclic(true);
    }
}
