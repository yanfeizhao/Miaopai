package com.qst.fly.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
* @author NoahZu
* @version
* bitmap工具类
*/
public class BitmapUtil {

    public static Bitmap scaleImage(Bitmap bm, int newWidth, int newHeight){
        if (bm == null){
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,true);
        if (bm != null & !bm.isRecycled()){
            bm.recycle();
            bm = null;
        }
        return newbm;
    }
    public static Bitmap roateImage(Bitmap mBitmap,float degree) { 
	    if (mBitmap.getWidth() > mBitmap.getHeight()) { 
	        Matrix matrix = new Matrix(); 
	        matrix.postRotate(degree);
	        mBitmap = Bitmap.createBitmap(mBitmap , 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
	    }
	    return mBitmap;
	}
}
