package com.qst.fly.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;

/**
 * @author NoahZu
 * @version bitmap工具类
 */
public class BitmapUtil {

	/**
	 * 缩放
	 * 
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap scaleImage(Bitmap bm, int newWidth, int newHeight) {
		if (bm == null) {
			return null;
		}
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		if (bm != null & !bm.isRecycled()) {
			bm.recycle();
			bm = null;
		}
		return newbm;
	}

	/**
	 * 任意角度翻转
	 * 
	 * @param mBitmap
	 * @param degree
	 * @return
	 */
	public static Bitmap roateImage(Bitmap mBitmap, float degree) {
		if (mBitmap.getWidth() > mBitmap.getHeight()) {
			Matrix matrix = new Matrix();
			matrix.postRotate(degree);
			mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
		}
		return mBitmap;
	}

	/**
	 * 对图片做镜面翻转
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap mirrorRoate(Bitmap bmp) {
		int w = bmp.getWidth();
		int h = bmp.getHeight();

		Matrix matrix = new Matrix();
		matrix.postScale(-1, 1); // 镜像水平翻转
		Bitmap convertBmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);

		return convertBmp;
	}

	/**
	 * 将图片存储到指定路径
	 * 
	 * @param bitmap
	 * @param path
	 */
	public static void saveBitmap(Bitmap bitmap, String path) {
		File f = new File(path);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
//			bitmap.recycle();
//			bitmap = null;
//			System.gc();
		}
	}

	/**
	 * 将两张图片合并
	 * 
	 * @param backBitmap
	 *            背景图
	 * @param frontBitmap
	 *            前景图
	 * @param left
	 *            前景图的左侧坐标
	 * @param top
	 *            前景图的顶部坐标
	 * @return
	 */
	public static Bitmap mergeBitmap(Bitmap backBitmap, Bitmap frontBitmap, float left, float top) {
		Bitmap bitmap = Bitmap.createBitmap(backBitmap.getWidth(), backBitmap.getHeight(), backBitmap.getConfig());
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(backBitmap, new Matrix(), null);
		canvas.drawBitmap(frontBitmap, left, top, null);
		frontBitmap.recycle();
		frontBitmap = null;
		return bitmap;
	}

	/**
	 * 将View转换为Bitmap
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap convertViewToBitmap(View v) {
		v.clearFocus();
		v.setPressed(false);

		boolean willNotCache = v.willNotCacheDrawing();
		v.setWillNotCacheDrawing(false);

		// Reset the drawing cache background color to fully transparent
		// for the duration of this operation
		int color = v.getDrawingCacheBackgroundColor();
		v.setDrawingCacheBackgroundColor(0);

		if (color != 0) {
			v.destroyDrawingCache();
		}
		v.buildDrawingCache();
		Bitmap cacheBitmap = v.getDrawingCache();
		if (cacheBitmap == null) {
			return null;
		}

		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

		// Restore the view
		v.destroyDrawingCache();
		v.setWillNotCacheDrawing(willNotCache);
		v.setDrawingCacheBackgroundColor(color);

		return bitmap;
	}

	/**
	 * 图片压缩：质量压缩
	 * 
	 * @param image
	 * @param size
	 *            大小
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image, int size) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		image.recycle();
		image = null;
		return bitmap;
	}

	/**
	 * 
	 * @param res
	 * @param resId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {

		// 第一次加载时 将inJustDecodeBounds设置为true 表示不真正加载图片到内存
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// 根据目标宽和高 以及当前图片的大小 计算出压缩比率
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// 将inJustDecodeBounds设置为false 真正加载图片 然后根据压缩比率压缩图片 再去解码
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	// 计算压缩比率 android官方提供的算法
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			// 将当前宽和高 分别减小一半
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

}
