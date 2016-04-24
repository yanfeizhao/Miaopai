package com.qst.fly.utils;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

/**
* @author NoahZu
* @version
* @date 2016年3月12日 下午1:50:37
* 类说明
*/
public class CameraUtils {
	
	/**
	 * 减产设备是否有摄像头
	 * @param context
	 * @return
	 */
	public static boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // this device has a camera 
	        return true; 
	    } else { 
	        // no camera on this device 
	        return false; 
	    } 
	} 
	/**
	 * 获取相机的功能集合
	 * @param mCamera
	 * @return
	 */
	public static List<String> getFeaturesOfCamera(Camera mCamera){
		Camera.Parameters params = mCamera.getParameters(); 
		List<String> flashModes = params.getSupportedFlashModes();
		String flashMode = params.getFlashMode();
		return flashModes;
	}
	
	/**
	 * 检查相机是否具备某一个功能
	 * @param feature 比如 Camera.Parameters.FLASH_MODE_ON
	 * @param mCamera 
	 * @return
	 */
	public static boolean checkFeatureOfCamera(Context context,String feature,Camera mCamera){
		List<String> flashModes = getFeaturesOfCamera(mCamera);
		if(flashModes.size() == 1){
			return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		}else if (flashModes.contains(feature)) {
			return true;
		} 
		return false;
	}
	
}
