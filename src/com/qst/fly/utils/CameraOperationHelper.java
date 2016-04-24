package com.qst.fly.utils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * @author NoahZu
 * @version
 * @date 2016年3月12日 下午3:30:41 照相机帮助类
 */
public class CameraOperationHelper {
	private static CameraOperationHelper mCameraOperationHelper;

	private Camera camera;
	private int cameraid;
	private CameraOverCallback mCameraOverCallback;
	private SurfaceHolder surfaceHolder;
	private Camera.Parameters parameters;
	private Context context;

	public static final int FRONT_CAMERA = 1;
	public static final int BACK_CAMERA = 0;
	public static final int FLASH_ON = 2;
	public static final int FLASH_OFF = 3;

	protected static final String TAG = "camerahelper";


	private CameraOperationHelper(Context context) {
		this.context = context;
	}

	public static synchronized CameraOperationHelper getInstance(Context context) {
		if (mCameraOperationHelper == null) {
			mCameraOperationHelper = new CameraOperationHelper(context);
		}
		return mCameraOperationHelper;
	}

	/**
	 * 开启相机
	 * 
	 * @param id
	 *            相机的id FRONT_CAMERA 或者 BACK_CAMERA
	 * @param callback
	 *            回调接口
	 * @param surfaceHolder
	 *            传入surface的surfaceHolder
	 */
	public void doOpenCamera(int id, CameraOverCallback callback, SurfaceHolder surfaceHolder) {
		try {
			this.cameraid = id;
			camera = Camera.open(cameraid);
			this.mCameraOverCallback = callback;
			this.surfaceHolder = surfaceHolder;
			this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			this.surfaceHolder.setKeepScreenOn(true);
			camera.setPreviewDisplay(surfaceHolder);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 开始在surfaceView上边预览
	 */
	public void doStartPreview() {
		initCamera();
	}

	/**
	 * 在SurfaceView的surfaceChanged中回调此方法
	 */
	public void surfaceChanged() {
		if(cameraid == BACK_CAMERA){
			camera.autoFocus(new AutoFocusCallback() {
				@Override
				public void onAutoFocus(boolean success, Camera camera) {
					if (success) {
						initCamera();// 实现相机的参数初始化
						camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦。
					}
				}

			});
		}else{
			camera.stopPreview();
			try {
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * 切换相机
	 * 
	 * @param cameraId
	 * @return ture 切换成功 false 切换失败
	 */
	public boolean changeCamera(int cameraId) {
		if (this.cameraid == cameraId) {
			return false;
		}
		camera.stopPreview();
		camera.release();
		camera = null;
		camera = Camera.open(cameraId);
		this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		this.surfaceHolder.setKeepScreenOn(true);
			try {
				camera.setPreviewDisplay(this.surfaceHolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		this.cameraid = cameraId;
		initCamera();
		return true;
	}

	/**
	 * 初始化相机
	 */
	private void initCamera() {
		parameters = camera.getParameters();
		parameters.setPictureFormat(PixelFormat.JPEG);
		if(cameraid == BACK_CAMERA){
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦			
		}
		setDispaly(parameters, camera);
		camera.setParameters(parameters);
		camera.startPreview();
		if(cameraid == BACK_CAMERA){
			camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上			
		}

	}

	/**
	 * 控制图像的正确显示方向
	 * 
	 * @param parameters
	 * @param camera
	 */
	private void setDispaly(Camera.Parameters parameters, Camera camera) {
		if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
			setDisplayOrientation(camera, 90);
		} else {
			parameters.setRotation(90);
		}

	}

	/**
	 * 实现的图像的正确显示
	 * 
	 * @param camera
	 * @param i
	 */
	private void setDisplayOrientation(Camera camera, int i) {
		Method downPolymorphic;
		try {
			downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
			if (downPolymorphic != null) {
				downPolymorphic.invoke(camera, new Object[] { i });
			}
		} catch (Exception e) {
			Log.e("Came_e", "图像出错");
		}
	}

	/**
	 * 停止在SurfaceView上预览
	 */
	public void doStopPreview() {
		camera.stopPreview();
	}

	/**
	 * 销毁相机 在activity的onDestory中调用
	 */
	public void destroyCamera() {
		if (this.camera != null) {
			this.camera.release();
			this.camera = null;
		}
	}

	/**
	 * 拍摄照片
	 * 
	 * @param path
	 *            照片存储的路径
	 */
	public void takePicture(final String path) {
		camera.takePicture(null, null, new PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				mCameraOverCallback.cameraPhotoTaken(data, cameraid);
			}
		});
	}

	/**
	 * 打开闪光灯
	 */
	public void turnFlashOn() {
		if (this.camera == null) {
			return;
		}
		Parameters parameters = this.camera.getParameters();
		List<String> flashModes = parameters.getSupportedFlashModes();
		if (flashModes == null) {
			return;
		}
		String flashMode = parameters.getFlashMode();
		if (!Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
			if (flashModes.contains(Parameters.FLASH_MODE_TORCH)) {
				parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
				this.camera.setParameters(parameters);
			}
		}
	}

	/**
	 * 关闭闪光灯
	 * 
	 * @param mCamera
	 */
	public void turnFlashOff() {
		if (this.camera == null) {
			return;
		}
		Parameters parameters = this.camera.getParameters();
		List<String> flashModes = parameters.getSupportedFlashModes();
		String flashMode = parameters.getFlashMode();
		if (flashModes == null) {
			return;
		}
		if (!Parameters.FLASH_MODE_OFF.equals(flashMode)) {
			if (flashModes.contains(Parameters.FLASH_MODE_OFF)) {
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
				this.camera.setParameters(parameters);
			} else {
			}
		}
	}

	public interface CameraOverCallback {
		public void cameraFlashModeChanged(int flashMode);

		public void cameraFacingChanged(boolean hasFaceCamera, int cameraId);

		public void cameraPhotoTaken(byte[] data, int cameraId);
	}

}
