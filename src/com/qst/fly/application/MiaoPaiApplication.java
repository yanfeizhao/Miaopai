package com.qst.fly.application;

import android.app.Application;
import android.graphics.Bitmap;

/**
 * @author NoahZu
 * @version
 * @date 2016年3月14日 下午7:34:57 类说明
 */
public class MiaoPaiApplication extends Application {
	private static final String IS_FIRST_ENTER_SHARED = "IS_FIRST_ENTER_SHARED";
	private static final String IS_FIRST_ENTER = "IS_FIRST_ENTER";

	private static MiaoPaiApplication sMiaoPaiApplication = null;

	private int isFirstEnter = -1;//-1：未检测 ，0：否 ，1：是
	private Bitmap mBitmap = null;
	
	synchronized public static MiaoPaiApplication getApplication() {
		if (sMiaoPaiApplication == null) {
			sMiaoPaiApplication = new MiaoPaiApplication();
		}
		return sMiaoPaiApplication;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public void saveBitmap(Bitmap bitmap){
		this.mBitmap = bitmap;
	}
	public Bitmap getBitmap(){
		return this.mBitmap;
	}
	
	public int getIsFirstOpen(){
		return this.isFirstEnter;
	}
	public void setIsFirstEnter(boolean b){
		if(b){
			this.isFirstEnter = 1;
		}else{
			this.isFirstEnter = 0;
		}
	}
	public boolean isNeedResetData = false;
}
