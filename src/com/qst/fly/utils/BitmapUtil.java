package com.qst.fly.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;
import android.view.View.MeasureSpec;

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
}
