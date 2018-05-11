package com.atlas.crmapp.register;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.wheel.NumericWheelAdapter;
import com.atlas.crmapp.adapter.wheel.PhoneWheelAdapter;
import com.atlas.crmapp.bean.RegionCodeBean;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.Utils;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.dagger.component.DaggerRegInfoActivity_Component;
import com.atlas.crmapp.dagger.module.RegInfoActivity_Module;
import com.atlas.crmapp.huanxin.HuanXinManager;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.presenter.RegInfoActivity_Presenter;
import com.atlas.crmapp.selector.entry.Image;
import com.atlas.crmapp.selector.views.ImageSelectorActivity;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.FileUtils;
import com.atlas.crmapp.util.RegisterCommonUtils;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.util.TakePhoneHelper;
import com.atlas.crmapp.view.wheel.WheelView;
import com.atlas.crmapp.widget.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

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
 * Created by Administrator on 2018/3/27.
 */
@RuntimePermissions
public class RegInfoActivity_ extends BaseStatusActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.btnDone)
    Button btnDone;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;
    @BindView(R.id.ed_name)
    EditText edName;
    //    TextView edName;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.ll_name)
    LinearLayout llName;

    long durationTme = 400;
    float originalSizeTV = 0;
    float originalSizeED = 0;
    float density = 0;

    @Inject
    RegInfoActivity_Presenter presenter;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.iv_icon)
    CircleImageView ivIcon;
    @BindView(R.id.tv_icon)
    TextView tvIcon;
    @BindView(R.id.tv_male)
    TextView tvMale;
    @BindView(R.id.tv_female)
    TextView tvFemale;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.et_company)
    EditText etCompany;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_zipcode)
    TextView tvZipcode;
    @BindView(R.id.et_number)
    EditText etNumber;

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reginfo_activity_);
        ButterKnife.bind(this);

        initView();
    }
    String id = "";
    public void initView() {
        GlobalParams.getInstance().setIsLogin(false);
        DaggerRegInfoActivity_Component.builder().regInfoActivity_Module(new RegInfoActivity_Module(this)).build().inject(this);

        llName.setVisibility(View.VISIBLE);
        llInfo.setVisibility(View.INVISIBLE);

        originalSizeTV = tvName.getTextSize();
        originalSizeED = edName.getTextSize();
        density = getResources().getDisplayMetrics().density;

        id = String.valueOf(getIntent().getLongExtra("id",0));

        edName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    edName.setHint(R.string.t41);
                else
                    edName.setHint("");
//                tvName.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        zoomThread();

        presenter.getRegionCode();
        tvBirthday.setText(R.string.tv_birthday);
        tvBirthday.setTextColor(getResources().getColor(R.color.print_grey));

        gender = "MALE";
        tvMale.setTextColor(getResources().getColor(R.color.gender_male));
        tvFemale.setTextColor(getResources().getColor(R.color.gender_null));
        tvMale.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.login_icon_man_color,0,0,0);
        tvFemale.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.login_icon_lady_nocolor,0,0,0);
    }

    @OnClick({R.id.tv_name, R.id.btnSubmit, R.id.tv_more, R.id.rl_ed,R.id.tv_icon,R.id.iv_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_name:
                initOutAnim();
                break;
            case R.id.btnSubmit:
                hideKeyboard(edName);
                initInAnim();
                break;
            case R.id.tv_more:
                showLogoutDialog();
                break;
            case R.id.rl_ed:
                setFocus(edName);
                break;
            case R.id.tv_icon:
            case R.id.iv_icon:
                alertStudentDialog();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            showLogoutDialog();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

    private void showLogoutDialog(){
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_logout);
        dialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                RegisterCommonUtils.logout(RegInfoActivity_.this);
            }
        });

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void checkToSubmit(){
        String nickName = tvName.getText().toString();
        String company = etCompany.getText().toString();


        if (TextUtils.isEmpty(nickName)) {
            Toast.makeText(this, getResources().getString(R.string.please_input_you_name), Toast.LENGTH_LONG).show();
            return;
        }

//        if (!Pattern.compile("^[a-zA-Z0-9\\u4E00-\\u9FA5]+$").matcher(nickName).matches())


        Pattern p = Pattern.compile("[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]");
        Pattern p1 = Pattern.compile("[a-zA-Z0-9\\u4e00-\\u9fa5]+");
//
        if (p.matcher(nickName).find() || !p1.matcher(nickName).find()) {
            Toast.makeText(this, getResources().getString(R.string.please_input_you_name_correct), Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(company)) {
            Toast.makeText(this, getResources().getString(R.string.please_input_company_name), Toast.LENGTH_LONG).show();
            return;
        }

        if (p.matcher(company).find() || !p1.matcher(company).find()) {
            Toast.makeText(this, getResources().getString(R.string.please_input_company_name_correct), Toast.LENGTH_LONG).show();
            return;
        }

        if (StringUtils.isEmpty(gender)){

            return;
        }

        String sex = gender;

        if (getString(R.string.please_input_your_birthday).equals(tvBirthday.getText().toString())){
            showToast(getString(R.string.t37));
            return;
        }

        if (System.currentTimeMillis() < Long.valueOf(DateUtil.dateToStamp(tvBirthday.getText().toString(),"yyyy - MM"))){
            showToast(getString(R.string.t38));
            return;
        }

        Log.i("RegsInfo","submit");
        editUserInfo(nickName,sex,company);
    }


    private void editUserInfo(final String nickName, final String gendar, final String mCompanyName) {

        HashMap params = new HashMap();
        params.put("nick", nickName);
        params.put("avatar", uploadUrl);
        params.put("gender", gendar);
        params.put("birthday", DateUtil.dateToStamp(tvBirthday.getText().toString(),"yyyy - MM"));
        if (StringUtils.isNotEmpty(mCompanyName)) {
            params.put("company", mCompanyName.trim());
        }
        String referrerMobile = etNumber.getText().toString();
        String zipCode = "";
        if (currentItem != -1 && list != null && list.size() != 0)
            zipCode = list.get(currentItem).getZip_code();
        else
            zipCode = "86";
        if ((!StringUtils.isPhone(referrerMobile,zipCode)) && !StringUtils.isEmpty(referrerMobile)) {
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

        presenter.submit(params,id);
//        BizDataRequest.requestModifyUserInfo(this, params, new BizDataRequest.OnRequestResult() {
//            @Override
//            public void onSuccess() {
//                HuanXinManager.login(getGlobalParams().getPersonInfoJson().getId() + "", getGlobalParams().getPersonInfoJson().getUid());
//                PersonInfoJson personInfoJson = getGlobalParams().getPersonInfoJson();
//                personInfoJson.setNick(nickName);
//                personInfoJson.setCompany(mCompanyName);
//                personInfoJson.setGender(gendar);
//                personInfoJson.setAvatar(uploadUrl);
//
//                Utils.storeAccountToken(this, getGlobalParams().getAccessToken(), getGlobalParams().getRefreshToken(), personInfoJson);
////                Intent intent = new Intent(RegInfoActivity.this, MainActivity.class);
//                Intent intent = new Intent(RegInfoActivity.this, IndexActivity.class);
//                RegInfoActivity.this.startActivity(intent);
//                RegInfoActivity.this.finish();
//            }
//
//            @Override
//            public void onError(DcnException error) {
//
//            }
//        });
    }

    //-------------------------------anim----------------------------------

    AnimationSet animationInSet;
    AnimationSet animationInSet1;
    AnimationSet animationInSet2;

    private void initInAnim() {
        if (animationInSet == null) {
            Animation animationT = new TranslateAnimation(0, 0, 0, btnDone.getY() - btnSubmit.getY());
            Animation animationA = new AlphaAnimation(1.0f, 0.5f);
            animationInSet = new AnimationSet(false);
            animationInSet.addAnimation(animationT);
            animationInSet.addAnimation(animationA);
            animationInSet.setDuration(durationTme);
        }


        if (animationInSet1 == null)
            animationInSet1 = new AnimationSet(false);
        //放大动画会导致移动位置不准确
        Animation animationT1 = new TranslateAnimation(0
                , (tvName.getX() - edName.getX())
//                        + edName.getWidth() * (24.0f/14.0f - 1)
                , 0, 0);
        animationInSet1.getAnimations().clear();
        animationInSet1.addAnimation(animationT1);
        animationInSet1.setDuration(durationTme);
//        animationInSet1.setFillAfter(true);


//        edName.m


        tvName.setVisibility(View.INVISIBLE);
        btnDone.setVisibility(View.INVISIBLE);
        llInfo.setVisibility(View.VISIBLE);
        llContent.setVisibility(View.VISIBLE);

        if (animationInSet2 == null) {
            Animation animationT2 = new TranslateAnimation(0, 0, llContent.getHeight(), 0);
            Animation animationA1 = new AlphaAnimation(0.2f, 1.0f);
            animationInSet2 = new AnimationSet(false);
            animationInSet2.addAnimation(animationT2);
            animationInSet2.addAnimation(animationA1);
            animationInSet2.setDuration(durationTme);
        }

        animationInSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                inEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

//        if (!isZoom) {
//            isZoom = true;
//            zoom = true;
//            zoomThread.start();
//        }

        btnSubmit.startAnimation(animationInSet);
        edName.startAnimation(animationInSet1);
        llContent.startAnimation(animationInSet2);

    }

    AnimationSet animationOutSet;
    AnimationSet animationOutSet1;
    AnimationSet animationOutSet2;

    private void initOutAnim() {
        if (animationOutSet == null) {
            Animation animationT = new TranslateAnimation(0, 0, 0, -(btnDone.getY() - btnSubmit.getY()));
            Animation animationA = new AlphaAnimation(0.5f, 1f);
            animationOutSet = new AnimationSet(false);
            animationOutSet.addAnimation(animationT);
            animationOutSet.addAnimation(animationA);
            animationOutSet.setDuration(durationTme);
//            animationOutSet.setFillAfter(true);
        }


        if (animationOutSet1 == null) {
            animationOutSet1 = new AnimationSet(false);
            animationOutSet1.setDuration(durationTme);
        }
        //放大动画会导致移动位置不准确
        Animation animationT1 = new TranslateAnimation(0,
                -(tvName.getX() - edName.getX())
//                        + edName.getWidth() * (24.0f/14.0f - 1)
                , 0, 0);
        animationOutSet1.getAnimations().clear();
        animationOutSet1.addAnimation(animationT1);
//        animationOutSet1.setFillAfter(true);


        if (animationOutSet2 == null) {
            Animation animationT2 = new TranslateAnimation(0, 0, 0, llContent.getHeight());
            Animation animationA1 = new AlphaAnimation(1.0f, 0);
            animationOutSet2 = new AnimationSet(false);
            animationOutSet2.addAnimation(animationT2);
            animationOutSet2.addAnimation(animationA1);
            animationOutSet2.setDuration(durationTme);
//            animationOutSet2.setFillAfter(true);
        }


        animationOutSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i("Animation", "onAnimationEnd");
                outEnd();
                Log.i("Animation", "onAnimationEnd_");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

//
//        if (!isZoom) {
//            isZoom = true;
//            zoom = false;
//            zoomThread.start();
//        }

        btnDone.startAnimation(animationOutSet);
        tvName.startAnimation(animationOutSet1);
        llContent.startAnimation(animationOutSet2);
    }

    Thread zoomThread;
    private boolean isZoom = false;
    private boolean zoom = false;

    private void zoomThread() {
        zoomThread = new Thread() {
            @Override
            public void run() {
                float size = (originalSizeTV - originalSizeED) / (durationTme * 1f / 15);
                float currentSize = zoom ? originalSizeED : originalSizeTV;
//                float currentSize = originalSizeED;

                while (isZoom) {
                    Message msg = new Message();
                    Bundle b = new Bundle();
                    if (zoom) {
                        currentSize += size;
                        msg.what = 1;
                        if (currentSize >= originalSizeTV)
                            isZoom = false;
                    } else {
                        currentSize -= size;
                        msg.what = 3;
                        if (currentSize <= originalSizeED)
                            isZoom = false;
                    }
                    b.putFloat("size", currentSize / density);
                    msg.setData(b);
                    handler.sendMessage(msg);
                    if (!isZoom)
                        Log.i("Animation", "isZoom = false");
                    try {
                        sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                if (zoom)
                    handler.sendEmptyMessage(2);
                else
                    handler.sendEmptyMessage(4);

            }
        };
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    edName.setTextSize(msg.getData().getFloat("size"));
                    break;
                case 2:
                    inEnd();
//                    edName.setTextSize(originalSizeED/ density);
//                    tvName.setTextSize(originalSizeTV/ density);
                    break;
                case 3:
                    tvName.setTextSize(msg.getData().getFloat("size"));
                    break;
                case 4:
                    isZoom = true;
                    Log.i("Animation", "handler");
                    outEnd();
                    Log.i("Animation", "handler_");
                    break;
            }
        }
    };

    private void outEnd() {
        tvName.setText("");
//        if (!isZoom)
//            return;
//        tvName.setVisibility(View.GONE);
//        llContent.setVisibility(View.GONE);
//        btnDone.setVisibility(View.GONE);
        llInfo.setVisibility(View.INVISIBLE);
        llName.setVisibility(View.VISIBLE);
        edName.setVisibility(View.VISIBLE);
//                btnSubmit.setVisibility(View.VISIBLE);
        isZoom = false;
        tvName.setTextSize(originalSizeTV / density);
    }

    private void inEnd() {
        llName.setVisibility(View.INVISIBLE);
        llInfo.setVisibility(View.VISIBLE);
        btnDone.setVisibility(View.VISIBLE);
        tvName.setVisibility(View.VISIBLE);
        tvName.setText(edName.getText());
        isZoom = false;
        edName.setTextSize(originalSizeED / density);
    }

    String gender = "";
    @OnClick({R.id.tv_more, R.id.iv_icon, R.id.tv_icon, R.id.tv_male, R.id.tv_female, R.id.tv_birthday, R.id.tv_area, R.id.tv_zipcode, R.id.btnDone})
    public void onViewClicked_(View view) {
        switch (view.getId()) {
            case R.id.tv_more:
                break;
            case R.id.iv_icon:
            case R.id.tv_icon:
                break;
            case R.id.tv_male:
                gender = "MALE";
                tvMale.setTextColor(getResources().getColor(R.color.gender_male));
                tvFemale.setTextColor(getResources().getColor(R.color.gender_null));

                tvMale.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.login_icon_man_color,0,0,0);
                tvFemale.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.login_icon_lady_nocolor,0,0,0);
                break;
            case R.id.tv_female:
                gender = "FEMALE";
                tvMale.setTextColor(getResources().getColor(R.color.gender_null));
                tvFemale.setTextColor(getResources().getColor(R.color.gender_female));

                tvMale.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.login_icon_man_nocolor,0,0,0);
                tvFemale.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.login_icon_lady_color,0,0,0);
                break;
            case R.id.tv_birthday:
                onViewClicked();
                break;
            case R.id.tv_area:
            case R.id.tv_zipcode:
                initAreaDialog();
                break;
            case R.id.btnDone:
                checkToSubmit();
                break;
        }
    }

    List<RegionCodeBean> list;

    public void setAreaCode(List<RegionCodeBean> list) {
        this.list = list;
    }

    BottomDialog dialog;
    int currentItem = -1;

    private void initAreaDialog() {
        dialog = BottomDialog.create(getSupportFragmentManager()).setViewListener(new BottomDialog.ViewListener() {

            @Override
            public void bindView(View v) {
                final WheelView area = (WheelView) v.findViewById(R.id.phone_area);
                PhoneWheelAdapter adapter = new PhoneWheelAdapter(RegInfoActivity_.this, list);
                area.setViewAdapter(adapter);
                area.setVisibleItems(7);
                if (currentItem != -1)
                    area.setCurrentItem(currentItem);

                v.findViewById(R.id.set).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentItem = area.getCurrentItem();
                        RegionCodeBean bean = list.get(area.getCurrentItem());
                        tvArea.setText(bean.getRegion());
                        tvZipcode.setText("+" + bean.getZip_code());

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
        }).setLayoutRes(R.layout.view_phone_area)
                .setDimAmount(0.5f)
                .setCancelOutside(true)
                .setTag("PhoneDialog");
        dialog.show();
    }

    BottomDialog dialogb;
    public void onViewClicked() {
        if (dialogb == null) {
            dialogb = BottomDialog.create(getSupportFragmentManager()).setViewListener(new BottomDialog.ViewListener() {

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
                            tvBirthday.setTextColor(getResources().getColor(R.color.black_text));
                            tvBirthday.setText(year.getCurrentItem() + 1900 + " - " + ((mouth.getCurrentItem() + 1)%12 == 0 ? 12 :(mouth.getCurrentItem()+1)%12));
                            dialogb.dismiss();
                        }
                    });

                    v.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogb.dismiss();
                        }
                    });

                }
            }).setLayoutRes(R.layout.view_timepick)
                    .setDimAmount(0.5f)
                    .setCancelOutside(true)
                    .setTag("DateDialog");
            dialogb.show();;
        }else
            dialogb.show();
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

    private String uploadUrl;
    private File output;
    private final String headIconSamllName = "info_icon";
    private boolean isMale = true;
    private void alertStudentDialog() {
        TakePhoneHelper.alertStudentDialog(this, new TakePhoneHelper.OnClickItemListener() {
            @Override
            public void onClickItemListener(int position) {
                switch (position) {
                    case 0:
                        RegInfoActivity_PermissionsDispatcher.takePhotoWithCheck(RegInfoActivity_.this);
//                        startActivity(FoldersActivity.class);
                        break;
                    case 1:
//                        IndexPostNewsActivityPermissionsDispatcher.selectPhotoWithCheck(IndexPostNewsActivity.this);
                        RegInfoActivity_PermissionsDispatcher.selectPhotoWithCheck(RegInfoActivity_.this);
                        break;
                }
            }
        });
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void selectPhoto() {
        startActivityForResult(new Intent(getContext(),ImageSelectorActivity.class).putExtra("size",1),0x111);
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
//            GlideUtils.loadCustomImageView(this, R.drawable.header_icon, new File(filePath), ivHeader);
            TakePhoneHelper.uploadPhotoToServer(this, path, iUploadSuccess);
//            list.add(filePath);
            Glide.with(this).load(path).apply(new RequestOptions().dontAnimate().centerCrop()).into(ivIcon);
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
        RegInfoActivity_PermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.delete(output);
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
            case 0x111:
                if (data == null || res != 0x111)
                    return;
                ArrayList<Image> list  = data.getParcelableArrayListExtra("images");
//                adapter.setList(list);
                Glide.with(this).load(list.get(0).getPath()).apply(new RequestOptions().dontAnimate().centerCrop()).into(ivIcon);
                iPhotoPath.getTakePhotoPath(list.get(0).getPath());
                break;
            case 0x112:
                if (data == null || res != 0x112)
                    return;
                List<String> list_s = data.getStringArrayListExtra("images");
                Glide.with(this).load(list_s.get(0)).apply(new RequestOptions().dontAnimate().centerCrop()).into(ivIcon);
                iPhotoPath.getTakePhotoPath(list_s.get(0));
//                adapter.setList(list_s);
                break;
            default:
                break;
        }
    }
}
