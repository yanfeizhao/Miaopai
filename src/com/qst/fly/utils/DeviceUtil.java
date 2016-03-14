package com.qst.fly.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/**
*@author NoahZu
* @version
* @date 2016年3月12日 下午1:50:37
* 设备相关工具类
*/
public class DeviceUtil {

    /**
     * x width
     * y height
     */
    public static Point point = null;

    public static Point getScreenInfo(Context context){
        if (point == null){
            WindowManager wm = (WindowManager)context
                    .getSystemService(Context.WINDOW_SERVICE);
            point = new Point();
            wm.getDefaultDisplay().getSize(point);
        }
        return point;
    }
}
