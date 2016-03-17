package com.qst.fly.utils;
/** 
* @Title: ApplicationUtil.java 
 * @Package com.test.testappexit 
 * @Description: TODO 
 * @author yanfeizhao 417470640@qq.com 
 * @date 2016-3-16 ����4:04:19 
 * @version V1.0 
 */
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;


public class CheckAppExistUtils {
	

	/**
	 * 根据包名检测App是否存在
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean checkAppExist(Context context, String packageName) {
		try {
			ApplicationInfo info = context.getPackageManager()
					.getApplicationInfo(packageName, 0);
			return info != null && info.packageName.equals(packageName);
		} catch (PackageManager.NameNotFoundException e) {

		} catch (Exception e) {
		}
		return false;
	}

	

}
