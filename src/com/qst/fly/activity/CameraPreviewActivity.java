package com.qst.fly.activity;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.qst.R;
import com.qst.fly.utils.CameraOperationHelper;
import com.qst.fly.utils.StringUtils;
import com.qst.fly.utils.CameraOperationHelper.CameraOverCallback;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
/**
* @author NoahZu
* @version
*/
public class CameraPreviewActivity extends Activity implements Callback, OnClickListener, CameraOverCallback {

	private static final String CAMERA_PATH = "cameraDemos";
	private static final String TAG = "MainActivity";
	public static final String PICPATH = "picturePath";
	private static final int CROP_PHOTO = 1;
	private static final int PICK_PHOTO = 2;

	private ImageButton switchCameraBtn;
	private ImageButton flashBtn;
	private SurfaceView disCameraSurface;
	private SurfaceHolder disCameraSurfaceHolder;
	private CameraOperationHelper cameraOperationHelper;
	
	private Button mBtnOpenAlbum;

	private SoundPool soundPool;
	private boolean isFlashing = false;
	private boolean isFront = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_camera);

		
		initSound();
		initCamera();
		initView();
	}

	
		
	private void openAlbum() {
		Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
		getImage.addCategory(Intent.CATEGORY_OPENABLE);
		getImage.setType("image/*");
		startActivityForResult(getImage, PICK_PHOTO);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == PICK_PHOTO) {
				openCropActivity(data);
			} else if (requestCode == CROP_PHOTO) {
				Bitmap bitmap = data.getParcelableExtra("cropedPhoto");
				//TODO 处理裁剪后的图片
			}
		}
	}
	
	/**
	 * ����ͼƬ�ü�ҳ��
	 * @param data
	 */
	private void openCropActivity(Intent data) {
		Intent intent = new Intent(CameraPreviewActivity.this, CropPhotoActivity.class);
		
		Uri uri = data.getData();
		intent.putExtra("uri", uri);
		startActivityForResult(intent, CROP_PHOTO);
	}
	
	
	private void initCamera() {
		cameraOperationHelper = CameraOperationHelper.getInstance();
	}

	private void initSound() {
		soundPool= new SoundPool(21,AudioManager.STREAM_SYSTEM,10);
		soundPool.load(this,R.raw.take_photo,1);
	}

	private void initView() {
		switchCameraBtn = (ImageButton) findViewById(R.id.btn_switch);
		flashBtn = (ImageButton) findViewById(R.id.btn_flash);
		mBtnOpenAlbum = (Button) findViewById(R.id.btn_openalbum);
		mBtnOpenAlbum.setOnClickListener(this);
		switchCameraBtn.setOnClickListener(this);
		flashBtn.setOnClickListener(this);

		disCameraSurface = (SurfaceView) findViewById(R.id.sf_dis_camera);
		disCameraSurface.setOnClickListener(this);
		disCameraSurfaceHolder = disCameraSurface.getHolder();
		disCameraSurfaceHolder.addCallback(this);
		disCameraSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		cameraOperationHelper.surfaceChanged();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		cameraOperationHelper.doOpenCamera(CameraOperationHelper.FRONT_CAMERA, this, holder);
		cameraOperationHelper.doStartPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		cameraOperationHelper.destroyCamera();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();	
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_switch) {
			ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(switchCameraBtn, "rotationY", 0f, 180f);
			objectAnimator.setDuration(1000);
			objectAnimator.addListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {
					
				}
				
				@Override
				public void onAnimationRepeat(Animator animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animator animation) {
					switchCameraBtn.setRotationX(0f);
					if (isFront) {
						switchCameraBtn.setSelected(true);
						cameraOperationHelper.changeCamera(CameraOperationHelper.BACK_CAMERA);
						isFront = false;
					} else {
						switchCameraBtn.setSelected(false);
						cameraOperationHelper.changeCamera(CameraOperationHelper.FRONT_CAMERA);
						isFront = true;
					}
				}
				
				@Override
				public void onAnimationCancel(Animator animation) {
					
				}
			});
			objectAnimator.start();
		} else if (v.getId() == R.id.sf_dis_camera) {
			File file = new File(Environment.getExternalStorageDirectory(), CAMERA_PATH);
			if (!file.exists()) {
				file.mkdir();
			}
			File picFile = new File(file, StringUtils.formatDate(new Date(), "yyyyMMddHHmmss") + ".jpg");
			if (!picFile.exists()) {
				try {
					picFile.createNewFile();
					cameraOperationHelper.takePicture(picFile.getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (v.getId() == R.id.btn_flash) {
			
			ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(flashBtn, "rotationY", 0f, 180f);
			objectAnimator.setDuration(1000);
			objectAnimator.addListener(new AnimatorListener() {				
				@Override
				public void onAnimationStart(Animator animation) {
					
				}
				
				@Override
				public void onAnimationRepeat(Animator animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animator animation) {	
					flashBtn.setRotationX(0f);
					if(!isFlashing){
						flashBtn.setSelected(true);
						cameraOperationHelper.openFlashMode();
						isFlashing = true;
					}else{
						flashBtn.setSelected(false);
						cameraOperationHelper.closeFlashMode();
						isFlashing = false;
					}
				}
				
				@Override
				public void onAnimationCancel(Animator animation) {
					
				}
			});
			objectAnimator.start();
			
		}else if(v.getId() == R.id.btn_openalbum){
			openAlbum();
		}
	}

	@Override
	public void cameraFlashModeChanged(int flashMode) {

	}

	@Override
	public void cameraFacingChanged(boolean hasFaceCamera, int cameraId) {

	}

	@Override
	public void cameraPhotoTaken(String path) {
		soundPool.play(1,1, 1, 0, 0,  1f);
		cameraOperationHelper.doStopPreview();
		Intent intent = new Intent(CameraPreviewActivity.this,PreviewActivity.class);
		intent.putExtra(PICPATH, path);
		startActivity(intent);
	}

}
