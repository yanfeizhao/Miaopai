package com.qst.fly.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.view.SurfaceHolder;

/**
* @author NoahZu
* @version
* @date 2016年3月12日 下午3:30:41
* 照相机帮助类
*/
public class CameraOperationHelper {
	private static CameraOperationHelper mCameraOperationHelper;
	
	private Camera camera;
	private int cameraid;
	private CameraOverCallback mCameraOverCallback;
	private SurfaceHolder surfaceHolder;
	
	public static final int FRONT_CAMERA  = 1;
	public static final int BACK_CAMERA = 0;
	public static final int FLASH_ON = 2;
	public static final int FLASH_OFF = 3;
	
	private boolean isFacingDetection = false;
	private String flashStatus = Camera.Parameters.FLASH_MODE_OFF;
	
	private CameraOperationHelper(){
		
	}
	
	public static synchronized CameraOperationHelper getInstance(){
		if(mCameraOperationHelper == null){
			mCameraOperationHelper = new CameraOperationHelper();
		}
		return mCameraOperationHelper;
	}
	/**
	 * 开启相机
	 * @param id 相机的id FRONT_CAMERA 或者 BACK_CAMERA
	 * @param callback 回调接口
	 * @param surfaceHolder 传入surface的surfaceHolder
	 */
	public void doOpenCamera(int id,CameraOverCallback callback,SurfaceHolder surfaceHolder){
		try {
			this.cameraid = id;
			camera = Camera.open(cameraid);
			camera.setPreviewDisplay(surfaceHolder);
			this.mCameraOverCallback = callback;
			this.surfaceHolder = surfaceHolder;
			camera.setDisplayOrientation(90);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 开始在surfaceView上边预览
	 */
	public void doStartPreview(){
		camera.startPreview();
	}
	
	/**
	 * 停止在SurfaceView上预览
	 */
	public void doStopPreview(){
		camera.stopPreview();
	}
	
	/**
	 * 销毁相机
	 * 在activity的onDestory中调用
	 */
	public void destroyCamera(){
		if(this.camera != null){
			this.camera.release();
			this.camera = null;
		}
	}
	
	/**
	 * 切换相机
	 * @param cameraId
	 * @return ture 切换成功 false 切换失败
	 */
	public boolean changeCamera(int cameraId){
		if(this.cameraid == cameraId){
			return false;
		}
		camera.stopPreview();
		camera.release();
		camera = null;
		camera = Camera.open(cameraId);
		camera.setDisplayOrientation(90);
		try {
			camera.setPreviewDisplay(this.surfaceHolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		camera.startPreview();
		this.cameraid = cameraId;
		return true;
	}
	
	/**
	 * 拍摄照片
	 * @param path 照片存储的路径
	 */
	public void takePicture(final String path){
		  camera.takePicture(null, null, new PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
				File file = new File(path);
				if(!file.exists()){
					try {
						file.createNewFile();
					} catch (IOException e) {
						throw new IllegalArgumentException("不是合法的路径");
					}
				}
				 FileOutputStream fos;
				try {
					fos = new FileOutputStream(file);
					fos.write(data);
					fos.close();
					mCameraOverCallback.cameraPhotoTaken(path,cameraid);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 开启人脸识别
	 * @param listener 传入Listener监听人脸识别的回调
	 * @throws Exception 重复开启人脸识别，报异常
	 */
	public void startFaceDetection(Camera.FaceDetectionListener listener) throws Exception{
		if(!isFacingDetection){
			camera.setFaceDetectionListener(listener);
			startFaceDetection();
			this.mCameraOverCallback.cameraFacingChanged(true, this.cameraid);	
			isFacingDetection = true;
		}else{
			throw new Exception("已经开启人脸识别，无法重复开启");
		}
	}
	
	/**
	 * 停止人脸识别
	 * @throws Exception 不能重复停止
	 */
	public void stopFaceDeection() throws Exception{
		if(isFacingDetection){
			camera.stopFaceDetection();			
			isFacingDetection = false;
			this.mCameraOverCallback.cameraFacingChanged(false, this.cameraid);
		}else{
			throw new Exception("已经停止人脸识别");
		}
	}
	
	/**
	 * 开启相机闪光灯
	 * @return
	 */
	public boolean openFlashMode(){
		if(flashStatus == Camera.Parameters.FLASH_MODE_ON){
			return false;
		}
		if(!CameraUtils.checkFeatureOfCamera(Camera.Parameters.FLASH_MODE_ON, this.camera)){
			return false;
		}
		Camera.Parameters params = this.camera.getParameters();
		params.setFocusMode(Camera.Parameters.FLASH_MODE_ON);
		this.camera.setParameters(params);
		flashStatus = Camera.Parameters.FLASH_MODE_ON;
		this.mCameraOverCallback.cameraFlashModeChanged(FLASH_ON);
		return true;
	}
	/**
	 * 停止闪光灯
	 * @return
	 */
	public boolean closeFlashMode(){
		if(flashStatus == Camera.Parameters.FLASH_MODE_OFF){
			return false;
		}
		if(!CameraUtils.checkFeatureOfCamera(Camera.Parameters.FLASH_MODE_OFF, this.camera)){
			return false;
		}
		Camera.Parameters params = this.camera.getParameters();
		params.setFocusMode(Camera.Parameters.FLASH_MODE_OFF);
		this.camera.setParameters(params);
		flashStatus = Camera.Parameters.FLASH_MODE_OFF;
		this.mCameraOverCallback.cameraFlashModeChanged(FLASH_OFF);
		return true;
	}
	
	public void setCameraOritation(){
		
	}
	/**
	 * 在SurfaceView的surfaceChanged中回调此方法
	 */
	public void surfaceChanged(){
		camera.stopPreview();
		try {
			camera.setPreviewDisplay(surfaceHolder);
			camera.startPreview();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 开启之前检查硬件是否支持
	 */
	private void startFaceDetection(){
	    Camera.Parameters params = camera.getParameters();

	    if (params.getMaxNumDetectedFaces() > 0){
	    	camera.startFaceDetection();
	    }
	}
	
	public interface CameraOverCallback{
		public void cameraFlashModeChanged(int flashMode);
		public void cameraFacingChanged(boolean hasFaceCamera,int cameraId);
		public void cameraPhotoTaken(String path,int cameraId);
	}
	
}
