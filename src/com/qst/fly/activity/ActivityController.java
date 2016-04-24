package com.qst.fly.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * @author smallzoo
 * @version
 * @date 2016年3月19日 上午8:49:51 类说明
 */
public class ActivityController {
	public static List<Activity> activities = new ArrayList<Activity>();

	/**
	 * 添加Activity
	 * @param activity
	 */
	public static void addActivity(Activity activity) {
		activities.add(activity);
	}

	/**
	 * 移除activity
	 * @param activity
	 */
	public static void removeActivity(Activity activity) {
		activities.add(activity);
	}
	/**
	 * 结束所有activity
	 */
	public static void finishAll() {
		for (Activity activity : activities) {
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
	}
}
