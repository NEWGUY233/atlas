package com.atlas.crmapp.commonactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.util.ProgressWebView;
import com.atlas.crmapp.util.ShareHelper;
import com.atlas.crmapp.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.UMShareAPI;

import butterknife.BindView;
import butterknife.OnClick;

public class WebActivity  extends BaseActivity {

    private int mIsError = 0;
    ProgressWebView webViewInfo;

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @OnClick(R.id.ibBack)
    void onBack(){
        this.finish();
    }

    @BindView(R.id.ibHome)
    ImageButton ibShareMenu;

    private String url;
    private String content;
    private String thumbImageUrl;
    private Drawable thumbBitmap;
    private boolean isOnPause;
    private boolean isShowShare;
    public static String KEY_IS_SHOW_SHARE = "KEY_IS_SHOW_SHARE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ibShareMenu.setVisibility(View.VISIBLE);
        ibShareMenu.setImageResource(R.drawable.ic_share);
        url= getIntent().getStringExtra(ActionUriUtils.url);
        content = getIntent().getStringExtra(ActionUriUtils.content);
        thumbImageUrl = getIntent().getStringExtra(ActionUriUtils.imageUrl);
        isShowShare = getIntent().getBooleanExtra(KEY_IS_SHOW_SHARE, true);
        if(isShowShare){
            ibShareMenu.setVisibility(View.VISIBLE);
        }else{
            ibShareMenu.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(content)){
            textViewTitle.setText(content);
            if(Constants.CUSTOM_URL.INSERT_VIP.equals(url)){
                setUmengPageTitle(getString(R.string.add_vip));
            }else{
                setUmengPageTitle(content);
            }
        }else{
            textViewTitle.setText("");
        }
        webViewInfo = (ProgressWebView) findViewById(R.id.webViewInfo);
        webViewInfo.getSettings().setJavaScriptEnabled(true);
        webViewInfo.getSettings().setBlockNetworkImage(false);
        webViewInfo.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        WebSettings webSettings = webViewInfo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setUseWideViewPort(true);//自适应屏幕
        webSettings.setLoadWithOverviewMode(true);//自适应屏幕
        webViewInfo.setWebViewClient(new MyWebViewClient()); //让WebView支持弹出框

//        webViewInfo.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onReceivedTitle(WebView webView, String s) {
//                super.onReceivedTitle(webView, s);
//                textViewTitle.setText(s);
//            }
//        });

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            //webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webViewInfo.getSettings().setDefaultTextEncodingName("UTF-8");
        webViewInfo.loadUrl(url);

        if(StringUtils.isNotEmpty(thumbImageUrl)){
            Glide.with(this).load(thumbImageUrl)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                            thumbBitmap = resource;
                        }
                    });
//                    .asBitmap()
//                    .into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    thumbBitmap = resource;
//                }
//            });
        }
    }

    public static void newInstance(Context context,String url,  String content, boolean isShowShare){
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(KEY_IS_SHOW_SHARE, isShowShare);
        intent.putExtra(ActionUriUtils.url, url);
        intent.putExtra(ActionUriUtils.content, content);
        context.startActivity(intent);
    }

    //分享按钮
    @OnClick(R.id.ibHome)
    void showShareView(){
        ShareHelper.shareToWX(this, thumbBitmap, url, content, "" ,  null);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    public class MyWebViewClient extends WebViewClient {
        // 如果页面中链接，如果希望点击链接继续在当前browser中响应，
        // 而不是新开Android的系统browser中响应该链接，必须覆盖 webview的WebViewClient对象。
        @Override
        public boolean shouldOverrideUrlLoading(WebView wv, String url) {
            if(url == null) return false;

            try {
                if(url.startsWith("weixin://") //微信
                        || url.startsWith("alipays://") //支付宝
                        || url.startsWith("mailto://") //邮件
                        || url.startsWith("tel://")//电话
                        || url.startsWith("dianping://")//大众点评

                    //其他自定义的scheme
                        ) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
            }

            //处理http和https开头的url
            wv.loadUrl(url);
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }

        public void onPageFinished(WebView view, String url) {
            mIsError = 0;
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            mIsError = 1;
        }
    }


    /**
     * 当Activity执行onPause()时让WebView执行pause
     */
    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (webViewInfo != null) {
                webViewInfo.getClass().getMethod("onPause").invoke(webViewInfo, (Object[]) null);
                isOnPause = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当Activity执行onResume()时让WebView执行resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (isOnPause) {
                if (webViewInfo != null) {
                    webViewInfo.getClass().getMethod("onResume").invoke(webViewInfo, (Object[]) null);
                }
                isOnPause = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 该处的处理尤为重要:
     * 应该在内置缩放控件消失以后,再执行webViewInfo.destroy()
     * 否则报错WindowLeaked
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webViewInfo.destroy();
        isOnPause = false;
        webViewInfo = null;
    }

}