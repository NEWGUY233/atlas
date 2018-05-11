package com.atlas.crmapp.usercenter;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.wheel.NumericWheelAdapter;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.FileUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.SpUtil;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.util.TakePhoneHelper;
import com.atlas.crmapp.view.UserInfoItemView;
import com.atlas.crmapp.view.wheel.WheelView;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UmengTool;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shaohui.bottomdialog.BottomDialog;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

import static com.atlas.crmapp.util.TakePhoneHelper.CROP_PHOTO;
import static com.atlas.crmapp.util.TakePhoneHelper.REQUEST_CODE_PICK_IMAGE;


/**
 * Created by hoda_fang on 2017/5/23.
 */
@RuntimePermissions
public class UserInfoActivity extends BaseStatusActivity {
    @BindView(R.id.v_habit)
    UserInfoItemView vHabit;
    @BindView(R.id.v_birthday)
    UserInfoItemView vBirthday;
    @BindView(R.id.v_skill)
    UserInfoItemView vSkill;
    @BindView(R.id.v_type)
    UserInfoItemView vType;
    @BindView(R.id.v_crow)
    UserInfoItemView vCrow;
    @BindView(R.id.v_email)
    UserInfoItemView vEmail;
    private UserInfoItemView vName;
    private UserInfoItemView vSex;
    private UserInfoItemView vCompany;
    private UserInfoItemView vPhone;
    private LinearLayout llImageHeader;
    private ImageView ivHeader;

    private String name;
    private String sex;
    private String company;
    private String phone;
    private String avatar;//头像url

    public static final String SEX = "SEX";
    public static final int SEX_RESULT = 1001;
    public static final int COMPANY_RESUT_CODE = 1002;
    public static final int NAME_RESUT_CODE = 1003;
    private File output;
    private PersonInfoJson personInfoJson;

    private final String headIconSamllName = "head_small_icon";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infos_);
    }


    @Override
    protected void initActivityViews() {
        super.initActivityViews();
        setTitle(getString(R.string.s13));
        vName = (UserInfoItemView) findViewById(R.id.v_name);
        vSex = (UserInfoItemView) findViewById(R.id.v_sex);
        vCompany = (UserInfoItemView) findViewById(R.id.v_company);
        vPhone = (UserInfoItemView) findViewById(R.id.v_phone);
        llImageHeader = (LinearLayout) findViewById(R.id.ll_header_icon);
        ivHeader = (ImageView) findViewById(R.id.iv_header);
        llImageHeader.setOnClickListener(onClickListener);
        vName.setOnClickListener(onClickListener);
        vSex.setOnClickListener(onClickListener);
        vCompany.setOnClickListener(onClickListener);
        vPhone.setOnClickListener(onClickListener);
        prepareActivityData();

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.ll_header_icon:
                    alertStudentDialog();
                    break;
                case R.id.v_name:
                    Intent intent = new Intent(UserInfoActivity.this, NickNameActivity.class);
                    intent.putExtra("value", name);
                    startActivityForResult(intent, NAME_RESUT_CODE);
                    break;
                case R.id.v_sex:
                    Intent intentSex = new Intent(UserInfoActivity.this, SexSelectActivity.class);
                    intentSex.putExtra("sex",sex);
                    startActivityForResult(intentSex, SEX_RESULT);
                    break;
                case R.id.v_company:
                    Intent intentComp = new Intent(UserInfoActivity.this, CompanyActivity.class);
                    intentComp.putExtra("value", company);
                    startActivityForResult(intentComp, COMPANY_RESUT_CODE);
                    break;
                case R.id.v_phone:
                    UserInfoActivity.this.startActivity(new Intent(UserInfoActivity.this, EditMobileActivity.class));
                    break;
            }
        }
    };

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        personInfoJson = getGlobalParams().getPersonInfoJson();
        name = personInfoJson.getNick();
        Logger.d(personInfoJson.getGender() + "----");
        if (personInfoJson.getGender().equals("FEMALE")) {
            sex = getString(R.string.female);
        } else if (personInfoJson.getGender().equals("MALE")) {
            sex = getString(R.string.male);
        } else {
            sex = "";
        }
        avatar = personInfoJson.getAvatar();
        company = personInfoJson.getCompany();
        phone = personInfoJson.getMobile();
        updateActivityViews();


        getInfo();
    }

    private void getInfo(){
        BizDataRequest.requestLoginUserInfo(this, new BizDataRequest.OnLoginUserInfo() {
            @Override
            public void onSuccess(PersonInfoJson personInfoJson) {
                Log.i("personInfoJson","personInfoJson = " + new Gson().toJson(personInfoJson).toString());
                UserInfoActivity.this.personInfoJson = personInfoJson;
                SpUtil.putString(getContext(),SpUtil.ICON,personInfoJson.getAvatar());
                setInfo();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private void setInfo(){
        if (personInfoJson == null)
            return;
        name = personInfoJson.getNick();
        Logger.d(personInfoJson.getGender() + "----");
        if (personInfoJson.getGender().equals("FEMALE")) {
            sex = getString(R.string.female);
        } else if (personInfoJson.getGender().equals("MALE")) {
            sex = getString(R.string.male);
        } else {
            sex = "";
        }
        avatar = personInfoJson.getAvatar();
        company = personInfoJson.getCompany();
        if (StringUtils.isEmpty(personInfoJson.getZipCode())) {
            if (StringUtils.isEmpty(SpUtil.getString(this, SpUtil.AREA, ""))){
                phone = "+" + StringUtils.getBizCode(personInfoJson.getMobile()) + " " + personInfoJson.getMobile();
            }else {
                phone = "+" + SpUtil.getString(this, SpUtil.AREA, "86") + " " + personInfoJson.getMobile();
            }
        }else
            phone = "+" + personInfoJson.getZipCode() + " " + personInfoJson.getMobile();
        GlideUtils.loadCustomImageView(this, R.drawable.header_icon, avatar, ivHeader);
        vName.updateView(getString(R.string.s14), name);
        vSex.updateView(getString(R.string.s15), sex);
        vPhone.updateView(getString(R.string.s16), phone);
        vCompany.updateView(getString(R.string.s17), company);
        vBirthday.updateView(getString(R.string.s18), DateUtil.tim(personInfoJson.getBirthday(),"yyyy - MM"));


        if (personInfoJson.getInterestList() == null || personInfoJson.getInterestList().size() == 0)
            vHabit.updateView(getString(R.string.s19), getString(R.string.s20));
        else {
            String habit = "";
            for (PersonInfoJson.InterestListBean bean : personInfoJson.getInterestList()){
                habit += bean.getName() + "/";
            }
            if (habit.endsWith("/")){
                vHabit.updateView(getString(R.string.s19),  habit.substring(0,habit.length()-1));
            }
        }

        if (StringUtils.isEmpty(personInfoJson.getSkill())){
            vSkill.updateView(getString(R.string.s21), getString(R.string.s22));
        }else {
            vSkill.updateView(getString(R.string.s21), personInfoJson.getSkill());
        }

        if (personInfoJson.getIndustry() == null || StringUtils.isEmpty(personInfoJson.getIndustry().getName())){
            vType.updateView(getString(R.string.s23), getString(R.string.s24));
        }else {
            vType.updateView(getString(R.string.s23), personInfoJson.getIndustry().getName());
        }
        if (!StringUtils.isEmpty(personInfoJson.getOtherIndustry()))
            vType.updateView(getString(R.string.s23), personInfoJson.getOtherIndustry());

        vCrow.updateView(getString(R.string.s25), StringUtils.isEmpty(personInfoJson.getJob())?getString(R.string.s26) : personInfoJson.getJob());

        vEmail.updateView(getString(R.string.s27), StringUtils.isEmpty(personInfoJson.getEmail())?getString(R.string.s28) : personInfoJson.getEmail());
    }


    @Override
    protected void updateActivityViews() {
        super.updateActivityViews();
        GlideUtils.loadCustomImageView(this, R.drawable.header_icon, avatar, ivHeader);
        vName.updateView(getString(R.string.s14), name);
        vSex.updateView(getString(R.string.s15), sex);
        vPhone.updateView(getString(R.string.s16), phone);
        vCompany.updateView(getString(R.string.s17), company);

        vHabit.updateView(getString(R.string.s19), getString(R.string.s20));
        vSkill.updateView(getString(R.string.s21), getString(R.string.s22));
        vType.updateView(getString(R.string.s23), getString(R.string.s24));
        vCrow.updateView(getString(R.string.s25), getString(R.string.s26));
        vEmail.updateView(getString(R.string.s27), getString(R.string.s28));
    }

    private void alertStudentDialog() {
        TakePhoneHelper.alertStudentDialog(this, new TakePhoneHelper.OnClickItemListener() {
            @Override
            public void onClickItemListener(int position) {
                switch (position) {
                    case 0:
                        UserInfoActivityPermissionsDispatcher.takePhotoWithCheck(UserInfoActivity.this);
                        break;
                    case 1:
                        UserInfoActivityPermissionsDispatcher.selectPhotoWithCheck(UserInfoActivity.this);
                        break;
                }
            }
        });
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

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void selectPhoto() {
        TakePhoneHelper.choosePhotoIntent(UserInfoActivity.this);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void noCameraPermission() {
        showToast(getString(R.string.please_open_permisstion_camera));
    }

    @Override
    public void onActivityResult(int req, int res, Intent data) {
        getInfo();
        switch (req) {
            case COMPANY_RESUT_CODE:
                company = getGlobalParams().getPersonInfoJson().getCompany();
                vCompany.updateView(getString(R.string.s17), company);
                break;

            case NAME_RESUT_CODE:
                name = getGlobalParams().getPersonInfoJson().getNick();
                vName.updateView(getString(R.string.s29), name);
                break;
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
                    TakePhoneHelper.getChoosePhoto(UserInfoActivity.this, data, iPhotoPath);
                }
                break;
            //性别选择
            case SEX_RESULT:
                if (null == data) {
                    return;
                }
                int sexInt = data.getIntExtra(SEX, 1);
                if (1 == sexInt) {
                    getGlobalParams().getPersonInfoJson().setGender(Constants.Sex.MALE);
                    sex = getString(R.string.male);
                } else {
                    getGlobalParams().getPersonInfoJson().setGender(Constants.Sex.FEMALE);
                    sex = getString(R.string.female);
                }
                vSex.updateView(getString(R.string.s15), sex);
                break;
            case 0x100:
                if (null == data) {
                    return;
                }
                vHabit.updateView(getString(R.string.s19), data.getStringExtra("habits"));
                break;
            case 0x101:
                if (null == data) {
                    return;
                }
                if ("".equals(data.getStringExtra("skill")))
                    vSkill.updateView(getString(R.string.s21), getString(R.string.s22));
                else
                    vSkill.updateView(getString(R.string.s21), data.getStringExtra("skill"));
                break;
            case 0x102:
                if (null == data) {
                    return;
                }
                if ("".equals(data.getStringExtra("industry")))
                    vType.updateView(getString(R.string.s23), getString(R.string.s24));
                else
                    vType.updateView(getString(R.string.s23), data.getStringExtra("industry"));
                break;
            case 0x103:
                if (null == data) {
                    return;
                }
                if ("".equals(data.getStringExtra("job")))
                    vCrow.updateView(getString(R.string.s25), getString(R.string.s26));
                else
                    vCrow.updateView(getString(R.string.s25), data.getStringExtra("job"));
                break;
            case 0x104:
                if (null == data) {
                    return;
                }
                if ("".equals(data.getStringExtra("email")))
                    vCrow.updateView(getString(R.string.s27), getString(R.string.s28));
                else
                    vCrow.updateView(getString(R.string.s27), data.getStringExtra("email"));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UserInfoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    //上传图片
    private void uploadPhoto(String filePath) {
        String path = TakePhoneHelper.getCompressFilePath(this, headIconSamllName, filePath);
        File file = new File(path);
        if (file.exists()) {
            GlideUtils.loadCustomImageView(this, R.drawable.header_icon, new File(filePath), ivHeader);
            TakePhoneHelper.uploadPhotoToServer(this, path, iUploadSuccess);
        } else {
            Toast.makeText(UserInfoActivity.this, getString(R.string.image_not_exist_retry_select), Toast.LENGTH_LONG).show();
        }

    }

    TakePhoneHelper.IUploadSuccess iUploadSuccess = new TakePhoneHelper.IUploadSuccess() {
        @Override
        public void getDownloadUrl(String serverImagePath) {
            editUserAvatar(serverImagePath);
            FileUtils.delete(output);
        }
    };

    //保存头像数据
    private void editUserAvatar(String url) {
        PersonInfoJson personInfoJson = getGlobalParams().getPersonInfoJson();
        personInfoJson.setAvatar(url);
        Logger.d("downloadUrl---" + url);
        BizDataRequest.requestModifyUserInfo(UserInfoActivity.this, personInfoJson, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                getInfo();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.delete(output);
    }

    @OnClick({R.id.v_habit, R.id.v_skill, R.id.v_type, R.id.v_crow, R.id.v_birthday, R.id.v_email})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.v_habit:
                startActivityForResult(new Intent(this,HabitActivity.class)
                        .putExtra("habits",getString(R.string.s20).equals(vHabit.getText())?"":vHabit.getText())
                        .putExtra("info",personInfoJson)
                        ,0x100);
                break;
            case R.id.v_skill:
                startActivityForResult(new Intent(this,SkillActivity.class)
                        .putExtra("skill",getString(R.string.s22).equals(vSkill.getText())?"":vSkill.getText())
                                .putExtra("info",personInfoJson)
                        ,0x101);
                break;
            case R.id.v_type:
                startActivityForResult(new Intent(this,IndustryActivity.class)
                                .putExtra("industry",getString(R.string.s24).equals(vType.getText())?"":vType.getText())
                                .putExtra("info",personInfoJson)
                        ,0x102);
                break;
            case R.id.v_crow:
//                showJobDialog();
                startActivityForResult(new Intent(this,JobActivity.class)
                                .putExtra("job",getString(R.string.s26).equals(vCrow.getText())?"":vCrow.getText())
                                .putExtra("info",personInfoJson)
                        ,0x103);
                break;
            case R.id.v_birthday:
                showBirthdayDialog();
                break;
            case R.id.v_email:
                startActivityForResult(new Intent(this,EmailActivity.class)
                                .putExtra("email",getString(R.string.s28).equals(vEmail.getText())?"":vEmail.getText())
                                .putExtra("info",personInfoJson)
                        ,0x104);
                break;
        }
    }

    BottomDialog dialog;
    public void showBirthdayDialog() {
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
                            vBirthday.updateView(getString(R.string.s18), year.getCurrentItem() + 1900 + " - " + ((mouth.getCurrentItem() + 1)%12 == 0 ? 12 :(mouth.getCurrentItem()+1)%12));
                            setBirthday();
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

    private void setBirthday(){
        if (System.currentTimeMillis() < Long.valueOf(DateUtil.dateToStamp(vBirthday.getText().toString(),"yyyy - MM"))){
            showToast(getString(R.string.s30));
            return;
        }
        HashMap params = new HashMap();
        params.put("birthday",DateUtil.dateToStamp(vBirthday.getText().toString(),"yyyy - MM"));

        BizDataRequest.requestModifyUserInfo(this, params, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

//    private void showJobDialog(){
//        AlertDialog.Builder builder;
//        builder = new AlertDialog.Builder(this);
//        builder.setTitle("您的职位");
//        View v = LayoutInflater.from(this).inflate(R.layout.dialog_industry_edit,null,false);
//        final EditText ed = (EditText) v.findViewById(R.id.et_industry);
//        builder.setView(v);
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (!StringUtils.isEmpty(ed.getText().toString())) {
//                    setInfo("job",ed.getText().toString());
//                    vCrow.updateView("职位", ed.getText().toString());
//                }
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

//    private void setInfo(String key, final String values){
//        HashMap params = new HashMap();
//        params.put(key, values);
//
//        BizDataRequest.requestModifyUserInfo(this, params, new BizDataRequest.OnRequestResult() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onError(DcnException error) {
//
//            }
//        });
//    }
}
