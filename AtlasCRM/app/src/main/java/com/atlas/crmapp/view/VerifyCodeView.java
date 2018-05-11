package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoda on 2017/9/18.
 */

public class VerifyCodeView extends RelativeLayout {
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tv_code_1)
    TextView tvCode1;
    @BindView(R.id.tv_code_2)
    TextView tvCode2;
    @BindView(R.id.tv_code_3)
    TextView tvCode3;
    @BindView(R.id.tv_code_4)
    TextView tvCode4;
    private Context context;
    private String code;

    private OnTextChangedListener onTextChangedListener;

    public VerifyCodeView(Context context) {
        super(context);
        initView(context);

    }

    public VerifyCodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    public VerifyCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VerifyCodeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_verify_code, this, true);
        ButterKnife.bind(this);
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code = charSequence.toString();
                int codeLength = code.length();

                if(codeLength > 0){
                    etCode.setCursorVisible(true);
                    tvCode1.setText(code.substring(0, 1));
                }else{
                    etCode.setCursorVisible(true);
                    tvCode1.setText("");
                }
                if(codeLength > 1){
                    tvCode2.setText(code.substring(1, 2));
                }else{
                    tvCode2.setText("");
                }
                if(codeLength > 2){
                    tvCode3.setText(code.substring(2, 3));
                }else{
                    tvCode3.setText("");
                }
                if(codeLength > 3){
                    tvCode4.setText(code.substring(3, 4));
                }else{
                    tvCode4.setText("");
                }
                if(onTextChangedListener != null){
                    onTextChangedListener.onTextChanged(code);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public interface OnTextChangedListener{
        void  onTextChanged(String code);
    }

    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
        this.onTextChangedListener = onTextChangedListener;
    }
}
