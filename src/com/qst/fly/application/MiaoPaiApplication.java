package com.qst.fly.application;

import com.qst.fly.utils.SharedPreferenceUtil;

import android.app.Application;

/**
 * @author NoahZu
 * @version
 * @date 2016年3月14日 下午7:34:57 类说明
 */
public class MiaoPaiApplication extends Application {
	private static final String IS_FIRST_ENTER_SHARED = "IS_FIRST_ENTER_SHARED";
	private static final String IS_FIRST_ENTER = "IS_FIRST_ENTER";

	private static MiaoPaiApplication sMiaoPaiApplication = null;

	synchronized public static MiaoPaiApplication getApplication() {
		if (sMiaoPaiApplication == null) {
			sMiaoPaiApplication = new MiaoPaiApplication();
		}
		return sMiaoPaiApplication;
	}

	private boolean isFirstOpen;
	@Override
	public void onCreate() {
		super.onCreate();
		SharedPreferenceUtil.setSharedPreferenceName(IS_FIRST_ENTER_SHARED);
		isFirstOpen  = SharedPreferenceUtil.getBoolean(this, IS_FIRST_ENTER, true);
		if(isFirstOpen){
			SharedPreferenceUtil.addInSharePreference(this, IS_FIRST_ENTER, false);			
		}
	}
	
	public boolean isFirstOpen(){
		return this.isFirstOpen;
	}
}
