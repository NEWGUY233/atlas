package com.atlas.crmapp.view.popupwindow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.atlas.crmapp.util.ScreenUtils;
import com.atlas.crmapp.util.UiUtil;

/**
 * Created by hoda on 2017/10/8.
 */

public class MaskImageView extends ImageView {

    private Context context;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;

    private int radius ;
    private int maxRadius ;

    private boolean isHidden ;
    private int repeatInterval = 20 ;

    private int count = 5;


    public void setRadius(int radius) {
        this.radius = this.radius + radius;
        if(this.radius <  -maxRadius){
            this.radius = -maxRadius;
        }else if(this.radius > maxRadius){
            this.radius  = maxRadius;
        }

        if(Math.abs(this.radius) == maxRadius ){
            count  = count + 1;
            if(count > 5){
                mHandler.removeCallbacks(runable);
                count = 0;
            }
        }

        invalidate();
    }


    public MaskImageView(Context context) {
        super(context);
        initViews(context);
    }

    public MaskImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public MaskImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MaskImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        maxRadius =  UiUtil.dipToPx(context, 80);
        this.context = context;
        radius = - maxRadius;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    private float width, height ;
    private BitmapShader bitmapShader;
    private Path path;
    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        if(width == 0){
            width = canvas.getWidth();
            height = (float) (canvas.getHeight() - maxRadius * 1.2);
        }



        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //paint.setColor(Color.RED);
        if(bitmapShader == null){
            bitmapShader = new BitmapShader(getBitmapFromDrawable(getDrawable()), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        }
        paint.setShader(bitmapShader);
        if(path == null){
            path = new Path();
        }
        path.reset();
        path.moveTo(0, 0);
        path.lineTo(0, 0);
        path.lineTo(0, height);
        path.quadTo(width / 2, height + radius , width, height);
        path.lineTo(width, height);
        path.lineTo(width, 0);
        canvas.drawPath(path, paint);
        path.close();

        Log.d("mask ", "width / 2 "  + width / 2 + "    radius   " + radius + " width: " + width  + "   height: " + height);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            setImageResource(msg.what);
        }
    };

    public void setBitmapShader(){
//        bitmapShader = new BitmapShader(getBitmapFromDrawable(getDrawable()), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        bitmapShader = new BitmapShader(drawableToBitmap(getDrawable()), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    }

    public void setBitmapShader(final int id){
        new Thread(){
            @Override
            public void run() {
//                bitmapShader = new BitmapShader(drawableToBitmap(getResources().getDrawable(id)), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                bitmapShader = new BitmapShader(getBitmapFromDrawable(getResources().getDrawable(id)), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//                bitmapShader = new BitmapShader(getBitmapFromDrawable(getDrawable()), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                handler.sendEmptyMessage(id);
            }
        }.start();

    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = Bitmap.createScaledBitmap( ((BitmapDrawable) drawable).getBitmap(), ScreenUtils.getScreenWidth(context), ScreenUtils.getScreenHeight(context)  , true);
            return bitmap;
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static Bitmap drawableToBitmap(Drawable drawable) // drawable 转换成bitmap
    {
        int width = drawable.getIntrinsicWidth();// 取drawable的长宽
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ?Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;// 取drawable的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);// 建立对应bitmap
        Canvas canvas = new Canvas(bitmap);// 建立对应bitmap的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);// 把drawable内容画到画布中
        return bitmap;
    }

    public void startSpring( boolean isHidden){
        this.isHidden = isHidden;
        mHandler.postDelayed(runable, repeatInterval);
    }


    public void stopAnimate(){

        /*this.isHidden = false;
        mHandler.postDelayed(runable, repeatInterval);*/

    }

    Handler mHandler = new Handler();
    Runnable runable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(this, repeatInterval);
            if(isHidden){
                setRadius(15);
            }else{
                setRadius(-15);
            }
        }
    };

}
