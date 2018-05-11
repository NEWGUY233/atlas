package com.atlas.crmapp.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hoda on 2017/7/11.
 */

public class QRCodeUtil {


    /**
     *
     * @param strUrl 二维码链接 的值
     * @param margin 边距  1-4
     * @param height 图片高度
     * @param margin 图片宽度
     * @return
     */

    public static Bitmap encodeAsBitmap(String strUrl, int width, int height,int margin){
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Map<EncodeHintType,Integer> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, margin);
        try {
            result = multiFormatWriter.encode(strUrl, BarcodeFormat.QR_CODE, width, height, hints);
            // 使用 ZXing Android Embedded 要写的代码
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e){
            e.printStackTrace();
        } catch (IllegalArgumentException iae) { // ?
            return null;
        }
        return bitmap;
    }

    public static Bitmap encodeAsBitmap(Context context, String strUrl){
        return encodeAsBitmap(strUrl, UiUtil.dipToPx(context, 200), UiUtil.dipToPx(context, 200), 2);
    }

}
