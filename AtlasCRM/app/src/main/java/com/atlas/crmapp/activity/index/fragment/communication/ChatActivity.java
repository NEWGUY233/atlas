package com.atlas.crmapp.activity.index.fragment.communication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.selector.entry.Image;
import com.atlas.crmapp.selector.views.ImageSelectorActivity;
import com.atlas.crmapp.tim.adapter.ChatAdapter;
import com.atlas.crmapp.tim.adapter.EmoAdapter;
import com.atlas.crmapp.tim.model.FriendProfile;
import com.atlas.crmapp.tim.model.FriendshipInfo;
import com.atlas.crmapp.tim.model.ImageMessage;
import com.atlas.crmapp.tim.model.Message;
import com.atlas.crmapp.tim.model.MessageFactory;
import com.atlas.crmapp.tim.model.TextMessage;
import com.atlas.crmapp.tim.model.VoiceMessage;
import com.atlas.crmapp.tim.presenter.ChatPresenter;
import com.atlas.crmapp.tim.utils.RecorderUtil;
import com.atlas.crmapp.tim.viewfeatures.ChatView;
import com.atlas.crmapp.tim.views.VoiceSendingView;
import com.atlas.crmapp.util.EmoUtil;
import com.atlas.crmapp.util.FileUtils;
import com.atlas.crmapp.util.TakePhoneHelper;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageStatus;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.imsdk.ext.message.TIMMessageDraft;
import com.tencent.imsdk.ext.message.TIMMessageLocator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

import static com.atlas.crmapp.util.TakePhoneHelper.CROP_PHOTO;

/**
 * Created by Administrator on 2018/4/16.
 */
@RuntimePermissions
public class ChatActivity extends BaseStatusActivity implements ChatView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.ll_input)
    LinearLayout llInput;
    @BindView(R.id.ll_voice)
    LinearLayout llVoice;
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.iv_input)
    ImageView ivInput;
    @BindView(R.id.tv_voice)
    TextView tvVoice;
    @BindView(R.id.emoticonPanel)
    ViewPager emoticonPanel;

    @BindView(R.id.voice_sending)
    VoiceSendingView animationView;

    RecorderUtil recorder = new RecorderUtil();

    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.v_delete)
    View vDelete;

    public static void navToChat(Context context, String identify, TIMConversationType type) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("identify", identify);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tim_activity_chat);
        ButterKnife.bind(this);

        initView();

    }

    private ChatPresenter presenter;
    private FriendProfile profile;
    private String identify;
    private TIMConversationType type;
    private List<Message> messageList = new ArrayList<>();
    ChatAdapter adapter;

    private void initView() {
        initToolbar();
        setTopRightButton("", R.mipmap.nav_icon_more, new OnClickListener() {
            @Override
            public void onClick() {
                showPop();
            }
        });


        setVerticalManager(recyclerView);
        adapter = new ChatAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setConversationList(messageList);

        identify = getIntent().getStringExtra("identify");
        type = (TIMConversationType) getIntent().getSerializableExtra("type");
        presenter = new ChatPresenter(this, identify, type);
        profile = FriendshipInfo.getInstance().getProfile(identify);

        setTitle(profile == null ? "" : profile.getName());
        presenter.start();

        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ("".equals(etInput.getText().toString().trim())) {
                    ivMore.setImageResource(R.mipmap.im_icon_talk_plus);
                } else {
                    ivMore.setImageResource(R.mipmap.im_icon_talk_send);
                }
            }
        });

        tvVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        tvVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("setOnTouchListener", "ACTION_DOWN");
                        tvVoice.setBackgroundResource(R.drawable.voice_pressed_true);
                        animationView.setVisibility(View.VISIBLE);
                        animationView.showRecording();
                        startSendVoice();
                        break;
                    case MotionEvent.ACTION_UP:
                        tvVoice.setBackgroundResource(R.drawable.voice_pressed_false);
                        tvVoice.setText(getString(R.string.input_voice));
                        Log.i("setOnTouchListener", "ACTION_UP x = " + event.getY() + " ; tvVoice x= " + tvVoice.getHeight());
                        if (event.getX() >= 0 && event.getX() <= tvVoice.getWidth()
                                && event.getY() >= -100) {
                            endSendVoice();
                        } else {
                            recorder.stopRecording();
                            animationView.setVisibility(View.GONE);
                            animationView.release();
                        }

                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (event.getX() >= 0 && event.getX() <= tvVoice.getWidth()
                                && event.getY() >= -100) {
                            tvVoice.setText(getString(R.string.input_voice));
                        } else {
                            tvVoice.setText(getString(R.string.chat_up_finger));
                        }
                        break;
                }
                return false;
            }
        });

        etInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    emoticonPanel.setVisibility(View.GONE);
                    llMore.setVisibility(View.GONE);
                }
            }
        });
//        etInput.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                emoticonPanel.setVisibility(View.GONE);
//                llMore.setVisibility(View.GONE);
//            }
//        });

        vDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vDelete.setVisibility(View.GONE);
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vDelete.setVisibility(View.GONE);
                TIMManagerExt.getInstance().deleteConversationAndLocalMsgs(TIMConversationType.C2C,identify);
                adapter.getConversationList().clear();
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void showMessage(TIMMessage message) {
        Log.i("ChatActivity", "showMessage TIMMessage");
        if (message == null) {
            adapter.notifyDataSetChanged();
        } else {
            Message mMessage = MessageFactory.getMessage(message);
            if (mMessage != null) {
//                if (mMessage instanceof CustomMessage){
//                    CustomMessage.Type messageType = ((CustomMessage) mMessage).getType();
//                    switch (messageType){
//                        case TYPING:
//                            TemplateTitle title = (TemplateTitle) findViewById(R.id.chat_title);
//                            title.setTitleText(getString(R.string.chat_typing));
//                            handler.removeCallbacks(resetTitle);
//                            handler.postDelayed(resetTitle,3000);
//                            break;
//                        default:
//                            break;
//                    }
//                }else{
                if (messageList.size() == 0) {
                    mMessage.setHasTime(null);
                } else {
                    mMessage.setHasTime(messageList.get(messageList.size() - 1).getMessage());
                }
                messageList.add(mMessage);
                adapter.notifyDataSetChanged();
                moveToBottom();
//                }

            }
        }
    }

    @Override
    public void showMessage(List<TIMMessage> messages) {
        Log.i("ChatActivity", "showMessage List<TIMMessage>");
        int newMsgNum = 0;
        for (int i = 0; i < messages.size(); ++i) {
            Message mMessage = MessageFactory.getMessage(messages.get(i));
            if (mMessage == null || messages.get(i).status() == TIMMessageStatus.HasDeleted)
                continue;
//            if (!(mMessage instanceof TextMessage)) continue;
            ++newMsgNum;
            if (i != messages.size() - 1) {
                mMessage.setHasTime(messages.get(i + 1));
                messageList.add(0, mMessage);
            } else {
                mMessage.setHasTime(null);
                messageList.add(0, mMessage);
            }
        }
        adapter.notifyDataSetChanged();
        moveToBottom();
    }

    private void moveToBottom() {
        if (recyclerView != null)
            recyclerView.scrollToPosition(messageList == null && messageList.size() == 0 ? 0 : messageList.size() - 1);
    }

    @Override
    public void showRevokeMessage(TIMMessageLocator timMessageLocator) {

    }

    @Override
    public void clearAllMessage() {

    }

    @Override
    public void onSendMessageSuccess(TIMMessage message) {

    }

    @Override
    public void onSendMessageFail(int code, String desc, TIMMessage message) {

    }

    @Override
    public void sendImage() {

    }

    @Override
    public void sendPhoto() {

    }

    @Override
    public void sendText() {
        Message message = new TextMessage(etInput.getText());
        presenter.sendMessage(message.getMessage());
        etInput.setText("");
    }

    @Override
    public void sendFile() {

    }

    @Override
    public void startSendVoice() {
        animationView.setVisibility(View.VISIBLE);
        animationView.showRecording();
        recorder.startRecording();

    }

    @Override
    public void endSendVoice() {
        animationView.setVisibility(View.GONE);
        animationView.release();
        recorder.stopRecording();
        if (recorder.getTimeInterval() < 1) {
            Toast.makeText(this, getResources().getString(R.string.chat_audio_too_short), Toast.LENGTH_SHORT).show();
        } else if (recorder.getTimeInterval() > 60) {
            Toast.makeText(this, getResources().getString(R.string.chat_audio_too_long), Toast.LENGTH_SHORT).show();
        } else {
            Message message = new VoiceMessage(recorder.getTimeInterval(), recorder.getFilePath());
            presenter.sendMessage(message.getMessage());
        }
    }

    @Override
    public void sendVideo(String fileName) {

    }

    @Override
    public void cancelSendVoice() {

    }

    @Override
    public void sending() {

    }

    @Override
    public void showDraft(TIMMessageDraft draft) {

    }

    @Override
    public void videoAction() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.stop();
    }

    @OnClick({R.id.iv_voice, R.id.iv_emo, R.id.iv_more, R.id.iv_input, R.id.iv_camera, R.id.iv_location, R.id.iv_img, R.id.et_input})
    public void onViewClicked(View view) {
        if (view.getId() != R.id.iv_more)
            llMore.setVisibility(View.GONE);
        switch (view.getId()) {
            case R.id.iv_voice:
                llInput.setVisibility(View.GONE);
                llVoice.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_emo:
                if (!isEmoticonReady) {
                    prepareEmoticon();
                }
                if (emoticonPanel.getVisibility() == View.VISIBLE) {
                    emoticonPanel.setVisibility(View.GONE);
                } else {
                    emoticonPanel.setVisibility(View.VISIBLE);
                    hideKeyboard(etInput);
                    llMore.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_more:
                if ("".equals(etInput.getText().toString().trim())) {
                    if (llMore.getVisibility() == View.VISIBLE) {
                        llMore.setVisibility(View.GONE);
                    } else {
                        llMore.setVisibility(View.VISIBLE);
                        hideKeyboard(etInput);
                        emoticonPanel.setVisibility(View.GONE);
                    }
                } else {
                    sendText();
                }

                break;
            case R.id.iv_input:
                llInput.setVisibility(View.VISIBLE);
                llVoice.setVisibility(View.GONE);
                hideKeyboard(etInput);
                break;
            case R.id.iv_camera:
                ChatActivityPermissionsDispatcher.takePhotoWithCheck(this);
                break;
            case R.id.iv_img:
                ChatActivityPermissionsDispatcher.selectPhotoWithCheck(this);
                break;
            case R.id.iv_location:
                break;
            case R.id.et_input:
                break;
        }
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void selectPhoto() {
        startActivityForResult(new Intent(getContext(), ImageSelectorActivity.class).putExtra("size", 1), 0x111);
    }

    private File output;

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ChatActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    TakePhoneHelper.IPhotoPath iPhotoPath = new TakePhoneHelper.IPhotoPath() {
        @Override
        public void getTakePhotoPath(String path) {
            sendImg(path);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            /**
             * 拍照的请求标志
             */
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    TakePhoneHelper.getTakePhotoPath(output, iPhotoPath);
                }
                break;
            case 0x111:
                if (data == null || resultCode != 0x111)
                    return;
                ArrayList<Image> list = data.getParcelableArrayListExtra("images");
                for (Image img : list)
                    sendImg(img.getPath());
                break;

        }

    }

    private void sendImg(String path) {
        Message message = new ImageMessage(path, false);
        presenter.sendMessage(message.getMessage());
    }


    private boolean isEmoticonReady;
//    private void prepareEmoticon(){
//        if (emoticonPanel == null) return;
//        List<View> list = new ArrayList<>();
//        for (int k = 0; k < 3 ; ++k) {
//            LinearLayout linearLayout1 = new LinearLayout(getContext());
//            linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, 1f));
//            linearLayout1.setOrientation(LinearLayout.VERTICAL);
//            for (int i = 0; i < 5; ++i) {
//                LinearLayout linearLayout = new LinearLayout(getContext());
//                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, 1f));
//                for (int j = 0; j < 7; ++j) {
//
//                    try {
//                        AssetManager am = getContext().getAssets();
//                        final int index = 7 * i + j + k*35;
//                        InputStream is = am.open(String.format("emoticon/%d.gif", index));
//                        Bitmap bitmap = BitmapFactory.decodeStream(is);
//                        Matrix matrix = new Matrix();
//                        int width = bitmap.getWidth();
//                        int height = bitmap.getHeight();
//                        matrix.postScale(3.5f, 3.5f);
//                        final Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
//                                width, height, matrix, true);
//                        ImageView image = new ImageView(getContext());
//                        image.setImageBitmap(resizedBitmap);
//                        image.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, 1f));
//                        linearLayout.addView(image);
//                        image.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                String content = String.valueOf(index);
//                                SpannableString str = new SpannableString(String.valueOf(index));
//                                ImageSpan span = new ImageSpan(getContext(), resizedBitmap, ImageSpan.ALIGN_BASELINE);
//                                str.setSpan(span, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                etInput.append(str);
//                            }
//                        });
//                        is.close();
//                    } catch (IOException e) {
//
//                    }
//
//                }
//                linearLayout1.addView(linearLayout);
//            }
//            list.add(linearLayout1);
//        }
//        emoticonPanel.setAdapter(new EmoAdapter(getContext(),list));
//        isEmoticonReady = true;
//    }

    private void prepareEmoticon() {
        if (emoticonPanel == null) return;
        List<View> list = new ArrayList<>();
        for (int k = 0; k < 2; ++k) {
            LinearLayout linearLayout1 = new LinearLayout(getContext());
            linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, 1f));
            linearLayout1.setOrientation(LinearLayout.VERTICAL);
            for (int i = 0; i < 4; ++i) {
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, 1f));
                for (int j = 0; j < 7; ++j) {

                    final int index = 7 * i + j + k * 28;
                    if (index >= EmoUtil.getEmoList().size())
                        break;
                    TextView image = new TextView(getContext());
                    image.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, 1f));
                    linearLayout.addView(image);
                    image.setGravity(Gravity.CENTER);
                    image.setText(new String(Character.toChars(EmoUtil.getEmoList().get(index))));
                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            etInput.append(new String(Character.toChars(EmoUtil.getEmoList().get(index))));
                        }
                    });

                }
                linearLayout1.addView(linearLayout);
            }
            list.add(linearLayout1);
        }
        emoticonPanel.setAdapter(new EmoAdapter(getContext(), list));
        isEmoticonReady = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }

        if (vDelete.getVisibility() == View.VISIBLE) {
            vDelete.setVisibility(View.GONE);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (emoticonPanel.getVisibility() == View.VISIBLE) {
            emoticonPanel.setVisibility(View.GONE);
            return;
        }

        if (llMore.getVisibility() == View.VISIBLE) {
            llMore.setVisibility(View.GONE);
            return;
        }

        finish();
    }

    private void showPop() {
        vDelete.setVisibility(View.VISIBLE);
    }
}
