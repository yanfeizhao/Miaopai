package com.qst.fly.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * Created by NoahZu on 2016/3/11.
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
