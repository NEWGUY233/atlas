package com.atlas.crmapp.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.communication.ChatActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.PreviewPhotosActivity;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.huanxin.IMHelper;
import com.atlas.crmapp.huanxin.db.InviteMessgeDao;
import com.atlas.crmapp.huanxin.domain.InviteMessage;
import com.atlas.crmapp.model.CoachJson;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.SpUtil;
import com.atlas.crmapp.util.StringUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.domain.EaseUser;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMAddFriendRequest;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.umeng.socialize.utils.DeviceConfig.context;

public class UserCardActivity extends BaseStatusActivity {

    @BindView(R.id.tv_user_card_title)
    TextView activityTitle;

    @BindView(R.id.tv_nick)
    TextView mTvNick;
    @BindView(R.id.item_user_info)
    View vCompany;
    @BindView(R.id.tv_company)
    TextView mTvCompany;
    @BindView(R.id.iv_avatar)
    ImageView mTvAvatar;

    @BindView(R.id.item_trainer_intro)
    View vTrainerIntro;
    @BindView(R.id.tv_trainer_intro)
    TextView tvTrainerIntro;

    @BindView(R.id.item_trainer_qualification)
    View vTrainerQualification;
    @BindView(R.id.tv_trainer_qualification)
    TextView tvTrainerQualification;

    @OnClick(R.id.ibBack)
    void onBack() {
        UserCardActivity.this.finish();
    }

    @BindView(R.id.btn_add)
    Button mBtnAdd;

    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.iv_sex)
    ImageView ivSex;

    @BindView(R.id.btn_accept)
    Button btnAccept;
    @BindView(R.id.btn_refuse)
    Button btnRefuse;

    private String uid ;
    private String id ;
    private String qCode;
    private String type;
    private InviteMessage msg;
    public final static  String KEY_CODE ="code";
    public final static  String KEY_UID ="uid";
    public final static  String KEY_ID ="id";
    public final static String KEY_COACH = "coach";
    public final static String KEY_MSG= "msg";
    private CoachJson coachJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_card);

        coachJson = (CoachJson) getIntent().getExtras().getSerializable(KEY_COACH);
        uid = getIntent().getStringExtra(KEY_UID);
        long id = getIntent().getLongExtra(KEY_ID,0);
        Log.i("UserCardActivity"," id = " + id);
        if (id != 0)
            this.id = String.valueOf(id);
        qCode = getIntent().getStringExtra(KEY_CODE);
        msg = (InviteMessage) getIntent().getExtras().getSerializable(KEY_MSG);
        if(msg!= null){
            uid = msg.getFrom();
        }
        if(!TextUtils.isEmpty(uid)){
            ArrayList<String> list = new ArrayList<String>();
            list.add(uid);
            getPersonInfoJsonWithUid(list);
        } else if(!TextUtils.isEmpty(qCode)) {
            getPersonInfoJson(qCode);
        } else if(coachJson != null) {
            initWithCoach(coachJson);
        } else if (!StringUtils.isEmpty(this.id)){
            getPersonInfoJsonWithUid(this.id);
            Log.i("UserCardActivity","1 id = " + this.id);
        }
        Log.i("UserCardActivity","2 id = " + this.id);
    }

    @Override
    protected int setTopBar() {
        return BaseStatusActivity.NO_SHOW_TOP_BAR;
    }

    private void initWithCoach(CoachJson coachJson) {
        uid = coachJson.uid;
        activityTitle.setText(R.string.s3);
        setUpView();
        configView(coachJson.name,
                coachJson.gender,
                coachJson.thumbnail,
                null,
                coachJson.aptitude,
                coachJson.description);
    }

    private void getPersonInfoJson(String code){
        BizDataRequest.requestCodeUserInfo(UserCardActivity.this, code,statusLayout, new BizDataRequest.OnLoginUserInfo() {
            @Override
            public void onSuccess(PersonInfoJson personInfoJson) {
                uid = personInfoJson.uid;
                id = String.valueOf(personInfoJson.id);
                setUpView();

                configView(personInfoJson.nick,
                        personInfoJson.gender,
                        personInfoJson.avatar,
                        personInfoJson.company,
                        null,null);
                initInfoBtn();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    //根据UID获取用户信息
    private void getPersonInfoJsonWithUid(ArrayList<String> list){
        BizDataRequest.requestUidUserInfoNormal(UserCardActivity.this, list, new BizDataRequest.OnUidUserInfo() {
            @Override
            public void onSuccess(List<PersonInfoJson> personList) {
                if(personList!=null&&personList.size()>0){
                    PersonInfoJson personInfoJson = personList.get(0);
                    uid = String.valueOf(personInfoJson.uid);
                    setUpView();

                    configView(personInfoJson.nick,
                            personInfoJson.gender,
                            personInfoJson.avatar,
                            personInfoJson.company,
                            null,null);
                    id = String.valueOf(personInfoJson.id);
                    initInfoBtn();


                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    //根据ID获取用户信息
    private void getPersonInfoJsonWithUid(String otherId){
        String mine = "";
        if (getGlobalParams().isLogin())
            mine = String.valueOf(getGlobalParams().getPersonInfoJson().getId());
        BizDataRequest.requestUidUserInfoNormal(UserCardActivity.this,mine,otherId, new BizDataRequest.OnUidUserInfo_() {

            @Override
            public void onSuccess(PersonInfoJson personList) {
//                if(personList!=null&&personList.size()>0){
                PersonInfoJson personInfoJson = personList;
                uid = String.valueOf(personInfoJson.uid);
                setUpView();

                configView(personInfoJson.nick,
                        personInfoJson.gender,
                        personInfoJson.avatar,
                        personInfoJson.company,
                        null,null);
                id = String.valueOf(personInfoJson.id);
                initInfoBtn();
            }


            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private void configView(String nick, String gender, String avtarUrl,@Nullable String company,@Nullable String qualification, @Nullable String intro) {
        mTvNick.setText(nick);

        if(Constants.Sex.MALE.equals(gender)){
            ivSex.setImageResource(R.drawable.male);
        }else if(Constants.Sex.FEMALE.equals(gender)){
            ivSex.setImageResource(R.drawable.female);
        }else{
            ivSex.setVisibility(View.GONE);
            tvSex.setVisibility(View.GONE);
        }

        if (coachJson!= null){
            mBtnAdd.setVisibility(View.GONE);
        }
        tvSex.setText(FormatCouponInfo.getHZSsx(gender)+"");

        GlideUtils.loadCustomImageView(this,R.drawable.header_icon,LoadImageUtils.loadSmallImage(avtarUrl),mTvAvatar);
        final ArrayList<String> list = new ArrayList<>();
        list.add(avtarUrl);
        if (!StringUtils.isEmpty(avtarUrl)){
            mTvAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), PreviewPhotosActivity.class).putExtra("type","just_show")
                    .putStringArrayListExtra("images",list));
                }
            });
        }

        vCompany.setVisibility(company == null ? View.GONE : View.VISIBLE);
        if(TextUtils.isEmpty(company)){
            mTvCompany.setText(R.string.s4);
        }else{
            mTvCompany.setText(company);
        }

        vTrainerQualification.setVisibility(qualification == null ? View.GONE : View.VISIBLE);
        tvTrainerQualification.setText(qualification);

        vTrainerIntro.setVisibility(intro == null ? View.GONE : View.VISIBLE);
        tvTrainerIntro.setText(intro);

    }

    private void addContacts(){

        if(!getGlobalParams().isLogin()){
            showAskLoginDialog();
            return;
        }
//        try {
//            String reason = getString(R.string.s5) + getGlobalParams().getPersonInfoJson().getUid()
//                    + "\",\"nickname\":\"" + getGlobalParams().getPersonInfoJson().getNick()
//                    + "\",\"gender\":\"" + getGlobalParams().getPersonInfoJson().getGender()
//                    + "\",\"company\":\"" + getGlobalParams().getPersonInfoJson().getCompany()
//                    + "\",\"imageUrl\":\"" + getGlobalParams().getPersonInfoJson().getAvatar()
//                    + "\"}}";
//
//            EMClient.getInstance().contactManager().addContact(uid,reason);
            TIMAddFriendRequest request = new TIMAddFriendRequest(id);
            List<TIMAddFriendRequest> friends = new ArrayList<>();
            friends.add(request);
            TIMFriendshipManagerExt.getInstance().addFriend(friends, new TIMValueCallBack<List<TIMFriendResult>>() {
                @Override
                public void onError(int i, String s) {
                }

                @Override
                public void onSuccess(List<TIMFriendResult> timFriendResults) {
                    Toast.makeText(UserCardActivity.this, R.string.s6,Toast.LENGTH_LONG).show();
                }
            });

            //UserCardActivity.this.finish();
            mBtnAdd.setBackgroundResource(R.drawable.button_gray);
            mBtnAdd.setClickable(false);
//        }catch (Exception e){
//
//        }
    }

    private View.OnClickListener onClickAcceptOrRefuse = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(UserCardActivity.this);
            switch (id){
                case  R.id.btn_accept:
                    EMMessage message = EMMessage.createTxtSendMessage(getString(R.string.s7), msg.getFrom());
                    String userPic = SpUtil.getString(context,"logoUrl", "");
                    if (!TextUtils.isEmpty(userPic)) {
                        message.setAttribute("userPic", userPic);
                    }
                    String userName = SpUtil.getString(context,"name", "");
                    if (!TextUtils.isEmpty(userName)) {
                        message.setAttribute("userName", userName);
                    }
                    EMClient.getInstance().chatManager().sendMessage(message);
                    inviteMessgeDao.updateMessageAgreed(msg);
                    showToast(getString(R.string.s8));
                    UserCardActivity.this.finish();
                    break;
                case  R.id.btn_refuse:
                    inviteMessgeDao.updateMessageRefused(msg);
                    showToast(getString(R.string.s9));
                    UserCardActivity.this.finish();
                    break;
            }
        }
    };

    private void setUpView(){
        if(msg!= null){
            mBtnAdd.setVisibility(View.GONE);
            btnAccept.setOnClickListener(onClickAcceptOrRefuse);
            btnRefuse.setOnClickListener(onClickAcceptOrRefuse);
        }else{
            mBtnAdd.setVisibility(View.VISIBLE);
            btnRefuse.setVisibility(View.GONE);
            btnAccept.setVisibility(View.GONE);
        }
        if(uid.equals(getGlobalParams().getPersonInfoJson().getUid())){
            mBtnAdd.setVisibility(View.GONE);
        }else {
            if (uid != null && uid.length() > 0) {

//                Map<String, EaseUser> users = IMHelper.getInstance().getContactList();
//                if (users != null && users.size() > 0) {
//
//                    if (users.containsKey(uid.toLowerCase())) {
//                        mBtnAdd.setText(R.string.s10);
//                        mBtnAdd.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
////                                startActivity(new Intent(UserCardActivity.this, ChatActivity.class).putExtra("userId", uid.toLowerCase()));
////                                UserCardActivity.this.finish();
//                            }
//                        });
//                    } else {
//                        mBtnAdd.setText(R.string.s11);
//                        mBtnAdd.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                addContacts();
//                            }
//                        });
//                    }
//                } else {
//
//                    mBtnAdd.setText(R.string.s11);
//                    mBtnAdd.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            addContacts();
//                        }
//                    });
//
//                }
//                initInfoBtn();
                mBtnAdd.setText(R.string.s11);
                mBtnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addContacts();
                    }
                });
            } else {
                Toast.makeText(UserCardActivity.this, R.string.s12, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initInfoBtn(){
        TIMFriendshipManagerExt.getInstance().getFriendList(new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {
                mBtnAdd.setText(R.string.s11);
                mBtnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addContacts();
                    }
                });
            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                boolean isFriends = false;
                for (TIMUserProfile file : timUserProfiles){
                    if (id.equals(file.getIdentifier())){
                        isFriends = true;
                        break;
                    }
                }

                if (isFriends){
                    mBtnAdd.setText(R.string.s10);
                    mBtnAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            startActivity(new Intent(UserCardActivity.this, ChatActivity.class).putExtra("userId", uid.toLowerCase()));
//                            UserCardActivity.this.finish();
                            ChatActivity.navToChat(getContext(),id, TIMConversationType.C2C);
                        }
                    });
                }else {
                    mBtnAdd.setText(R.string.s11);
                    mBtnAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addContacts();
                        }
                    });
                }

            }
        });
    }
}
