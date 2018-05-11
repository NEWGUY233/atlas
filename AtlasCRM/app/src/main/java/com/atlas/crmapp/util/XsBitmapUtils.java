package com.atlas.crmapp.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * XsBitmapUtils Bitmap操作类
 * 
 * @version 1.00 20120501
 * @author Digicloud Network Technology Co.,Ltd.
 * @author Harry
 */
public class XsBitmapUtils {

	/**
	 * Byte数组转Bitmap类
	 * 
	 * @param b
	 *            byte数组
	 * @return Bitmap类
	 */
	public static Bitmap BytesToBitmap(byte[] b) {
		return BytesToBitmap(b, 500);
	}

	/**
	 * 字节数组转bitmap
	 * 
	 * @param b
	 * @return
	 */
	private Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * Byte数组转Bitmap类
	 * 
	 * @param b
	 *            byte数组
	 * @param displaysize
	 *            显示大小
	 * @return Bitmap类
	 */
	public static Bitmap BytesToBitmap(byte[] b, int displaysize) {
		if (b.length != 0) {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inSampleSize = 1;
			opt.inJustDecodeBounds = true;
			// 获取这个图片的宽和高
			BitmapFactory.decodeByteArray(b, 0, b.length, opt);
			opt.inJustDecodeBounds = false;
			opt.inSampleSize = computeSampleSize(opt, displaysize);
			opt.inDither = false;
			opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
			return BitmapFactory.decodeByteArray(b, 0, b.length, opt);
		} else {
			return null;
		}
	}

	/**
	 * 对图片的大小进行判断，并得到合适的缩放比例，比如2即1/2,3即1/3
	 * 
	 * @param options
	 *            BitmapFactory.Options值
	 * @param target
	 *            显示大小
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int target) {
		int w = options.outWidth;
		int h = options.outHeight;
		int candidateW = w / target;
		int candidateH = h / target;
		int candidate = Math.max(candidateW, candidateH);
		if (candidate == 0)
			return 1;
		if (candidate > 1) {
			if ((w > target) && (w / candidate) < target)
				candidate -= 1;
		}
		if (candidate > 1) {
			if ((h > target) && (h / candidate) < target)
				candidate -= 1;
		}
		return candidate;
	}

	/**
	 * Bitmap类转Byte数组
	 * 
	 * @param bm
	 *            Bitmap类
	 * @param quality
	 *            压缩大小 值越大压缩比越少
	 * @return 数组
	 */
	public static byte[] BitmapToBytes(Bitmap bm, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
		return baos.toByteArray();
	}

	/**
	 * bitmap转字节数组
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		opt.inSampleSize = computeSampleSize(opt, -1, 128 * 128); // 计算出图片使用的inSampleSize
		opt.inJustDecodeBounds = false;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

}
