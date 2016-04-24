package com.qst.fly.utils;

import java.util.List;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
* @author smallzoo
* @version
* @date 2016年3月17日 上午10:47:14
* 类说明
*/
public class NetUtil {
    public static boolean isNetworkConnected(Context context){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
    /** 
     * 检测网络是否连接 
     *  
     * @return 
     */  
    public static boolean isNetConnected(Context context) {  
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (cm != null) {  
            NetworkInfo[] infos = cm.getAllNetworkInfo();  
            if (infos != null) {  
                for (NetworkInfo ni : infos) {  
                    if (ni.isConnected()) {  
                        return true;  
                    }  
                }  
            }  
        }  
        return false;  
    }  
  
    /** 
     * 检测wifi是否连接 
     *  
     * @return 
     */  
    public static boolean isWifiConnected(Context context) {  
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (cm != null) {  
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();  
            if (networkInfo != null  
                    && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {  
                return true;  
            }  
        }  
        return false;  
    }  
  
    /** 
     * 检测3G是否连接 
     *  
     * @return 
     */  
    public static boolean is3gConnected(Context context) {  
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (cm != null) {  
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();  
            if (networkInfo != null  
                    && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {  
                return true;  
            }  
        }  
        return false;  
    }  
  
    /** 
     * 检测GPS是否打开 
     *  
     * @return 
     */  
    public static boolean isGpsEnabled(Context context) {  
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);  
        List<String> accessibleProviders = lm.getProviders(true);  
        for (String name : accessibleProviders) {  
            if ("gps".equals(name)) {  
                return true;  
            }  
        }  
        return false;  
    }
}
